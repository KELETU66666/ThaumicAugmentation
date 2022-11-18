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

package thecodex6824.thaumicaugmentation.common.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityItemImportant extends EntityItemIndestructible {

    public EntityItemImportant(World world) {
        super(world);
    }
    
    public EntityItemImportant(World world, double x, double y, double z) {
        super(world, x, y, z);
    }
    
    public EntityItemImportant(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        motionX *= 0.9;
        motionY *= 0.9;
        motionZ *= 0.9;
    }
    
    @Override
    public boolean hasNoGravity() {
        return true;
    }
    
    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 0 || pass == 1;
    }
    
}
