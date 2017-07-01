package misterpemodder.tmo.main.apiimpl.io;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class IOTypeFluid extends IOType<IFluidHandler> {

	@Override
	public String getID() {
		return "fluid";
	}

	@Override
	public Capability<IFluidHandler> getCapabilityInstance() {
		return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
	}

}
