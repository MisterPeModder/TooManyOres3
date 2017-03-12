package misterpemodder.tmo.main.blocks.properties;

import javax.annotation.Nullable;

import misterpemodder.tmo.main.blocks.BlockBrick;
import misterpemodder.tmo.main.blocks.BlockDeco;
import misterpemodder.tmo.main.blocks.BlockLamp;
import misterpemodder.tmo.main.blocks.BlockOre;
import misterpemodder.tmo.main.blocks.slab.BlockHalfSlab;
import misterpemodder.tmo.main.init.ModBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

public enum EnumBlocksValues implements IBlockValues{
	
	BRICK() {
		public float getHardness(IBlockState state) {
			return 2.0F;
		}
		public float getResistance(IBlockState state) {
			return 10.0F;
		}
		public SoundType getSoundType(IBlockState state) {
			return SoundType.STONE;
		}
		public Material getMaterial(@Nullable IBlockState state) {
			return Material.ROCK;
		}
		public int getHarvestLevel(IBlockState state) {
			return 2;
		}
		public boolean isOpaqueCube(IBlockState state) {
			if(state.getBlock() instanceof BlockHalfSlab) {
				return false;
			}
			return true;
		}
		@Override
		public MapColor getMapColor(@Nullable IBlockState state) {
			if(state == null) {
				return super.getMapColor(state);
			}
			if(state.getBlock() instanceof BlockHalfSlab) {
				return state.getValue(BlockHalfSlab.VARIANT).getMapColor();
			} else if(state.getBlock() instanceof BlockBrick) {
				return state.getValue(BlockBrick.VARIANT).getMapColor();
			}
			return super.getMapColor(state);
		}
	},
	FROZIUM_BLOCK() {
		public float getHardness(IBlockState state) {
			return 3.0F;
		}
		public float getResistance(IBlockState state) {
			return 10.0F;
		}
		public SoundType getSoundType(IBlockState state) {
			return SoundType.GLASS;
		}
		public Material getMaterial(@Nullable IBlockState state) {
			return Material.ICE;
		}
		public MapColor getMapColor(@Nullable IBlockState state) {
			return MapColor.ICE;
		}
		public float getSlipperiness(IBlockState state) {
			return 0.98F;
		}
		public int getLightOpacity(IBlockState state) {
			return 3;
		}
		public BlockRenderLayer getRenderLayer() {
			return BlockRenderLayer.TRANSLUCENT;
		}
		@Override
		public boolean isFullCube(IBlockState state) {
			return false;
		}
		public boolean isOpaqueCube(IBlockState state){
	        return false;
	    }
		public int getHarvestLevel(IBlockState state) {
			return 2;
		}
	},
	ORE() {
		public float getHardness(IBlockState state) {
			return 3.0F;
		}
		public float getResistance(IBlockState state) {
			return 5.0F;
		}
		public int getHarvestLevel(IBlockState state) {
			return state.getValue(BlockOre.VARIANT).getMiningLevel(state);
		}
	},
	LAMP() {
		public float getHardness(IBlockState state) {
			return 0.3F;
		}
		public float getResistance(IBlockState state) {
			return 1.5F;
		}
		public float getLightLevel(IBlockState state) {
			return state.getValue(BlockLamp.ACTIVATED) == true? 1.0F: 0.0F;
		}
		public SoundType getSoundType(IBlockState state) {
			return SoundType.GLASS;
		}
		public Material getMaterial(@Nullable IBlockState state) {
			return Material.REDSTONE_LIGHT;
		}
		@Override
		public int getLightOpacity(IBlockState state) {
			return 0;
		}
		public MapColor getMapColor(@Nullable IBlockState state) {
			if(state == null) {
				return MapColor.YELLOW;
			}
			if(state.getBlock() == ModBlocks.TheBlocks.DARKANIUM_LAMP.getBlock()) {
				return MapColor.PURPLE;
			}
			return MapColor.YELLOW;
		}
	},
	TITANIUM_CHEST() {
		public SoundType getSoundType(IBlockState state) {
			return SoundType.METAL;
		}
		public Material getMaterial(@Nullable IBlockState state) {
			return Material.IRON;
		}
		public MapColor getMapColor(@Nullable IBlockState state) {
			return MapColor.BLUE;
		}
		public float getHardness(IBlockState state) {
			return 50.0F;
		}
		@Override
		public float getResistance(IBlockState state) {
			return 0.0F;
		}
		@Override
		public int getHarvestLevel(IBlockState state) {
			return 2;
		}
		@Override
		public int getLightOpacity(IBlockState state) {
			return 3;
		}
		@Override
		public boolean isFullCube(IBlockState state) {
			return false;
		}
		@Override
		public boolean isOpaqueCube(IBlockState state) {
			return false;
		}
	},
	TITANIUM_ANVIL() {
		public SoundType getSoundType(IBlockState state) {
			return SoundType.ANVIL;
		}
		public Material getMaterial(@Nullable IBlockState state) {
			return Material.ANVIL;
		}
		public MapColor getMapColor(@Nullable IBlockState state) {
			return MapColor.BLUE;
		}
		public float getHardness(IBlockState state) {
			return 10.0F;
		}
		@Override
		public float getResistance(IBlockState state) {
			return 5.0F;
		}
		@Override
		public int getHarvestLevel(IBlockState state) {
			return 1;
		}
		@Override
		public int getLightOpacity(IBlockState state) {
			return 3;
		}
		@Override
		public boolean isFullCube(IBlockState state) {
			return false;
		}
		@Override
		public boolean isOpaqueCube(IBlockState state) {
			return false;
		}
	},
	DECORATION() {
		@Override
		public float getHardness(IBlockState state) {
			return 2.0F;
		}
		@Override
		public MapColor getMapColor(IBlockState state) {
			if(state == null) return super.getMapColor(state);
			return state.getValue(BlockDeco.VARIANT).getMapColor();
		}
		@Override
		public int getHarvestLevel(IBlockState state) {
			if(state == null) return super.getHarvestLevel(state);
			return state.getValue(BlockDeco.VARIANT).getMiningLevel(state);
		}
		@Override
		public Material getMaterial(IBlockState state) {
			if(state == null) return super.getMaterial(state);
			return state.getValue(BlockDeco.VARIANT).getMaterial();
		}
		@Override
		public SoundType getSoundType(IBlockState state) {
			if(state == null) return super.getSoundType(state);
			return state.getValue(BlockDeco.VARIANT).getSoundType();
		}
	}
	;
	
//    *     Wood:    0
//    *     Stone:   1
//    *     Iron:    2
//    *     Diamond: 3
//    *     Gold:    0
	
	//metal blocks 'preset'
	public enum MetalBlocks implements IBlockValues {
		TITANIUM_BLOCK(6.0F, 15.0F, 2,MapColor.BLUE),
		COPPER_BLOCK(5.0F, 10.0F, 1, MapColor.BROWN),
		IGNUM_BLOCK(5.0F, 10.0F, 2, MapColor.RED),
		ENDER_MATTER_BLOCK(5.0F, 10.0F, 2, MapColor.CYAN),
		DARKANIUM_BLOCK(5.0F, 10.0F, 3, MapColor.PURPLE),
		ANCIENT_GOLD_BLOCK(5.0F, 10.0F, 3, MapColor.GOLD),
		PLATINUM_BLOCK(5.0F, 10.0F, 3, MapColor.IRON),
		EXPLODER(5.0F, 0.0F, 1, MapColor.IRON, false),
		MACHINE(5.0F, 7.0F, 1, MapColor.IRON, false),
		;
		
		private float hardness;
		private float resistance;
		private int harvestLevel;
		private MapColor color;
		private boolean isFullCube;
		
		public SoundType getSoundType(IBlockState state) {
			return SoundType.METAL;
		}
		public Material getMaterial(@Nullable IBlockState state) {
			return Material.IRON;
		}
		public MapColor getMapColor(@Nullable IBlockState state) {
			return this.color;
		}
		public float getHardness(IBlockState state) {
			return this.hardness;
		}
		public float getResistance(IBlockState state) {
			return this.resistance;
		}
		public int getHarvestLevel(IBlockState state) {
			return this.harvestLevel;
		}
		public boolean isFullCube(IBlockState state) {
			return this.isFullCube;
		}
		
		MetalBlocks(float hardness, float resistance, int harvestLevel, MapColor color) {
			this(hardness, resistance, harvestLevel, color, true);
		}
		
		MetalBlocks(float hardness, float resistance, int harvestLevel, MapColor color, boolean fullCube) {
			this.harvestLevel = harvestLevel;
			this.hardness = hardness;
			this.resistance = resistance;
			this.color = color;
			this.isFullCube = fullCube;
		}
	}
	
}
