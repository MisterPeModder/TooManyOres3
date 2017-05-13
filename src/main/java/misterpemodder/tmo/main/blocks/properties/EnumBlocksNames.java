package misterpemodder.tmo.main.blocks.properties;

import net.minecraft.item.EnumRarity;

public enum EnumBlocksNames implements IBlockNames {
	GENERIC("generic_block", "blockGeneric", EnumRarity.EPIC),
	TITANIUM_BLOCK("titanium_block", "blockTitanium", EnumRarity.RARE, "blockTitaniumBlue"),
	COPPER_BLOCK("copper_block", "blockCopper", "blockCopper"),
	FROZIUM_BLOCK("frozium_block", "blockFrozium", "blockFrozium"),
	IGNUM_BLOCK("ignum_block", "blockIgnum", "blockIgnum"),
	ENDER_MATTER_BLOCK("ender_matter_block", "blockEnderMatter", "blockEnderMatter"),
	DARKANIUM_BLOCK("darkanium_block", "blockDarkanium", EnumRarity.EPIC, "blockDarkanium"),
	ANCIENT_GOLD_BLOCK("ancient_gold_block", "blockGoldAncient", EnumRarity.UNCOMMON, "blockGoldAncient"),
	PLATINUM("platinum_block", "blockPlatinum", EnumRarity.RARE, "blockPlatinum"),
	BRICK("brick", "blockBrick", "blockBrick"),
	ORE("ore", "blockOre"),
	SLAB("slab", "blockSlab"),
	STORAGE("storage_block", "blockStorage"),
	DECORATION("decoration", "blockDecoration"),
	ANCIENT_GOLD_LAMP("ancient_gold_lamp", "blockGoldAncientLamp"),
	DARKANIUM_LAMP("darkanium_lamp", "blockDarkaniumLamp"),
	TITANIUM_CHEST("titanium_chest", "blockTitaniumChest", "chestMetal"),
	TITANIUM_ANVIL("titanium_anvil", "blockTitaniumAnvil", "anvil"),
	MACHINE_CASING("machine_casing", "machineCasing"),
	EXPLODER("exploder", "blockExploder"),
	STRONG_REDSTONE_BLOCK("strong_redstone_block", "blockRedstoneStrong"),
	STRONG_PISTON_BASE("strong_piston", "blockPistonBase", EnumRarity.RARE),
	STRONG_PISTON_BASE_STICKY("sticky_strong_piston", "blockPistonBaseSticky", EnumRarity.RARE),
	STRONG_PISTON_EXTENSION("strong_piston_head", "blockPistonBase"),
	STRONG_PISTON_MOVING("strong_piston_extension", "blockPistonExtension"),
	INJECTOR("fluid_injector", "blockInjector", EnumRarity.RARE),
	CRYSTAL_DESTABILIZER("crystal_destabilizer", "blockCrystalDestabilizer", EnumRarity.RARE),
	
	IGNUM_FUEL("liquid_ignum", "liquidIgnum", EnumRarity.UNCOMMON),
	FROZIUM_FUEL("liquid_frozium", "liquidFrozium", EnumRarity.RARE),
	;
	String unlocalizedName;
	String registryName;
	String[] oreDictNames;
	EnumRarity rarity;

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public String getRegistryName() {
		return registryName;
	}
	
	public String[] getOreDictNames() {
		return oreDictNames;
	}
	
	public EnumRarity getRarity() {
		return this.rarity;
	}

	EnumBlocksNames(String registryName, String unlocalizedName) {
		this(registryName, unlocalizedName, EnumRarity.COMMON, new String[0]);
	}
	
	EnumBlocksNames(String registryName, String unlocalizedName, EnumRarity rarity) {
		this(registryName, unlocalizedName, rarity, new String[0]);
	}
	
	EnumBlocksNames(String registryName, String unlocalizedName, String... oreDictNames) {
		this(registryName, unlocalizedName, EnumRarity.COMMON, oreDictNames);
	}
	
	EnumBlocksNames(String registryName, String unlocalizedName, EnumRarity rarity, String... oreDictNames) {
		this.registryName = registryName;
		this.unlocalizedName = unlocalizedName;
		this.oreDictNames = oreDictNames;
		this.rarity = rarity;
	}
}
