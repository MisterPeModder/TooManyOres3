package misterpemodder.tmo.main.config;

import net.minecraftforge.common.config.Configuration;

public final class ConfigValues {
	
	public enum ConfigCategories {
		ORE_GEN("ore gen", "in world ore generation."),
		TOOLS("tools", "too op for you?"),
		ARMOR("armor", "you don't like a special armor set? disable it right there!"),
		;
		
		public final String name;
		public final String desc;
		
		private ConfigCategories(String name, String desc) {
			this.name = name;
			this.desc = desc;
		}
	}
	
	public enum BoolValues {
		TITANIUM_GEN(ConfigCategories.ORE_GEN, "titanium gen", true, "Should titanium ore spawn in the World?"),
		ANCIENT_GOLD_GEN(ConfigCategories.ORE_GEN, "ancient gold gen", true, "Should ancient gold ore spawn in the World?"),
		DARKANIUM_GEN(ConfigCategories.ORE_GEN, "darkanium gen", true, "Should darkanium ore spawn in the World?"),
		DARKANIUM_GEN_NETHER(ConfigCategories.ORE_GEN, "nether darkanium gen", true, "Should nether darkanium ore spawn in the World?"),
		FROZIUM_GEN(ConfigCategories.ORE_GEN, "frozium gen", true, "Should frozium ore spawn in the World?"),
		IGNUM_GEN(ConfigCategories.ORE_GEN, "ignum gen", true, "Should ignum ore spawn in the World?"),
		IGNUM_GEN_NETHER(ConfigCategories.ORE_GEN, "nether ignum gen", true, "Should nether ignum ore spawn in the World?"),
		PLATINUM_GEN(ConfigCategories.ORE_GEN, "platinum gen", true, "Should platinum ore spawn in the World?"),
		PLATINUM_GEN_NETHER(ConfigCategories.ORE_GEN, "nether platinum gen", true, "Should nether platinum ore spawn in the World?"),
		ENDER_MATTER_GEN(ConfigCategories.ORE_GEN, "ender matter gen", true, "Should ender matter ore spawn in the World?"),
		COPPER(ConfigCategories.ORE_GEN, "copper gen", true, "Should copper ore spawn in the World?"),
		CARBON_GEN(ConfigCategories.ORE_GEN, "carbon gen", true, "Should carbon ore spawn in the World?"),
		
		TITANIUM_TOOLS(ConfigCategories.TOOLS, "titanium tools", true, "Enable titanium tools?"),
		TITANIUM_DARK_TOOLS(ConfigCategories.TOOLS, "dark titanium tools", true, "Enable dark titanium tools?"),
		COPPER_TOOLS(ConfigCategories.TOOLS, "copper tools", true, "Enable copper tools?"),
		IGNUM_TOOLS(ConfigCategories.TOOLS, "ignum tools", true, "Enable ignum tools?"),
		FROZIUM_TOOLS(ConfigCategories.TOOLS, "frozium tools", true, "Enable frozium tools?"),
		
		TITANIUM_ARMOR(ConfigCategories.ARMOR, "titanium armor", true, "Enable titanium armor?"),
		DARK_TITANIUM_ARMOR(ConfigCategories.ARMOR, "dark titanium armor", true, "Enable dark titanium armor?"),
		COPPER_ARMOR(ConfigCategories.ARMOR, "copper armor", true, "Enable copper armor?"),
		FROZIUM_ARMOR(ConfigCategories.ARMOR, "frozium armor", true, "Enable frozium armor?"),
		IGNUM_ARMOR(ConfigCategories.ARMOR, "ignum armor", true, "Enable ignum armor?"),
		DARKANIUM_ARMOR(ConfigCategories.ARMOR, "darkanium armor", true, "Enable darkanium armor?"),
		HALLOWED_ARMOR(ConfigCategories.ARMOR, "hallowed armor", true, "Enable hallowed armor?"),
		;
		
		public boolean currentValue;
		public final String categoryName;
		public final String name;
		public final boolean defaultValue;
		public final String desc;
		
		private BoolValues(ConfigCategories category, String name, boolean defaultValue, String desc) {
			this.categoryName = category.name;
			this.name = name;
			this.defaultValue = defaultValue;
			this.desc = desc;
		}
		
		
		
	}
	
	public static void setValues(Configuration config) {
		
		for(BoolValues value : BoolValues.values()) {
			value.currentValue = config.getBoolean(value.name, value.categoryName , value.defaultValue, value.desc);
		}
		
	}
	
}
