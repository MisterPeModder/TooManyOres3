package com.misterpemodder.tmo.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import com.misterpemodder.tmo.hook.ItemHook;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(Item.class)
public final class ItemMixin implements ItemHook {
  @Override
  public boolean tmoCanApplyEnchantment(ItemStack stack, Enchantment enchantment) {
    return false;
  }

  @Override
  public int tmoGetDurabilityBarColor(ItemStack stack, int originalColor) {
    return originalColor;
  }
}
