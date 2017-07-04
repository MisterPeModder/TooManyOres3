package misterpemodder.tmo.main.blocks;

import misterpemodder.tmo.main.blocks.BlockMachineCasing.EnumMachineCasingVariant;
import misterpemodder.tmo.main.blocks.base.BlockMulti;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.BlockRenderLayer;

public class BlockMachineCasing extends BlockMulti<EnumMachineCasingVariant> {
	
	public static final PropertyEnum<EnumMachineCasingVariant> VARIANT = PropertyEnum.create("variant", EnumMachineCasingVariant.class);
	public static EnumMachineCasingVariant[] casingVariants = EnumMachineCasingVariant.values();
	
	public enum EnumMachineCasingVariant implements IBlockVariant {
		
		STANDARD("standard", EnumRarity.RARE, 1.0F),
		BASIC("basic", EnumRarity.COMMON, 1.5F),
		IMPROVED("improved", EnumRarity.EPIC, 0.75F),
		;
		
		private final String name;
		private final EnumRarity rarity;
		
		public final float speedModifier;

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
			return this.ordinal();
		}
		
		public static EnumMachineCasingVariant fromMeta(int meta) {
			return values()[Math.min(values().length-1, meta)];
		}
		
		@Override
		public String getUnlocalizedName() {
			return this.name;
		}
		
		@Override
		public MapColor getMapColor() {
			return EnumBlocksValues.MetalBlocks.MACHINE.getMapColor(null);
		}
		
		@Override
		public EnumRarity getRarity() {
			return this.rarity;
		}

		EnumMachineCasingVariant(String name, EnumRarity rarity, float speedModifier) {
			this.name = name;
			this.rarity = rarity;
			this.speedModifier = speedModifier;
		}
		
	}

	public BlockMachineCasing() {
		super(EnumBlocksNames.MACHINE_CASING, EnumBlocksValues.MetalBlocks.MACHINE, "_machine_casing");
	}
	
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public EnumMachineCasingVariant[] getVariants() {
		return casingVariants;
	}
	
	@Override
	public IProperty<EnumMachineCasingVariant> getPropertyVariant() {
		return VARIANT;
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
