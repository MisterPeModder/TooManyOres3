package misterpemodder.tmo.main.apiimpl;

import misterpemodder.tmo.api.IStrongPistonBehavior;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DefaultStrongPistonBehavior implements IStrongPistonBehavior {

	@Override
	public boolean canPushBlock(IBlockState state, World world, BlockPos pos, EnumFacing facing, boolean destroyBlocks) {
		if (!(state.getBlock() instanceof BlockPistonBase)) {
            if (state.getBlockHardness(world, pos) == -1.0F) {
                return false;
            }

            if (state.getMobilityFlag() == EnumPushReaction.BLOCK) {
                return false;
            }

            if (state.getMobilityFlag() == EnumPushReaction.DESTROY) {
                return destroyBlocks;
            }
        }
        else if (state.getValue(BlockPistonBase.EXTENDED)) {
            return false;
        }
        return true;
	}

}
