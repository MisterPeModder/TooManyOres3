package com.misterpemodder.tmo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.misterpemodder.tmo.block.TmoBlocks;
import com.misterpemodder.tmo.block.entity.TmoBlockEntityTypes;
import com.misterpemodder.tmo.item.TmoItems;
import com.misterpemodder.tmo.tag.TmoItemTags;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;

public class TooManyOres implements ModInitializer {
  public static final Logger LOGGER = LogManager.getLogger("TooManyOres");

  @Override
  public void onInitialize() {
    TmoBlocks.register(Registry.BLOCK, Registry.ITEM);
    TmoItems.register(Registry.ITEM);
    TmoBlockEntityTypes.register(Registry.BLOCK_ENTITY);
    TmoItemTags.AXES.entries();
  }
}
