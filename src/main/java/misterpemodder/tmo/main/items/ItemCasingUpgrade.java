package misterpemodder.tmo.main.items;

import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.items.ItemVariant.CasingUpgradeVariant;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemCasingUpgrade extends ItemMulti<CasingUpgradeVariant> {

	public ItemCasingUpgrade() {
		super(EnumItemsNames.CASING_UPGRADE, CasingUpgradeVariant.casingUpgradeVariants, "_casing_upgrade");
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
			
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote && !stack.isEmpty() && stack.getItem() instanceof ItemCasingUpgrade) {
			IBlockState state = world.getBlockState(pos);
			Block b = state.getBlock();
			if(b instanceof BlockMachine && ((BlockMachine<?>)b).canChangeCasings) {
				CasingUpgradeVariant variant = ((ItemCasingUpgrade)stack.getItem()).getVariant(stack.getMetadata());
				if(state.getValue(BlockMachine.CASING) == variant.from) {
					world.setBlockState(pos, state.withProperty(BlockMachine.CASING, variant.to), 3);
					if(!player.isCreative()) stack.shrink(1);
					
					TileEntity te = world.getTileEntity(pos);
					if(te != null && te instanceof TileEntityMachine) {
						((TileEntityMachine<?>)te).setProgress(0);
					}
					
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

}
