package misterpemodder.tmo.api.handler;

import misterpemodder.tmo.api.IStrongPistonBehavior;
import misterpemodder.tmo.api.block.ISlimeBlock;
import net.minecraft.block.Block;

/**
 * Handles tasks related to registration, such as recipes.
 */
public interface ITMORegistryHandler {
	/**
	 * Registers the ISlimeBlock and binds it to the passed block.
	 * Useful for registering ISlimeBlocks for blocks that are not in your own mod.
	 * @param slime
	 * @param block
	 */
	public void registerSlimeBlock(ISlimeBlock slime, Block block);
	
	/**
	 * Registers a strong piston behavior
	 */
	public void registerStrongPistonBehavior(IStrongPistonBehavior behavior);
	
}
