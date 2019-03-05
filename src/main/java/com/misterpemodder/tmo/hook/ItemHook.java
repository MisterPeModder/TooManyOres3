package com.misterpemodder.tmo.hook;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public interface ItemHook {
  /**
   * Is the passed enchantment compatible with this itemstack?
   */
  default boolean tmoCanApplyEnchantment(ItemStack stack, Enchantment enchantment) {
    return false;
  }

  /**
   * Modify the color of the durability bar.
   * 
   * @param stack         The stack.
   * @param originalColor The color of the durablilty bar before the change.
   * 
   * @return The RBG color.
   */
  @Environment(EnvType.CLIENT)
  default int tmoGetDurabilityBarColor(ItemStack stack, int originalColor) {
    return originalColor;
  }
}
