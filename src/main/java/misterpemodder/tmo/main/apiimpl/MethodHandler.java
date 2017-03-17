package misterpemodder.tmo.main.apiimpl;

import javax.annotation.Nullable;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.block.ISlimeBlock;
import misterpemodder.tmo.api.handler.ITMOMethodHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

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

}
