package misterpemodder.tmo.main.blocks;

import misterpemodder.hc.main.blocks.properties.IBlockVariant;
import misterpemodder.tmo.main.blocks.BlockBrick.EnumBrickVariant;
import misterpemodder.tmo.main.blocks.base.BlockMultiTMO;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;

public class BlockBrick extends BlockMultiTMO<EnumBrickVariant> {
	
	public static final PropertyEnum<EnumBrickVariant> VARIANT = PropertyEnum.create("variant", EnumBrickVariant.class);
	public static EnumBrickVariant[] brickVariants = EnumBrickVariant.values();
	
	@Override
	public IProperty<EnumBrickVariant> getPropertyVariant() {
		return VARIANT;
	}
	
	public enum EnumBrickVariant implements IBlockVariant {
		ANCIENT_GOLD(0, "ancient_gold", "goldAncient", MapColor.GOLD),
		ANCIENT_GOLD_CARVED(1, "ancient_gold_carved", "goldAncientCarved", MapColor.GOLD),
		DARKANIUM(2, "darkanium", "darkanium", MapColor.PURPLE),
		ENDSTONE(4, "endstone", "endstoneClean", MapColor.SAND),
		FROZIUM(6, "frozium", "frozium", MapColor.ICE),
		IGNUM(8, "ignum", "ignum", MapColor.RED),
		GOLD(10, "gold", "gold", MapColor.GOLD),
		PLATINUM(12, "platinum", "platinum", MapColor.SNOW),
		PLATINUM_CARVED(13, "platinum_carved", "platinumCarved", MapColor.SNOW);
		
		private final int meta;
		private final String name;
		private final String unlocalizedName;
		private final MapColor mapColor;

		@Override
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return this.name;
		}
		public int getMeta() {
			return this.meta;
		}
		public String getUnlocalizedName() {
			return unlocalizedName;
		}
		public MapColor getMapColor() {
			return this.mapColor;
		}

		EnumBrickVariant(int id, String name, String unlocalizedName, MapColor color) {
			this.meta = id;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.mapColor = color;
		}
		
	}
	
	public BlockBrick() {
		super(EnumBlocksNames.BRICK, EnumBlocksValues.BRICK, "_brick");
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumBrickVariant.ANCIENT_GOLD));
	}
	
		
	@Override
	public IBlockState getStateFromMeta(int meta) {
	    return getDefaultState().withProperty(VARIANT, getVariant(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
	    return state.getValue(VARIANT).getMeta();
	}
	
	@Override
	public MapColor getMapColor(IBlockState state) {
		return state.getValue(VARIANT).getMapColor();
	}
	
	@Override
	public EnumBrickVariant[] getVariants() {
		return brickVariants;
	}
	
}
