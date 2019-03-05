package misterpemodder.tmo.item.tool;

import misterpemodder.tmo.TmoConstants;
import misterpemodder.tmo.item.BlinkingItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;

public class TmoShovelItem extends ShovelItem implements BlinkingItem {
  public TmoShovelItem(ToolMaterial material) {
    super(material, 1.5F, -3.0F, new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP));
  }

  @Override
  @Environment(EnvType.CLIENT)
  public int tmoGetDurabilityBarColor(ItemStack stack, int originalColor) {
    return BlinkingItem.super.tmoGetDurabilityBarColor(stack, originalColor);
  }
}
