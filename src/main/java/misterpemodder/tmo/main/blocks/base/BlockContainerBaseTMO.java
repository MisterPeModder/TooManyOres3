package misterpemodder.tmo.main.blocks.base;

import buildcraft.api.tools.IToolWrench;
import misterpemodder.hc.api.block.ILockable;
import misterpemodder.hc.main.blocks.BlockContainerBase;
import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.hc.main.blocks.properties.IBlockValues;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public abstract class BlockContainerBaseTMO<TE extends TileEntityContainerBase> extends BlockContainerBase<TE> {

	public BlockContainerBaseTMO(IBlockNames n, IBlockValues v) {
		super(n, v, TMORefs.TMO_TAB);
	}
	
	@Override
	public void handleItemSpawningLogic(TE te, World world, BlockPos pos, IBlockState state) {
		if(te instanceof ILockable) {
			IItemHandler lockItemHandler = ((ILockable) te).getLockItemHandler();
			if(!lockItemHandler.getStackInSlot(0).isEmpty()) {
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), lockItemHandler.getStackInSlot(0)));
			}
		}
		super.handleItemSpawningLogic(te, world, pos, state);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		TE tileEntity = this.getTileEntity(world, pos);
		
		if(tileEntity == null) {
			return false;
		}
		else if(world.isRemote) {
			return true;
		} 
		else if (stack.getItem() instanceof IToolWrench && player.isSneaking()) {

			if (((IToolWrench) stack.getItem()).canWrench(player, pos)) {
				((IToolWrench) stack.getItem()).wrenchUsed(player, pos);
				world.setBlockToAir(pos);
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), this.toItem(tileEntity, state)));
			}
			
			return true;
		}
		
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}

}
