package misterpemodder.tmo.item.tool;

import misterpemodder.tmo.TmoConstants;
import misterpemodder.tmo.item.BlinkingItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;

public class TmoAxeItem extends AxeItem implements BlinkingItem {
  public TmoAxeItem(ToolMaterial material) {
    super(material, 6, -3F, new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP));
  }

  @Override
  @Environment(EnvType.CLIENT)
  public int tmoGetDurabilityBarColor(ItemStack stack, int originalColor) {
    return BlinkingItem.super.tmoGetDurabilityBarColor(stack, originalColor);
  }
}
