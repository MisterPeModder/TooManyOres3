package misterpemodder.tmo.main.apiimpl.io;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class IOTypeEnergy extends IOType<IEnergyStorage> {

	@Override
	public String getID() {
		return "energy";
	}

	@Override
	public Capability<IEnergyStorage> getCapabilityInstance() {
		return CapabilityEnergy.ENERGY;
	}

}
