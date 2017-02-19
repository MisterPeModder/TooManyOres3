package misterpemodder.tmo.main.tileentity;

import misterpemodder.tmo.main.Tmo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityItemKeeper extends TileEntityContainerBase {

	private String displayName;
	public final ItemStackHandler inventory = new ItemStackHandler(1);
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		displayName = displayName == null? "_":displayName;
		displayName = displayName.isEmpty()? "_":displayName;
		compound.setString("displayName", displayName);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		this.displayName = compound.getString("displayName");
		super.readFromNBT(compound);
	}
	
	@Override
	public ItemStackHandler getInventory() {
		return this.inventory;
	}
	
	@Override
	public void onInvOpen(EntityPlayer player) {}
	@Override
	public void onInvClose(EntityPlayer player) {}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY? (T) inventory : super.getCapability(capability, facing);
	}
	
    public ITextComponent getDisplayName() {
    	//String test = this.writeToNBT(new NBTTagCompound()).getString("displayName");
    	String name = displayName == null || displayName == "_" || displayName == ""? TextFormatting.getTextWithoutFormattingCodes(Tmo.proxy.translate("tile.blockItemKeeper.name")) : displayName;
        return new TextComponentString(name);
    }
	
}
