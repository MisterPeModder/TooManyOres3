package misterpemodder.tmo.main.tileentity;

import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.network.TMOPacketHandler;
import misterpemodder.tmo.main.network.packet.PacketClientToServer;
import misterpemodder.tmo.main.network.packet.PacketServerToClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.ItemStackHandler;

public abstract class TileEntityContainerBase extends TileEntity {
	
	public abstract ItemStackHandler getInventory(); 
	public abstract void onInvOpen(EntityPlayer player);
	public abstract void onInvClose(EntityPlayer player);
	
	public void sync() {
		NBTTagCompound toSend = new NBTTagCompound();
		toSend.setLong("pos", this.pos.toLong());
		if(!this.hasWorld()) return;
		
		if(!this.world.isRemote) {
			NetworkRegistry.TargetPoint target = new TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64);
			toSend.setTag("tileEntity", this.serializeNBT());
			TMOPacketHandler.network.sendToAllAround(new PacketServerToClient(PacketDataHandlers.TE_UPDATE_HANDLER, toSend), target);
		} else {
			toSend.setInteger("world_dim_id", world.provider.getDimension());
			toSend.setTag("player_id", NBTUtil.createUUIDTag(Minecraft.getMinecraft().player.getUniqueID()));
			TMOPacketHandler.network.sendToServer(new PacketClientToServer(PacketDataHandlers.TE_UPDATE_REQUEST_HANDLER, toSend));
		}
	}
	
}
