package com.misterpemodder.tmo.item;

import com.google.common.base.Supplier;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

public enum TmoToolMaterials implements ToolMaterial {
  TITANIUM(4, 2755, 11F, 5, 16, () -> Ingredient.ofItems(TmoItems.TITANIUM_INGOT)),

  DARK_TITANIUM(4, 2855, 12F, 6, 17, () -> Ingredient.ofItems(TmoItems.DARK_TITANIUM_INGOT)),

  COPPER(2, 163, 5F, 1, 8, () -> Ingredient.ofItems(TmoItems.COPPER_INGOT)),

  FROZIUM(3, 310, 7F, 2, 14, () -> Ingredient.ofItems(TmoItems.FROZIUM_GEM)),

  IGNUM(3, 310, 7F, 2, 14, () -> Ingredient.ofItems(TmoItems.IGNUM_GEM));

  private final int miningLevel;
  private final int durability;
  private final float blockBreakSpeed;
  private final float attackDamage;
  private final int enchantability;
  private final Lazy<Ingredient> repairIngredient;

  private TmoToolMaterials(int miningLevel, int durability, float blockBreakSpeed,
      float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
    this.miningLevel = miningLevel;
    this.durability = durability;
    this.blockBreakSpeed = blockBreakSpeed;
    this.attackDamage = attackDamage;
    this.enchantability = enchantability;
    this.repairIngredient = new Lazy<>(repairIngredient);
  }

  @Override
  public int getDurability() {
    return this.durability;
  }

  @Override
  public float getBlockBreakingSpeed() {
    return this.blockBreakSpeed;
  }

  @Override
  public float getAttackDamage() {
    return this.attackDamage;
  }

  @Override
  public int getMiningLevel() {
    return this.miningLevel;
  }

  @Override
  public int getEnchantability() {
    return this.enchantability;
  }

  @Override
  public Ingredient getRepairIngredient() {
    return (Ingredient) this.repairIngredient.get();
  }
}
