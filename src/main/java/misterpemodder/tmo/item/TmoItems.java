package misterpemodder.tmo.item;

import java.util.HashMap;
import java.util.Map;
import misterpemodder.tmo.TmoConstants;
import misterpemodder.tmo.TooManyOres;
import misterpemodder.tmo.item.tool.TmoShovelItem;
import misterpemodder.tmo.item.tool.TmoSwordItem;
import misterpemodder.tmo.item.tool.HammerItem;
import misterpemodder.tmo.item.tool.TmoAxeItem;
import misterpemodder.tmo.item.tool.TmoHoeItem;
import misterpemodder.tmo.item.tool.TmoPickaxeItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public final class TmoItems {
  private static final Map<Identifier, Item> TMO_ITEMS = new HashMap<>();

  public static final Item TAB_ICON = add("tab_icon", new Item(new Item.Settings()));
  public static final Item TITANIUM_INGOT =
      add("titanium_ingot", new Item(new Settings().rarity(Rarity.RARE)));
  public static final Item DARK_TITANIUM_INGOT =
      add("dark_titanium_ingot", new Item(new Settings().rarity(Rarity.EPIC)));
  public static final Item POOR_TITANIUM_INGOT =
      add("poor_titanium_ingot", new Item(new Settings()));
  public static final Item TITANITE_INGOT = add("titanite_ingot", new Item(new Settings()));
  public static final Item COPPER_INGOT = add("copper_ingot", new Item(new Settings()));
  public static final Item DARKANIUM_INGOT =
      add("darkanium_ingot", new Item(new Settings().rarity(Rarity.EPIC)));
  public static final Item ENDER_MATTER_INGOT = add("ender_matter_ingot", new Item(new Settings()));
  public static final Item ANCIENT_GOLD_INGOT =
      add("ancient_gold_ingot", new Item(new Settings().rarity(Rarity.UNCOMMON)));
  public static final Item PLATINUM_INGOT = add("platinum_ingot", new Item(new Settings()));
  public static final Item REDSTONE_INGOT = add("redstone_ingot", new Item(new Settings()));
  public static final Item CARBON_INGOT = add("carbon_ingot", new Item(new Settings()));
  public static final Item FROZIUM_GEM = add("frozium_gem", new Item(new Settings()));
  public static final Item IGNUM_GEM = add("ignum_gem", new Item(new Settings()));

  public static final Item TITANIUM_PLATE = add("titanium_plate", new Item(new Settings()));
  public static final Item IRON_PLATE = add("iron_plate", new Item(new Settings()));
  public static final Item REDSTONE_PLATE = add("redstone_plate", new Item(new Settings()));
  public static final Item HALLOWED_PLATE =
      add("hallowed_plate", new Item(new Settings().rarity(Rarity.UNCOMMON)));

  public static final Item BASIC_LOCK = add("basic_lock", new Item(new Settings()));
  public static final Item REINFORCED_LOCK = add("reinforced_lock", new Item(new Settings()));
  public static final Item TITANIUM_BUCKET = add("titanium_bucket", new Item(new Settings()));
  public static final Item BASIC_TO_STANDARD_CASING_UPGRADE =
      add("basic_to_standard_casing_upgrade", new Item(new Settings()));
  public static final Item STANDARD_TO_IMPROVED_CASING_UPGRADE =
      add("standard_to_improved_casing_upgrade", new Item(new Settings()));

  public static final Item TITANIUM_DUST = add("titanium_dust", new Item(new Settings()));
  public static final Item DIRTY_TITANIUM_DUST =
      add("dirty_titanium_dust", new Item(new Settings()));
  public static final Item POOR_TITANIUM_DUST = add("poor_titanium_dust", new Item(new Settings()));
  public static final Item TITANITE_DUST = add("titanite_dust", new Item(new Settings()));
  public static final Item COPPER_DUST = add("copper_dust", new Item(new Settings()));
  public static final Item DIRTY_COPPER_DUST = add("dirty_copper_dust", new Item(new Settings()));
  public static final Item ANCIENT_GOLD_DUST = add("ancient_gold_dust", new Item(new Settings()));
  public static final Item PLATINUM_DUST = add("platinum_dust", new Item(new Settings()));
  public static final Item DIRTY_PLATINUM_DUST =
      add("dirty_platinum_dust", new Item(new Settings()));
  public static final Item FROZIUM_DUST = add("frozium_dust", new Item(new Settings()));
  public static final Item IGNUM_DUST = add("ignum_dust", new Item(new Settings()));

  public static final PickaxeItem TITANIUM_PICKAXE =
      add("titanium_pickaxe", new TmoPickaxeItem(TmoToolMaterials.TITANIUM));
  public static final PickaxeItem DARK_TITANIUM_PICKAXE =
      add("dark_titanium_pickaxe", new TmoPickaxeItem(TmoToolMaterials.DARK_TITANIUM));
  public static final PickaxeItem COPPER_PICKAXE =
      add("copper_pickaxe", new TmoPickaxeItem(TmoToolMaterials.COPPER));
  public static final PickaxeItem FROZIUM_PICKAXE =
      add("frozium_pickaxe", new TmoPickaxeItem(TmoToolMaterials.FROZIUM));
  public static final PickaxeItem IGNUM_PICKAXE =
      add("ignum_pickaxe", new TmoPickaxeItem(TmoToolMaterials.IGNUM));

  public static final AxeItem TITANIUM_AXE =
      add("titanium_axe", new TmoAxeItem(TmoToolMaterials.TITANIUM));
  public static final AxeItem DARK_TITANIUM_AXE =
      add("dark_titanium_axe", new TmoAxeItem(TmoToolMaterials.DARK_TITANIUM));
  public static final AxeItem COPPER_AXE =
      add("copper_axe", new TmoAxeItem(TmoToolMaterials.COPPER));
  public static final AxeItem FROZIUM_AXE =
      add("frozium_axe", new TmoAxeItem(TmoToolMaterials.FROZIUM));
  public static final AxeItem IGNUM_AXE = add("ignum_axe", new TmoAxeItem(TmoToolMaterials.IGNUM));

  public static final ShovelItem TITANIUM_SHOVEL =
      add("titanium_shovel", new TmoShovelItem(TmoToolMaterials.TITANIUM));
  public static final ShovelItem DARK_TITANIUM_SHOVEL =
      add("dark_titanium_shovel", new TmoShovelItem(TmoToolMaterials.DARK_TITANIUM));
  public static final ShovelItem COPPER_SHOVEL =
      add("copper_shovel", new TmoShovelItem(TmoToolMaterials.COPPER));
  public static final ShovelItem FROZIUM_SHOVEL =
      add("frozium_shovel", new TmoShovelItem(TmoToolMaterials.FROZIUM));
  public static final ShovelItem IGNUM_SHOVEL =
      add("ignum_shovel", new TmoShovelItem(TmoToolMaterials.IGNUM));

  public static final SwordItem TITANIUM_SWORD =
      add("titanium_sword", new TmoSwordItem(TmoToolMaterials.TITANIUM));
  public static final SwordItem DARK_TITANIUM_SWORD =
      add("dark_titanium_sword", new TmoSwordItem(TmoToolMaterials.DARK_TITANIUM));
  public static final SwordItem COPPER_SWORD =
      add("copper_sword", new TmoSwordItem(TmoToolMaterials.COPPER));
  public static final SwordItem FROZIUM_SWORD =
      add("frozium_sword", new TmoSwordItem(TmoToolMaterials.FROZIUM));
  public static final SwordItem IGNUM_SWORD =
      add("ignum_sword", new TmoSwordItem(TmoToolMaterials.IGNUM));

  public static final HoeItem TITANIUM_HOE =
      add("titanium_hoe", new TmoHoeItem(TmoToolMaterials.TITANIUM));
  public static final HoeItem DARK_TITANIUM_HOE =
      add("dark_titanium_hoe", new TmoHoeItem(TmoToolMaterials.DARK_TITANIUM));
  public static final HoeItem COPPER_HOE =
      add("copper_hoe", new TmoHoeItem(TmoToolMaterials.COPPER));
  public static final HoeItem FROZIUM_HOE =
      add("frozium_hoe", new TmoHoeItem(TmoToolMaterials.FROZIUM));
  public static final HoeItem IGNUM_HOE = add("ignum_hoe", new TmoHoeItem(TmoToolMaterials.IGNUM));

  public static final HammerItem TITANIUM_HAMMER =
      add("titanium_hammer", new HammerItem(TmoToolMaterials.TITANIUM));
  public static final HammerItem DARK_TITANIUM_HAMMER =
      add("dark_titanium_hammer", new HammerItem(TmoToolMaterials.DARK_TITANIUM));
  public static final HammerItem COPPER_HAMMER =
      add("copper_hammer", new HammerItem(TmoToolMaterials.COPPER));
  public static final HammerItem FROZIUM_HAMMER =
      add("frozium_hammer", new HammerItem(TmoToolMaterials.FROZIUM));
  public static final HammerItem IGNUM_HAMMER =
      add("ignum_hammer", new HammerItem(TmoToolMaterials.IGNUM));

  public static final ArmorItem TITANIUM_HELMET =
      add("titanium_helmet", new TmoArmorItem(TmoArmorMaterials.TITANIUM, EquipmentSlot.HEAD));
  public static final ArmorItem TITANIUM_CHESTPLATE =
      add("titanium_chestplate", new TmoArmorItem(TmoArmorMaterials.TITANIUM, EquipmentSlot.CHEST));
  public static final ArmorItem TITANIUM_LEGGINGS =
      add("titanium_leggings", new TmoArmorItem(TmoArmorMaterials.TITANIUM, EquipmentSlot.LEGS));
  public static final ArmorItem TITANIUM_BOOTS =
      add("titanium_boots", new TmoArmorItem(TmoArmorMaterials.TITANIUM, EquipmentSlot.FEET));

  public static final ArmorItem DARK_TITANIUM_HELMET = add("dark_titanium_helmet",
      new TmoArmorItem(TmoArmorMaterials.DARK_TITANIUM, EquipmentSlot.HEAD));
  public static final ArmorItem DARK_TITANIUM_CHESTPLATE = add("dark_titanium_chestplate",
      new TmoArmorItem(TmoArmorMaterials.DARK_TITANIUM, EquipmentSlot.CHEST));
  public static final ArmorItem DARK_TITANIUM_LEGGINGS = add("dark_titanium_leggings",
      new TmoArmorItem(TmoArmorMaterials.DARK_TITANIUM, EquipmentSlot.LEGS));
  public static final ArmorItem DARK_TITANIUM_BOOTS = add("dark_titanium_boots",
      new TmoArmorItem(TmoArmorMaterials.DARK_TITANIUM, EquipmentSlot.FEET));

  public static final ArmorItem COPPER_HELMET =
      add("copper_helmet", new TmoArmorItem(TmoArmorMaterials.COPPER, EquipmentSlot.HEAD));
  public static final ArmorItem COPPER_CHESTPLATE =
      add("copper_chestplate", new TmoArmorItem(TmoArmorMaterials.COPPER, EquipmentSlot.CHEST));
  public static final ArmorItem COPPER_LEGGINGS =
      add("copper_leggings", new TmoArmorItem(TmoArmorMaterials.COPPER, EquipmentSlot.LEGS));
  public static final ArmorItem COPPER_BOOTS =
      add("copper_boots", new TmoArmorItem(TmoArmorMaterials.COPPER, EquipmentSlot.FEET));

  public static final ArmorItem FROZIUM_HELMET =
      add("frozium_helmet", new TmoArmorItem(TmoArmorMaterials.FROZIUM, EquipmentSlot.HEAD));
  public static final ArmorItem FROZIUM_CHESTPLATE =
      add("frozium_chestplate", new TmoArmorItem(TmoArmorMaterials.FROZIUM, EquipmentSlot.CHEST));
  public static final ArmorItem FROZIUM_LEGGINGS =
      add("frozium_leggings", new TmoArmorItem(TmoArmorMaterials.FROZIUM, EquipmentSlot.LEGS));
  public static final ArmorItem FROZIUM_BOOTS =
      add("frozium_boots", new TmoArmorItem(TmoArmorMaterials.FROZIUM, EquipmentSlot.FEET));

  public static final ArmorItem IGNUM_HELMET =
      add("ignum_helmet", new TmoArmorItem(TmoArmorMaterials.IGNUM, EquipmentSlot.HEAD));
  public static final ArmorItem IGNUM_CHESTPLATE =
      add("ignum_chestplate", new TmoArmorItem(TmoArmorMaterials.IGNUM, EquipmentSlot.CHEST));
  public static final ArmorItem IGNUM_LEGGINGS =
      add("ignum_leggings", new TmoArmorItem(TmoArmorMaterials.IGNUM, EquipmentSlot.LEGS));
  public static final ArmorItem IGNUM_BOOTS =
      add("ignum_boots", new TmoArmorItem(TmoArmorMaterials.IGNUM, EquipmentSlot.FEET));

  public static final ArmorItem DARKANIUM_HELMET =
      add("darkanium_helmet", new TmoArmorItem(TmoArmorMaterials.DARKANIUM, EquipmentSlot.HEAD));
  public static final ArmorItem DARKANIUM_CHESTPLATE = add("darkanium_chestplate",
      new TmoArmorItem(TmoArmorMaterials.DARKANIUM, EquipmentSlot.CHEST));
  public static final ArmorItem DARKANIUM_LEGGINGS =
      add("darkanium_leggings", new TmoArmorItem(TmoArmorMaterials.DARKANIUM, EquipmentSlot.LEGS));
  public static final ArmorItem DARKANIUM_BOOTS =
      add("darkanium_boots", new TmoArmorItem(TmoArmorMaterials.DARKANIUM, EquipmentSlot.FEET));

  public static final ArmorItem HALLOWED_HELMET =
      add("hallowed_helmet", new TmoArmorItem(TmoArmorMaterials.HALLOWED, EquipmentSlot.HEAD));
  public static final ArmorItem HALLOWED_CHESTPLATE =
      add("hallowed_chestplate", new TmoArmorItem(TmoArmorMaterials.HALLOWED, EquipmentSlot.CHEST));
  public static final ArmorItem HALLOWED_LEGGINGS =
      add("hallowed_leggings", new TmoArmorItem(TmoArmorMaterials.HALLOWED, EquipmentSlot.LEGS));
  public static final ArmorItem HALLOWED_BOOTS =
      add("hallowed_boots", new TmoArmorItem(TmoArmorMaterials.HALLOWED, EquipmentSlot.FEET));

  private static <T extends Item> T add(String id, T item) {
    TMO_ITEMS.put(new Identifier(TmoConstants.MOD_ID, id), item);
    return item;
  }

  public static void register(Registry<? super Item> registry) {
    TooManyOres.LOGGER.info("[TooManyOres] Registering {} items...", TMO_ITEMS.size());
    for (Map.Entry<Identifier, Item> entry : TMO_ITEMS.entrySet())
      Registry.register(registry, entry.getKey(), entry.getValue());
    TMO_ITEMS.clear();
    TooManyOres.LOGGER.info("[TooManyOres] done!");
  }

  private static class Settings extends Item.Settings {
    public Settings() {
      itemGroup(TmoConstants.ITEM_GROUP);
    }
  }
}
