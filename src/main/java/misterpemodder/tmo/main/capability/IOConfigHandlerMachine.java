package misterpemodder.tmo.main.capability;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import misterpemodder.tmo.api.io.IIOConfigHandler;
import misterpemodder.tmo.api.io.IIOType;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.network.TMOPacketHandler;
import misterpemodder.tmo.main.network.packet.PacketClientToServer;
import misterpemodder.tmo.main.network.packet.PacketServerToClient;
import misterpemodder.tmo.main.utils.EnumBlockSide;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;

public class IOConfigHandlerMachine implements IIOConfigHandler, INBTSerializable<NBTTagList> {
	
	private final IIOType[] ioTypes;
	private final ImmutableMap<EnumBlockSide, SideConfig> sideConfigs;
	
	private final TileEntity te;
	
	public IOConfigHandlerMachine(TileEntity te, IIOType... types) {
		this.te = te;
		this.ioTypes = types;
		ImmutableMap.Builder<EnumBlockSide, SideConfig> b = new Builder<>();
		for(EnumBlockSide side : EnumBlockSide.values()) {
			b.put(side, new SideConfig());
		}
		this.sideConfigs = b.build();
	}

	@Override
	public IIOType[] getValidIOTypes() {
		return this.ioTypes;
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
					sideConfigs.get(side).deserializeNBT((NBTTagList)c.getTag("config"));					
				}
			}
		}
	}

	@Override
	public boolean hasIOType(EnumFacing side, IIOType type) {
		return sideConfigs.get(getMachineSide(side)).hasIOType(type);
	}

	@Override
	public IIOType[] getIOTypes(EnumFacing side) {
		return sideConfigs.get(getMachineSide(side)).getIOTypes();
	}

	@Override
	public boolean isSideInput(EnumFacing side, IIOType type) {
		return sideConfigs.get(getMachineSide(side)).isSideInput(type);
	}

	@Override
	public boolean isSideOutput(EnumFacing side, IIOType type) {
		return sideConfigs.get(getMachineSide(side)).isSideOutput(type);
	}

	@Override
	public void setIOTypeConfig(EnumFacing side, IIOType type, boolean isInput, boolean isOutput) {
		sideConfigs.get(getMachineSide(side)).setIOTypeConfig(type, isInput, isOutput);
		
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
	
	private EnumFacing getMachineFacing() {
		EnumFacing facing = te.getWorld().getBlockState(this.te.getPos()).getValue(BlockMachine.FACING);
		return facing == null? EnumFacing.NORTH : facing;
	}
	
	private EnumBlockSide getMachineSide(EnumFacing side) {
		return EnumBlockSide.fromFacing(getMachineFacing(), side);
	}

}
