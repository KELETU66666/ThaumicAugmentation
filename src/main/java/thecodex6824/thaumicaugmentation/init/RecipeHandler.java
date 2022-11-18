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

package thecodex6824.thaumicaugmentation.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApi.BluePrint;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IDustTrigger;
import thaumcraft.api.crafting.Part;
import thaumcraft.api.crafting.ShapelessArcaneRecipe;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.common.blocks.basic.BlockPillar;
import thaumcraft.common.lib.crafting.DustTriggerMultiblock;
import thaumcraft.common.lib.enchantment.EnumInfusionEnchantment;
import thecodex6824.thaumicaugmentation.api.TABlocks;
import thecodex6824.thaumicaugmentation.api.TAItems;
import thecodex6824.thaumicaugmentation.api.ThaumicAugmentationAPI;
import thecodex6824.thaumicaugmentation.api.augment.CapabilityAugment;
import thecodex6824.thaumicaugmentation.api.augment.IAugment;
import thecodex6824.thaumicaugmentation.api.augment.builder.caster.CasterAugmentBuilder;
import thecodex6824.thaumicaugmentation.api.augment.builder.caster.ICustomCasterAugment;
import thecodex6824.thaumicaugmentation.api.item.*;
import thecodex6824.thaumicaugmentation.common.item.ItemThaumiumRobes.MaskType;
import thecodex6824.thaumicaugmentation.common.recipe.*;
import thecodex6824.thaumicaugmentation.common.util.MorphicArmorHelper;

import java.lang.reflect.Field;

public final class RecipeHandler {

    private RecipeHandler() {}
    
    private static final Field TRIGGER_RESEARCH;
    
    static {
        Field f = null;
        try {
            f = DustTriggerMultiblock.class.getDeclaredField("research");
            f.setAccessible(true);
        }
        catch (Exception ex) {
            FMLCommonHandler.instance().raiseException(ex, "Failed to access Thaumcraft's DustTriggerMultiblock#research", true);
        }
        
        TRIGGER_RESEARCH = f;
    }
    
    public static void initInfusionRecipes() {
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumium_robes_hood"),
            new InfusionRecipeComplexResearch("THAUMIUM_ROBES", new ItemStack(TAItems.THAUMIUM_ROBES_HOOD), 3,
                    new AspectList().add(Aspect.METAL, 15).add(Aspect.ENERGY, 30).add(Aspect.PROTECT, 20).add(Aspect.SENSES, 35).add(Aspect.AURA, 20),
                    ItemsTC.goggles,
                    ItemsTC.fabric, ItemsTC.fabric, ItemsTC.salisMundus, "plateThaumium", "leather"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumium_robes_chestplate"),
            new InfusionRecipeComplexResearch("THAUMIUM_ROBES", new ItemStack(TAItems.THAUMIUM_ROBES_CHESTPLATE), 3,
                    new AspectList().add(Aspect.METAL, 30).add(Aspect.ENERGY, 40).add(Aspect.PROTECT, 35).add(Aspect.AURA, 10),
                    ItemsTC.clothChest,
                    ItemsTC.fabric, ItemsTC.fabric, ItemsTC.fabric, ItemsTC.salisMundus, "plateThaumium", "plateThaumium"));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumium_robes_leggings"),
            new InfusionRecipeComplexResearch("THAUMIUM_ROBES", new ItemStack(TAItems.THAUMIUM_ROBES_LEGGINGS), 3,
                    new AspectList().add(Aspect.METAL, 25).add(Aspect.ENERGY, 20).add(Aspect.PROTECT, 25).add(Aspect.AURA, 5),
                    ItemsTC.clothLegs,
                    ItemsTC.fabric, new ItemStack(ItemsTC.baubles, 1, 2), ItemsTC.salisMundus, "plateThaumium", "plateThaumium"));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "gauntlet_void"), 
            new InfusionRecipeComplexResearch("GAUNTLET_VOID", new ItemStack(TAItems.GAUNTLET, 1, 1), 6, 
                    new AspectList().add(Aspect.ENERGY, 50).add(Aspect.ELDRITCH, 75).add(Aspect.VOID, 75), 
                    ItemsTC.charmVoidseer,
                    ItemsTC.fabric, "plateVoid", "plateVoid", "plateVoid", "plateVoid", ItemsTC.salisMundus));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "seal_copier"), 
            new InfusionRecipeComplexResearch("SEAL_COPIER", new ItemStack(TAItems.SEAL_COPIER), 1, 
                    new AspectList().add(Aspect.MIND, 25).add(Aspect.MECHANISM, 10), 
                    ItemsTC.golemBell,
                    ItemsTC.brain, ItemsTC.brain, new ItemStack(ItemsTC.seals, 1, 0)));
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "boots_void"),
            new InfusionRecipeComplexResearch("BOOTS_VOID", new ItemStack(TAItems.VOID_BOOTS), 6,
                    new AspectList().add(Aspect.VOID, 50).add(Aspect.ELDRITCH, 50).add(Aspect.MOTION, 150).add(Aspect.FLIGHT, 150), 
                    ItemsTC.travellerBoots,
                    ItemsTC.fabric, ItemsTC.fabric, "plateVoid", "plateVoid", "feather",
                    Items.FISH, ItemsTC.primordialPearl, "quicksilver"));

        for (int i = 1; i < 4; ++i) {
            ItemStack in = new ItemStack(TAItems.RIFT_SEED, 1, 1);
            in.setTagCompound(new NBTTagCompound());
            in.getTagCompound().setInteger("flux", 100);
            in.getTagCompound().setBoolean("grown", false);
            ItemStack out = in.copy();
            out.getTagCompound().setInteger("flux", i == 3 ? 1000 : i * 100 + 100);
            out.getTagCompound().setBoolean("grown", true);
            Object[] inputs = new Object[i == 3 ? 9 : i];
            for (int k = 0; k < (i == 3 ? 9 : i); ++k)
                inputs[k] = ThaumcraftApiHelper.makeCrystal(Aspect.FLUX);

            ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "flux_seed_growth_fake" + (i == 3 ? 1000 : i * 100 + 100)), 
                    new InfusionRecipeComplexResearch("RIFT_STUDIES", out, i == 3 ? 9 : i, new AspectList().add(Aspect.FLUX, i == 3 ? 500 : i * 50),
                            in, inputs));
        }

        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "flux_seed_growth"), 
                new FluxSeedGrowthRecipe());
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "rift_energy_cell"),
                new InfusionRecipeComplexResearch("RIFT_POWER@2", new ItemStack(TAItems.MATERIAL, 1, 3), 8, new AspectList().add(Aspect.ELDRITCH, 25).add(Aspect.VOID, 50).add(Aspect.ENERGY, 100),
                ItemsTC.voidSeed,
                        "plateVoid", ItemsTC.primordialPearl, "plateVoid", ThaumcraftApiHelper.makeCrystal(Aspect.ELDRITCH),
                        "plateVoid", "dustRedstone", "plateVoid", "gemAmber"));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "morphic_tool_binding"),
                new MorphicToolBindingRecipe());
        
        ItemStack morphicSample = new ItemStack(TAItems.MORPHIC_TOOL);
        IMorphicItem tool = morphicSample.getCapability(CapabilityMorphicTool.MORPHIC_TOOL, null);
        tool.setDisplayStack(new ItemStack(Items.GOLDEN_HOE));
        ((IMorphicTool) tool).setFunctionalStack(new ItemStack(Items.DIAMOND_SWORD));
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "morphic_tool_binding_fake"),
                new InfusionRecipeComplexResearch("MORPHIC_TOOL", morphicSample, 5, new AspectList().add(Aspect.VOID, 15),
                Items.DIAMOND_SWORD,
                        ItemsTC.primordialPearl, Items.GOLDEN_HOE, ItemsTC.quicksilver));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "morphic_armor_binding"),
                new MorphicArmorBindingRecipe());
        
        morphicSample = new ItemStack(Items.DIAMOND_CHESTPLATE);
        MorphicArmorHelper.setMorphicArmor(morphicSample, new ItemStack(Items.GOLDEN_CHESTPLATE));
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "morphic_armor_binding_fake"),
                new InfusionRecipeComplexResearch("MORPHIC_ARMOR", morphicSample, 8, new AspectList().add(Aspect.VOID, 100),
                Items.DIAMOND_CHESTPLATE,
                        ItemsTC.primordialPearl, Items.GOLDEN_CHESTPLATE, ItemsTC.quicksilver));
        
        ItemStack cutter = new ItemStack(TAItems.PRIMAL_CUTTER);
        EnumInfusionEnchantment.addInfusionEnchantment(cutter, EnumInfusionEnchantment.ARCING, 2);
        EnumInfusionEnchantment.addInfusionEnchantment(cutter, EnumInfusionEnchantment.BURROWING, 1);
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "primal_cutter"),
                new InfusionRecipeComplexResearch("PRIMAL_CUTTER", cutter, 7, new AspectList().add(Aspect.EARTH, 75).add(
                Aspect.TOOL, 50).add(Aspect.MAGIC, 50).add(Aspect.VOID, 50).add(Aspect.AVERSION, 75).add(
                Aspect.ELDRITCH, 50).add(Aspect.DESIRE, 50), ItemsTC.primordialPearl,
                        ItemsTC.voidAxe, ItemsTC.voidSword, ItemsTC.elementalAxe, ItemsTC.elementalSword));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "autocaster"), new InfusionRecipeComplexResearch(
                "AUTOCASTER", new ItemStack(TAItems.AUTOCASTER_PLACER, 1, 0), 4, new AspectList().add(Aspect.AURA, 25).add(Aspect.AVERSION, 25).add(Aspect.MIND, 30).add(
                Aspect.MECHANISM, 50).add(Aspect.SENSES, 25), new ItemStack(ItemsTC.mind, 1, 1),
                ItemsTC.visResonator, ItemsTC.morphicResonator, ItemsTC.mechanismSimple, BlocksTC.plankGreatwood, BlocksTC.plankGreatwood,
                "plateBrass", ItemsTC.casterBasic));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "impetus_mirror"), new InfusionRecipeComplexResearch(
                "IMPETUS_MIRROR", new ItemStack(TAItems.IMPETUS_MIRROR), 4, new AspectList().add(Aspect.MOTION, 25).add(Aspect.EXCHANGE, 25).add(Aspect.ENERGY, 25), ItemsTC.mirroredGlass,
                Items.ENDER_PEARL, BlocksTC.stoneEldritchTile, new ItemStack(TAItems.MATERIAL, 1, 5), "plateVoid"));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "rift_mover_input"), new InfusionRecipeComplexResearch(
                "RIFT_MOVING", new ItemStack(TABlocks.RIFT_MOVER_INPUT), 6, new AspectList().add(Aspect.FLUX, 25).add(Aspect.ELDRITCH, 20).add(Aspect.TRAP, 50).add(
                        Aspect.EXCHANGE, 10).add(Aspect.MECHANISM, 30).add(Aspect.VOID, 30), "blockAmber",
                "plateIron", BlocksTC.stoneArcane, "plateVoid", ItemsTC.mechanismComplex, "plateBrass", BlocksTC.stoneArcane, "plateVoid", ItemsTC.voidSeed));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "rift_mover_output"), new InfusionRecipeComplexResearch(
                "RIFT_MOVING", new ItemStack(TABlocks.RIFT_MOVER_OUTPUT), 6, new AspectList().add(Aspect.FLUX, 25).add(Aspect.ELDRITCH, 20).add(Aspect.ENTROPY, 50).add(
                        Aspect.EXCHANGE, 10).add(Aspect.MECHANISM, 30).add(Aspect.VOID, 30), ItemsTC.alumentum,
                ItemsTC.mechanismSimple, "plateVoid", BlocksTC.stoneEldritchTile, "plateBrass", "plateVoid", ItemsTC.voidSeed));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "rift_feeder"), new InfusionRecipeComplexResearch(
                "RIFT_FEEDER", new ItemStack(TABlocks.RIFT_FEEDER), 5, new AspectList().add(Aspect.FLUX, 30).add(Aspect.ELDRITCH, 10).add(Aspect.MECHANISM, 50).add(Aspect.VOID, 5),
                BlocksTC.essentiaTransportInput,
                ItemsTC.mechanismSimple, "plateBrass", BlocksTC.tube, ItemsTC.morphicResonator, BlocksTC.plankGreatwood, BlocksTC.tube));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumostatic_harness"), new InfusionRecipeComplexResearch(
                "THAUMOSTATIC_HARNESS", new ItemStack(TAItems.THAUMOSTATIC_HARNESS), 6, new AspectList().add(Aspect.MECHANISM, 50).add(Aspect.MOTION, 25).add(Aspect.ENERGY, 50).add(Aspect.FLIGHT, 50),
                new ItemStack(TAItems.MATERIAL, 1, 4),
                ThaumcraftApiHelper.makeCrystal(Aspect.AIR), ThaumcraftApiHelper.makeCrystal(Aspect.AIR), BlocksTC.levitator, BlocksTC.plankGreatwood, BlocksTC.plankGreatwood,
                ItemsTC.morphicResonator));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumostatic_gyroscope"), new InfusionRecipeComplexResearch(
                "THAUMOSTATIC_GYROSCOPE", new ItemStack(TAItems.THAUMOSTATIC_HARNESS_AUGMENT, 1, 0), 5, new AspectList().add(Aspect.TRAP, 35).add(Aspect.AIR, 25).add(Aspect.FLIGHT, 25),
                new ItemStack(ItemsTC.baubles, 1, 2),
                "dustRedstone", "plateThaumium", ThaumcraftApiHelper.makeCrystal(Aspect.TRAP), "plateThaumium"));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumostatic_girdle"), new InfusionRecipeComplexResearch(
                "THAUMOSTATIC_GIRDLE", new ItemStack(TAItems.THAUMOSTATIC_HARNESS_AUGMENT, 1, 1), 8, new AspectList().add(Aspect.AIR, 50).add(Aspect.MOTION, 25).add(Aspect.FLIGHT, 25),
                new ItemStack(ItemsTC.baubles, 1, 2),
                "feather", ThaumcraftApiHelper.makeCrystal(Aspect.FLIGHT), "ingotGold", "feather", ThaumcraftApiHelper.makeCrystal(Aspect.AIR), "ingotGold"));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "elytra_harness"), new InfusionRecipeComplexResearch(
                "ELYTRA_HARNESS", new ItemStack(TAItems.ELYTRA_HARNESS), 9, new AspectList().add(Aspect.MOTION, 75).add(Aspect.ENERGY, 50).add(Aspect.FLIGHT, 50),
                new ItemStack(TAItems.MATERIAL, 1, 4),
                "plateVoid", ItemsTC.visResonator, "feather", Items.ELYTRA));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "elytra_booster"), new InfusionRecipeComplexResearch(
                "ELYTRA_BOOSTER", new ItemStack(TAItems.ELYTRA_HARNESS_AUGMENT, 1, 0), 11, new AspectList().add(Aspect.MOTION, 75).add(Aspect.ENERGY, 50).add(Aspect.FLIGHT, 45).add(Aspect.ELDRITCH, 25),
                new ItemStack(ItemsTC.baubles, 1, 2),
                new ItemStack(TAItems.MATERIAL, 1, 3), ThaumcraftApiHelper.makeCrystal(Aspect.ELDRITCH), "plateVoid", new ItemStack(TAItems.MATERIAL, 1, 5),
                ThaumcraftApiHelper.makeCrystal(Aspect.VOID), "plateVoid"));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "impulse_cannon"), new InfusionRecipeComplexResearch(
                "IMPULSE_CANNON", new ItemStack(TAItems.IMPULSE_CANNON, 1, 0), 12, new AspectList().add(Aspect.ELDRITCH, 75).add(Aspect.AVERSION, 75).add(Aspect.ENERGY, 75).add(Aspect.MECHANISM, 50).add(
                        Aspect.DEATH, 50).add(Aspect.VOID, 30).add(Aspect.DARKNESS, 25),
                new ItemStack(TAItems.MATERIAL, 1, 3),
                BlocksTC.stabilizer, ItemsTC.focus3, "plateBrass", ItemsTC.morphicResonator, BlocksTC.inlay, "plateVoid", new ItemStack(TAItems.MATERIAL, 1, 5), ItemsTC.mechanismComplex,
                "plateThaumium", "plateVoid"));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumium_robes_hood_warp"), new InfusionRecipeComplexResearch(
                "THAUMIUM_ROBES&&FORTRESSMASK", new Object[] {"maskType", new NBTTagInt(MaskType.WARP_REDUCTION.getID())}, 8, new AspectList().add(Aspect.MIND, 80).add(Aspect.LIFE, 80).add(Aspect.PROTECT, 20),
                new ItemStack(TAItems.THAUMIUM_ROBES_HOOD, 1, OreDictionary.WILDCARD_VALUE),
                "plateIron", "dyeBlack", "plateIron", "leather", BlocksTC.shimmerleaf, ItemsTC.brain));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumium_robes_hood_wither"), new InfusionRecipeComplexResearch(
                "THAUMIUM_ROBES&&FORTRESSMASK", new Object[] {"maskType", new NBTTagInt(MaskType.WITHER.getID())}, 8, new AspectList().add(Aspect.ENTROPY, 80).add(Aspect.DEATH, 80).add(Aspect.PROTECT, 20),
                new ItemStack(TAItems.THAUMIUM_ROBES_HOOD, 1, OreDictionary.WILDCARD_VALUE),
                "plateIron", "dyeWhite", "plateIron", "leather", Items.POISONOUS_POTATO, new ItemStack(Items.SKULL, 1, 1)));
        
        ThaumcraftApi.addInfusionCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "thaumium_robes_hood_lifesteal"), new InfusionRecipeComplexResearch(
                "THAUMIUM_ROBES&&FORTRESSMASK", new Object[] {"maskType", new NBTTagInt(MaskType.LIFESTEAL.getID())}, 8, new AspectList().add(Aspect.UNDEAD, 80).add(Aspect.LIFE, 80).add(Aspect.PROTECT, 20),
                new ItemStack(TAItems.THAUMIUM_ROBES_HOOD, 1, OreDictionary.WILDCARD_VALUE),
                "plateIron", "dyeRed", "plateIron", "leather", Items.GHAST_TEAR, Items.MILK_BUCKET));
    }
    
    public static void initCrucibleRecipes() {
        ItemStack fluxSeedStack = new ItemStack(TAItems.RIFT_SEED, 1, 1);
        fluxSeedStack.setTagCompound(new NBTTagCompound());
        fluxSeedStack.getTagCompound().setInteger("flux", 100);
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "flux_seed"), 
                new CrucibleRecipe("RIFT_STUDIES", fluxSeedStack, ItemsTC.voidSeed, 
                        new AspectList().add(Aspect.FLUX, 50)));
        
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "effect_provider_power"),
                new CrucibleRecipe("GAUNTLET_AUGMENTATION@2", CasterAugmentBuilder.createStackForEffectProvider(
                        new ResourceLocation(ThaumicAugmentationAPI.MODID, "effect_power")),
                        ThaumcraftApiHelper.makeCrystal(Aspect.ORDER), new AspectList().add(Aspect.AVERSION, 15).add(Aspect.CRYSTAL, 10).add(Aspect.MAGIC, 5)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "effect_provider_cost"),
                new CrucibleRecipe("GAUNTLET_AUGMENTATION@2", CasterAugmentBuilder.createStackForEffectProvider(
                        new ResourceLocation(ThaumicAugmentationAPI.MODID, "effect_cost")),
                        ThaumcraftApiHelper.makeCrystal(Aspect.ORDER), new AspectList().add(Aspect.AURA, 15).add(Aspect.CRYSTAL, 10).add(Aspect.MAGIC, 5)));
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "effect_provider_cast_speed"),
                new CrucibleRecipe("GAUNTLET_AUGMENTATION@2", CasterAugmentBuilder.createStackForEffectProvider(
                        new ResourceLocation(ThaumicAugmentationAPI.MODID, "effect_cast_speed")),
                        ThaumcraftApiHelper.makeCrystal(Aspect.ORDER), new AspectList().add(Aspect.ENERGY, 15).add(Aspect.CRYSTAL, 10).add(Aspect.MAGIC, 5)));
    
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "seal_secure"),
                new CrucibleRecipe("SEAL_SECURE", GolemHelper.getSealStack(ThaumicAugmentationAPI.MODID + ":attack"), new ItemStack(ItemsTC.seals, 1, 0),
                        new AspectList().add(Aspect.AVERSION, 30).add(Aspect.PROTECT, 10)));
        
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "seal_secure_advanced"),
                new CrucibleRecipe("SEAL_SECURE&&MINDBIOTHAUMIC", GolemHelper.getSealStack(ThaumicAugmentationAPI.MODID + ":attack_advanced"), GolemHelper.getSealStack(ThaumicAugmentationAPI.MODID + ":attack"),
                        new AspectList().add(Aspect.SENSES, 20).add(Aspect.MIND, 20)));
        
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "eldritch_stone"),
                new CrucibleRecipe("VOID_STONE_USAGE", new ItemStack(BlocksTC.stoneEldritchTile), "stoneVoid", new AspectList().add(Aspect.ELDRITCH, 8)));
    
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "impetus_jewel"), new CrucibleRecipe(
                "IMPETUS", new ItemStack(TAItems.MATERIAL, 2, 5), ItemsTC.voidSeed, new AspectList().add(Aspect.ORDER, 25).add(Aspect.ENERGY, 10)));
    
        ThaumcraftApi.addCrucibleRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "ancient_cobblestone"), new CrucibleRecipe(
                "ELDRITCH_SPIRE@1", new ItemStack(TABlocks.STONE, 1, 10), BlocksTC.stoneAncient, new AspectList().add(Aspect.ENTROPY, 2)));
    }
    
    public static void initArcaneCraftingRecipes() {
        // so the "group" is just a recipe book thing, which we don't have to care about
        ResourceLocation defaultGroup = new ResourceLocation("");
        
        ItemStack elemental = CasterAugmentBuilder.createStackForStrengthProvider(new ResourceLocation(ThaumicAugmentationAPI.MODID, "strength_elemental"));
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "strength_provider_elemental_fake"), new ShapelessArcaneRecipe(
                defaultGroup, "GAUNTLET_AUGMENTATION@2", 25, new AspectList().add(Aspect.AIR, 1).add(Aspect.EARTH, 1).add(Aspect.FIRE, 1).add(
                Aspect.ENTROPY, 1).add(Aspect.ORDER, 1).add(Aspect.WATER, 1), elemental, new Object[] { new ItemStack(ItemsTC.plate, 1, 2),
                ThaumcraftApiHelper.makeCrystal(Aspect.ORDER, 1), ItemsTC.visResonator }));
        
        ItemStack customAugment = new ItemStack(TAItems.AUGMENT_CUSTOM);
        IAugment aug = customAugment.getCapability(CapabilityAugment.AUGMENT, null);
        if (aug instanceof ICustomCasterAugment) {
            ICustomCasterAugment custom = (ICustomCasterAugment) aug;
            custom.setStrengthProvider(elemental);
            custom.setEffectProvider(CasterAugmentBuilder.createStackForEffectProvider(new ResourceLocation(ThaumicAugmentationAPI.MODID, "effect_power")));
            ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "custom_augment_example"), new ShapelessOreRecipe(
                    defaultGroup, customAugment, custom.getStrengthProvider(), custom.getEffectProvider()));
        }
        
        ThaumcraftApi.addArcaneCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "strength_provider_elemental"), new ElementalAugmentCraftingRecipe());
        
        ItemStack biomeResult = new ItemStack(TAItems.BIOME_SELECTOR, 1);
        IBiomeSelector selector = biomeResult.getCapability(CapabilityBiomeSelector.BIOME_SELECTOR, null);
        if (selector != null)
            selector.setBiomeID(IBiomeSelector.RESET);
        ThaumcraftApi.addFakeCraftingRecipe(new ResourceLocation(ThaumicAugmentationAPI.MODID, "biome_focus_special_reset"), new ShapelessOreRecipe(
                defaultGroup, biomeResult, TAItems.BIOME_SELECTOR, ItemsTC.primordialPearl));
    }
    
    public static void initMultiblocks() {
        Part matrix = new Part(BlocksTC.infusionMatrix, TABlocks.IMPETUS_MATRIX);
        Part base = new Part(BlocksTC.pedestalEldritch, TABlocks.IMPETUS_MATRIX_BASE);
        Part[][][] blueprint = new Part[][][] {
            {
                {base}
            },
            {
                {matrix}
            },
            {
                {base}
            }
        };
        
        IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("IMPETUS_MATRIX", blueprint));
        ThaumcraftApi.addMultiblockRecipeToCatalog(new ResourceLocation(ThaumicAugmentationAPI.MODID, "impetus_matrix"), new BluePrint("IMPETUS_MATRIX", blueprint, new ItemStack(BlocksTC.infusionMatrix),
                new ItemStack(BlocksTC.pedestalEldritch, 2)));
        
        fixInfusionAltarMultiblocks();
    }
    
    public static String getDustTriggerResearch(DustTriggerMultiblock trigger) {
        try {
            return (String) TRIGGER_RESEARCH.get(trigger);
        }
        catch (Exception ex) {
            FMLCommonHandler.instance().raiseException(ex, "Failed to invoke Thaumcraft's DustTriggerMultiblock#research", true);
            return null;
        }
    }
    
    /*
     * The multiblock recipes registered by TC require a pedestal with a meta value
     * of 1 for ancient and 2 for eldritch. However, the meta value is for the redstone inlay power
     * going through the pedestal (the arcane/ancient/eldritch pillars are their own blocks).
     * Removing the metadata requirement fixes the multiblocks. This was probably an oversight in
     * the last few betas (since the stablizier mechanics were reworked).
     */
    public static void fixInfusionAltarMultiblocks() {
        Part matrix = new Part(BlocksTC.infusionMatrix, null);
        
        Part ancientStone = new Part(BlocksTC.stoneAncient, "AIR");
        Part ancientPillarEast = new Part(BlocksTC.stoneAncient, new ItemStack(BlocksTC.pillarAncient, 1, BlockPillar.calcMeta(EnumFacing.EAST)));
        Part ancientPillarNorth = new Part(BlocksTC.stoneAncient, new ItemStack(BlocksTC.pillarAncient, 1, BlockPillar.calcMeta(EnumFacing.NORTH)));
        Part ancientPillarSouth = new Part(BlocksTC.stoneAncient, new ItemStack(BlocksTC.pillarAncient, 1, BlockPillar.calcMeta(EnumFacing.SOUTH)));
        Part ancientPillarWest = new Part(BlocksTC.stoneAncient, new ItemStack(BlocksTC.pillarAncient, 1, BlockPillar.calcMeta(EnumFacing.WEST)));
        Part ancientPedestal = new Part(BlocksTC.pedestalAncient, null);
        Part[][][] ancientBlueprint = new Part[][][] {
            {
                {null, null, null},
                {null, matrix, null},
                {null, null, null}
            },
            {
                {ancientStone, null, ancientStone},
                {null, null, null},
                {ancientStone, null, ancientStone}
            },
            {
                {ancientPillarEast, null, ancientPillarNorth},
                {null, ancientPedestal, null},
                {ancientPillarSouth, null, ancientPillarWest}
            }
        };
        
        // IDustTrigger stores an ArrayList of triggers, so we need to find / remove ourselves
        for (int i = 0; i < IDustTrigger.triggers.size(); ++i) {
            IDustTrigger trigger = IDustTrigger.triggers.get(i);
            if (trigger instanceof DustTriggerMultiblock && getDustTriggerResearch((DustTriggerMultiblock) trigger).equals("INFUSIONANCIENT")) {
                IDustTrigger.triggers.remove(i);
                break;
            }
        }
        
        IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("INFUSIONANCIENT", ancientBlueprint));
        // the catalog is just a map so this is ok
        ThaumcraftApi.addMultiblockRecipeToCatalog(new ResourceLocation("thaumcraft", "infusionaltarancient"), new BluePrint("INFUSIONANCIENT", ancientBlueprint, new ItemStack(BlocksTC.stoneAncient, 8),
                new ItemStack(BlocksTC.pedestalAncient),
                new ItemStack(BlocksTC.infusionMatrix)));
        
        Part eldritchStone = new Part(BlocksTC.stoneEldritchTile, "AIR");
        Part eldritchPillarEast = new Part(BlocksTC.stoneEldritchTile, new ItemStack(BlocksTC.pillarEldritch, 1, BlockPillar.calcMeta(EnumFacing.EAST)));
        Part eldritchPillarNorth = new Part(BlocksTC.stoneEldritchTile, new ItemStack(BlocksTC.pillarEldritch, 1, BlockPillar.calcMeta(EnumFacing.NORTH)));
        Part eldritchPillarSouth = new Part(BlocksTC.stoneEldritchTile, new ItemStack(BlocksTC.pillarEldritch, 1, BlockPillar.calcMeta(EnumFacing.SOUTH)));
        Part eldritchPillarWest = new Part(BlocksTC.stoneEldritchTile, new ItemStack(BlocksTC.pillarEldritch, 1, BlockPillar.calcMeta(EnumFacing.WEST)));
        Part eldritchPedestal = new Part(BlocksTC.pedestalEldritch, null);
        Part[][][] eldritchBlueprint = new Part[][][] {
            {
                {null, null, null},
                {null, matrix, null},
                {null, null, null}
            },
            {
                {eldritchStone, null, eldritchStone},
                {null, null, null},
                {eldritchStone, null, eldritchStone}
            },
            {
                {eldritchPillarEast, null, eldritchPillarNorth},
                {null, eldritchPedestal, null},
                {eldritchPillarSouth, null, eldritchPillarWest}
            }
        };
        
        for (int i = 0; i < IDustTrigger.triggers.size(); ++i) {
            IDustTrigger trigger = IDustTrigger.triggers.get(i);
            if (trigger instanceof DustTriggerMultiblock && getDustTriggerResearch((DustTriggerMultiblock) trigger).equals("INFUSIONELDRITCH")) {
                IDustTrigger.triggers.remove(i);
                break;
            }
        }
        
        IDustTrigger.registerDustTrigger(new DustTriggerMultiblock("INFUSIONELDRITCH", eldritchBlueprint));
        ThaumcraftApi.addMultiblockRecipeToCatalog(new ResourceLocation("thaumcraft", "infusionaltareldritch"), new BluePrint("INFUSIONELDRITCH", eldritchBlueprint, new ItemStack(BlocksTC.stoneEldritchTile, 8),
                new ItemStack(BlocksTC.pedestalEldritch),
                new ItemStack(BlocksTC.infusionMatrix)));
    }
    
}
