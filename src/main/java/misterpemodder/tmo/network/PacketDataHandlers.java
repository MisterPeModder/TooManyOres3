package misterpemodder.tmo.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.network.packet.PacketServerToClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class PacketDataHandlers {
	
	public static final List<IPacketDataHandler> HANDLERS = new ArrayList<>();
	
	public static void registerHandlers() {
		HANDLERS.add(TCHEST_UPDATE_HANDLER);
		HANDLERS.add(TE_UPDATE_HANDLER);
	}
	
	public static final IPacketDataHandler TCHEST_UPDATE_HANDLER = new IPacketDataHandler() {
		
		@Override
		@SideOnly(Side.CLIENT)
		public void procData(NBTTagCompound data) {
			WorldClient world = Minecraft.getMinecraft().world;
			BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TileEntityTitaniumChest) {
				TileEntityTitaniumChest t = (TileEntityTitaniumChest) te;
				t.numPlayersUsing = data.getInteger("numPlayersUsing");
			}
		}
	};
	
    public static final IPacketDataHandler TE_UPDATE_HANDLER = new IPacketDataHandler() {
		
		@Override
		public void procData(NBTTagCompound data) {
			
			//when recieved on SERVER side
			if(data.hasKey("client_flag")) {
				WorldServer world = DimensionManager.getWorld(data.getInteger("world_dim_id"));

				UUID playerUUID = NBTUtil.getUUIDFromTag(data.getCompoundTag("player_id"));
				EntityPlayer player = world.getPlayerEntityByUUID(playerUUID);
				
				BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
				TileEntity te = world.getTileEntity(pos);
				
				NBTTagCompound toSend = new NBTTagCompound();
				if(te instanceof TileEntityTitaniumChest) {
					TileEntityTitaniumChest t = (TileEntityTitaniumChest) te;
					toSend.setTag("lock", t.getLockItemHandler().serializeNBT());
				}
				
				if(player != null) {
					toSend.setLong("pos", pos.toLong());
					TMOPacketHandler.network.sendTo(new PacketServerToClient(PacketDataHandlers.TE_UPDATE_HANDLER, toSend), (EntityPlayerMP)player);
				}
			//when recieved on CLIENT side
			} else {
				WorldClient world = Minecraft.getMinecraft().world;
				BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof TileEntityTitaniumChest) {
					TileEntityTitaniumChest t = (TileEntityTitaniumChest) te;
					t.getLockItemHandler().deserializeNBT(data.getCompoundTag("lock"));
				}
			}
		}
	};

}
