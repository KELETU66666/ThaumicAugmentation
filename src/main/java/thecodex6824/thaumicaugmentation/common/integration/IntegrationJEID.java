/*
 *  Thaumic Augmentation
 *  Copyright (c) 2022 TheCodex6824.
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

package thecodex6824.thaumicaugmentation.common.integration;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import org.dimdev.jeid.INewChunk;
import thecodex6824.thaumicaugmentation.common.network.PacketBiomeUpdate;
import thecodex6824.thaumicaugmentation.common.network.TANetwork;

public class IntegrationJEID implements IIntegrationHolder {

    @Override
    public void preInit() {}
    
    @Override
    public void init() {}
    
    @Override
    public void postInit() {}
    
    public void setBiomeJEID(World world, BlockPos pos, Biome newBiome) {
        INewChunk newChunk = (INewChunk) world.getChunk(pos);
        int[] biomes = newChunk.getIntBiomeArray();
        biomes[(pos.getZ() & 15) << 4 | (pos.getX() & 15)] = Biome.getIdForBiome(newBiome);
        if (!world.isRemote) {
            world.markChunkDirty(pos, null);
            TargetPoint point = new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64.0);
            TANetwork.INSTANCE.sendToAllTracking(new PacketBiomeUpdate(pos.getX(), pos.getZ(), Biome.getIdForBiome(newBiome)), point);
        }
        else
            world.markBlocksDirtyVertical(pos.getX(), pos.getZ(), 0, 255);
    }
    
}
