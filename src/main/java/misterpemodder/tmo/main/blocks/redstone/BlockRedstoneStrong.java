package misterpemodder.tmo.main.blocks.redstone;

import misterpemodder.tmo.main.blocks.base.BlockMechanical;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneStrong extends BlockMechanical {

	public BlockRedstoneStrong() {
		super(EnumBlocksNames.STRONG_REDSTONE_BLOCK, EnumBlocksValues.MetalBlocks.MACHINE);
	}
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
	
	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return 15;
    }
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return 15;
	}
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		updateSides(worldIn, pos, state);
    }
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		updateSides(worldIn, pos, state);
	}
	
	public void updateSides(World worldIn, BlockPos pos, IBlockState state) {
		for (EnumFacing enumfacing : EnumFacing.values()) {
            worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
        }
	}

}
