package misterpemodder.tmo.item.tool;

import misterpemodder.tmo.TmoConstants;
import misterpemodder.tmo.item.BlinkingItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

public class TmoHoeItem extends HoeItem implements BlinkingItem {
  public TmoHoeItem(ToolMaterial material) {
    super(material, -3F, new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP));
  }

  @Override
  @Environment(EnvType.CLIENT)
  public int tmoGetDurabilityBarColor(ItemStack stack, int originalColor) {
    return BlinkingItem.super.tmoGetDurabilityBarColor(stack, originalColor);
  }
}
