package misterpemodder.tmo.main.blocks;


import misterpemodder.tmo.main.blocks.BlockOre.EnumVariant;
import misterpemodder.tmo.main.blocks.base.BlockMulti;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class BlockOre extends BlockMulti<EnumVariant> {
	
	public static final PropertyEnum<EnumVariant> VARIANT = PropertyEnum.create("variant", BlockOre.EnumVariant.class);
	public static BlockOre.EnumVariant[] oreVariants = EnumVariant.values();
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}
	
	public enum EnumVariant implements IBlockVariant {
		TITANIUM_ORE(0, "titanium", "titanium", 2),
		ANCIENT_GOLD_ORE(1, "ancient_gold", "goldAncient", 3),
		DARKANIUM_ORE(2, "darkanium", "darkanium", 3),
		DARKANIUM_ORE_NETHER(3, "nether_darkanium", "darkaniumNether", 3),
		FROZIUM_ORE(4, "frozium", "frozium", 1),
		IGNUM_ORE(5, "ignum", "ignum", 1),
		IGNUM_ORE_NETHER(6, "nether_ignum", "ignumNether", 1),
		PLATINUM_ORE(7, "platinum", "platinum", 3),
		PLATINUM_ORE_NETHER(8, "nether_platinum", "platinumNether", 3),
		ENDER_MATTER_ORE(9, "ender_matter", "enderMatter", 2),
		COPPER_ORE(10, "copper", "copper", 1),
		CARBON_ORE(11, "carbon", "carbon", 1),
		;
		private final int meta;
		private final String name;
		private final String unlocalizedName;
		private final int miningLevel;

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
			return MapColor.STONE;
		};

		EnumVariant(int id, String name, String unlocalizedName, int miningLevel) {
			this.meta = id;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.miningLevel = miningLevel;
		}
		
	}
	
	public BlockOre() {
		super(EnumBlocksNames.ORE, EnumBlocksValues.ORE, "_ore");
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumVariant.TITANIUM_ORE));
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
		return MapColor.STONE;
	}
	
	
	@Override
	public void registerOreDict() {
		for(IBlockVariant v : oreVariants) {
			String uname = v.getUnlocalizedName();
			String name = "ore"+String.valueOf(uname.charAt(0)).toUpperCase() + uname.substring(1);
			OreDictionary.registerOre(name, new ItemStack(this, 1, v.getMeta()));
		}
	}

	public EnumVariant[] getVariants() {
		return oreVariants;
	}
}