package misterpemodder.tmo.main.apiimpl;

import misterpemodder.tmo.api.IStrongPistonBehavior;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.block.ISlimeBlock;
import misterpemodder.tmo.api.handler.ITMORegistryHandler;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;

public class RegistryHandler implements ITMORegistryHandler {

	@Override
	public void registerSlimeBlock(ISlimeBlock slime, Block block) {
		if(TooManyOresAPI.SLIME_BLOCKS.containsKey(block)) {
			TMORefs.LOGGER.info(block.getRegistryName()+" has already been registered has an ISlimeBlock!");
		} else {
			TooManyOresAPI.SLIME_BLOCKS.put(block, slime);
		}
	}
	
	@Override
	public void registerStrongPistonBehavior(IStrongPistonBehavior behavior) {
		if(TooManyOresAPI.STRONG_PISTON_BEHAVIORS.contains(behavior)) {
			TMORefs.LOGGER.info("The behavior "+behavior.toString()+" has already been registered!");
		} else {
			TooManyOresAPI.STRONG_PISTON_BEHAVIORS.add(behavior);
		}
	}

}
