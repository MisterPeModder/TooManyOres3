package misterpemodder.tmo.api.handler;

import javax.annotation.Nullable;

import misterpemodder.tmo.api.block.ISlimeBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

/**
 * Provides a bunch of random helper methods.
 */
public interface ITMOMethodHandler {
	
	/**
	 * Used to check if the passed block has been registered as a ISlimeBlock.
	 * 
	 * @param state the BlockState of the block.
	 * @return true if it is an ISlimeBlock
	 */
	public boolean isSlimeBlock(IBlockState state);
	
	/**
	 * Used to check if the passed block has been registered as a ISlimeBlock.
	 *
	 * @return true if it is an ISlimeBlock
	 */
	public boolean isSlimeBlock(Block block);
	
	/**
	 * Used to get ISlimeBlock instance from the passed block 
	 * has been registered has a ISlimeBlock.
	 * 
	 * @param state the BlockState of the block.
	 * @return the ISlimeBlock instance, returns null if this block does not have one.
	 */
	@Nullable
	public ISlimeBlock getSlimeBlock(IBlockState state);
	
	/**
	 * Used to get ISlimeBlock instance from the passed block 
	 * has been registered has a ISlimeBlock.
	 * 
	 * @param state the BlockState of the block.
	 * @return the ISlimeBlock instance, returns null if this block does not have one.
	 */
	@Nullable
	public ISlimeBlock getSlimeBlock(Block block);
	
	/**
	 * Used to check if the passed ItemStack has been registered as an ender matter item.
	 * 
	 * @param the ItemStack
	 */
	public boolean isEnderMatterItem(ItemStack stack);
	
	/**
	 * returns the ender matter value of this stack.
	 */
	public int getEnderMatterValue(ItemStack stack);
	
}
