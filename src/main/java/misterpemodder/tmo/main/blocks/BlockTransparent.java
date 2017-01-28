package misterpemodder.tmo.main.blocks;

import misterpemodder.tmo.main.blocks.base.BlockBase;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.IBlockValues;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTransparent extends BlockBase{

	public BlockTransparent(EnumBlocksNames n, IBlockValues v) {
		super(n, v);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side){
		IBlockState sideState = blockAccess.getBlockState(pos.offset(side));
		Block sideBlock = sideState.getBlock();
		
		boolean differentState = state != sideState;
		boolean differentBlock = sideBlock != this && super.shouldSideBeRendered(state, blockAccess, pos, side);
		return  differentState && differentBlock;
		
	}
	
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing face) {
	    return false;
	}
	
}
