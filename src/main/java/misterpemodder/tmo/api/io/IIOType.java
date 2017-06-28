package misterpemodder.tmo.api.io;

public interface IIOType {

	String getID();
	
	String getUnlocalizedName();
	
	/**
	 * If this IOType is a subtype of an other IOType, this method should return the main IOType.
	 * If this IOType is already a main one, this method should return this one.
	 */
	IIOType getMainType();
	
}
