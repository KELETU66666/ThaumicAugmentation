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

package thecodex6824.thaumicaugmentation.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.items.IWarpingGear;
import thaumcraft.common.lib.SoundsTC;
import thecodex6824.thaumicaugmentation.api.TAMaterials;
import thecodex6824.thaumicaugmentation.common.entity.EntityItemIndestructible;
import thecodex6824.thaumicaugmentation.common.item.prefab.ItemTABase;

import javax.annotation.Nullable;
import java.util.List;

public class ItemResearchNotes extends ItemTABase implements IWarpingGear {

    public ItemResearchNotes() {
        super("eldritch");
        setMaxStackSize(1);
    }
    
    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return TAMaterials.RARITY_ELDRITCH;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            player.sendStatusMessage(new TextComponentTranslation("thaumicaugmentation.text.research_notes_use").setStyle(new Style()
                .setColor(TextFormatting.DARK_PURPLE)), true);
            world.playSound(null, player.getPosition().up(), SoundsTC.learn, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
        }
        
        return super.onItemRightClick(world, player, hand);
    }
    
    @Override
    public int getWarp(ItemStack stack, EntityPlayer player) {
        if (stack.getMetadata() == 0)
            return 5;
        else
            return 0;
    }
    
    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }
    
    @Override
    @Nullable
    public Entity createEntity(World world, Entity location, ItemStack stack) {
        EntityItemIndestructible item = new EntityItemIndestructible(world, location.posX, location.posY, location.posZ, stack);
        item.setDefaultPickupDelay();
        item.setNoDespawn();
        item.motionX = location.motionX;
        item.motionY = location.motionY;
        item.motionZ = location.motionZ;
        if (location instanceof EntityItem) {
            item.setThrower(((EntityItem) location).getThrower());
            item.setOwner(((EntityItem) location).getOwner());
        }
        
        return item;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        int meta = Math.min(subItemNames.length, stack.getMetadata());
        tooltip.add(new TextComponentTranslation("thaumicaugmentation.text.research_notes_" + subItemNames[meta]).setStyle(new Style()
                .setItalic(true)
                .setColor(TextFormatting.DARK_PURPLE))
            .getFormattedText());
    }
    
}
