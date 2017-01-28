package misterpemodder.tmo.main.blocks.slab;

import misterpemodder.tmo.main.blocks.base.ItemBlockMulti;
import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockSlab extends ItemBlockMulti {

	public ItemBlockSlab(BlockHalfSlab block) {
		super(block);
	}
	
	@Override
	
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getCount() != 0 && player.canPlayerEdit(pos, facing, stack)) {
        	IBlockState newState = this.block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, this.getMetadata(stack.getMetadata()), player, hand);
            BlockPos npos = pos.offset(facing);
            //Same block
            if(block == this.getBlock()) {
            	//Same variant
            	BlockHalfSlab slabBlock = (BlockHalfSlab)this.getBlock();
            	
            	
            	if(((IBlockVariant)state.getValue(slabBlock.getPropertyVariant())).getMeta() == stack.getMetadata()) {
            		//Opposite sides
            		if((facing == EnumFacing.UP && state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM) || (facing == EnumFacing.DOWN && state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP)) {
            			newState = slabBlock.getFullSlab().getStateFromMeta(stack.getMetadata());
            			npos = pos;
                	}
            	}
            	
            } else if(world.getBlockState(npos).getBlock() instanceof BlockHalfSlab) {
            	return EnumActionResult.FAIL;
            }
            if (placeBlockAt(stack, player, world, npos, facing, hitX, hitY, hitZ, newState)) {
                SoundType soundtype = world.getBlockState(npos).getBlock().getSoundType(world.getBlockState(npos), world, npos, player);
                world.playSound(player, npos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                stack.shrink(1);
            }
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack){
        IBlockState state = worldIn.getBlockState(pos);

        if(state.getBlock() == this.block){
            if((side == EnumFacing.UP && state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM) || (side == EnumFacing.DOWN && state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP)){
                return true;
            }
        }	
        
        boolean stateEqualsThisBlock = worldIn.getBlockState(pos.offset(side)).getBlock() == this.block;
        boolean superCanPlaceBlocks = super.canPlaceBlockOnSide(worldIn, pos, side, player, stack);
        return stateEqualsThisBlock || superCanPlaceBlocks;
        //return worldIn.getBlockState(pos.offset(side)).getBlock() == this.block || super.canPlaceBlockOnSide(worldIn, pos, side, player, stack);
    }
	
	@Override
	public int getMetadata(int damage) {
		return damage*2;
	}

}
