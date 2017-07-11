package misterpemodder.tmo.main.capability.fluid;

import com.google.common.collect.ImmutableMap;

import misterpemodder.tmo.main.capability.IMachineElementHandler;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerConcatenate;

public class CombinedFluidHandlerMachine extends FluidHandlerConcatenate implements IMachineElementHandler<FluidHandlerConcatenate> {
	
private ImmutableMap<EnumFacing, FluidHandlerConcatenate> combinedHandlers;
	
	public CombinedFluidHandlerMachine(MachineFluidTank... tanks) {
		super(tanks);
		
		ImmutableMap.Builder<EnumFacing, FluidHandlerConcatenate> builder = new ImmutableMap.Builder<>();
		for(EnumFacing side : EnumFacing.values()) {
			if(tanks != null && tanks.length > 0) {
				int l = tanks.length;
				IFluidHandler[] sideHandlers = new IFluidHandler[l];
				for(int i=0; i<l; i++) {
					sideHandlers[i] = tanks[i].getSideHandler(side);
				}
				builder.put(side, new FluidHandlerConcatenate(sideHandlers));
			}
		}
		this.combinedHandlers = builder.build();
	}

	@Override
	public FluidHandlerConcatenate getSideHandler(EnumFacing side) {
		return combinedHandlers.get(side);
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s", this.getClass().getName(), this.combinedHandlers);
	}
	
}
