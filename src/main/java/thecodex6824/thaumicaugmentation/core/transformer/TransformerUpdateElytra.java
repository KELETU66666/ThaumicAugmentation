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

package thecodex6824.thaumicaugmentation.core.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

public class TransformerUpdateElytra  extends Transformer {

    private static final String CLASS = "net.minecraft.entity.EntityLivingBase";
    
    @Override
    public boolean needToComputeFrames() {
        return false;
    }
    
    @Override
    public boolean isTransformationNeeded(String transformedName) {
        return transformedName.equals(CLASS);
    }
    
    @Override
    public boolean isAllowedToFail() {
        return false;
    }
    
    @Override
    public boolean transform(ClassNode classNode, String name, String transformedName) {
        try {
            MethodNode update = TransformUtil.findMethod(classNode, TransformUtil.remapMethodName("net/minecraft/entity/EntityLivingBase", "func_184616_r",
                    Type.VOID_TYPE), "()V");
            int con = TransformUtil.findFirstInstanceOfOpcode(update, 0, Opcodes.ICONST_0);
            int ret = TransformUtil.findFirstInstanceOfOpcode(update, con, Opcodes.ISTORE);
            if (con != -1 && ret != -1 && ret == con + 1) {
                AbstractInsnNode insertAfter = update.instructions.get(ret);
                update.instructions.insert(insertAfter, new VarInsnNode(Opcodes.ISTORE, 1));
                update.instructions.insert(insertAfter, new MethodInsnNode(Opcodes.INVOKESTATIC,
                        TransformUtil.HOOKS_COMMON,
                        "updateElytraFlag",
                        "(Lnet/minecraft/entity/EntityLivingBase;Z)Z",
                        false
                ));
                update.instructions.insert(insertAfter, new VarInsnNode(Opcodes.ILOAD, 1));
                update.instructions.insert(insertAfter, new VarInsnNode(Opcodes.ALOAD, 0));
            }
            else
                throw new TransformerException("Could not locate required instructions");
            
            return true;
        }
        catch (Throwable anything) {
            error = new RuntimeException(anything);
            return false;
        }
    }
    
}