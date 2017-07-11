package misterpemodder.tmo.main.capability.fluid;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.main.capability.IMachineElementHandler;
import misterpemodder.tmo.main.capability.io.IOConfigHandlerMachine;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class MachineFluidTank extends SyncedFluidTank implements IMachineElementHandler<IFluidHandler> {
	
	private final IOConfigHandlerMachine configHandler;
	private final boolean canFill;
	private final boolean canDrain;
	
	public MachineFluidTank(TileEntityMachine te, int capacity) {
		this(te, capacity, true, true);
	}

	public MachineFluidTank(TileEntityMachine te, int capacity, boolean canFill, boolean canDrain) {
		super(capacity);
		this.configHandler = te.getIoConfigHandler();
		this.canFill = canFill;
		this.canDrain = canDrain;
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return super.fill(resource, doFill);
	}
	
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		return super.drain(resource, doDrain);
	}
	
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return super.drain(maxDrain, doDrain);
	}

	@Override
	public IFluidHandler getSideHandler(EnumFacing side) {
		return new SideHandler(side);
	}
	
	public final class SideHandler implements IFluidHandler {
		
		private final EnumFacing side;

		private SideHandler(EnumFacing side) {
			this.side = side;
		}
		
		private boolean isInput() {
			return MachineFluidTank.this.configHandler.isSideInput(side, TooManyOresAPI.fluidIoType);
		}
		
		private boolean isOutput() {
			return MachineFluidTank.this.configHandler.isSideOutput(side, TooManyOresAPI.fluidIoType);
		}
		
		@Override
		public IFluidTankProperties[] getTankProperties() {
			return MachineFluidTank.this.getTankProperties();
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if(isInput() && MachineFluidTank.this.canFill) {
				return MachineFluidTank.this.fill(resource, doFill);
			} else {				
				return 0;
			}
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			if(isOutput() && MachineFluidTank.this.canDrain) {
				return MachineFluidTank.this.drain(resource, doDrain);
			} else {
				return null;
			}
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			if(isOutput() && MachineFluidTank.this.canDrain) {
				return MachineFluidTank.this.drain(maxDrain, doDrain);
			} else {
				return null;
			}
		}
		
	}

}
