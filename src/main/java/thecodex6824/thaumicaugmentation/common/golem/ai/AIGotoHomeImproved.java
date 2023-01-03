/*
 *  Thaumic Augmentation
 *  Copyright (c) 2023 TheCodex6824.
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

package thecodex6824.thaumicaugmentation.common.golem.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import thaumcraft.common.golems.EntityThaumcraftGolem;

public class AIGotoHomeImproved extends EntityAIBase {

    protected static final int IDLE_WAIT = 30;

    protected EntityThaumcraftGolem entity;
    protected int idleTime;
    protected BlockPos target;
    protected double speed;
    protected boolean targetIsNotDest;
    
    public AIGotoHomeImproved(EntityThaumcraftGolem golem, double moveSpeed) {
        entity = golem;
        idleTime = IDLE_WAIT;
        speed = moveSpeed;
        targetIsNotDest = false;
    }
    
    @Override
    public boolean shouldExecute() {
        if (--idleTime == 0) {
            idleTime = IDLE_WAIT;
            double currentHomeDist = entity.getDistanceSqToCenter(entity.getHomePosition());
            if (currentHomeDist < 1.25)
                return false;
            else if (currentHomeDist > 64 * 64) {
                Vec3d targetPos = RandomPositionGenerator.findRandomTargetBlockTowards(entity, 16, 7,
                        new Vec3d(entity.getHomePosition()));
                if (targetPos == null)
                    return false;
                
                target = new BlockPos(targetPos);
                targetIsNotDest = true;
            }
            else
                target = entity.getHomePosition();
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public void startExecuting() {
        entity.getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), speed);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return entity.getTask() == null && entity.getDistanceSqToCenter(target) > 1.25;
    }

    @Override
    public void updateTask() {
        boolean updatePath = entity.getNavigator().noPath();
        if (targetIsNotDest && entity.getDistanceSqToCenter(target) <= 1.25) {
            double currentHomeDist = entity.getDistanceSqToCenter(entity.getHomePosition());
            if (currentHomeDist > 1024.0) {
                Vec3d targetPos = RandomPositionGenerator.findRandomTargetBlockTowards(entity, 16, 7,
                        new Vec3d(entity.getHomePosition()));
                if (targetPos != null)
                    target = new BlockPos(targetPos);
            }
            else {
                target = entity.getHomePosition();
                targetIsNotDest = false;
            }

            updatePath = true;
        }

        if (updatePath)
            entity.getNavigator().tryMoveToXYZ(target.getX(), target.getY(), target.getZ(), speed);
    }

    @Override
    public void resetTask() {
        entity.getNavigator().clearPath();
        idleTime = IDLE_WAIT;
        targetIsNotDest = false;
    }
    
}
