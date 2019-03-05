package com.misterpemodder.tmo.tag;

import com.misterpemodder.tmo.TmoConstants;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public final class TmoBlockTags {
  public static final Tag<Block> BEACON_BASE = TagRegistry.block(new Identifier("beacon_base"));
  public static final Tag<Block> ENDER_ORES = register("ender_ores");
  public static final Tag<Block> NETHER_ORES = register("nether_ores");
  public static final Tag<Block> ORES = register("ores");
  public static final Tag<Block> OVERWORLD_ORES = register("overworld_ores");
  public static final Tag<Block> RESOURCE_STORAGE = register("resource_storage");

  private static Tag<Block> register(String id) {
    return TagRegistry.block(new Identifier(TmoConstants.MOD_ID, id));
  }
}
