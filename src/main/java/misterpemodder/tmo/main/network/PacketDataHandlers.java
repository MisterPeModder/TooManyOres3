package misterpemodder.tmo.main.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.capability.CapabilityFreezing;
import misterpemodder.tmo.main.capability.CapabilityFreezing.IFreezing;
import misterpemodder.tmo.main.client.gui.ContainerInjector;
import misterpemodder.tmo.main.client.gui.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.client.gui.tabs.TabBase.TabID;
import misterpemodder.tmo.main.client.gui.tabs.TabMainInjector;
import misterpemodder.tmo.main.client.gui.tabs.TabSecurity;
import misterpemodder.tmo.main.network.packet.PacketServerToClient;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
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
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public final class PacketDataHandlers {
	
	public static final List<IPacketDataHandler> HANDLERS = new ArrayList<>();
	
	public static void registerHandlers() {
		HANDLERS.add(TCHEST_UPDATE_HANDLER);
		HANDLERS.add(TE_UPDATE_HANDLER);
		HANDLERS.add(TE_UPDATE_REQUEST_HANDLER);
		HANDLERS.add(BUTTON_CLICK_HANDLER);
		HANDLERS.add(ANVIL_ITEM_NAME_HANDLER);
		HANDLERS.add(PROGRESS_ARROW_UPDATE_HANDLER);
		HANDLERS.add(FREEZING_CAPABILITY_UPDATE_HANDLER);
	}
	
	/**
	 * <p> Handler type: server to client
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>pos: BlockPos serialized into long
	 * 	<li>numPlayersUsing: integer
	 * </ul>
	 */
	public static final IPacketDataHandler TCHEST_UPDATE_HANDLER = new IPacketDataHandler() {
		
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
	};
	
	/**
	 * <p> Handler type: server to client
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>pos: BlockPos serialized into long
	 * 	<li>tileEntity: TileEntity
	 * </ul>
	 */
    public static final IPacketDataHandler TE_UPDATE_HANDLER = new IPacketDataHandler() {
		
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
	};
	

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
	public static final IPacketDataHandler TE_UPDATE_REQUEST_HANDLER = new IPacketDataHandler() {
		
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
	};
	
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
	public static final IPacketDataHandler BUTTON_CLICK_HANDLER = new IPacketDataHandler() {
		
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
					if(te instanceof TileEntityInjector && info.hasKey("mode")) {
						TransferMode m = TransferMode.values()[info.getInteger("mode")];
						((TileEntityInjector)te).setTransferMode(m);
					}
				} 
				else if(bId == TabMainInjector.CLEAR_TANK_BUTTON_ID) {
					if(te instanceof TileEntityInjector) {
						((TileEntityInjector)te).getTank().drain(TileEntityInjector.CAPACITY, true);
					}
				}
			break;
			
			default:
				TMORefs.LOGGER.warn("This tab can not handle button events!");
			break;
			}
			
		}
	};
	
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
    public static final IPacketDataHandler ANVIL_ITEM_NAME_HANDLER = new IPacketDataHandler() {
		
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
	};
	
	
	/**
	 * <p> Handler type: server to client
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>progress: Integer
	 * </ul>
	 */
    public static final IPacketDataHandler PROGRESS_ARROW_UPDATE_HANDLER = new IPacketDataHandler() {
		
		@Override
		public void procData(NBTTagCompound data) {
			Container c = Minecraft.getMinecraft().player.openContainer;
			if(c instanceof ContainerInjector && data.hasKey("progress")) {
				((ContainerInjector)c).progress = data.getInteger("progress");
			}
		}
	};
	
	/**
	 * <p> Handler type: server to client
	 * 
	 * <p> NBT tags:
	 * <ul>
	 * 	<li>freezing_cap:Freezing capability, serialized to NBT
	 * 	<li>entity_uuid: UUID
	 * </ul>
	 */
    public static final IPacketDataHandler FREEZING_CAPABILITY_UPDATE_HANDLER = new IPacketDataHandler() {
		
		@Override
		public void procData(NBTTagCompound data) {
			
			WorldClient world = Minecraft.getMinecraft().world;
			UUID entityUUID = NBTUtil.getUUIDFromTag(data.getCompoundTag("entity_uuid"));
			List<EntityLivingBase> l = world.getEntities(EntityLivingBase.class, filter -> filter.getUniqueID().equals(entityUUID));
			
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
	};

}
