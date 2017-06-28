package misterpemodder.tmo.main.network;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;

import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.capability.CapabilityFreezing;
import misterpemodder.tmo.main.capability.CapabilityFreezing.IFreezing;
import misterpemodder.tmo.main.client.gui.tabs.TabBase.TabID;
import misterpemodder.tmo.main.client.gui.tabs.TabMainInjector;
import misterpemodder.tmo.main.client.gui.tabs.TabSecurity;
import misterpemodder.tmo.main.inventory.ContainerBase;
import misterpemodder.tmo.main.inventory.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.inventory.ISyncedContainerElement;
import misterpemodder.tmo.main.inventory.elements.ContainerElementTank;
import misterpemodder.tmo.main.network.packet.PacketServerToClient;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
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
	 * <p> Handler type: server to client
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>pos: BlockPos serialized into long
	 * 	<li>numPlayersUsing: integer
	 * </ul>
	 */
	TCHEST_UPDATE_HANDLER {
		
		@Override
		public void procData(NBTTagCompound data) {
			WorldClient world = Minecraft.getMinecraft().world;
			BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof TileEntityTitaniumChest) {
				TileEntityTitaniumChest t = (TileEntityTitaniumChest) te;
				if(data.hasKey("numPlayersUsing")) t.numPlayersUsing = data.getInteger("numPlayersUsing");
			}
		}
	},
	
	/**
	 * <p> Handler type: server to client
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>pos: BlockPos serialized into long
	 * 	<li>tileEntity: TileEntity
	 * </ul>
	 */
    TE_UPDATE_HANDLER {
		
		@Override
		public void procData(NBTTagCompound data) {
			WorldClient world = Minecraft.getMinecraft().world;
			BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
			TileEntity te = world.getTileEntity(pos);
			if(te == null) return;
			else {
				te.deserializeNBT(data.getCompoundTag("tileEntity"));
			}
		}
	},
	

	/**
	 * <p> Handler type: client to server
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>pos: BlockPos serialized into long
	 * 	<li>world_dim_id: integer
	 * 	<li>player_id: UUID
	 * </ul>
	 */
	TE_UPDATE_REQUEST_HANDLER {
		
		@Override
		public void procData(NBTTagCompound data) {
			WorldServer world = DimensionManager.getWorld(data.getInteger("world_dim_id"));
			UUID playerUUID = NBTUtil.getUUIDFromTag(data.getCompoundTag("player_id"));
			
			EntityPlayer player = world.getPlayerEntityByUUID(playerUUID);
			
			BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
			TileEntity te = world.getTileEntity(pos);
			
			if(te != null && player != null) {
				NBTTagCompound toSend = new NBTTagCompound();
				toSend.setLong("pos", pos.toLong());
				toSend.setTag("tileEntity", te.serializeNBT());
				
				TMOPacketHandler.network.sendTo(new PacketServerToClient(PacketDataHandlers.TE_UPDATE_HANDLER, toSend), (EntityPlayerMP)player);
				
			}
		}
	},
	
	/**
	 * <p> Handler type: client to server
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>pos: BlockPos serialized into long
	 * 	<li>world_dim_id: integer
	 * 	<li>tab_id: TabID
	 * 	<li>button_id: integer
	 * 	<li>info: NBTTagCompound additional data
	 * </ul>
	 */
	BUTTON_CLICK_HANDLER {
		
		@Override
		public void procData(NBTTagCompound data) {
			WorldServer world = DimensionManager.getWorld(data.getInteger("world_dim_id"));
			BlockPos pos = BlockPos.fromLong(data.getLong("pos"));
			TabID tId = TabID.values()[data.getInteger("tab_id")];
			int bId = data.getInteger("button_id");
			NBTTagCompound info = data.getCompoundTag("info");
			
			TileEntity te = world.getTileEntity(pos);
			
			switch(tId) {
			case SECURITY:
				if(bId == TabSecurity.LOCK_BUTTON_ID) {
					if(te instanceof ILockable && info.hasKey("locked")) {
						((ILockable)te).setLocked(info.getBoolean("locked"));
					}
				}
			break;
			
			case MAIN_INJECTOR:
				if(bId == TabMainInjector.TOGGLE_MODE_BUTTON_ID) {
					if(te instanceof TileEntityInjector && info.hasKey("mode", Constants.NBT.TAG_INT)) {
						TransferMode m = TransferMode.values()[info.getInteger("mode")];
						((TileEntityInjector)te).setTransferMode(m);
					}
				}
			break;
			
			case MISC:
				if(bId == ContainerElementTank.CLEAR_TANK_BUTTON_ID) {
					if(te instanceof TileEntityMachine && info.hasKey("tank_id", Constants.NBT.TAG_SHORT)) {
						//((TileEntityInjector)te).getTank().drain(TileEntityInjector.CAPACITY, true);
						((TileEntityMachine)te).emptyTank(info.getShort("tank_id"));
					}
				}
			break;
			
			default:
				TMORefs.LOGGER.warn("This tab can not handle button events!");
			break;
			}
			
		}
	},
	
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
					if(entity instanceof EntityLivingBase && entity.getUniqueID().equals(entityUUID)) {
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
	 * <p> Handler type: server to client
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>element_id: integer
	 * 	<li>element_data: NBTTagCompound
	 * </ul>
	 */
     SYNCED_CONTAINER_ELEMENTS_HANDLER {
		
		@Override
		public void procData(NBTTagCompound data) {
			Container c = Minecraft.getMinecraft().player.openContainer;
			
			if(c instanceof ContainerBase) {
				if(data.hasKey("element_id", Constants.NBT.TAG_INT)) {
					ImmutableList<ISyncedContainerElement> elements = ((ContainerBase) c).containerElements;
					int id = data.getInteger("element_id");
					if(id >= 0 && id < elements.size()) {
						ISyncedContainerElement element = elements.get(id);
						NBTTagCompound edata = data.hasKey("element_data")? data.getCompoundTag("element_data"):new NBTTagCompound();
						element.procData(edata);
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
					TMOPacketHandler.network.sendToDimension(new PacketServerToClient(IO_CONFIG_SYNC_HANDLER, data), dimId);
				}
			}
 			
 		}
 	},
    ;

}
