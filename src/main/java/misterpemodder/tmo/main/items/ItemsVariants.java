package misterpemodder.tmo.main.items;

import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.items.base.IItemVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ItemsVariants {
	
	public static ItemStack getVariantStack(IItemVariant variant) {
		return ItemsVariants.getVariantStack(variant, 1);
	}
	
	public static ItemStack getVariantStack(IItemVariant variant, int count) {
		return new ItemStack(variant.getItem(), count, variant.getMeta());
	}
	
	public static enum Ingot implements IItemVariant {
		TITANIUM_INGOT("titanium", "titanium", "ingotTitaniumBlue"),
		TITANIUM_INGOT_DARK("titanium_dark", "titaniumDark", "ingotTitaniumDark"),
		TITANIUM_INGOT_POOR("titanium_poor", "titaniumPoor", "ingotTitanium"),
		CARBON_INGOT("carbon", "carbon", "ingotCarbon"),
		COPPER_INGOT("copper", "copper", "ingotCopper"),
		DARKANIUM_INGOT("darkanium", "darkanium", "ingotDarkanium"),
		ENDER_MATTER_INGOT("ender_matter", "enderMatter", "ingotEnderMatter"),
		ANCIENT_GOLD_INGOT("ancient_gold", "goldAncient", "ingotGoldAncient"),
		PLATINUM_INGOT("platinum", "platinum", "ingotPlatinum"),
		REDSTONE_INGOT("redstone", "redstone", "ingotRedstone"),
		;
		
		private String name;
		private String unlocalizedName;
		private int meta;
		private String[] oreDictNames;

		public String getName() {
			return this.name;
		}
		
		@Override
		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		@Override
		public int getMeta() {
			return this.ordinal();
		}
		
		@Override
		public String[] getOreDictNames() {
			return this.oreDictNames;
		}
		
		@Override
		public ItemMulti getItem() {
			return (ItemMulti)ModItems.TheItems.INGOT.getItem();
		}
		
		private Ingot(String name, String unlocalizedName, String... oreDictNames) {
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.oreDictNames = oreDictNames;
		}
		
	}
	public static enum Gem implements IItemVariant {
		FROZIUM_GEM("frozium", "frozium", "gemFrozium"),
		IGNUM_GEM("ignum", "ignum", "gemIgnum"),
		;
		
		private String name;
		private String unlocalizedName;
		private String[] oreDictNames;

		public String getName() {
			return this.name;
		}
		
		@Override
		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		@Override
		public int getMeta() {
			return this.ordinal();
		}
		
		@Override
		public String[] getOreDictNames() {
			return this.oreDictNames;
		}
		
		@Override
		public ItemMulti getItem() {
			return (ItemMulti)ModItems.TheItems.GEM.getItem();
		}
		
		private Gem(String name, String unlocalizedName, String... oreDictNames) {
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.oreDictNames = oreDictNames;
		}
		
	}
	
	public static enum Plate implements IItemVariant {
		TITANIUM_PLATE("titanium", "titanium", "plateTitanium"),
		IRON_PLATE("iron", "iron", "plateIron"),
		REDSTONE_PLATE("redstone", "redstone", "plateRedstone"),
		HALLOWED_PLATE("hallowed", "hallowed", "plateHallowed"),
		;
		
		private String name;
		private String unlocalizedName;
		private String[] oreDictNames;

		public String getName() {
			return this.name;
		}
		
		@Override
		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		@Override
		public int getMeta() {
			return this.ordinal();
		}
		
		@Override
		public String[] getOreDictNames() {
			return this.oreDictNames;
		}
		
		@Override
		public ItemMulti getItem() {
			return (ItemMulti)ModItems.TheItems.PLATE.getItem();
		}
		
		private Plate(String name, String unlocalizedName, String... oreDictNames) {
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.oreDictNames = oreDictNames;
		}
		
	}
	
	public static enum Lock implements IItemVariant {
		BASIC("basic", "basic", 1),
		BASIC_BROKEN("basic_broken", "basic", ItemsVariants.getVariantStack(Plate.TITANIUM_PLATE, 2), 0, 10),
		REINFORCED("reinforced", "reinforced", 3),
		REINFORCED_BROKEN("reinforced_broken", "reinforced", ItemsVariants.getVariantStack(Plate.TITANIUM_PLATE, 4), 2, 20),
		;
		
		private String name;
		private String unlocalizedName;
		private String[] oreDictNames;
		private ItemStack repairItem;
		private int otherVariant;
		private int cost;
		private Boolean isBroken = false;

		public String getName() {
			return this.name;
		}
		
		@Override
		public String getUnlocalizedName() {
			return this.unlocalizedName;
		}

		@Override
		public int getMeta() {
			return this.ordinal();
		}
		
		@Override
		public String[] getOreDictNames() {
			return this.oreDictNames;
		}
		
		@Override
		public ItemMulti getItem() {
			return (ItemMulti)ModItems.TheItems.LOCK.getItem();
		}
		

		public ItemStack getRepairItem() {
			return repairItem;
		}

		public ItemStack getOtherVariant() {
			return ItemsVariants.getVariantStack(Lock.values()[otherVariant]);
		}
		
		public int getCost() {
			return this.cost;
		}
		
		public boolean isBroken() {
			return this.isBroken;
		}
		
		Lock(String name, String unlocalizedName, int otherVariant, String... oreDictNames) {
			this(name, unlocalizedName, ItemStack.EMPTY, otherVariant, 0, oreDictNames);
			this.isBroken = false;
		}

		Lock(String name, String unlocalizedName, ItemStack repairItem, int otherVariant, int cost, String... oreDictNames) {
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.oreDictNames = oreDictNames;
			this.repairItem = repairItem;
			this.otherVariant = otherVariant;
			this.cost = cost;
			this.isBroken = true;
		}
		
	}
	
}
