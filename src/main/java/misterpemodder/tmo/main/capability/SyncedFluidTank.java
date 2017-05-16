package misterpemodder.tmo.main.capability;

import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class SyncedFluidTank extends FluidTank {

	public SyncedFluidTank(Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
	}

	public SyncedFluidTank(FluidStack fluidStack, int capacity) {
		super(fluidStack, capacity);
	}

	public SyncedFluidTank(int capacity) {
		super(capacity);
	}
	
	@Override
	protected void onContentsChanged() {
		super.onContentsChanged();
		if(this.tile instanceof TileEntityContainerBase) {
			((TileEntityContainerBase)this.tile).sync();
		}
	}

}
