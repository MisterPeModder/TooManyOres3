package misterpemodder.tmo.main.world;

import java.util.Random;

import misterpemodder.tmo.main.blocks.BlockOre;
import misterpemodder.tmo.main.blocks.BlockOre.EnumVariant;
import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.config.ConfigValues.BoolValues;
import misterpemodder.tmo.main.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class OreGen implements IWorldGenerator {
	
	public enum Ores {
		TITANIUM_ORE(EnumVariant.TITANIUM_ORE, 0, 0, 16, 3, 6, 8, BoolValues.TITANIUM_GEN),
		ANCIENT_GOLD_ORE(EnumVariant.ANCIENT_GOLD_ORE, 0, 0, 20, 2, 4, 5, BoolValues.ANCIENT_GOLD_GEN),
		DARKANIUM_ORE(EnumVariant.DARKANIUM_ORE, 0, 0, 16, 3, 6, 6, BoolValues.DARKANIUM_GEN),
		DARKANIUM_ORE_NETHER(EnumVariant.DARKANIUM_ORE_NETHER, -1, 10 , 50, 4, 9, 12, Blocks.NETHERRACK, BoolValues.DARKANIUM_GEN_NETHER),
		FROZIUM_ORE(EnumVariant.FROZIUM_ORE, 0, 16, 48, 3, 6, 13, BoolValues.FROZIUM_GEN),
		IGNUM_ORE(EnumVariant.IGNUM_ORE, 0, 16, 48, 3, 6, 13, BoolValues.IGNUM_GEN),
		IGNUM_ORE_NETHER(EnumVariant.IGNUM_ORE_NETHER, -1, 16, 60, 5, 7, 22, Blocks.NETHERRACK, BoolValues.IGNUM_GEN_NETHER),
		PLATINUM_ORE(EnumVariant.PLATINUM_ORE, 0, 0, 20, 2, 4, 3, BoolValues.PLATINUM_GEN),
		PLATINUM_ORE_NETHER(EnumVariant.PLATINUM_ORE_NETHER, -1, 10, 50, 2, 5, 16, Blocks.NETHERRACK, BoolValues.PLATINUM_GEN_NETHER),
		ENDER_MATTER_ORE(EnumVariant.ENDER_MATTER_ORE, 1, 0, 128, 4, 8, 25, Blocks.END_STONE, BoolValues.ENDER_MATTER_GEN),
		COPPER(EnumVariant.COPPER_ORE, 0, 30, 54, 5, 8, 10, BoolValues.COPPER),
		CARBON_ORE(EnumVariant.CARBON_ORE, 0, 0, 60, 2, 3, 13, BoolValues.CARBON_GEN),
		;
		
		public IBlockState ore;
		public int dim;
		public int minY;
		public int maxY;
		private int minOres;
		private int maxOres;
		private int chances;
		private Block spanwnIn;
		private ConfigValues.BoolValues configValue;
		
		public int oreNum(Random random) {
			return minOres + random.nextInt(maxOres+1);
		}
		
		public boolean isEnabled() {
			return configValue.currentValue;
		}
		
		private Ores(BlockOre.EnumVariant ore , int dim, int minY, int maxY, int minOres, int maxOres, int chances, ConfigValues.BoolValues configValue) {
			this(ore, dim, minY, maxY, minOres, maxOres, chances, Blocks.STONE, configValue);
		}
		
		private Ores(BlockOre.EnumVariant ore , int dim, int minY, int maxY, int minOres, int maxOres, int chances, Block spawnIn, ConfigValues.BoolValues configValue) {
			this.ore = ModBlocks.TheBlocks.ORE.getBlock().getDefaultState().withProperty(BlockOre.VARIANT, ore);
			this.dim = dim;
			this.minY = minY;
			this.maxY = maxY;
			this.minOres = minOres;
			this.maxOres = maxOres;
			this.chances = chances;
			this.spanwnIn = spawnIn;
			this.configValue = configValue;
		}
		
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		for(Ores ore : Ores.values()) {
			if(ore.dim == world.provider.getDimension() && ore.isEnabled()) {
				generateOre(new WorldGenMinable(ore.ore, ore.oreNum(random), BlockMatcher.forBlock(ore.spanwnIn)), world, random, chunkX*16, chunkZ*16, ore.minY, ore.maxY, ore.chances);
			}
		}
	}
	
	private void generateOre(WorldGenMinable generator, World world, Random random, int x, int z, int minY, int maxY, int chances) {
		for (int i = 0; i < chances; i++) {
			BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(maxY - minY), z + random.nextInt(16));
			
			generator.generate(world, random, pos);
		}
	}

}
