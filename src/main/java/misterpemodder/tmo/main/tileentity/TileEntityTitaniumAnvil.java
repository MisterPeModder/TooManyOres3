package misterpemodder.tmo.main.tileentity;

import misterpemodder.tmo.api.item.IItemForgeHammer;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.capability.SyncedItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityTitaniumAnvil extends TileEntityContainerBase {
	
	private ItemStackHandler hammerItemHandler;
	
	public TileEntityTitaniumAnvil() {
		super();
		this.hammerItemHandler = new SyncedItemHandler(this, 1) {
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				if(!(stack.getItem() instanceof IItemForgeHammer)) {
					return stack;
				}
				return super.insertItem(slot, stack, simulate);
			}
			
		};
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		this.sync();
	}

	@Override
	public ItemStackHandler getInventory() {
		return hammerItemHandler;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("hammerInv", hammerItemHandler.serializeNBT());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		hammerItemHandler.deserializeNBT(compound.getCompoundTag("hammerInv"));
		super.readFromNBT(compound);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY? true : super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY? (T)hammerItemHandler : super.getCapability(capability, facing);
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(TextFormatting.getTextWithoutFormattingCodes(Tmo.proxy.translate("tile.blockTitaniumAnvil.name")));
	}

	@Override
	public void onInvOpen(EntityPlayer player) {}

	@Override
	public void onInvClose(EntityPlayer player) {}

}
