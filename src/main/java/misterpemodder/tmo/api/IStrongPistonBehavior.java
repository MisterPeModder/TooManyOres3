package misterpemodder.tmo.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IStrongPistonBehavior {
	
	/**
	 * Used to check if the strong piston can move this block.
	 * Implementations of this method SHOULD ALWAYS RETURN TRUE if they don't handle this block.
	 * 
	 * @return this block can be pushed/pulled by strong pistons?
	 */
	boolean canPushBlock(IBlockState state, World world, BlockPos pos, EnumFacing facing, boolean destroyBlocks);

}
