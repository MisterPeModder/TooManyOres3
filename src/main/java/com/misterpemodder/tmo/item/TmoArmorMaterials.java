package com.misterpemodder.tmo.item;

import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Lazy;
import net.minecraft.util.Rarity;

public enum TmoArmorMaterials implements ArmorMaterial {
  TITANIUM("titanium", 33, new int[] {3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F,
      () -> Ingredient.ofItems(TmoItems.TITANIUM_INGOT), Rarity.RARE),

  DARK_TITANIUM("dark_titanium", 34, new int[] {3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON,
      2.0F, () -> Ingredient.ofItems(TmoItems.DARK_TITANIUM_INGOT), Rarity.EPIC),

  COPPER("copper", 15, new int[] {2, 4, 5, 2}, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F,
      () -> Ingredient.ofItems(TmoItems.COPPER_INGOT), Rarity.COMMON),

  FROZIUM("frozium", 15, new int[] {2, 6, 7, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.5F,
      () -> Ingredient.ofItems(TmoItems.FROZIUM_GEM), Rarity.COMMON),

  IGNUM("ignum", 15, new int[] {2, 6, 7, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.5F,
      () -> Ingredient.ofItems(TmoItems.IGNUM_GEM), Rarity.COMMON),

  DARKANIUM("darkanium", 36, new int[] {3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.5F,
      () -> Ingredient.ofItems(TmoItems.DARKANIUM_INGOT), Rarity.EPIC),

  HALLOWED("hallowed", 36, new int[] {3, 6, 8, 3}, 10, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE,
      2.5F, () -> Ingredient.ofItems(TmoItems.DARKANIUM_INGOT), Rarity.UNCOMMON);

  private static final int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};
  private final String name;
  private final int durabilityMultiplier;
  private final int[] protectionAmounts;
  private final int enchantability;
  private final SoundEvent equipSound;
  private final float toughness;
  private final Lazy<Ingredient> repairIngredientSupplier;
  private final Rarity rarity;

  private TmoArmorMaterials(String name, int durabilityMultiplier, int[] protectionAmounts,
      int enchantability, SoundEvent equipSound, float toughness, Supplier<Ingredient> supplier,
      Rarity rarity) {
    this.name = name;
    this.durabilityMultiplier = durabilityMultiplier;
    this.protectionAmounts = protectionAmounts;
    this.enchantability = enchantability;
    this.equipSound = equipSound;
    this.toughness = toughness;
    this.repairIngredientSupplier = new Lazy<>(supplier);
    this.rarity = rarity;
  }

  @Override
  public int getDurability(EquipmentSlot equipmentSlot_1) {
    return BASE_DURABILITY[equipmentSlot_1.getEntitySlotId()] * this.durabilityMultiplier;
  }

  @Override
  public int getProtectionAmount(EquipmentSlot equipmentSlot_1) {
    return this.protectionAmounts[equipmentSlot_1.getEntitySlotId()];
  }

  @Override
  public int getEnchantability() {
    return this.enchantability;
  }

  @Override
  public SoundEvent getEquipSound() {
    return this.equipSound;
  }

  @Override
  public Ingredient getRepairIngredient() {
    return this.repairIngredientSupplier.get();
  }

  @Override
  @Environment(EnvType.CLIENT)
  public String getName() {
    return this.name;
  }

  @Override
  public float getToughness() {
    return this.toughness;
  }

  public Rarity getRarity() {
    return this.rarity;
  }
}
