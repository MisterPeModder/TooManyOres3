package misterpemodder.tmo.main.blocks;


import misterpemodder.tmo.main.blocks.BlockOre.EnumVariant;
import misterpemodder.tmo.main.blocks.base.BlockMulti;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class BlockOre extends BlockMulti<EnumVariant> {
	
	public static final PropertyEnum<EnumVariant> VARIANT = PropertyEnum.create("variant", BlockOre.EnumVariant.class);
	public static BlockOre.EnumVariant[] oreVariants = EnumVariant.values();
	
	@Override
	public IProperty<EnumVariant> getPropertyVariant() {
		return VARIANT;
	}
	
	public enum EnumVariant implements IBlockVariant {
		TITANIUM_ORE(0, "titanium", "titanium", "oreTitaniumBlue", 2),
		ANCIENT_GOLD_ORE(1, "ancient_gold", "goldAncient", "oreGoldAncient", 3),
		DARKANIUM_ORE(2, "darkanium", "darkanium", "oreDarkanium", 3),
		DARKANIUM_ORE_NETHER(3, "nether_darkanium", "darkaniumNether", "oreDarkanium",3),
		FROZIUM_ORE(4, "frozium", "frozium", "oreFrozium", 1),
		IGNUM_ORE(5, "ignum", "ignum", "oreIgnum", 1),
		IGNUM_ORE_NETHER(6, "nether_ignum", "ignumNether", "oreIgnum", 1),
		PLATINUM_ORE(7, "platinum", "platinum", "orePlatinum", 3),
		PLATINUM_ORE_NETHER(8, "nether_platinum", "platinumNether", "orePlatinum", 3),
		ENDER_MATTER_ORE(9, "ender_matter", "enderMatter", "oreEnderMatter", 2),
		COPPER_ORE(10, "copper", "copper", "oreCopper", 1),
		CARBON_ORE(11, "carbon", "carbon", "oreCarbon", 1),
		;
		private final int meta;
		private final String name;
		private final String unlocalizedName;
		private final int miningLevel;
		private final String oreDictName;

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
		}
		public String getOreDictName() {
			return oreDictName;
		}

		EnumVariant(int id, String name, String unlocalizedName, String oreDictName, int miningLevel) {
			this.meta = id;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.oreDictName = oreDictName;
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
		for(EnumVariant v : oreVariants) {
			OreDictionary.registerOre(v.getOreDictName(), new ItemStack(this, 1, v.getMeta()));
		}
	}

	public EnumVariant[] getVariants() {
		return oreVariants;
	}
}