package misterpemodder.tmo.main.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityContainerBase extends TileEntity{
	
	public abstract ItemStackHandler getInventory(); 
	public abstract void onInvOpen(EntityPlayer player);
	public abstract void onInvClose(EntityPlayer player);
	
}
