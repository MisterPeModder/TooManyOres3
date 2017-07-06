package misterpemodder.tmo.main.blocks.itemblock;

import misterpemodder.tmo.main.blocks.BlockSpecialRedstoneWire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockSpecialRedstone extends ItemBlock {
	
	public final BlockSpecialRedstoneWire wire;

	public ItemBlockSpecialRedstone(BlockSpecialRedstoneWire block) {
		super(block);
		this.wire = block;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return this.wire.getNames().getRarity();
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
        BlockPos blockpos = flag ? pos : pos.offset(facing);
        ItemStack itemstack = player.getHeldItem(hand);

        if (player.canPlayerEdit(blockpos, facing, itemstack) && worldIn.mayPlace(worldIn.getBlockState(blockpos).getBlock(), blockpos, false, facing, (Entity)null) && Blocks.REDSTONE_WIRE.canPlaceBlockAt(worldIn, blockpos)) {
            itemstack.shrink(1);
            worldIn.setBlockState(blockpos, this.wire.getDefaultState());
            return EnumActionResult.SUCCESS;
        }
        else {
            return EnumActionResult.FAIL;
        }
    }
	
}
