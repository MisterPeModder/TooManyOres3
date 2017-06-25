package misterpemodder.tmo.main.blocks;

import misterpemodder.tmo.main.blocks.BlockMachineCasing.EnumMachineCasingVariant;
import misterpemodder.tmo.main.blocks.base.BlockMulti;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.BlockRenderLayer;

public class BlockMachineCasing extends BlockMulti<EnumMachineCasingVariant> {
	
	public static final PropertyEnum<EnumMachineCasingVariant> VARIANT = PropertyEnum.create("variant", EnumMachineCasingVariant.class);
	public static EnumMachineCasingVariant[] casingVariants = EnumMachineCasingVariant.values();
	
	public enum EnumMachineCasingVariant implements IBlockVariant {
		
		STANDARD(0, "standard", "standard", EnumRarity.RARE),
		BASIC(1, "basic", "basic", EnumRarity.COMMON),
		IMPROVED(2, "improved", "improved", EnumRarity.EPIC),
		;
		
		private final int meta;
		private final String name;
		private final String unlocalizedName;
		private final EnumRarity rarity;

		@Override
		public String getName() {
			return this.name;
		}
		@Override
		public String toString() {
			return this.name;
		}
		@Override
		public int getMeta() {
			return this.meta;
		}
		@Override
		public String getUnlocalizedName() {
			return unlocalizedName;
		}
		@Override
		public MapColor getMapColor() {
			return EnumBlocksValues.MetalBlocks.MACHINE.getMapColor(null);
		}
		@Override
		public EnumRarity getRarity() {
			return this.rarity;
		}

		EnumMachineCasingVariant(int id, String name, String unlocalizedName, EnumRarity rarity) {
			this.meta = id;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.rarity = rarity;
		}
		
	}

	public BlockMachineCasing() {
		super(EnumBlocksNames.MACHINE_CASING, EnumBlocksValues.MetalBlocks.MACHINE, "_machine_casing");
	}
	
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected EnumMachineCasingVariant[] getVariants() {
		return casingVariants;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).getMeta();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, getVariant(meta));
	}

}