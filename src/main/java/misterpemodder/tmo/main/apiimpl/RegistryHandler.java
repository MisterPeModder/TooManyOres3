package misterpemodder.tmo.main.apiimpl;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.block.ISlimeBlock;
import misterpemodder.tmo.api.handler.ITMORegistryHandler;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;

public class RegistryHandler implements ITMORegistryHandler {

	@Override
	public void registerSlimeBlock(ISlimeBlock slime, Block block) {
		if(TooManyOresAPI.SLIME_BLOCKS.containsKey(block)) {
			TMORefs.LOGGER.info(block.getRegistryName()+" has already registered has an ISlimeBlock!");
		} else {
			TooManyOresAPI.SLIME_BLOCKS.put(block, slime);
		}
	}

}
