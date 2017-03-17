package misterpemodder.tmo.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;

/**
 * Used to tell the strong piston a block should be considered as a slime block.
 * You can directly implement this interface on your block to make it work or 
 * you create a separate class implementing {@code ISlimeBlock} an register an instance
 * of it via {@code TooManyOresAPI.registryHandler.registerSlimeBlock(ISlimeBlock, Block);} 
 * <p><em> Gooey!
 */
public interface ISlimeBlock {
	
	/**
	 * Returns whether this block should be sticky or not with the given state
	 * @param state The state of the block
	 * @return is this block sticky?
	 */
	boolean isSticky(IBlockState state);
	
	
	/**
	 * Returns whether this block can launch the given entity
	 * 
	 * @param state The state of the block
	 * @param entity The entity that may be launched
	 * @return is this block sticky?
	 */
	boolean canLaunchEntity(IBlockState state, Entity entity);
	
	/**
	 * Retrieves how much the entity motion should be multiplied while lauching it.
	 * a value of 1.0D corresponds to the vanilla slime block
	 * 
	 * @param state The state of the block
	 * @param entity Launched entity
	 * @return Power multiplier
	 */
	double getLaunchingPowerMultiplier(IBlockState state, Entity entity);

}
