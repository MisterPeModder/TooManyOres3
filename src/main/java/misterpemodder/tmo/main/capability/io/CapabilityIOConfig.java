package misterpemodder.tmo.main.capability.io;

import misterpemodder.tmo.api.capability.io.DefaultIOConfigHandler;
import misterpemodder.tmo.api.capability.io.IIOConfigHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityIOConfig {
	
	 @CapabilityInject(IIOConfigHandler.class)
	 public static Capability<IIOConfigHandler> CONFIG_HANLDER_CAPABILITY = null;
	 
	 public static void register() {
		 CapabilityManager.INSTANCE.register(IIOConfigHandler.class, new Capability.IStorage<IIOConfigHandler>() {

			@Override
			public NBTBase writeNBT(Capability<IIOConfigHandler> capability, IIOConfigHandler instance, EnumFacing side) {
				return null;
			}
			@Override
			public void readNBT(Capability<IIOConfigHandler> capability, IIOConfigHandler instance, EnumFacing side, NBTBase nbt) {}
		}, DefaultIOConfigHandler.class);
	 }
	 
}
