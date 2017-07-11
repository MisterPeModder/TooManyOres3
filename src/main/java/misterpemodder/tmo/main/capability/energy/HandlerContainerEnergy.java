package misterpemodder.tmo.main.capability.energy;

import misterpemodder.tmo.main.capability.HandlerContainer;
import net.minecraftforge.energy.IEnergyStorage;

public class HandlerContainerEnergy extends HandlerContainer<IEnergyStorage> implements IEnergyStorage {

	public HandlerContainerEnergy() {
		this(EmptyEnergyStorage.INSTANCE);
	}
	
	public HandlerContainerEnergy(IEnergyStorage handler) {
		super(handler);
	}
	
	@Override
	public void setEmptyHandler() {
		this.containedHandler = EmptyEnergyStorage.INSTANCE;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return this.containedHandler.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return this.containedHandler.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored() {
		return this.containedHandler.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return this.containedHandler.getMaxEnergyStored();
	}

	@Override
	public boolean canExtract() {
		return this.containedHandler.canExtract();
	}

	@Override
	public boolean canReceive() {
		return this.containedHandler.canReceive();
	}

}
