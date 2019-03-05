package com.misterpemodder.tmo.item;

import com.misterpemodder.tmo.hook.ItemHook;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

public interface BlinkingItem extends ItemHook {
  @Override
  default int tmoGetDurabilityBarColor(ItemStack stack, int originalColor) {
    if ((stack.getDamage() * 100) / stack.getDurability() > 80)
      return MinecraftClient.getInstance().world.getTime() % 20 < 10 ? originalColor : 0;
    else
      return originalColor;
  }
}
