package misterpemodder.tmo.main.blocks;

import misterpemodder.tmo.main.blocks.BlockStorage.EnumVariant;
import misterpemodder.tmo.main.blocks.base.BlockMulti;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.blocks.properties.IBlockValues;
import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.oredict.OreDictionary;

public class BlockStorage extends BlockMulti<EnumVariant> {
	
	public static final PropertyEnum<EnumVariant> VARIANT = PropertyEnum.create("variant", EnumVariant.class);
	public static EnumVariant[] blockVariants = EnumVariant.values();
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}
	
	public enum EnumVariant implements IBlockVariant {
		TITANIUM_BLOCK(0, EnumBlocksNames.TITANIUM_BLOCK, EnumBlocksValues.MetalBlocks.TITANIUM_BLOCK),
		COPPER_BLOCK(1, EnumBlocksNames.COPPER_BLOCK, EnumBlocksValues.MetalBlocks.COPPER_BLOCK),
		IGNUM_BLOCK(2, EnumBlocksNames.IGNUM_BLOCK, EnumBlocksValues.MetalBlocks.IGNUM_BLOCK),
		ENDER_MATTER_BLOCK(3, EnumBlocksNames.ENDER_MATTER_BLOCK, EnumBlocksValues.MetalBlocks.ENDER_MATTER_BLOCK),
		DARKANIUM_BLOCK(4, EnumBlocksNames.DARKANIUM_BLOCK, EnumBlocksValues.MetalBlocks.DARKANIUM_BLOCK),
		ANCIENT_GOLD_BLOCK(5, EnumBlocksNames.ANCIENT_GOLD_BLOCK, EnumBlocksValues.MetalBlocks.ANCIENT_GOLD_BLOCK),
		PLATINUM_BLOCK(6, EnumBlocksNames.PLATINUM, EnumBlocksValues.MetalBlocks.PLATINUM_BLOCK),
		;
		private final int meta;
		private final IBlockValues values;
		private final IBlockNames names;

		@Override
		public String getName() {
			return this.names.getRegistryName();
		}
		
		@Override
		public String toString() {
			return this.names.getRegistryName();
		}
		@Override
		public int getMeta() {
			return meta;
		}
		public IBlockValues getValues() {
			return this.values;
		}
		@Override
		public String getUnlocalizedName() {
			return this.names.getUnlocalizedName();
		}
		public int getMiningLevel(IBlockState state) {
			return values.getHarvestLevel(state);
		}
		@Override
		public MapColor getMapColor() {
			return getValues().getMapColor(null);
		}
		@Override
		public EnumRarity getRarity() {
			return this.names.getRarity();
		}

		EnumVariant(int id, IBlockNames names, IBlockValues values) {
			this.meta = id;
			this.values = values;
			this.names = names;
		}
		
	}
	
	public BlockStorage() {
		super(EnumBlocksNames.STORAGE, EnumBlocksValues.MetalBlocks.IGNUM_BLOCK, "");
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumVariant.TITANIUM_BLOCK));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, (EnumVariant) getVariant(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).meta;
	}

	@Override
	public MapColor getMapColor(IBlockState state) {
		return state.getValue(VARIANT).getMapColor();
	}

	@Override
	public void registerOreDict() {
		for(EnumVariant v : blockVariants) {
			for(String n : v.names.getOreDictNames()) {
				OreDictionary.registerOre(n, new ItemStack(this, 1, v.getMeta()));
			}
		}
	}

	public EnumVariant[] getVariants() {
		return blockVariants;
	}
	
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
        return true;
    }
	
}
