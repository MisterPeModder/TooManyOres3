package misterpemodder.tmo.api.handler;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import misterpemodder.tmo.api.block.ISlimeBlock;
import misterpemodder.tmo.api.capability.io.IIOType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

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
	 * returns a Pair holding in the left side the ender matter value corresponding
	 * to the amount of items in the right side.
	 */
	public Pair<Integer, Integer> getEnderMatterValue(ItemStack stack);
	
	/**
	 * Used to get the IIOType corresponding to the specified capability
	 */
	@Nullable
	public <T> IIOType<T> getIOTypeForCapability(Capability<T> cap);
	
}
