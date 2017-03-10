package misterpemodder.tmo.main.items;

public enum EnumItemsNames {
	
	TAB_ICON("tab_icon", "tabIcon"),
	INGOT("ingot", "itemIngot"), 
	GEM("gem", "itemGem"),
	PLATE("plate", "itemPlate", "plate"),
	LOCK("lock", "itemLock", "lock"),
	
	TITANIUM_BUCKET("titanium_bucket", "titaniumBucket"),
	
	PICKAXE_TITANIUM("titanium_pickaxe", "pickaxeTitanium"),
	PICKAXE_TITANIUM_DARK("dark_titanium_pickaxe", "pickaxeTitaniumDark"),
	PICKAXE_COPPER("copper_pickaxe", "pickaxeCopper"),
	PICKAXE_FROZIUM("frozium_pickaxe", "pickaxeFrozium"),
	PICKAXE_IGNUM("ignum_pickaxe", "pickaxeIgnum"),
	
	SHOVEL_TITANIUM("titanium_shovel", "shovelTitanium"),
	SHOVEL_TITANIUM_DARK("dark_titanium_shovel", "shovelTitaniumDark"),
	SHOVEL_COPPER("copper_shovel", "shovelCopper"),
	SHOVEL_FROZIUM("frozium_shovel", "shovelFrozium"),
	SHOVEL_IGNUM("ignum_shovel", "shovelIgnum"),
	
	AXE_TITANIUM("titanium_axe", "axeTitanium"),
	AXE_TITANIUM_DARK("dark_titanium_axe", "axeTitaniumDark"),
	AXE_COPPER("copper_axe", "axeCopper"),
	AXE_FROZIUM("frozium_axe", "axeFrozium"),
	AXE_IGNUM("ignum_axe", "axeIgnum"),
	
	SWORD_TITANIUM("titanium_sword", "swordTitanium"),
	SWORD_TITANIUM_DARK("dark_titanium_sword", "swordTitaniumDark"),
	SWORD_COPPER("copper_sword", "swordCopper"),
	SWORD_FROZIUM("frozium_sword", "swordFrozium"),
	SWORD_IGNUM("ignum_sword", "swordIgnum"),
	
	HOE_TITANIUM("titanium_hoe", "hoeTitanium"),
	HOE_TITANIUM_DARK("dark_titanium_hoe", "hoeTitaniumDark"),
	HOE_COPPER("copper_hoe", "hoeCopper"),
	HOE_FROZIUM("frozium_hoe", "hoeFrozium"),
	HOE_IGNUM("ignum_hoe", "hoeIgnum"),
	
	WRENCH_COPPER("copper_wrench", "wrenchCopper"),
	WRENCH_ADMIN("admin_wrench", "wrenchAdmin"),
	
	HAMMER_TITANIUM("titanium_hammer", "hammerTitanium"),
	
	HELMET_TITANIUM("titanium_helmet", "helmetTitanium"),
	HELMET_TITANIUM_DARK("dark_titanium_helmet", "helmetTitaniumDark"),
	HELMET_COPPER("copper_helmet", "helmetCopper"),
	HELMET_FROZIUM("frozium_helmet", "helmetFrozium"),
	HELMET_IGNUM("ignum_helmet", "helmetIgnum"),
	HELMET_DARKANIUM("darkanium_helmet", "helmetDarkanium"),
	HELMET_HALLOWED("hallowed_helmet", "helmetHallowed"),
	
	CHESTPLATE_TITANIUM("titanium_chestplate", "chestplateTitanium"),
	CHESTPLATE_TITANIUM_DARK("dark_titanium_chestplate", "chestplateTitaniumDark"),
	CHESTPLATE_COPPER("copper_chestplate", "chestplateCopper"),
	CHESTPLATE_FROZIUM("frozium_chestplate", "chestplateFrozium"),
	CHESTPLATE_IGNUM("ignum_chestplate", "chestplateIgnum"),
	CHESTPLATE_DARKANIUM("darkanium_chestplate", "chestplateDarkanium"),
	CHESTPLATE_HALLOWED("hallowed_chestplate", "chestplateHallowed"),
	
	LEGGINGS_TITANIUM("titanium_leggings", "leggingsTitanium"),
	LEGGINGS_TITANIUM_DARK("dark_titanium_leggings", "leggingsTitaniumDark"),
	LEGGINGS_COPPER("copper_leggings", "leggingsCopper"),
	LEGGINGS_FROZIUM("frozium_leggings", "leggingsFrozium"),
	LEGGINGS_IGNUM("ignum_leggings", "leggingsIgnum"),
	LEGGINGS_DARKANIUM("darkanium_leggings", "leggingsDarkanium"),
	LEGGINGS_HALLOWED("hallowed_leggings", "leggingsHallowed"),
	
	BOOTS_TITANIUM("titanium_boots", "bootsTitanium"),
	BOOTS_TITANIUM_DARK("dark_titanium_boots", "bootsTitaniumDark"),
	BOOTS_COPPER("copper_boots", "bootsCopper"),
	BOOTS_FROZIUM("frozium_boots", "bootsFrozium"),
	BOOTS_IGNUM("ignum_boots", "bootsIgnum"),
	BOOTS_DARKANIUM("darkanium_boots", "bootsDarkanium"),
	BOOTS_HALLOWED("hallowed_boots", "bootsHallowed"),
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

	EnumItemsNames(String registryName, String unlocalizedName) {
		this(registryName, unlocalizedName, new String[0]);
	}
	
	EnumItemsNames(String registryName, String unlocalizedName, String... oreDictNames) {
		this.registryName = registryName;
		this.unlocalizedName = unlocalizedName;
		this.oreDictNames = oreDictNames;
	}

}
