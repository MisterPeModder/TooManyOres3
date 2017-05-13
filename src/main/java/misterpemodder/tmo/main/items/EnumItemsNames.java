package misterpemodder.tmo.main.items;

import net.minecraft.item.EnumRarity;

public enum EnumItemsNames {
	
	TAB_ICON("tab_icon", "tabIcon"),
	INGOT("ingot", "itemIngot"), 
	GEM("gem", "itemGem"),
	PLATE("plate", "itemPlate", "plate"),
	LOCK("lock", "itemLock", "lock"),
	
	TITANIUM_BUCKET("titanium_bucket", "titaniumBucket"),
	TITANIUM_CAN("titanium_can", "titaniumCan"),
	
	PICKAXE_TITANIUM("titanium_pickaxe", "pickaxeTitanium", EnumRarity.RARE),
	PICKAXE_TITANIUM_DARK("dark_titanium_pickaxe", "pickaxeTitaniumDark", EnumRarity.EPIC),
	PICKAXE_COPPER("copper_pickaxe", "pickaxeCopper"),
	PICKAXE_FROZIUM("frozium_pickaxe", "pickaxeFrozium"),
	PICKAXE_IGNUM("ignum_pickaxe", "pickaxeIgnum"),
	
	SHOVEL_TITANIUM("titanium_shovel", "shovelTitanium", EnumRarity.RARE),
	SHOVEL_TITANIUM_DARK("dark_titanium_shovel", "shovelTitaniumDark", EnumRarity.EPIC),
	SHOVEL_COPPER("copper_shovel", "shovelCopper"),
	SHOVEL_FROZIUM("frozium_shovel", "shovelFrozium"),
	SHOVEL_IGNUM("ignum_shovel", "shovelIgnum"),
	
	AXE_TITANIUM("titanium_axe", "axeTitanium", EnumRarity.RARE),
	AXE_TITANIUM_DARK("dark_titanium_axe", "axeTitaniumDark", EnumRarity.EPIC),
	AXE_COPPER("copper_axe", "axeCopper"),
	AXE_FROZIUM("frozium_axe", "axeFrozium"),
	AXE_IGNUM("ignum_axe", "axeIgnum"),
	
	SWORD_TITANIUM("titanium_sword", "swordTitanium", EnumRarity.RARE),
	SWORD_TITANIUM_DARK("dark_titanium_sword", "swordTitaniumDark", EnumRarity.EPIC),
	SWORD_COPPER("copper_sword", "swordCopper"),
	SWORD_FROZIUM("frozium_sword", "swordFrozium"),
	SWORD_IGNUM("ignum_sword", "swordIgnum"),
	
	HOE_TITANIUM("titanium_hoe", "hoeTitanium", EnumRarity.RARE),
	HOE_TITANIUM_DARK("dark_titanium_hoe", "hoeTitaniumDark", EnumRarity.EPIC),
	HOE_COPPER("copper_hoe", "hoeCopper"),
	HOE_FROZIUM("frozium_hoe", "hoeFrozium"),
	HOE_IGNUM("ignum_hoe", "hoeIgnum"),
	
	HAMMER_TITANIUM("titanium_hammer", "hammerTitanium", EnumRarity.RARE),
	HAMMER_TITANIUM_DARK("dark_titanium_hammer", "hammerTitaniumDark", EnumRarity.EPIC),
	HAMMER_COPPER("copper_hammer", "hammerCopper"),
	HAMMER_FROZIUM("frozium_hammer", "hammerFrozium"),
	HAMMER_IGNUM("ignum_hammer", "hammerIgnum"),
	
	HELMET_TITANIUM("titanium_helmet", "helmetTitanium", EnumRarity.RARE),
	HELMET_TITANIUM_DARK("dark_titanium_helmet", "helmetTitaniumDark", EnumRarity.EPIC),
	HELMET_COPPER("copper_helmet", "helmetCopper"),
	HELMET_FROZIUM("frozium_helmet", "helmetFrozium"),
	HELMET_IGNUM("ignum_helmet", "helmetIgnum"),
	HELMET_DARKANIUM("darkanium_helmet", "helmetDarkanium", EnumRarity.EPIC),
	HELMET_HALLOWED("hallowed_helmet", "helmetHallowed", EnumRarity.UNCOMMON),
	
	CHESTPLATE_TITANIUM("titanium_chestplate", "chestplateTitanium", EnumRarity.RARE),
	CHESTPLATE_TITANIUM_DARK("dark_titanium_chestplate", "chestplateTitaniumDark", EnumRarity.EPIC),
	CHESTPLATE_COPPER("copper_chestplate", "chestplateCopper"),
	CHESTPLATE_FROZIUM("frozium_chestplate", "chestplateFrozium"),
	CHESTPLATE_IGNUM("ignum_chestplate", "chestplateIgnum"),
	CHESTPLATE_DARKANIUM("darkanium_chestplate", "chestplateDarkanium", EnumRarity.EPIC),
	CHESTPLATE_HALLOWED("hallowed_chestplate", "chestplateHallowed", EnumRarity.UNCOMMON),
	
	LEGGINGS_TITANIUM("titanium_leggings", "leggingsTitanium", EnumRarity.RARE),
	LEGGINGS_TITANIUM_DARK("dark_titanium_leggings", "leggingsTitaniumDark"),
	LEGGINGS_COPPER("copper_leggings", "leggingsCopper"),
	LEGGINGS_FROZIUM("frozium_leggings", "leggingsFrozium"),
	LEGGINGS_IGNUM("ignum_leggings", "leggingsIgnum"),
	LEGGINGS_DARKANIUM("darkanium_leggings", "leggingsDarkanium", EnumRarity.EPIC),
	LEGGINGS_HALLOWED("hallowed_leggings", "leggingsHallowed", EnumRarity.UNCOMMON),
	
	BOOTS_TITANIUM("titanium_boots", "bootsTitanium", EnumRarity.RARE),
	BOOTS_TITANIUM_DARK("dark_titanium_boots", "bootsTitaniumDark", EnumRarity.EPIC),
	BOOTS_COPPER("copper_boots", "bootsCopper"),
	BOOTS_FROZIUM("frozium_boots", "bootsFrozium"),
	BOOTS_IGNUM("ignum_boots", "bootsIgnum"),
	BOOTS_DARKANIUM("darkanium_boots", "bootsDarkanium", EnumRarity.EPIC),
	BOOTS_HALLOWED("hallowed_boots", "bootsHallowed", EnumRarity.UNCOMMON),
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
		return rarity;
	}

	EnumItemsNames(String registryName, String unlocalizedName) {
		this(registryName, unlocalizedName, EnumRarity.COMMON, new String[0]);
	}
	
	EnumItemsNames(String registryName, String unlocalizedName, String... oreDictNames) {
		this(registryName, unlocalizedName, EnumRarity.COMMON, oreDictNames);
	}
	
	EnumItemsNames(String registryName, String unlocalizedName, EnumRarity rarity) {
		this(registryName, unlocalizedName, rarity, new String[0]);
	}
	
	EnumItemsNames(String registryName, String unlocalizedName, EnumRarity rarity, String... oreDictNames) {
		this.registryName = registryName;
		this.unlocalizedName = unlocalizedName;
		this.oreDictNames = oreDictNames;
		this.rarity = rarity;
	}

}
