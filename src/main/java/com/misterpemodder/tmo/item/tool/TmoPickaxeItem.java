package com.misterpemodder.tmo.item.tool;

import com.misterpemodder.tmo.TmoConstants;
import com.misterpemodder.tmo.item.BlinkingItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;

public class TmoPickaxeItem extends PickaxeItem implements BlinkingItem {
  public TmoPickaxeItem(ToolMaterial material) {
    super(material, 1, -2.8F, new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP));
  }

  @Override
  @Environment(EnvType.CLIENT)
  public int tmoGetDurabilityBarColor(ItemStack stack, int originalColor) {
    return BlinkingItem.super.tmoGetDurabilityBarColor(stack, originalColor);
  }
}
