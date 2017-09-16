package misterpemodder.tmo.main.network;

import java.util.List;
import java.util.UUID;

import misterpemodder.hc.main.network.IPacketDataHandler;
import misterpemodder.tmo.main.TooManyOres;
import misterpemodder.tmo.main.capability.CapabilityFreezing;
import misterpemodder.tmo.main.capability.CapabilityFreezing.IFreezing;
import misterpemodder.tmo.main.inventory.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

public enum PacketDataHandlers implements IPacketDataHandler {
	
	/**
	 * <p> Handler type: client to server
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>world_dim_id: Integer
	 *  <li>player_id: UUID
	 * 	<li>item_name: String
	 * </ul>
	 */
    ANVIL_ITEM_NAME_HANDLER {
		
		@Override
		public void procData(NBTTagCompound data) {
			
			WorldServer world = DimensionManager.getWorld(data.getInteger("world_dim_id"));
			UUID playerUUID = NBTUtil.getUUIDFromTag(data.getCompoundTag("player_id"));
			
			EntityPlayer player = world.getPlayerEntityByUUID(playerUUID);
			if(player != null && player.openContainer != null  && player.openContainer instanceof ContainerTitaniumAnvil) {
				ContainerTitaniumAnvil c = ((ContainerTitaniumAnvil)player.openContainer);
				if(data.hasKey("input_item")) {
					ItemStack st = c.input.getStackInSlot(0).copy();
					st.deserializeNBT(data.getCompoundTag("input_item"));
					c.input.setStackInSlot(0, st);
				}
				c.updateItemName(data.getString("item_name"));
			}
		}
	},
	
	/**
	 * <p> Handler type: server to client
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>freezing_cap:Freezing capability, serialized to NBT
	 * 	<li>entity_uuid: UUID
	 * </ul>
	 */
    FREEZING_CAPABILITY_UPDATE_HANDLER {
		
		@Override
		public void procData(NBTTagCompound data) {
			
			WorldClient world = Minecraft.getMinecraft().world;
			if(world != null && data.hasKey("entity_uuid", Constants.NBT.TAG_COMPOUND)) {
				UUID entityUUID = NBTUtil.getUUIDFromTag(data.getCompoundTag("entity_uuid"));
				List<EntityLivingBase> l = world.getEntities(EntityLivingBase.class, entity -> entity != null && entity.getUniqueID().equals(entityUUID));
				
				if(l != null && !l.isEmpty()) {
					EntityLivingBase entity = l.get(0);
					if(entity.getUniqueID().equals(entityUUID)) {
						if(entity.hasCapability(CapabilityFreezing.FREEZING_CAPABILITY, null)) {
							IFreezing freezingCap = entity.getCapability(CapabilityFreezing.FREEZING_CAPABILITY, null);
							freezingCap.deserializeNBT(data.getCompoundTag("freezing_cap"));
							freezingCap.updateEntity(entity);
						}
					}
				}
			}
		}
	},
     
    /**
 	* <p> Handler type: both sides
 	* 
 	* <p> NBT tags:
 	* <ul>
	* 	<li>pos: BlockPos serialized into long
	* 	<li>to_server: boolean true is this packet is sent to the server
	* 	<li>world_dim_id: integer (only if to_server == true)
	* 	<li>config: NBTTagList serialized IOConfigHandlerMachine
 	* </ul>
 	*/
    IO_CONFIG_SYNC_HANDLER {
 		
		@Override
		public void procData(NBTTagCompound data) {
			
			boolean toServer = false;
			if(data.hasKey("to_server")) {
				toServer = data.getBoolean("to_server");
			}
			
			World world;
			int dimId = 0;
			if(toServer) {
				dimId = data.getInteger("world_dim_id");
				world = DimensionManager.getWorld(dimId);
			} else {
				world = Minecraft.getMinecraft().world;
			}
			
			BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
			TileEntity te = world.getTileEntity(pos);
			
			NBTTagList configData = (NBTTagList) data.getTag("config");
			
			if(te != null && te instanceof TileEntityMachine) {
				((TileEntityMachine) te).getIoConfigHandler().deserializeNBT(configData);
				if(toServer) {
					data.removeTag("to_server");
					data.removeTag("world_dim_id");
					TooManyOres.PACKET_HANDLER.sendToDimension(IO_CONFIG_SYNC_HANDLER, data, dimId);
				}
			}
 			
 		}
 	},
    ;

}
