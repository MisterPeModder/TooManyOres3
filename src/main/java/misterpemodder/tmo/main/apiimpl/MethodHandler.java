package misterpemodder.tmo.main.apiimpl;

import javax.annotation.Nullable;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.block.ISlimeBlock;
import misterpemodder.tmo.api.handler.ITMOMethodHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class MethodHandler implements ITMOMethodHandler{

	@Override
	public boolean isSlimeBlock(IBlockState state) {
		return this.isSlimeBlock(state.getBlock());
	}

	@Override
	public boolean isSlimeBlock(Block block) {
		return TooManyOresAPI.SLIME_BLOCKS.containsKey(block);
	}
	
	@Override
	@Nullable
	public ISlimeBlock getSlimeBlock(IBlockState state) {
		return this.getSlimeBlock(state.getBlock());
	}
	
	@Override
	@Nullable
	public ISlimeBlock getSlimeBlock(Block block) {
		if(block instanceof ISlimeBlock) {
			return (ISlimeBlock) block;
		} 
		else if(this.isSlimeBlock(block)) {
			return TooManyOresAPI.SLIME_BLOCKS.get(block);
		} else {
			return null;
		}
	}
	
	@Override
	public boolean isEnderMatterItem(ItemStack stack) {
		for(ItemStack s : TooManyOresAPI.ENDER_MATTER_ITEMS.keySet()) {
			if(ItemStack.areItemsEqual(s, stack)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getEnderMatterValue(ItemStack stack) {
		for(ItemStack s : TooManyOresAPI.ENDER_MATTER_ITEMS.keySet()) {
			if(ItemStack.areItemsEqual(s, stack)) {
				return TooManyOresAPI.ENDER_MATTER_ITEMS.get(s);
			}
		}
		return 0;
	}

}
