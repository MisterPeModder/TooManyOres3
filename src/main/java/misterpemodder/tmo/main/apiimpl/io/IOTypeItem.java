package misterpemodder.tmo.main.apiimpl.io;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class IOTypeItem extends IOType<IItemHandler> {

	@Override
	public String getID() {
		return "item";
	}

	@Override
	public Capability<IItemHandler> getCapabilityInstance() {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

}
