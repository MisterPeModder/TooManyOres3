package misterpemodder.tmo.main.capability.io;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import misterpemodder.tmo.api.capability.io.IIOConfigHandler;
import misterpemodder.tmo.api.capability.io.IIOType;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.network.TMOPacketHandler;
import misterpemodder.tmo.main.network.packet.PacketClientToServer;
import misterpemodder.tmo.main.network.packet.PacketServerToClient;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import misterpemodder.tmo.main.utils.EnumBlockSide;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;

public class IOConfigHandlerMachine implements IIOConfigHandler, INBTSerializable<NBTTagList> {
	
	private final IIOType<?>[] ioTypes;
	private final ImmutableList<IOState> states;
	private final ImmutableMap<EnumBlockSide, SideConfig> sideConfigs;
	
	private final TileEntityMachine<?> te;
	
	public IOConfigHandlerMachine(TileEntityMachine<?> te, @Nullable List<IOState> extraIOStates, IIOType<?>... types) {
		this.te = te;
		this.ioTypes = types;
		
		this.states = ImmutableList.copyOf(IOState.completeStateList(extraIOStates));
		
		ImmutableMap.Builder<EnumBlockSide, SideConfig> b = new ImmutableMap.Builder<>();
		for(EnumBlockSide side : EnumBlockSide.values()) {
			b.put(side, new SideConfig());
		}
		this.sideConfigs = b.build();
	}

	@Override
	public IIOType<?>[] getValidIOTypes() {
		return this.ioTypes;
	}
	
	public boolean isIOTypeValid(IIOType<?> type) {
		if(ioTypes != null && ioTypes.length > 0) {
			for(IIOType<?> t : ioTypes) {
				if(t == type) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public NBTTagList serializeNBT() {
		NBTTagList l = new NBTTagList();
		for(EnumBlockSide side : sideConfigs.keySet()) {
			if(sideConfigs.get(side).isEnabled()) {
				NBTTagCompound c = new NBTTagCompound();
				c.setString("side", side.getName());
				c.setTag("config", sideConfigs.get(side).serializeNBT());
				l.appendTag(c);				
			}
		}
		return l;
	}

	@Override
	public void deserializeNBT(NBTTagList nbt) {
		for(int i=0, s=nbt.tagCount(); i<s; i++) {
			NBTBase base = nbt.get(i);
			if(base instanceof NBTTagCompound) {
				NBTTagCompound c = (NBTTagCompound) base;
				
				EnumBlockSide side = EnumBlockSide.forName(c.getString("side"));
				if(side != null) {
					
					if(te != null) {
						if(te.isSideDisabled(side)) {
							continue;
						}
					}
					
					SideConfig cfg = sideConfigs.get(side);
					cfg.deserializeNBT((NBTTagList)c.getTag("config"));
					
					IIOType<?>[] types = cfg.getIOTypes();
					if(types != null && types.length > 0) {
						EnumFacing facing = side.toFacing(getMachineFacing());
						for(IIOType<?> type : types) {
							te.updateHandlerContainers(type, facing, this);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean hasIOType(EnumFacing side, IIOType<?> type) {
		return sideConfigs.get(getMachineSide(side)).hasIOType(type);
	}

	@Override
	public IIOType<?>[] getIOTypes(EnumFacing side) {
		return sideConfigs.get(getMachineSide(side)).getIOTypes();
	}

	@Override
	public boolean isSideInput(EnumFacing side, IIOType<?> type) {
		if(te.isSideDisabled(side)) return false;
		return sideConfigs.get(getMachineSide(side)).isSideInput(type);
	}

	@Override
	public boolean isSideOutput(EnumFacing side, IIOType<?> type) {
		if(te.isSideDisabled(side)) return false;
		return sideConfigs.get(getMachineSide(side)).isSideOutput(type);
	}
	
	public IOState getIOStateConfig(EnumFacing side, IIOType<?> type) {
		if(te.isSideDisabled(side)) return IOState.DISABLED;
		return sideConfigs.get(getMachineSide(side)).getIOStateConfig(type);
	}
	
	public IOState getIOStateConfig(EnumBlockSide side, IIOType<?> type) {
		if(te.isSideDisabled(side)) return IOState.DISABLED;
		return sideConfigs.get(side).getIOStateConfig(type);
	}
	
	public void setIOStateConfig(EnumFacing side, IIOType<?> type, IOState state) {
		if(te.isSideDisabled(side)) return;
		sideConfigs.get(getMachineSide(side)).setIOStateConfig(type, state);
	}
	
	public void setIOStateConfig(EnumBlockSide side, IIOType<?> type, IOState state) {
		if(te.isSideDisabled(side)) return;
		sideConfigs.get(side).setIOStateConfig(type, state);
		te.updateHandlerContainers(type, side.toFacing(getMachineFacing()), this);
		
		NBTTagCompound toSend = new NBTTagCompound();
		toSend.setLong("pos", te.getPos().toLong());
		toSend.setTag("config", this.serializeNBT());
		
		int dimId = te.getWorld().provider.getDimension();
		
		if(te.getWorld().isRemote) {
			toSend.setBoolean("to_server", true);
			toSend.setInteger("world_dim_id", dimId);
			TMOPacketHandler.network.sendToServer(new PacketClientToServer(PacketDataHandlers.IO_CONFIG_SYNC_HANDLER, toSend));
		} else {
			TMOPacketHandler.network.sendToDimension(new PacketServerToClient(PacketDataHandlers.IO_CONFIG_SYNC_HANDLER, toSend), dimId);
		}
	}

	@Override
	public void setIOTypeConfig(EnumFacing side, IIOType<?> type, boolean isInput, boolean isOutput) {
		IOState state = isInput == isOutput? (isInput? IOState.ALL : IOState.DISABLED) : (isInput? IOState.INPUT : IOState.OUTPUT);
		setIOStateConfig(side, type, state);
	}
	
	private EnumFacing getMachineFacing() {
		EnumFacing facing = null;
		if(te.hasWorld()) {
			facing = te.getWorld().getBlockState(this.te.getPos()).getValue(BlockMachine.FACING);
		}
		return facing == null? EnumFacing.NORTH : facing;
	}
	
	private EnumBlockSide getMachineSide(EnumFacing side) {
		return EnumBlockSide.fromFacing(getMachineFacing(), side);
	}
	
	public List<IOState> getIOStates() {
		return this.states.asList();
	}

}
