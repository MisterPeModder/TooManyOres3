package misterpemodder.tmo.main.capability;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.io.IIOType;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

public class SideConfig implements INBTSerializable<NBTTagList> {
	
	private final Map<IIOType, Pair<Boolean, Boolean>> config = new HashMap<>();
	
	private static final Map<String, IIOType> ID_TO_IOTYPE = new HashMap<>();
	
	public boolean isEnabled() {
		return !config.isEmpty();
	}

	@Override
	public NBTTagList serializeNBT() {
		NBTTagList l = new NBTTagList();
		if(!config.isEmpty()) {
			for(IIOType t : config.keySet()) {
				Pair<Boolean, Boolean> p = config.get(t);
				if(p.getLeft() || p.getRight()) {
					NBTTagCompound tc = new NBTTagCompound();
					tc.setString("id", t.getID());
					tc.setBoolean("input", p.getLeft());
					tc.setBoolean("output", p.getRight());
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
				IIOType type = getIOTypeFromId(c.getString("id"));
				if(type != null) {
					config.put(type, Pair.of(c.getBoolean("input"), c.getBoolean("output")));
				}
			}
		}
	}
	
	private IIOType getIOTypeFromId(String id) {
		if(ID_TO_IOTYPE.containsKey(id)) {
			return ID_TO_IOTYPE.get(id);
		} else {
			for(IIOType type : TooManyOresAPI.IO_TYPES) {
				if(type.getID().equals(id)) {
					ID_TO_IOTYPE.put(id, type);
					return type;
				}
			}
		}
		return null;
	}
	
	public boolean hasIOType(IIOType type) {
		if(type == null) return false;
		return config.containsKey(type);
	}
	
	public IIOType[] getIOTypes() {
		if(config.isEmpty()) {
			return null;
		}
		return (IIOType[]) config.keySet().toArray();
	}
	
	public boolean isSideInput(IIOType type) {
		if(hasIOType(type)) {
			return config.get(type).getLeft();
		}
		return false;
	}
	
	public boolean isSideOutput(IIOType type) {
		if(hasIOType(type)) {
			return config.get(type).getRight();
		}
		return false;
	}
	
	public void setIOTypeConfig(IIOType type, boolean isInput, boolean isOutput) {
		config.put(type, Pair.of(isInput, isOutput));
	}

}
