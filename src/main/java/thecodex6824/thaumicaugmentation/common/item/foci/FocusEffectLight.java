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

package thecodex6824.thaumicaugmentation.common.item.foci;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.NodeSetting;
import thaumcraft.api.casters.Trajectory;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXGeneric;
import thecodex6824.thaumicaugmentation.api.TABlocks;
import thecodex6824.thaumicaugmentation.api.ThaumicAugmentationAPI;
import thecodex6824.thaumicaugmentation.api.block.property.ILightSourceBlock;

import javax.annotation.Nullable;

public class FocusEffectLight extends FocusEffect {

    protected class NodeSettingLightIntensity extends NodeSetting.NodeSettingIntRange {

        public NodeSettingLightIntensity() {
            super(1, 15);
        }

        @Override
        public int getDefault() {
            return 8;
        }

    }

    @Override
    public Aspect getAspect() {
        return Aspect.LIGHT;
    }

    @Override
    public int getComplexity() {
        return getSettingValue("intensity");
    }

    @Override
    public String getKey() {
        return "focus." + ThaumicAugmentationAPI.MODID + ".light";
    }

    @Override
    public String getResearch() {
        return "FOCUS_LIGHT";
    }

    @Override
    public boolean execute(RayTraceResult result, @Nullable Trajectory trajectory, float finalPower, int something) {
        if (result.typeOfHit == Type.BLOCK) {
            IBlockState state = getPackage().world.getBlockState(result.getBlockPos());
            if (state.getBlock().isAir(state, getPackage().world, result.getBlockPos()) || 
                    state.getBlock().isReplaceable(getPackage().world, result.getBlockPos())) {

                return placeLightSource(result.getBlockPos(), result.sideHit, getSettingValue("intensity"));
            }
            else {
                BlockPos pos = result.getBlockPos().offset(result.sideHit);
                state = getPackage().world.getBlockState(pos);
                if (state.getBlock().isAir(state, getPackage().world, pos) || 
                        state.getBlock().isReplaceable(getPackage().world, pos))

                    return placeLightSource(pos, result.sideHit, getSettingValue("intensity"));
            }
        }
        else if (result.typeOfHit == Type.MISS) 
            return placeLightSource(result.getBlockPos(), result.sideHit, getSettingValue("intensity"));
        else if (result.entityHit instanceof EntityLivingBase) {
            ((EntityLivingBase) result.entityHit).addPotionEffect(new PotionEffect(MobEffects.GLOWING, getSettingValue("intensity") * 10, 0, true, false));
            return true;
        }

        return false;
    }

    protected boolean placeLightSource(BlockPos pos, EnumFacing side, int intensity) {
        World world = getPackage().world;
        if (world.isAreaLoaded(pos, pos) && world.getBlockState(pos).getBlock() != TABlocks.TEMPORARY_LIGHT 
                && world.mayPlace(TABlocks.TEMPORARY_LIGHT, pos, true, side, getPackage().getCaster())) {

            return world.setBlockState(pos, TABlocks.TEMPORARY_LIGHT.getDefaultState().withProperty(ILightSourceBlock.LIGHT_LEVEL, intensity));
        }

        return false;
    }

    @Override
    public NodeSetting[] createSettings() {
        return new NodeSetting[] {
                new NodeSetting("intensity", "focus." + ThaumicAugmentationAPI.MODID + ".light.intensity", new NodeSettingLightIntensity())
        };
    }

    @Override
    public void onCast(Entity caster) {
        caster.world.playSound(null, caster.getPosition().up(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 
                SoundCategory.PLAYERS, 0.2F, 1.2F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderParticleFX(World world, double posX, double posY, double posZ, double velX, double velY,
            double velZ) {

        FXGeneric fb = new FXGeneric(world, posX, posY, posZ, velX, velY, velZ);
        fb.setMaxAge(40 + world.rand.nextInt(40));
        fb.setParticles(16, 1, 1);
        fb.setSlowDown(0.5);
        fb.setAlphaF(1.0F, 0.0F);
        fb.setScale((float) (0.699999988079071 + world.rand.nextGaussian() * 0.30000001192092896));
        int color = getAspect().getColor();
        fb.setRBGColorF(((color >> 16) & 0xFF) / 255.0F, ((color >> 8) & 0xFF) / 255.0F, (color & 0xFF) / 255.0F);
        fb.setRotationSpeed(world.rand.nextFloat(), 0.0F);
        ParticleEngine.addEffectWithDelay(world, fb, 0);
    }

}
