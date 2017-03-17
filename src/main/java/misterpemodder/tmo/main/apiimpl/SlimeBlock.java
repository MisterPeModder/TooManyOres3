package misterpemodder.tmo.main.apiimpl;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.block.ISlimeBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;

public class SlimeBlock implements ISlimeBlock {
	
	public static void registerSlimeBlocksInternal() {
		TooManyOresAPI.registryHandler.registerSlimeBlock(new SlimeBlock(), Blocks.SLIME_BLOCK);
	}
	
	@Override
	public boolean isSticky(IBlockState state) {
		return true;
	}
	
	@Override
	public boolean canLaunchEntity(IBlockState state, Entity entity) {
		return true;
	}

	@Override
	public double getLaunchingPowerMultiplier(IBlockState state, Entity entity) {
		return 1.0D;
	}

}
