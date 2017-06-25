package misterpemodder.tmo.main.inventory;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ISyncedContainerElement{

	public boolean shouldSendDataToClient();
	
	public NBTTagCompound writeData(NBTTagCompound data);
	
	@SideOnly(Side.CLIENT)
	public void procData(NBTTagCompound data);
	
}
