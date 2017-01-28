package misterpemodder.tmo.main.blocks.containers;

import misterpemodder.tmo.Tmo;
import misterpemodder.tmo.client.gui.GuiHandler;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.tileentity.TileEntityItemKeeper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockItemKeeper extends BlockContainerBase<TileEntityItemKeeper>{
	

	public BlockItemKeeper() {
		super(EnumBlocksNames.KEEPER_BLOCK, EnumBlocksValues.TEST_BLOCK);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			TileEntityItemKeeper te = getTileEntity(world, pos);
			if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
				IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
				
				if(player.isSneaking()) {
					ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
					if(heldItem.isEmpty()) {
						player.setHeldItem(EnumHand.MAIN_HAND, itemHandler.extractItem(0, 64, false));
					} else {
						player.setHeldItem(EnumHand.MAIN_HAND, itemHandler.insertItem(0, heldItem, false));
					}
					te.markDirty();
				} else {
					if(facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
						player.openGui(Tmo.instance, GuiHandler.EnumGuiElements.ITEM_KEEPER.ID, world, pos.getX(), pos.getY(), pos.getZ());
					} else {
						ItemStack itemStack = itemHandler.getStackInSlot(0);
						if(itemStack.isEmpty()) {
							((EntityPlayerMP)player).sendStatusMessage(new TextComponentString(Tmo.proxy.translate("tile.blockItemKeeper.empty")), true);
						} else {
							ITextComponent ic = itemStack.getTextComponent();
							TextComponentString txt = new TextComponentString(Tmo.proxy.translate("tile.blockItemKeeper.item", itemStack.getCount()));
							((EntityPlayerMP)player).sendStatusMessage(txt.appendSibling(ic),true);
						}
					}
				}
				
			}
		}
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, IBlockState state) {
		return new TileEntityItemKeeper();
	}

	@Override
	public Class<TileEntityItemKeeper> getTileEntityClass() {
		return TileEntityItemKeeper.class;
	}

}
