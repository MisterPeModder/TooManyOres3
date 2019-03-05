package com.misterpemodder.tmo.item;

import com.misterpemodder.tmo.TmoConstants;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TmoArmorItem extends ArmorItem implements BlinkingItem {
  public TmoArmorItem(TmoArmorMaterials material, EquipmentSlot slot) {
    super(material, slot,
        new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP).rarity(material.getRarity()));
  }

  @Override
  public int tmoGetDurabilityBarColor(ItemStack stack, int originalColor) {
    return BlinkingItem.super.tmoGetDurabilityBarColor(stack, originalColor);
  }
}
