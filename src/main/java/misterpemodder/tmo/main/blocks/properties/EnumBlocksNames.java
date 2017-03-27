package misterpemodder.tmo.main.blocks.properties;

public enum EnumBlocksNames implements IBlockNames {
	TEST_BLOCK("test_block", "blockTest"),
	TITANIUM_BLOCK("titanium_block", "blockTitanium", "blockTitanium", "blockTitaniumBlue"),
	COPPER_BLOCK("copper_block", "blockCopper", "blockCopper"),
	FROZIUM_BLOCK("frozium_block", "blockFrozium", "blockFrozium"),
	IGNUM_BLOCK("ignum_block", "blockIgnum", "blockIgnum"),
	ENDER_MATTER_BLOCK("ender_matter_block", "blockEnderMatter", "blockEnderMatter"),
	DARKANIUM_BLOCK("darkanium_block", "blockDarkanium", "blockDarkanium"),
	ANCIENT_GOLD_BLOCK("ancient_gold_block", "blockGoldAncient", "blockGoldAncient"),
	PLATINUM("platinum_block", "blockPlatinum", "blockPlatinum"),
	BRICK("brick", "blockBrick", "blockBrick"),
	ORE("ore", "blockOre"),
	SLAB("slab", "blockSlab"),
	STORAGE("storage_block", "blockStorage"),
	DECORATION("decoration", "blockDecoration"),
	ANCIENT_GOLD_LAMP("ancient_gold_lamp", "blockGoldAncientLamp"),
	DARKANIUM_LAMP("darkanium_lamp", "blockDarkaniumLamp"),
	KEEPER_BLOCK("item_keeper", "blockItemKeeper"),
	TITANIUM_CHEST("titanium_chest", "blockTitaniumChest", "chestMetal"),
	TITANIUM_ANVIL("titanium_anvil", "blockTitaniumAnvil", "anvil"),
	MACHINE_CASING("machine_casing", "machineCasing"),
	EXPLODER("exploder", "blockExploder"),
	STRONG_REDSTONE_BLOCK("strong_redstone_block", "blockRedstoneStrong"),
	STRONG_PISTON_BASE("strong_piston", "blockPistonBase"),
	STRONG_PISTON_BASE_STICKY("sticky_strong_piston", "blockPistonBaseSticky"),
	STRONG_PISTON_EXTENSION("strong_piston_head", "blockPistonBase"),
	STRONG_PISTON_MOVING("strong_piston_extension", "blockPistonExtension"),
	INJECTOR("fluid_injector", "blockInjector"),
	;
	String unlocalizedName;
	String registryName;
	String[] oreDictNames;

	public String getUnlocalizedName() {
		return unlocalizedName;
	}

	public String getRegistryName() {
		return registryName;
	}
	
	public String[] getOreDictNames() {
		return oreDictNames;
	}

	EnumBlocksNames(String registryName, String unlocalizedName) {
		this(registryName, unlocalizedName, new String[0]);
	}
	
	EnumBlocksNames(String registryName, String unlocalizedName, String... oreDictNames) {
		this.registryName = registryName;
		this.unlocalizedName = unlocalizedName;
		this.oreDictNames = oreDictNames;
	}
}
