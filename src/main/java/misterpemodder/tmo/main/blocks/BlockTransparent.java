package misterpemodder.tmo.main.blocks;

import misterpemodder.hc.main.blocks.properties.IBlockValues;
import misterpemodder.tmo.main.blocks.base.BlockBaseTMO;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTransparent extends BlockBaseTMO {

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
	
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
        if (!worldIn.isRemote && this == ModBlocks.TheBlocks.FROZIUM_BLOCK.getBlock()) {
            BlockBeacon.updateColorAsync(worldIn, pos);
        }
    }
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
		if (!worldIn.isRemote && this == ModBlocks.TheBlocks.FROZIUM_BLOCK.getBlock()) {
            BlockBeacon.updateColorAsync(worldIn, pos);
        }
    }
	
	@Override
	public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {
		if(this != ModBlocks.TheBlocks.FROZIUM_BLOCK.getBlock()) 
			return super.getBeaconColorMultiplier(state, world, pos, beaconPos);
		return new float[] {123/255F, 206/255F, 1F};
	}
	
}
