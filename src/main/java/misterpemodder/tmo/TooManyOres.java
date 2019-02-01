package misterpemodder.tmo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import misterpemodder.tmo.block.TmoBlocks;
import misterpemodder.tmo.item.TmoItems;
import misterpemodder.tmo.tags.TmoItemTags;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.registry.Registry;

public class TooManyOres implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("TooManyOres");

	@Override
	public void onInitialize() {
		TmoBlocks.register(Registry.BLOCK, Registry.ITEM);
		TmoItems.register(Registry.ITEM);
		TmoItemTags.AXES.entries();
	}
}
