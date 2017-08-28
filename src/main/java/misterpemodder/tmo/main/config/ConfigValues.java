package misterpemodder.tmo.main.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;

public final class ConfigValues {
	
	public static enum ConfigCategories {
		ORE_GEN("ore gen", "in world ore generation.", "oreGen"),
		TOOLS("tools", "too OP for you?", "tools"),
		ARMOR("armor", "you don't like a special armor set? disable it right there!", "armor"),
		COMPAT("compatibility", "enable/disables mod compatibility features.", "compat"),
		CLIENT("client", "client-only settings.", "client"),
		MISC("miscellaneous", "random configs.", "misc"),
		;
		
		public final String name;
		public final String desc;
		public final String langKey;
		
		private ConfigCategories(String name, String desc, String langKey) {
			this.name = name;
			this.desc = desc;
			this.langKey = "tmo.configCategory."+langKey;
		}
	}
	
	public static enum BoolValues {
		TITANIUM_GEN(ConfigCategories.ORE_GEN, "titanium gen", true, "Should titanium ore spawn in the World?", "gen.titanium"),
		ANCIENT_GOLD_GEN(ConfigCategories.ORE_GEN, "ancient gold gen", true, "Should ancient gold ore spawn in the World?", "gen.ancientGold"),
		DARKANIUM_GEN(ConfigCategories.ORE_GEN, "darkanium gen", true, "Should darkanium ore spawn in the World?", "gen.darkanium"),
		DARKANIUM_GEN_NETHER(ConfigCategories.ORE_GEN, "nether darkanium gen", true, "Should nether darkanium ore spawn in the World?", "gen.darkaniumNether"),
		FROZIUM_GEN(ConfigCategories.ORE_GEN, "frozium gen", true, "Should frozium ore spawn in the World?", "gen.frozium"),
		IGNUM_GEN(ConfigCategories.ORE_GEN, "ignum gen", true, "Should ignum ore spawn in the World?", "gen.ignum"),
		IGNUM_GEN_NETHER(ConfigCategories.ORE_GEN, "nether ignum gen", true, "Should nether ignum ore spawn in the World?", "gen.ignumNether"),
		PLATINUM_GEN(ConfigCategories.ORE_GEN, "platinum gen", true, "Should platinum ore spawn in the World?", "gen.platinum"),
		PLATINUM_GEN_NETHER(ConfigCategories.ORE_GEN, "nether platinum gen", true, "Should nether platinum ore spawn in the World?", "gen.platinumNether"),
		ENDER_MATTER_GEN(ConfigCategories.ORE_GEN, "ender matter gen", true, "Should ender matter ore spawn in the World?", "gen.enderMatter"),
		COPPER(ConfigCategories.ORE_GEN, "copper gen", true, "Should copper ore spawn in the World?", "gen.copper"),
		CARBON_GEN(ConfigCategories.ORE_GEN, "carbon gen", true, "Should carbon ore spawn in the World?", "gen.carbon"),
		
		TITANIUM_TOOLS(ConfigCategories.TOOLS, "titanium tools", true, "Enable titanium tools?", "tools.titanium"),
		TITANIUM_DARK_TOOLS(ConfigCategories.TOOLS, "dark titanium tools", true, "Enable dark titanium tools?", "tools.titaniumDark"),
		COPPER_TOOLS(ConfigCategories.TOOLS, "copper tools", true, "Enable copper tools?", "tools.copper"),
		IGNUM_TOOLS(ConfigCategories.TOOLS, "ignum tools", true, "Enable ignum tools?", "tools.ignum"),
		FROZIUM_TOOLS(ConfigCategories.TOOLS, "frozium tools", true, "Enable frozium tools?", "tools.frozium"),
		
		TITANIUM_ARMOR(ConfigCategories.ARMOR, "titanium armor", true, "Enable titanium armor?", "armor.titanium"),
		DARK_TITANIUM_ARMOR(ConfigCategories.ARMOR, "dark titanium armor", true, "Enable dark titanium armor?", "armor.titaniumDark"),
		COPPER_ARMOR(ConfigCategories.ARMOR, "copper armor", true, "Enable copper armor?", "armor.copper"),
		FROZIUM_ARMOR(ConfigCategories.ARMOR, "frozium armor", true, "Enable frozium armor?", "armor.frozium"),
		IGNUM_ARMOR(ConfigCategories.ARMOR, "ignum armor", true, "Enable ignum armor?", "armor.ignum"),
		DARKANIUM_ARMOR(ConfigCategories.ARMOR, "darkanium armor", true, "Enable darkanium armor?", "armor.darkanium"),
		HALLOWED_ARMOR(ConfigCategories.ARMOR, "hallowed armor", true, "Enable hallowed armor?", "armor.hallowed"),
		
		ACT_ADD_COMPAT_EMPOWERER(ConfigCategories.COMPAT, "Actually Additions empowerer recipes", true, "Add custom recipes to the ewmpowerer?", "compat.AAEmpowerer"),
		ACT_ADD_COMPAT_RECONSTRUCTOR(ConfigCategories.COMPAT, "Actually Additions atomic reconstructor recipes", true, "Molecular Shiftulator? Spatial Replacer?", "compat.AAReconstructor"),
		ACT_ADD_COMPAT_MINING(ConfigCategories.COMPAT, "Actually Additions mining lens", true, "Add TMO ores to the mining lens?", "compat.AAMiningLens"),
		TOP_COMPAT(ConfigCategories.COMPAT, "The One Probe compat", true, "The One Probe compatibility?", "compat.top"),
		TOP_COMPAT_HIDE_CONTENT(ConfigCategories.COMPAT, "The One Probe: hide content", true, "Hide locked titanium chest content?", "compat.topHideContent"),
		BAUBLES_COMPAT(ConfigCategories.COMPAT, "baubles inventory", true, "Enable baubles compatibility?", "compat.baubles"),
		;
		
		public boolean currentValue;
		public final String categoryName;
		public final String name;
		public final boolean defaultValue;
		public final String desc;
		public final String langKey;
		
		private BoolValues(ConfigCategories category, String name, boolean defaultValue, String desc, String langKey) {
			this.categoryName = category.name;
			this.name = name;
			this.defaultValue = defaultValue;
			this.desc = desc;
			this.langKey = "tmo.configValue.bool."+langKey;
		}
	}
	
	public static enum IntValues {
		BUCKET_CAPACITY(ConfigCategories.MISC, "Max bucket capacity", 4*Fluid.BUCKET_VOLUME, 0, 16*Fluid.BUCKET_VOLUME, "Too small capacity?", "misc.bucketCapacity"),
		BASIC_LOCK_BREAK_CHANCE(ConfigCategories.MISC, "Titanium Lock break chance", 50, 20, 200, "Increase or decrease titanium lock break chance. A high break percentage means high chance to break.", "misc.basicLockBreakChance"),
		REINFORCED_LOCK_BREAK_CHANCE(ConfigCategories.MISC, "Smart Lock break chance", 20, 20, 200, "Increase or decrease smart lock break chance.", "misc.reinforcedLockBreakChance"),
		TEMPERATURE_UNIT(ConfigCategories.CLIENT, "Temperature unit", 0, 0, 2, "0 for Kelvin, 1 for Celsius and 2 for Farenheit", "misc.temperatureUnit"),
		;
		public int currentValue;
		public final String categoryName;
		public final String name;
		public final int defaultValue;
		public final int minValue;
		public final int maxValue;
		public final String desc;
		public final String langKey;
		
		private IntValues(ConfigCategories category, String name, int defaultValue, int minValue, int maxValue, String desc, String langKey) {
			this.categoryName = category.name;
			this.name = name;
			this.defaultValue = defaultValue;
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.desc = desc;
			this.langKey = "tmo.configValue.int."+langKey;
		}
	}
	
	public enum FloatValues {
		EXPLODER_STRENGTH(ConfigCategories.MISC, "Exploder strength", 11.0F, 0.0F, 25.0F, "Change the strength of the explosion the explder makes", "misc.exploderStrength"),
		;
		public float currentValue;
		public final String categoryName;
		public final String name;
		public final float defaultValue;
		public final float minValue;
		public final float maxValue;
		public final String desc;
		public final String langKey;
		
		private FloatValues(ConfigCategories category, String name, float defaultValue, float minValue, float maxValue, String desc, String langKey) {
			this.categoryName = category.name;
			this.name = name;
			this.defaultValue = defaultValue;
			this.minValue = minValue;
			this.maxValue = maxValue;
			this.desc = desc;
			this.langKey = "tmo.configValue.float."+langKey;
		}
	}
	
	public static void setValues(Configuration config) {
		
		for(BoolValues value : BoolValues.values()) {
			value.currentValue = config.getBoolean(value.name, value.categoryName , value.defaultValue, value.desc, value.langKey);
		}
		
		for(IntValues value : IntValues.values()) {
			value.currentValue = config.getInt(value.name, value.categoryName, value.defaultValue, value.minValue, value.maxValue, value.desc, value.langKey);
		}
		
		for(FloatValues value : FloatValues.values()) {
			value.currentValue = config.getFloat(value.name, value.categoryName, value.defaultValue, value.minValue, value.maxValue, value.desc, value.langKey);
		}
	}
	
}
