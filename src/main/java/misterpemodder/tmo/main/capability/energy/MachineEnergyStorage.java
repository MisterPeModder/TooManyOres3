package misterpemodder.tmo.main.capability.energy;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.main.capability.IMachineElementHandler;
import misterpemodder.tmo.main.capability.io.IOConfigHandlerMachine;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class MachineEnergyStorage extends EnergyStorage implements IMachineElementHandler<IEnergyStorage>, INBTSerializable<NBTTagInt> {
	
	private final IOConfigHandlerMachine configHandler;
	private final boolean canExtract;
	
	public MachineEnergyStorage(TileEntityMachine te,int capacity, boolean canExtract) {
        this(te, capacity, capacity, capacity, 0, canExtract);
    }

    public MachineEnergyStorage(TileEntityMachine te, int capacity, int maxTransfer, boolean canExtract) {
        this(te, capacity, maxTransfer, maxTransfer, 0, canExtract);
    }

    public MachineEnergyStorage(TileEntityMachine te, int capacity, int maxReceive, int maxExtract, boolean canExtract) {
        this(te, capacity, maxReceive, maxExtract, 0, canExtract);
    }
    
    public MachineEnergyStorage(TileEntityMachine te, int capacity, int maxReceive, int maxExtract, int energy, boolean canExtract) {
    	super(capacity, maxReceive, maxExtract, energy);
    	this.configHandler = te.getIoConfigHandler();
    	this.canExtract = canExtract;
    }
    
    @Override
	public IEnergyStorage getSideHandler(EnumFacing side) {
		return new SideHandler(side);
	}
    
    public void setEnergy(int energy) {
    	this.energy = energy;
    }
    
    @Override
    public String toString() {
    	return String.format("%s: [%d/%d RF]", this.getClass().getName(), this.getEnergyStored(), this.getMaxEnergyStored());
    }
    
    @Override
    public NBTTagInt serializeNBT() {
    	return new NBTTagInt(this.energy);
    }
    
    @Override
    public void deserializeNBT(NBTTagInt nbt) {
    	this.deserializeNBT(nbt.getInt());
    }
    
    public void deserializeNBT(int energy) {
    	this.setEnergy(energy);
    }
    
    public final class SideHandler implements IEnergyStorage {
		
		private final EnumFacing side;

		private SideHandler(EnumFacing side) {
			this.side = side;
		}
		
		private boolean isInput() {
			return MachineEnergyStorage.this.configHandler.isSideInput(side, TooManyOresAPI.energyIoType);
		}
		
		private boolean isOutput() {
			return MachineEnergyStorage.this.configHandler.isSideOutput(side, TooManyOresAPI.energyIoType);
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			if(isInput()) {
				return MachineEnergyStorage.this.receiveEnergy(maxReceive, simulate);
			} else {				
				return 0;
			}
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			if(isOutput() && MachineEnergyStorage.this.canExtract) {
				return MachineEnergyStorage.this.extractEnergy(maxExtract, simulate);
			} else {				
				return 0;
			}
		}

		@Override
		public int getEnergyStored() {
			if(isOutput() || isInput()) {
				return MachineEnergyStorage.this.getEnergyStored();
			} else {				
				return 0;
			}
		}

		@Override
		public int getMaxEnergyStored() {
			if(isOutput() || isInput()) {
				return MachineEnergyStorage.this.getMaxEnergyStored();
			} else {				
				return 0;
			}
		}

		@Override
		public boolean canExtract() {
			return isOutput() && MachineEnergyStorage.this.canExtract;
		}

		@Override
		public boolean canReceive() {
			return isInput();
		}
		
		@Override
		public String toString() {
			return MachineEnergyStorage.this.toString();
		}
		
	}
	
}
