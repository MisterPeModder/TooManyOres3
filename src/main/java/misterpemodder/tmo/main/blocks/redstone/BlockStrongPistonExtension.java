package misterpemodder.tmo.main.blocks.redstone;

import misterpemodder.hc.main.blocks.IHexianBlock;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStrongPistonExtension extends BlockPistonExtension implements IHexianBlock {

	public BlockStrongPistonExtension() {
		super();
		this.setUnlocalizedName(EnumBlocksNames.STRONG_PISTON_EXTENSION.getUnlocalizedName());
		this.setRegistryName(TMORefs.PREFIX + EnumBlocksNames.STRONG_PISTON_EXTENSION.getRegistryName());
		this.setLightOpacity(0);
	}
	
	@Override
	public boolean hasOwnItemBlock() {
		return false;
	}
	
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		super.onBlockHarvested(worldIn, pos, state, player);
        if (player.capabilities.isCreativeMode) {
            BlockPos blockpos = pos.offset(state.getValue(FACING).getOpposite());
            Block block = worldIn.getBlockState(blockpos).getBlock();

            if (block instanceof BlockPistonBase) {
                worldIn.setBlockToAir(blockpos);
            }
        }
    }
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        pos = pos.offset(state.getValue(FACING).getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if ((iblockstate.getBlock() instanceof BlockPistonBase) && iblockstate.getValue(BlockPistonBase.EXTENDED).booleanValue()) {
            iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
            worldIn.setBlockToAir(pos);
        }
    }
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        BlockPos blockpos = pos.offset(state.getValue(FACING).getOpposite());
        IBlockState iblockstate = worldIn.getBlockState(blockpos);

        if (!(iblockstate.getBlock() instanceof BlockPistonBase)) {
            worldIn.setBlockToAir(pos);
        }
        else {
            iblockstate.neighborChanged(worldIn, blockpos, blockIn, fromPos);
        }
    }
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack((state.getValue(TYPE) == EnumPistonType.STICKY ? TheBlocks.STRONG_PISTON_STICKY : TheBlocks.STRONG_PISTON).getBlock());
    }
	
}
