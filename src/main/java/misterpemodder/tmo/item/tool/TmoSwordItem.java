package misterpemodder.tmo.item.tool;

import misterpemodder.tmo.TmoConstants;
import misterpemodder.tmo.item.BlinkingItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class TmoSwordItem extends SwordItem implements BlinkingItem {
  public TmoSwordItem(ToolMaterial material) {
    super(material, 3, -2.4F, new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP));
  }

  @Override
  @Environment(EnvType.CLIENT)
  public int getDurabilityBarColor(ItemStack stack, int originalColor) {
    return BlinkingItem.super.getDurabilityBarColor(stack, originalColor);
  }
}
