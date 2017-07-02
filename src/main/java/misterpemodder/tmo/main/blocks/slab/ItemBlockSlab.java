package misterpemodder.tmo.main.blocks.slab;

import misterpemodder.tmo.main.blocks.base.ItemBlockMulti;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockSlab extends ItemBlockMulti {
	
	private final BlockHalfSlab halfSlab;

	public ItemBlockSlab(BlockHalfSlab block) {
		super(block);
		this.halfSlab = block;
	}
	
	@Override
	
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        
		ItemStack stack = player.getHeldItem(hand);

        if (!stack.isEmpty() && player.canPlayerEdit(pos.offset(facing), facing, stack)) {
            Comparable<?> comparable = this.halfSlab.getStateFromMeta(this.getMetadata(stack.getItemDamage())).getValue(this.halfSlab.getFullSlab().getPropertyVariant());
            IBlockState iblockstate = world.getBlockState(pos);

            if (iblockstate.getBlock() == this.halfSlab) {
                IProperty<?> iproperty = this.halfSlab.getPropertyVariant();
                Comparable<?> comparable1 = iblockstate.getValue(iproperty);
                BlockAbstractSlab.EnumBlockHalf blockslab$enumblockhalf = iblockstate.getValue(BlockAbstractSlab.HALF);

                if ((facing == EnumFacing.UP && blockslab$enumblockhalf == BlockAbstractSlab.EnumBlockHalf.BOTTOM || facing == EnumFacing.DOWN && blockslab$enumblockhalf == BlockAbstractSlab.EnumBlockHalf.TOP) && comparable1 == comparable) {
                    IBlockState iblockstate1 = this.makeState(iproperty, comparable1);
                    AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(world, pos);

                    if (axisalignedbb != Block.NULL_AABB && world.checkNoEntityCollision(axisalignedbb.offset(pos)) && world.setBlockState(pos, iblockstate1, 11)) {
                        SoundType soundtype = this.halfSlab.getFullSlab().getSoundType(iblockstate1, world, pos, player);
                        world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        stack.shrink(1);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            return this.tryPlace(player, stack, world, pos.offset(facing), comparable) ? EnumActionResult.SUCCESS : super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        }
        else {
            return EnumActionResult.FAIL;
        }
    }
	
	private boolean tryPlace(EntityPlayer player, ItemStack stack, World worldIn, BlockPos pos, Object itemSlabType) {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if(iblockstate.getBlock() == this.halfSlab) {
            Comparable<?> comparable = iblockstate.getValue(this.halfSlab.getPropertyVariant());

            if (comparable == itemSlabType) {
                IBlockState iblockstate1 = this.makeState(this.halfSlab.getPropertyVariant(), comparable);
                AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, pos);

                if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, iblockstate1, 11)) {
                    SoundType soundtype = this.halfSlab.getFullSlab().getSoundType(iblockstate1, worldIn, pos, player);
                    worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    stack.shrink(1);
                }

                return true;
            }
        }
        return false;
    }
	
	@SuppressWarnings("unchecked")
	protected <T extends Comparable<T>> IBlockState makeState(IProperty<T> property, Comparable<?> comparable) {
        return this.halfSlab.getFullSlab().getDefaultState().withProperty(property, (T)comparable);
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack){
		IBlockState state = worldIn.getBlockState(pos);
		
		if(state.getBlock() instanceof BlockHalfSlab) {
		
			Comparable<?> p1 = state.getValue(halfSlab.getPropertyVariant());
			Comparable<?> p2 = halfSlab.getStateFromMeta(this.getMetadata(stack.getItemDamage())).getValue(halfSlab.getPropertyVariant());
		
			if(state.getBlock() instanceof BlockHalfSlab && p1 == p2) {
				if((side == EnumFacing.UP && state.getValue(BlockAbstractSlab.HALF) == BlockAbstractSlab.EnumBlockHalf.BOTTOM) || (side == EnumFacing.DOWN && state.getValue(BlockAbstractSlab.HALF) == BlockAbstractSlab.EnumBlockHalf.TOP)){
					return true;
				}
			}
		}
        
		boolean stateEqualsThisBlock = worldIn.getBlockState(pos.offset(side)).getBlock() == this.block;
		boolean superCanPlaceBlocks = super.canPlaceBlockOnSide(worldIn, pos, side, player, stack);
		return stateEqualsThisBlock || superCanPlaceBlocks;
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage*2;
	}

}
