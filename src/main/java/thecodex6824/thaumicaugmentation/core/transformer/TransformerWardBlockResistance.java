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

package thecodex6824.thaumicaugmentation.core.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import thecodex6824.thaumicaugmentation.core.ThaumicAugmentationCore;

public class TransformerWardBlockResistance extends Transformer {

    private static final String CLASS = "net.minecraft.block.Block";
    
    @Override
    public boolean needToComputeFrames() {
        return false;
    }
    
    @Override
    public boolean isTransformationNeeded(String transformedName) {
        return !ThaumicAugmentationCore.getConfig().getBoolean("DisableWardFocus", "gameplay.ward", false, "") &&
                transformedName.equals(CLASS);
    }
    
    @Override
    public boolean isAllowedToFail() {
        return false;
    }
    
    @Override
    public boolean transform(ClassNode classNode, String name, String transformedName) {
        try {
            MethodNode resistance = TransformUtil.findMethod(classNode, TransformUtil.remapMethodName("net/minecraft/block/Block", "getExplosionResistance", Type.FLOAT_TYPE,
                    Type.getType("Lnet/minecraft/world/World;"), Type.getType("Lnet/minecraft/util/math/BlockPos;"), Type.getType("Lnet/minecraft/entity/Entity;"), Type.getType("Lnet/minecraft/world/Explosion;")),
                    "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;Lnet/minecraft/world/Explosion;)F");
            boolean found = false;
            int ret = 0;
            while ((ret = TransformUtil.findFirstInstanceOfOpcode(resistance, ret, Opcodes.FRETURN)) != -1) {
                AbstractInsnNode insertAfter = resistance.instructions.get(ret).getPrevious();
                resistance.instructions.insert(insertAfter, new MethodInsnNode(Opcodes.INVOKESTATIC,
                        TransformUtil.HOOKS_COMMON,
                        "checkWardResistance",
                        "(FLnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F",
                        false
                ));
                resistance.instructions.insert(insertAfter, new VarInsnNode(Opcodes.ALOAD, 2));
                resistance.instructions.insert(insertAfter, new VarInsnNode(Opcodes.ALOAD, 1));
                ret += 4;
                found = true;
            }
            
            if (!found)
                throw new TransformerException("Could not locate required instructions");
            
            return true;
        }
        catch (Throwable anything) {
            error = new RuntimeException(anything);
            return false;
        }
    }
    
}
