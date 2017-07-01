package misterpemodder.tmo.api.capability.io;

import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;

public class DefaultIOConfigHandler implements IIOConfigHandler {

	@Override
	public IIOType<?>[] getValidIOTypes() {
		return new IIOType[]{};
	}

	@Override
	public boolean hasIOType(EnumFacing side, IIOType<?> type) {
		return false;
	}

	@Nullable
	@Override
	public IIOType<?>[] getIOTypes(EnumFacing side) {
		return null;
	}

	@Override
	public boolean isSideInput(EnumFacing side, IIOType<?> type) {
		return false;
	}

	@Override
	public boolean isSideOutput(EnumFacing side, IIOType<?> type) {
		return false;
	}
	
	@Override
	public void setIOTypeConfig(EnumFacing side, IIOType<?> type, boolean isInput, boolean isOutput) {}

}
