/**
 *  Thaumic Augmentation
 *  Copyright (c) 2019 TheCodex6824.
 *
 *  This file is part of Thaumic Augmentation.
 *
 *  Thaumic Augmentation is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Thaumic Augmentation is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Thaumic Augmentation.  If not, see <https://www.gnu.org/licenses/>.
 */

package thecodex6824.thaumicaugmentation.common.event;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.EntityArcaneBore;
import thaumcraft.common.golems.tasks.TaskHandler;
import thecodex6824.thaumicaugmentation.api.TAConfig;
import thecodex6824.thaumicaugmentation.api.event.BlockWardEvent;
import thecodex6824.thaumicaugmentation.api.warded.CapabilityWardStorage;
import thecodex6824.thaumicaugmentation.api.warded.IWardStorage;
import thecodex6824.thaumicaugmentation.api.warded.IWardStorageServer;

public class WardEventHandlerNoCoremodFallback extends WardEventHandler {

    private static final Field BORE_DIG_TARGET;
    
    static {
        Field dig = null;
        try {
            dig = EntityArcaneBore.class.getDeclaredField("digTarget");
            dig.setAccessible(true);
        }
        catch (Exception ex) {
            FMLCommonHandler.instance().raiseException(ex, "Failed to access Thaumcraft's EntityArcaneBore#digTarget", true);
        }

        BORE_DIG_TARGET = dig;
    }
    
    private static void handleBoreNotCaringAboutCanceledEvents(FakePlayer borePlayer) {
        for (EntityArcaneBore bore : borePlayer.getEntityWorld().getEntitiesWithinAABB(EntityArcaneBore.class, borePlayer.getEntityBoundingBox())) {
            try {
                BORE_DIG_TARGET.set(bore, bore.getPosition());
            }
            catch (Exception ex) {
                FMLCommonHandler.instance().raiseException(ex, "Failed to set Thaumcraft's EntityArcaneBore#digTarget", true);
            }
        }
    }
    
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (!TAConfig.disableWardFocus.getValue() && !event.world.isRemote && event.phase == Phase.END) {
            ConcurrentHashMap<Integer, Task> tasks = TaskHandler.tasks.get(event.world.provider.getDimension());
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                Task task = entry.getValue();
                if (task.getType() == 0 && event.world.isBlockLoaded(task.getPos())) {
                    Chunk chunk = event.world.getChunk(task.getPos());
                    if (chunk != null && chunk.hasCapability(CapabilityWardStorage.WARD_STORAGE, null)) {
                        IWardStorage storage = chunk.getCapability(CapabilityWardStorage.WARD_STORAGE, null);
                        if (storage.hasWard(task.getPos())) {
                            task.setPriority(Byte.MIN_VALUE);
                            task.setReserved(true);
                            task.setCompletion(true);
                            tasks.remove(entry.getKey());
                        }
                    }
                }
            }
        }
    }
    
    @Override
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!TAConfig.disableWardFocus.getValue()) {
            BlockPos pos = event.getPos();
            Chunk chunk = event.getWorld().getChunk(pos);
            if (chunk.hasCapability(CapabilityWardStorage.WARD_STORAGE, null)) {
                IWardStorage storage = chunk.getCapability(CapabilityWardStorage.WARD_STORAGE, null);
                if (storage.hasWard(pos) && !checkForSpecialCase(event.getPlayer())) {
                    event.setCanceled(true);
                    if (event.getPlayer() instanceof FakePlayer) {
                        if (event.getPlayer().getName().equals("FakeThaumcraftBore"))
                            handleBoreNotCaringAboutCanceledEvents((FakePlayer) event.getPlayer());
                    }
                }
                else if (storage instanceof IWardStorageServer)
                    ((IWardStorageServer) storage).clearWard(event.getWorld(), pos);
            }
        }
    }
    
    @Override
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        super.onNeighborNotify(event);
        if (!TAConfig.disableWardFocus.getValue() && event.getWorld().isBlockLoaded(event.getPos())) {
            BlockPos notifier = event.getPos();
            for (EnumFacing facing : event.getNotifiedSides()) {
                BlockPos pos = notifier.offset(facing);
                if (!event.getWorld().isAirBlock(pos) && event.getWorld().isBlockLoaded(pos)) {
                    Chunk chunk = event.getWorld().getChunk(pos);
                    if (chunk.hasCapability(CapabilityWardStorage.WARD_STORAGE, null)) {
                        IWardStorage storage = chunk.getCapability(CapabilityWardStorage.WARD_STORAGE, null);
                        if (storage.hasWard(pos)) {
                            event.setCanceled(true);
                            if (event.getState().getMaterial() == Material.FIRE) {
                                event.getWorld().setBlockState(notifier, Blocks.AIR.getDefaultState(), 2);
                                event.setCanceled(true);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onExplosion(ExplosionEvent.Detonate event) {
        if (!TAConfig.disableWardFocus.getValue()) {
            HashSet<BlockPos> blockers = new HashSet<>();
            ListIterator<BlockPos> iterator = event.getAffectedBlocks().listIterator();
            while (iterator.hasNext()) {
                BlockPos pos = iterator.next();
                Chunk chunk = event.getWorld().getChunk(pos);
                if (chunk.hasCapability(CapabilityWardStorage.WARD_STORAGE, null) && 
                        chunk.getCapability(CapabilityWardStorage.WARD_STORAGE, null).hasWard(pos)) {
                    blockers.add(pos);
                    iterator.remove();
                }
            }
            
            iterator = event.getAffectedBlocks().listIterator();
            while (iterator.hasNext()) {
                BlockPos pos = iterator.next();
                RayTraceResult ray = event.getWorld().rayTraceBlocks(event.getExplosion().getPosition(), new Vec3d(pos));
                if (ray != null && ray.typeOfHit == Type.BLOCK && blockers.contains(ray.getBlockPos()))
                    iterator.remove();
            }
            
            ListIterator<Entity> entityIterator = event.getAffectedEntities().listIterator();
            while (entityIterator.hasNext()) {
                Entity entity = entityIterator.next();
                RayTraceResult ray = event.getWorld().rayTraceBlocks(event.getExplosion().getPosition(), entity.getPositionVector());
                if (ray != null && ray.typeOfHit == Type.BLOCK && blockers.contains(ray.getBlockPos()))
                    entityIterator.remove();
                else {
                    ray = event.getWorld().rayTraceBlocks(event.getExplosion().getPosition(), entity.getPositionEyes(1.0F));
                    if (ray != null && ray.typeOfHit == Type.BLOCK && blockers.contains(ray.getBlockPos()))
                        entityIterator.remove();
                }
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWardBlock(BlockWardEvent.WardedServer.Post event) {
        BlockPos warded = event.getPos();
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos pos = warded.offset(facing);
            if (event.getWorld().getBlockState(pos).getMaterial() == Material.FIRE)
                event.getWorld().setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
        }
    }
    
}