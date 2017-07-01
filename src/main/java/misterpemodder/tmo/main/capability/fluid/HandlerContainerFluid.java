package misterpemodder.tmo.main.capability.fluid;

import misterpemodder.tmo.main.capability.HandlerContainer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

public class HandlerContainerFluid extends HandlerContainer<IFluidHandler> implements IFluidHandler {

	public HandlerContainerFluid() {
		this(EmptyFluidHandler.INSTANCE);
	}
	
	public HandlerContainerFluid(IFluidHandler handler) {
		super(handler);
	}
	
	@Override
	public void setEmptyHandler() {
		this.containedHandler = EmptyFluidHandler.INSTANCE;
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties() {
		return this.containedHandler.getTankProperties();
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return this.containedHandler.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		return this.containedHandler.drain(resource, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return this.containedHandler.drain(maxDrain, doDrain);
	}

}
