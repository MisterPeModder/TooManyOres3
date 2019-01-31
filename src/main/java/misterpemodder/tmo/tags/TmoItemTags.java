package misterpemodder.tmo.tags;

import misterpemodder.tmo.TmoConstants;
import net.fabricmc.fabric.tags.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public final class TmoItemTags {
  public static final Tag<Item> AXES = register("axes");
  public static final Tag<Item> DUSTS = register("dusts");
  public static final Tag<Item> GEMS = register("gems");
  public static final Tag<Item> HAMMERS = register("hammers");
  public static final Tag<Item> HOES = register("hoes");
  public static final Tag<Item> INGOTS = register("ingots");
  public static final Tag<Item> PICKAXES = register("pickaxes");
  public static final Tag<Item> PLATES = register("plates");
  public static final Tag<Item> SHOVELS = register("shovels");
  public static final Tag<Item> SWORDS = register("swords");

  private static Tag<Item> register(String id) {
    return TagRegistry.item(new Identifier(TmoConstants.MOD_ID, id));
  }
}
