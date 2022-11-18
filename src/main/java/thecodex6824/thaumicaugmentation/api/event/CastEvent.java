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

package thecodex6824.thaumicaugmentation.api.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import thecodex6824.thaumicaugmentation.api.util.FocusWrapper;

/*
 * Event superclass for all cast-related events. Subscribing to this event will notify the
 * callback for all subclasses of this class.
 * @author TheCodex6824
 */
public abstract class CastEvent extends LivingEvent {

    protected FocusWrapper focus;
    protected ItemStack caster;
    
    public CastEvent(EntityLivingBase living, ItemStack casterStack, FocusWrapper fPackage) {
        super(living);
        caster = casterStack;
        focus = fPackage;
    }
    
    public ItemStack getCasterStack() {
        return caster;
    }
    
    public FocusWrapper getFocus() {
        return focus;
    }
    
    @Cancelable
    public static class Pre extends CastEvent {
        
        public Pre(EntityLivingBase living, ItemStack casterStack, FocusWrapper fPackage) {
            super(living, casterStack, fPackage);
        }
        
    }
    
    public static class Post extends CastEvent {
        
        public Post(EntityLivingBase living, ItemStack casterStack, FocusWrapper fPackage) {
            super(living, casterStack, fPackage);
        }
        
    }
    
}
