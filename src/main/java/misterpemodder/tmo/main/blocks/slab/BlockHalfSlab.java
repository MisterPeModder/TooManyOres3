package misterpemodder.tmo.main.blocks.slab;

import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public final class BlockHalfSlab extends BlockAbstractSlab<BlockHalfSlab.EnumVariant> {
	
	private BlockFullSlab fullSlab;
	
	public static final PropertyEnum<BlockHalfSlab.EnumVariant> VARIANT = PropertyEnum.create("variant", BlockHalfSlab.EnumVariant.class);
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT, HALF);
	}
	
	@Override
	public PropertyEnum<BlockHalfSlab.EnumVariant> getPropertyVariant() {
		return VARIANT;
	}
	
	public BlockHalfSlab.EnumVariant[] getVariants() {
		return BlockHalfSlab.EnumVariant.values();
	}

	public BlockHalfSlab() {
		super(EnumBlocksNames.SLAB, EnumBlocksValues.BRICK, "_slab");
		this.itemBlock = new ItemBlockSlab(this);
		itemBlock.setRegistryName(this.getRegistryName());
	}
	
	public enum EnumVariant implements IBlockVariant {
		ANCIENT_GOLD(0, "ancient_gold", "goldAncient", MapColor.GOLD),
		DARKANIUM(1, "darkanium", "darkanium", MapColor.PURPLE),
		ENDSTONE(2, "endstone", "endstoneClean", MapColor.SAND),
		FROZIUM(3, "frozium", "frozium", MapColor.ICE),
		IGNUM(4, "ignum", "ignum", MapColor.RED),
		GOLD(5, "gold", "gold", MapColor.GOLD),
		PLATINUM(6, "platinum", "platinum", MapColor.SNOW);
		
		public final int meta;
		public final String name;
		public final String unlocalizedName;
		public final MapColor color;

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
			return null;
		}

		EnumVariant(int id, String name, String unlocalizedName, MapColor color) {
			this.meta = id;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.color = color;
		}

		
	}
	
	/*public int getTypeId(int meta) {
		return meta*2;
	}*/
	
	@Override
	public boolean isDouble() {
		return false;
	}
	
	void setFullSlab(BlockFullSlab fullslab) {
		this.fullSlab = fullslab;
	}
	
	public BlockFullSlab getFullSlab() {
		if(this.fullSlab == null) throw new IllegalStateException("Missing full slab block!");
		return this.fullSlab;
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state){
        return false;
    }

}
