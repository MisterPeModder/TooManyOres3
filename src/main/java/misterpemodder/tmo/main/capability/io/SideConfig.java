package misterpemodder.tmo.main.capability.io;

import java.util.HashMap;
import java.util.Map;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.capability.io.IIOType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

public class SideConfig implements INBTSerializable<NBTTagList> {
	
	private final Map<IIOType<?>, IOState> config = new HashMap<>();
	
	private static final Map<String, IIOType<?>> ID_TO_IOTYPE = new HashMap<>();
	private static final Map<String, IOState> ID_TO_IOSTATE = new HashMap<>();
	
	public boolean isEnabled() {
		return !config.isEmpty();
	}

	@Override
	public NBTTagList serializeNBT() {
		NBTTagList l = new NBTTagList();
		if(!config.isEmpty()) {
			for(IIOType<?> t : config.keySet()) {
				IOState state = config.get(t);
				if(state != IOState.DISABLED) {
					NBTTagCompound tc = new NBTTagCompound();
					tc.setString("id", t.getID());
					tc.setString("state", state.getId());
					l.appendTag(tc);
				}
			}
		}
		
		return l;
	}

	@Override
	public void deserializeNBT(NBTTagList l) {
		config.clear();
		for(int i=0, s=l.tagCount(); i<s; i++) {
			NBTBase base = l.get(i);
			if(base instanceof NBTTagCompound) {
				NBTTagCompound c = (NBTTagCompound) base;
				IIOType<?> type = getIOTypeFromId(c.getString("id"));
				if(type != null && c.hasKey("state")) {
					config.put(type, getIOStateFromId(c.getString("state")));
				}
			}
		}
	}
	
	private static IIOType<?> getIOTypeFromId(String id) {
		if(ID_TO_IOTYPE.containsKey(id)) {
			return ID_TO_IOTYPE.get(id);
		} else {
			for(IIOType<?> type : TooManyOresAPI.IO_TYPES) {
				if(type.getID().equals(id)) {
					ID_TO_IOTYPE.put(id, type);
					return type;
				}
			}
		}
		return null;
	}
	
	private static IOState getIOStateFromId(String id) {
		if(ID_TO_IOSTATE.containsKey(id)) {
			return ID_TO_IOSTATE.get(id);
		} else {
			for(IOState state : IOState.values()) {
				if(state.getId().equals(id)) {
					ID_TO_IOSTATE.put(id, state);
					return state;
				}
			}
		}
		return null;
	}
	
	public boolean hasIOType(IIOType<?> type) {
		if(type == null) return false;
		return config.containsKey(type);
	}
	
	public IIOType<?>[] getIOTypes() {
		if(config.isEmpty()) {
			return null;
		}
		return config.keySet().toArray(new IIOType<?>[]{});
	}
	
	public boolean isSideInput(IIOType<?> type) {
		if(hasIOType(type)) {
			return config.get(type).canInsert();
		}
		return false;
	}
	
	public boolean isSideOutput(IIOType<?> type) {
		if(hasIOType(type)) {
			return config.get(type).canExtract();
		}
		return false;
	}
	
	public IOState getIOStateConfig(IIOType<?> type) {
		if(hasIOType(type)) {
			return config.get(type);
		}
		return IOState.DISABLED;
	}
	
	public void setIOStateConfig(IIOType<?> type, IOState state) {
		config.put(type, state);
	}

}
