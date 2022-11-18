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

package thecodex6824.thaumicaugmentation.common.world.biome;

import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;
import thaumcraft.common.entities.monster.tainted.EntityTaintCrawler;
import thaumcraft.common.entities.monster.tainted.EntityTaintacle;
import thaumcraft.common.entities.monster.tainted.EntityTaintacleSmall;
import thecodex6824.thaumicaugmentation.api.TABlocks;
import thecodex6824.thaumicaugmentation.api.block.property.ITAStoneType;
import thecodex6824.thaumicaugmentation.api.block.property.ITAStoneType.StoneType;
import thecodex6824.thaumicaugmentation.api.world.IPurgeBiomeSpawns;
import thecodex6824.thaumicaugmentation.common.world.feature.WorldGenTaintFlower;

import java.util.Random;

public class BiomeTaintedLands extends Biome implements IPurgeBiomeSpawns, IFluxBiome, IBiomeSpecificSpikeBlockProvider {

    protected static final WorldGenTaintFlower FLOWER_GENERATOR = new WorldGenTaintFlower();
    protected static final Random POS_RNG = new Random();
    
    public BiomeTaintedLands() {
        super(new BiomeProperties("Tainted Lands").setBaseHeight(-1.8F).setHeightVariation(0.15F).setRainDisabled().setTemperature(
                0.25F).setWaterColor(0xFF00FF));

        purgeSpawns();
        flowers.clear();
        topBlock = TABlocks.STONE.getDefaultState().withProperty(ITAStoneType.STONE_TYPE, StoneType.SOIL_STONE_TAINT_NODECAY);
        fillerBlock = TABlocks.STONE.getDefaultState().withProperty(ITAStoneType.STONE_TYPE, StoneType.STONE_VOID);
    }
    
    @Override
    public void purgeSpawns() {
        spawnableCreatureList.clear();
        spawnableMonsterList.clear();
        spawnableMonsterList.add(new SpawnListEntry(EntityTaintCrawler.class, 100, 3, 5));
        spawnableMonsterList.add(new SpawnListEntry(EntityTaintacleSmall.class, 75, 1, 2));
        spawnableMonsterList.add(new SpawnListEntry(EntityTaintacle.class, 50, 1, 1));
        spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 1, 2, 2));
        
        spawnableWaterCreatureList.clear();
        spawnableCaveCreatureList.clear();
    }
    
    @Override
    public IBlockState getSpikeState(World world, BlockPos pos) {
        return TABlocks.STONE.getDefaultState().withProperty(ITAStoneType.STONE_TYPE, StoneType.STONE_TAINT_NODECAY);
    }
    
    @Override
    public float getBaseFluxConcentration() {
        return 0.5F;
    }

    @Override
    public boolean canRain() {
        return false;
    }

    @Override
    public int getSkyColorByTemp(float currentTemperature) {
        return 0;
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal) {
        int cX = x & 15;
        int cZ = z & 15;
        for (int y = Math.min(world.getActualHeight(), 255); y >= 0; --y) {
            IBlockState current = primer.getBlockState(cZ, y, cX);
            if (!current.getBlock().isAir(current, world, new BlockPos(x, y, z)))
                primer.setBlockState(cZ, y, cX, primer.getBlockState(cZ, y + 1, cX).isNormalCube() ? fillerBlock : topBlock);
        }
    }
    
    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random rand) {
        return FLOWER_GENERATOR;
    }

    @Override
    public BiomeDecorator createBiomeDecorator() {
        return new BiomeDecoratorTaintedLands();
    }

    @Override
    public EnumFlowerType pickRandomFlower(Random rand, BlockPos pos) {
        return EnumFlowerType.ALLIUM;
    }
    
    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        POS_RNG.setSeed(pos.getX() + pos.getZ() * Long.MAX_VALUE);
        int colorMod = POS_RNG.nextInt(64) - POS_RNG.nextInt(64);
        return getModdedBiomeFoliageColor(((0x66 + colorMod) << 16) + 0x66 + colorMod);
    }
    
    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        POS_RNG.setSeed(pos.getX() + pos.getZ() * Long.MAX_VALUE);
        int colorMod = POS_RNG.nextInt(64) - POS_RNG.nextInt(64);
        return getModdedBiomeGrassColor(((0x66 + colorMod) << 16) + 0x66 + colorMod);
    }
    
}
