package misterpemodder.tmo.api.io;

import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;

public interface IIOConfigHandler {
	
	/**
	 * Returns an array of all the io types that this handler can support.
	 */
	IIOType[] getValidIOTypes();
	
	/**
	 * Checks if the specified side can support this io type.
	 * @return false is the side is disabled
	 */
	boolean hasIOType(EnumFacing side, IIOType type);
	
	/**
	 * Returns an array container all the io type that the specified side can support.
	 * @return the array, returns null if {@code hasIOType(side, type) == false} for all IO types.
	 */
	@Nullable
	IIOType[] getIOTypes(EnumFacing side);
	
	/**
	 * Checks if the specified side is an input for this io type.
	 * @return false if {@code hasIOType(side, type) == false}.
	 */
	boolean isSideInput(EnumFacing side, IIOType type);
	
	/**
	 * Checks if the specified side is an output for this io type.
	 * @return false if {@code hasIOType(side, type) == false}.
	 */
	boolean isSideOutput(EnumFacing side, IIOType type);
	
	/**
	 * Changes the config on the specified side for this io type.
	 * @param side
	 * @param type
	 * @param isInput
	 * @param isOutput
	 */
	void setIOTypeConfig(EnumFacing side, IIOType type, boolean isInput, boolean isOutput);
	
	
}
