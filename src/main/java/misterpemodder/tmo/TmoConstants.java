package misterpemodder.tmo;

import misterpemodder.tmo.item.TmoItems;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public final class TmoConstants {
  public static final String MOD_ID = "tmo";
  public static final ItemGroup ITEM_GROUP =
      FabricItemGroupBuilder.build(new Identifier(MOD_ID), () -> new ItemStack(TmoItems.TAB_ICON));
}
