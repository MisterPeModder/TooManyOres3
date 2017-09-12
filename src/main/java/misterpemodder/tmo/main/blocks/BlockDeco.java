package misterpemodder.tmo.main.blocks;
import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.hc.main.blocks.properties.IBlockValues;
import misterpemodder.hc.main.blocks.properties.IBlockVariant;
import misterpemodder.tmo.main.blocks.base.BlockMultiTMO;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;

public class BlockDeco extends BlockMultiTMO<BlockDeco.EnumVariant> {
	
	public static final PropertyEnum<BlockDeco.EnumVariant> VARIANT = PropertyEnum.create("variant", BlockDeco.EnumVariant.class);
	public static BlockDeco.EnumVariant[] blockVariants = EnumVariant.values();
	
	@Override
	public IProperty<EnumVariant> getPropertyVariant() {
		return VARIANT;
	}
	
	public enum EnumVariant implements IBlockVariant {
		TITANIUM_PLATING("titanium_plating", "titaniumPlating", 1, MapColor.BLUE, Material.IRON, SoundType.METAL),
		COPPER_DECO_BLOCK("copper_decoration", "copperDecoration", 1, MapColor.BROWN, Material.IRON, SoundType.METAL)
		;
		private final int meta;
		private final String name;
		private final String unlocalizedName;
		private final int miningLevel;
		private final MapColor mapColor;
		private final Material material;
		private final SoundType soundType;
		
		@Override
		public String getName() {
			return this.name;
		}
		@Override
		public String toString() {
			return this.name;
		}
		public int getMeta() {
			return meta;
		}
		public String getUnlocalizedName() {
			return unlocalizedName;
		}
		public int getMiningLevel(IBlockState state) {
			return miningLevel;
		}
		public MapColor getMapColor() {
			return this.mapColor;
		}
		public Material getMaterial() {
			return this.material;
		}
		public SoundType getSoundType() {
			return this.soundType;
		}

		EnumVariant(String name, String unlocalizedName, int miningLevel, MapColor mapColor, Material material, SoundType soundType) {
			this.meta = this.ordinal();
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.miningLevel = miningLevel;
			this.mapColor = mapColor;
			this.material = material;
			this.soundType = soundType;
		}
		
	}

	public BlockDeco(IBlockNames names, IBlockValues values) {
		super(names, values, "");
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockDeco.EnumVariant.TITANIUM_PLATING));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, getVariant(meta));
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
	public BlockDeco.EnumVariant[] getVariants() {
		return blockVariants;
	}

}
