package misterpemodder.tmo.main.tileentity;

import java.util.Arrays;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.hc.main.capabilty.SyncedItemHandler;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.main.blocks.BlockMachineCasing.EnumMachineCasingVariant;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.capability.energy.MachineEnergyStorage;
import misterpemodder.tmo.main.capability.fluid.CombinedFluidHandlerMachine;
import misterpemodder.tmo.main.capability.fluid.MachineFluidTank;
import misterpemodder.tmo.main.capability.io.IOConfigHandlerMachine;
import misterpemodder.tmo.main.capability.io.IOState;
import misterpemodder.tmo.main.capability.item.CombinerItemStackHandlerMachine;
import misterpemodder.tmo.main.capability.item.MachineItemStackHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

public class TileEntityThemoelectricGenerator extends TileEntityMachine {
	
	private SyncedItemHandler buckets;
	private MachineItemStackHandler chargingSlotInput;
	private MachineItemStackHandler chargingSlotOutput;
	private CombinerItemStackHandlerMachine chargingSlots;
	
	private MachineFluidTank leftTank;
	private MachineFluidTank rightTank;
	
	private MachineEnergyStorage energy;
	private int energyBuffer = 0;
	
	private boolean working = false;
	
	@SideOnly(Side.CLIENT)
	private boolean wasWorking;
	@SideOnly(Side.CLIENT)
	private int lastEnergy;
	
	private final IOConfigHandlerMachine ioConfigHandler;
	
	public TileEntityThemoelectricGenerator() {
		super();
		this.ioConfigHandler = new IOConfigHandlerMachine(this, Arrays.asList(IOState.INPUT, IOState.OUTPUT, IOState.LEFT_TANK, IOState.RIGHT_TANK), TooManyOresAPI.itemIoType, TooManyOresAPI.fluidIoType, TooManyOresAPI.energyIoType);
		
		this.buckets = new SyncedItemHandler(this, 2) {
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				if(!stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
					return stack;
				}
				return super.insertItem(slot, stack, simulate);
			}
		};
		
		this.chargingSlotInput = new MachineItemStackHandler(this, 1, true, false) {
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				if(!stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
					return stack;
				}
				return super.insertItem(slot, stack, simulate);
			}
		};
		
		this.chargingSlotOutput = new MachineItemStackHandler(this, 1, false, true) {
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				return stack;
			}
		};
		
		this.chargingSlots = new CombinerItemStackHandlerMachine(chargingSlotInput, chargingSlotOutput);
		
		this.leftTank = new MachineFluidTank(this, CAPACITY, true, false);
		this.rightTank = new MachineFluidTank(this, CAPACITY, true, false);
		
		this.energy = new MachineEnergyStorage(this, 100000, true);
	}
	
	@Override
	protected IOStatesBuilder getIOStatesBuilder() {
		
		IOStatesBuilder builder = new IOStatesBuilder();
		
		builder.addPair(TooManyOresAPI.itemIoType, Pair.of(IOState.INPUT, this.chargingSlotInput));
		builder.addPair(TooManyOresAPI.itemIoType, Pair.of(IOState.OUTPUT, this.chargingSlotOutput));
		builder.addPair(TooManyOresAPI.itemIoType, Pair.of(IOState.ALL, chargingSlots));
		
		builder.addPair(TooManyOresAPI.fluidIoType, Pair.of(IOState.LEFT_TANK, this.leftTank));
		builder.addPair(TooManyOresAPI.fluidIoType, Pair.of(IOState.RIGHT_TANK, this.rightTank));
		builder.addPair(TooManyOresAPI.fluidIoType, Pair.of(IOState.ALL, new CombinedFluidHandlerMachine(this.leftTank, this.leftTank)));
		
		builder.addPair(TooManyOresAPI.energyIoType, Pair.of(IOState.OUTPUT, this.energy));
		
		return builder;
	}
	
	@Override
	public IOConfigHandlerMachine getIoConfigHandler() {
		return this.ioConfigHandler;
	}

	@Override
	public IItemHandler getInventory() {
		return this.chargingSlots;
	}
	
	public IItemHandler getTankBucketSlots() {
		return this.buckets;
	}
	
	public FluidTank getLeftTank() {
		return this.leftTank;
	}
	
	public FluidTank getRightTank() {
		return this.rightTank;
	}
	
	public IEnergyStorage getEnergyStorage() {
		return this.energy;
	}
	
	public boolean isMachineWorking() {
		return this.working;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("charging_slot_input", this.chargingSlotInput.serializeNBT());
		compound.setTag("charging_slot_output", this.chargingSlotOutput.serializeNBT());
		compound.setTag("left_tank", this.leftTank.writeToNBT(new NBTTagCompound()));
		compound.setTag("right_tank", this.rightTank.writeToNBT(new NBTTagCompound()));
		compound.setTag("energy", this.energy.serializeNBT());
		compound.setBoolean("working", this.working);
		if(this.energyBuffer > 0) {
			compound.setInteger("energy_buffer", this.energyBuffer);
		}
		NBTTagCompound tag = super.writeToNBT(compound);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.chargingSlotInput.deserializeNBT(compound.getCompoundTag("charging_slot_input"));
		this.chargingSlotOutput.deserializeNBT(compound.getCompoundTag("charging_slot_output"));
		this.leftTank.readFromNBT(compound.getCompoundTag("left_tank"));
		this.rightTank.readFromNBT(compound.getCompoundTag("right_tank"));
		this.lastEnergy = this.energy.getEnergyStored();
		this.energy.deserializeNBT(compound.getInteger("energy"));
		this.working = compound.getBoolean("working");
		if(compound.hasKey("energy_buffer", Constants.NBT.TAG_INT)) {
			this.energyBuffer = compound.getInteger("energy_buffer");
		} else {
			this.energyBuffer = 0;
		}
		
		super.readFromNBT(compound);
	}
	
	@Override
	public void update() {
		super.update();
		if(this.hasWorld()) {
			if(!this.world.isRemote) {
				
				boolean shouldSync = false;
				this.working = false;
				boolean w = this.working;
				if(energy.getEnergyStored() < energy.getMaxEnergyStored()) {
					if(this.energyBuffer > 0) {
						this.energyBuffer -= energy.receiveEnergy(this.energyBuffer, false);
						if(this.energyBuffer <= 0 && energy.getEnergyStored() < energy.getMaxEnergyStored()) {
							generatePower();
						}
						this.working = true;
						shouldSync = true;
					} else {
						this.working = generatePower();
						shouldSync = this.working;
					}
				}
				
				if(energy.getEnergyStored() >= energy.getMaxEnergyStored()) {
					this.working = false;
					shouldSync = true;
				}
				
				ItemStack stackToCharge = chargingSlotInput.getStackInSlot(0);
				if(stackToCharge != ItemStack.EMPTY && stackToCharge.hasCapability(CapabilityEnergy.ENERGY, null)) {
					IEnergyStorage h = stackToCharge.getCapability(CapabilityEnergy.ENERGY, null);

					int max = h.getMaxEnergyStored();
					int e = h.receiveEnergy(max, true);
					
					if(e > 0) {
						int energyToAdd = Math.min(energy.extractEnergy(max, true), e);
						h.receiveEnergy(energy.extractEnergy(energyToAdd, false), false);
						shouldSync = true;
					}
					else if(chargingSlotOutput.getStackInSlot(0) == ItemStack.EMPTY) {
						chargingSlotOutput.setStackInSlot(0, stackToCharge);
						chargingSlotInput.setStackInSlot(0, ItemStack.EMPTY);
						shouldSync = true;
					}
				}
				
				for(int i=0,s=buckets.getSlots();i<s;i++) {
					ItemStack bucketStack = buckets.getStackInSlot(i);
					if(bucketStack != ItemStack.EMPTY && bucketStack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
						MachineFluidTank tank = i == 0? leftTank : rightTank;
						if(tank.getFluidAmount() < tank.getCapacity()) {
							IFluidHandlerItem h = bucketStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
							int toDrain = tank.fill(h.drain(Fluid.BUCKET_VOLUME, false), false);
							buckets.setStackInSlot(i, h.getContainer());
							shouldSync = tank.fill(h.drain(toDrain, true), true) > 0? true : shouldSync;
						}
					}
				}
				
				if(shouldSync || w != this.working) {
					sync();
				}
			} else {
				if(this.working ^ this.wasWorking){
					IBlockState state = world.getBlockState(pos);
					this.world.notifyBlockUpdate(pos, state, state.getActualState(world, pos), 3);
					this.wasWorking = this.working;
				}
				if(!this.working) {
					this.lastEnergy = this.energy.getEnergyStored();
				}
			}
		}
	}
	
	protected boolean generatePower() {
		FluidStack leftStack = leftTank.getFluid();
		FluidStack rightStack = rightTank.getFluid();
		if(leftStack != null && rightStack != null) {
			
			EnumMachineCasingVariant casing = this.world.getBlockState(this.pos).getValue(BlockMachine.CASING);
			
			int p = calcultatePower(leftStack, rightStack, casing);
			if (p > 0) {
				p -= energy.receiveEnergy(p, false);
				this.energyBuffer = p;

				int d = getFluidConsumption(casing);
				leftTank.drain(d, true);
				rightTank.drain(d, true);
				
				return leftTank.getFluidAmount() > 0 && rightTank.getFluidAmount()> 0;
			}
		}
		return false;
	}
	
	public static int calcultatePower(@Nonnull FluidStack fs1,@Nonnull FluidStack fs2, TileEntityThemoelectricGenerator te) {
		return calcultatePower(fs1, fs2, te.world.getBlockState(te.pos).getValue(BlockMachine.CASING));
	}
	
	public static int calcultatePower(@Nonnull FluidStack fs1,@Nonnull FluidStack fs2, EnumMachineCasingVariant casing) {
		double delta = Math.abs(fs1.getFluid().getTemperature(fs1) - fs2.getFluid().getTemperature(fs2));
		
		if(delta > 100) {
			//power = 7*LN(0.01delta)^2
			//frozium + ignum => 70 RF/t 
			//lava + water => 37 RF/t
			int basePower = delta < 0 ? 0 : (int) Math.round(7.0D*Math.pow(Math.log(0.01D*delta),2));
			return (Math.round(basePower * getPowerBonus(casing)));
		}
		return 0;
	}
	
	public static float getPowerBonus(EnumMachineCasingVariant casing) {
		return casing == EnumMachineCasingVariant.IMPROVED? 1.3F : 1.0F;
	}
	 
	public static int getFluidConsumption(TileEntityThemoelectricGenerator te) {
		return getFluidConsumption(te.world.getBlockState(te.pos).getValue(BlockMachine.CASING));
	}
	
	public static int getFluidConsumption(EnumMachineCasingVariant casing) {
		return casing == EnumMachineCasingVariant.BASIC? 2 : 1;
	}
	
	@SideOnly(Side.CLIENT)
	public int getEnergyRate() {
		return this.energy.getEnergyStored() - this.lastEnergy;
	}
	
	@Override
	protected IBlockNames getBlockNames() {
		return EnumBlocksNames.THERMOELECTRIC_GENERATOR;
	}
	
	@Override
	public void emptyTank(short tankId) {
		if(tankId == 0) {
			this.leftTank.drain(CAPACITY, true);
		} else if(tankId == 1) {
			this.rightTank.drain(CAPACITY, true);
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(facing == null && capability == CapabilityEnergy.ENERGY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(facing == null && capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energy);
		}
		return super.getCapability(capability, facing);
	}

}
