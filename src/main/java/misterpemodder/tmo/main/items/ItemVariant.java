package misterpemodder.tmo.main.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public abstract class ItemVariant {

	private String name;
	private String unlocalizedName;
	private String[] oreDictNames;
	private EnumRarity rarity;
	
	private ItemVariant(String name, String unlocalizedName, String... oreDictNames) {
		this(name, unlocalizedName, null, oreDictNames);
	}
	
	private ItemVariant(String name, String unlocalizedName, @Nullable EnumRarity rarity, String... oreDictNames) {
		this.name = name;
		this.unlocalizedName = unlocalizedName;
		this.oreDictNames = oreDictNames;
		this.rarity = rarity;
	}
	
	public abstract <T extends ItemVariant> List<T> getVariants();
	
	public String getName() {
		return this.name;
	}
	
	public String getUnlocalizedName() {
		return this.unlocalizedName;
	}
	
	public int getMeta() {
		int meta = this.getVariants().indexOf(this);
		return meta < 0? 0 : meta;
	}

	public String[] getOreDictNames() {
		return this.oreDictNames;
	}
	
	public EnumRarity getRarity() {
		return this.rarity == null? EnumRarity.COMMON : rarity;
	}
	
	public static class IngotVariant extends ItemVariant {
		
		public static List<IngotVariant> ingotVariants = new ArrayList<>();
		
		public static final IngotVariant TITANIUM_INGOT = new IngotVariant("titanium", "titanium", EnumRarity.RARE, "ingotTitaniumBlue");
		public static final IngotVariant TITANIUM_INGOT_DARK = new IngotVariant("titanium_dark", "titaniumDark", EnumRarity.EPIC, "ingotTitaniumDark");
		public static final IngotVariant TITANIUM_INGOT_POOR = new IngotVariant("titanium_poor", "titaniumPoor", "ingotTitanium");
		public static final IngotVariant TITANITE_INGOT = new IngotVariant("titanite", "titanite", "ingotTitanite");
		public static final IngotVariant CARBON_INGOT = new IngotVariant("carbon", "carbon", "ingotCarbon");
		public static final IngotVariant COPPER_INGOT = new IngotVariant("copper", "copper", "ingotCopper");
		public static final IngotVariant DARKANIUM_INGOT = new IngotVariant("darkanium", "darkanium", EnumRarity.EPIC, "ingotDarkanium");
		public static final IngotVariant ENDER_MATTER_INGOT = new IngotVariant("ender_matter", "enderMatter", "ingotEnderMatter");
		public static final IngotVariant ANCIENT_GOLD_INGOT = new IngotVariant("ancient_gold", "goldAncient", EnumRarity.UNCOMMON, "ingotGoldAncient");
		public static final IngotVariant PLATINUM_INGOT = new IngotVariant("platinum", "platinum", "ingotPlatinum");
		public static final IngotVariant REDSTONE_INGOT = new IngotVariant("redstone", "redstone", "ingotRedstone");
		
		private IngotVariant(String name, String unlocalizedName, String... oreDictNames) {
			this(name, unlocalizedName, EnumRarity.COMMON, oreDictNames);
		}
		
		private IngotVariant(String name, String unlocalizedName, EnumRarity rarity, String... oreDictNames) {
			super(name, unlocalizedName, rarity, oreDictNames);
			ingotVariants.add(this);
		}

		@Override
		@SuppressWarnings("unchecked")
		public List<IngotVariant> getVariants() {
			return ingotVariants;
		}
	}
	
	public static class GemVariant extends ItemVariant {
		
		public static List<GemVariant> gemVariants = new ArrayList<>();
		
		public static final GemVariant FROZIUM_GEM = new GemVariant("frozium", "frozium", "gemFrozium");
		public static final GemVariant IGNUM_GEM = new GemVariant("ignum", "ignum", "gemIgnum");
		
		private GemVariant(String name, String unlocalizedName, String... oreDictNames) {
			super(name, unlocalizedName, oreDictNames);
			gemVariants.add(this);
		}

		@Override
		@SuppressWarnings("unchecked")
		public List<GemVariant> getVariants() {
			return gemVariants;
		}
	}
	
	public static class PlateVariant extends ItemVariant {
		
		public static List<PlateVariant> plateVariants = new ArrayList<>();
		
		public static final PlateVariant TITANIUM_PLATE = new PlateVariant("titanium", "titanium", "plateTitanium");
		public static final PlateVariant IRON_PLATE = new PlateVariant("iron", "iron", "plateIron");
		public static final PlateVariant REDSTONE_PLATE = new PlateVariant("redstone", "redstone", "plateRedstone");
		public static final PlateVariant HALLOWED_PLATE = new PlateVariant("hallowed", "hallowed", "plateHallowed");
		
		private PlateVariant(String name, String unlocalizedName, String... oreDictNames) {
			super(name, unlocalizedName, oreDictNames);
			plateVariants.add(this);
		}
		
		@Override
		public EnumRarity getRarity() {
			return this == PlateVariant.HALLOWED_PLATE? EnumRarity.UNCOMMON : super.getRarity();
		}

		@Override
		@SuppressWarnings("unchecked")
		public List<PlateVariant> getVariants() {
			return plateVariants;
		}
	}
	
	public static class LockVariant extends ItemVariant {
		
		public static List<LockVariant> lockVariants = new ArrayList<>();
		
		public static final LockVariant BASIC = new LockVariant("basic", "basic", 1);
		public static final LockVariant BASIC_BROKEN = new LockVariant("basic_broken", "basic", ItemStackUtils.newVariantStack(TheItems.PLATE, PlateVariant.TITANIUM_PLATE, 2), 0, 10);
		public static final LockVariant REINFORCED = new LockVariant("reinforced", "reinforced", 3);
		public static final LockVariant REINFORCED_BROKEN = new LockVariant("reinforced_broken", "reinforced", ItemStackUtils.newVariantStack(TheItems.PLATE, PlateVariant.TITANIUM_PLATE, 4), 2, 20);
		
		private ItemStack repairItem;
		private int otherVariant;
		private int cost;
		private Boolean isBroken = false;
		
		LockVariant(String name, String unlocalizedName, int otherVariant, String... oreDictNames) {
			this(name, unlocalizedName, ItemStack.EMPTY, otherVariant, 0, oreDictNames);
			this.isBroken = false;
		}

		private LockVariant(String name, String unlocalizedName, ItemStack repairItem, int otherVariant, int cost, String... oreDictNames) {
			super(name, unlocalizedName, oreDictNames);
			this.repairItem = repairItem;
			this.otherVariant = otherVariant;
			this.cost = cost;
			this.isBroken = true;
			lockVariants.add(this);
		}

		@Override
		@SuppressWarnings("unchecked")
		public List<LockVariant> getVariants() {
			return lockVariants;
		}
		
		public ItemStack getRepairItem() {
			return repairItem;
		}

		public ItemStack getOtherVariant() {
			return ItemStackUtils.newVariantStack(TheItems.LOCK, this.getVariants().get(otherVariant));
		}
		
		public int getCost() {
			return this.cost;
		}
		
		public boolean isBroken() {
			return this.isBroken;
		}
		
	}
	
}
