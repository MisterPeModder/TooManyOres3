package misterpemodder.tmo.main.tileentity;

import net.minecraft.world.IWorldNameable;

public interface IOwnable extends IWorldNameable{
	
	public String getOwner();
	public void setOwner(String owner);
	public boolean hasOwner();
	
	public void setCustomName(String name);
	
}
