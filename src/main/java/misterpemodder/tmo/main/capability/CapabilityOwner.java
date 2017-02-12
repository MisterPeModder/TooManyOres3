package misterpemodder.tmo.main.capability;

import misterpemodder.tmo.api.owner.DefaultOwner;
import misterpemodder.tmo.api.owner.IOwnerHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityOwner {
	
	 @CapabilityInject(IOwnerHandler.class)
	 public static Capability<IOwnerHandler> OWNER_HANDLER_CAPABILITY = null;
	 
	 public static void register() {
		 CapabilityManager.INSTANCE.register(IOwnerHandler.class, new Capability.IStorage<IOwnerHandler>() {

			@Override
			public NBTBase writeNBT(Capability<IOwnerHandler> capability, IOwnerHandler instance, EnumFacing side) {
				return null;
			}
			@Override
			public void readNBT(Capability<IOwnerHandler> capability, IOwnerHandler instance, EnumFacing side, NBTBase nbt) {}
		}, DefaultOwner.class);
	 }
	 
}
