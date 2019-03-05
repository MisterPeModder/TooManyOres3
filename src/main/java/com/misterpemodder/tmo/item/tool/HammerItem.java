package com.misterpemodder.tmo.item.tool;

import com.google.common.collect.Multimap;
import com.misterpemodder.tmo.TmoConstants;
import com.misterpemodder.tmo.item.BlinkingItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;

public class HammerItem extends ToolItem implements BlinkingItem {
  private final float attackDamage;

  public HammerItem(ToolMaterial material) {
    super(material, new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP));
    this.attackDamage = material.getAttackDamage() + 4F;
  }

  public float getAttackDamage() {
    return this.attackDamage;
  }

  @Override
  public boolean onEntityDamaged(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    stack.applyDamage(1, attacker);
    return true;
  }

  @Override
  public Multimap<String, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
    Multimap<String, EntityAttributeModifier> modifiers = super.getAttributeModifiers(slot);
    if (slot == EquipmentSlot.HAND_MAIN) {
      modifiers.put(EntityAttributes.ATTACK_DAMAGE.getId(),
          new EntityAttributeModifier(MODIFIER_DAMAGE, "Weapon modifier", this.attackDamage,
              EntityAttributeModifier.Operation.ADDITION));
      modifiers.put(EntityAttributes.ATTACK_SPEED.getId(),
          new EntityAttributeModifier(MODIFIER_SWING_SPEED, "Weapon modifier", -3.2,
              EntityAttributeModifier.Operation.ADDITION));
    }
    return modifiers;
  }

  @Override
  public boolean tmoCanApplyEnchantment(ItemStack stack, Enchantment enchantment) {
    return enchantment.type == EnchantmentTarget.WEAPON;
  }

  @Override
  @Environment(EnvType.CLIENT)
  public int tmoGetDurabilityBarColor(ItemStack stack, int originalColor) {
    return BlinkingItem.super.tmoGetDurabilityBarColor(stack, originalColor);
  }
}
