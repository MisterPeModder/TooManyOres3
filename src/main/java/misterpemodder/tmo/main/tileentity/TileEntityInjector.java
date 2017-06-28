package misterpemodder.tmo.main.tileentity;

import org.apache.commons.lang3.tuple.Triple;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.io.EnumIOType;
import misterpemodder.tmo.api.recipe.IInjectorRecipe;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.blocks.containers.BlockInjector;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.capability.IOConfigHandlerMachine;
import misterpemodder.tmo.main.capability.SyncedFluidTank;
import misterpemodder.tmo.main.capability.SyncedItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityInjector extends TileEntityMachine<IInjectorRecipe> {
	
	private ItemStackHandler input;
	private ItemStackHandler output;
	private FluidTank tank;
	private TransferMode mode;
	
	private final IOConfigHandlerMachine ioConfigHandler;
	
	public TileEntityInjector() {
		super();
		this.input = new SyncedItemHandler(this,1);
		this.output = new SyncedItemHandler(this, 1);
		this.tank = new SyncedFluidTank(CAPACITY);
		this.mode = TransferMode.INJECTION;
		this.ioConfigHandler = new IOConfigHandlerMachine(this, EnumIOType.ITEM, EnumIOType.FLUID);
	}
	
	@Override
	public IOConfigHandlerMachine getIoConfigHandler() {
		return this.ioConfigHandler;
	}

	@Override
	public ItemStackHandler getInventory() {
		return this.input;
	}
	
	public ItemStackHandler getOutput() {
		return this.output;
	}
	
	public FluidTank getTank() {
		return this.tank;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", this.input.serializeNBT());
		compound.setTag("output", this.output.serializeNBT());
		compound.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
		compound.setInteger("mode", this.mode.ordinal());
		NBTTagCompound tag = super.writeToNBT(compound);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.input.deserializeNBT(compound.getCompoundTag("inventory"));
		this.output.deserializeNBT(compound.getCompoundTag("output"));
		this.tank.readFromNBT(compound.getCompoundTag("tank"));
		this.mode = TransferMode.values()[compound.getInteger("mode")];
		super.readFromNBT(compound);
	}

	public TransferMode getTransferMode() {
		return this.mode;
	}
	
	public void setTransferMode(TransferMode mode) {
		this.mode = mode;
		this.sync();
	}
	
	protected boolean isValidSide(EnumFacing side) {
		EnumFacing facing;
		if(this.hasWorld() && (facing = this.world.getBlockState(this.pos).getValue(BlockInjector.FACING)) != null) {
			return !(side == facing || side == EnumFacing.UP);
		}
		return false;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(isValidSide(facing)) {
			if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
				return true;
			}
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(hasCapability(capability, facing)) {
			if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
				return (T) (facing == EnumFacing.DOWN? this.output : this.input);
			}
			else if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
				return (T) this.tank;
			}
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	protected IForgeRegistry<IInjectorRecipe> getRecipeRegistry() {
		return TooManyOresAPI.registryHandler.getInjectorRecipesRegistry();
	}
	
	@Override
	protected IInjectorRecipe findRecipe() {
		ItemStack s = this.input.getStackInSlot(0).copy();
		for(IInjectorRecipe r : getRecipeRegistry().getValues()) {
			if(r.getRecipeTransferType() == this.mode && r.isValid(this.tank, s.copy())) {
				return r;
			}
		}
		return null;
	}
	
	@Override
	public void update() {
		if(!this.world.isRemote) {
			ItemStack stack = this.input.getStackInSlot(0).copy();
			if(currentRecipe == null) {
				currentRecipe = findRecipe();
			}
			if(currentRecipe != null) {
				
				if(currentRecipe.getRecipeTransferType() == this.mode && currentRecipe.isValid(new FluidTank(tank.getFluid(), tank.getCapacity()), stack.copy())) {
					if(progress >= currentRecipe.getTotalTime()) {
						Triple<FluidStack, ItemStack,ItemStack> p = currentRecipe.onFinish(new FluidTank(tank.getFluid() == null? null : tank.getFluid().copy(), tank.getCapacity()), stack, this.output.getStackInSlot(0).copy());
						ItemStack testStack = output.insertItem(0, p.getRight(), true);
						if(testStack == ItemStack.EMPTY) {
							this.tank.setFluid(p.getLeft());
							this.input.setStackInSlot(0, p.getMiddle());
							this.output.insertItem(0, p.getRight(), false);
							currentRecipe = null;
							progress = 0;
							sync();
						}
					} else {
					progress++;
					}
				} else {
					currentRecipe = null;
					progress = 0;
				}
			}
		}
		
	}
	
	@Override
	protected IBlockNames getBlockNames() {
		return EnumBlocksNames.INJECTOR;
	}
	
	@Override
	public void emptyTank(short tankId) {
		if(tankId == 0) this.tank.drain(TileEntityInjector.CAPACITY, true);
	}
	
}
