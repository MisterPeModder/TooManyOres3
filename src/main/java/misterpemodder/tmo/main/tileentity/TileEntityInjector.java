package misterpemodder.tmo.main.tileentity;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.recipe.IInjectorRecipe;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.blocks.containers.BlockInjector;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.capability.fluid.MachineFluidTank;
import misterpemodder.tmo.main.capability.io.IOConfigHandlerMachine;
import misterpemodder.tmo.main.capability.io.IOState;
import misterpemodder.tmo.main.capability.item.CombinerItemStackHandlerMachine;
import misterpemodder.tmo.main.capability.item.MachineItemStackHandler;
import misterpemodder.tmo.main.utils.EnumBlockSide;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityInjector extends TileEntityMachine<IInjectorRecipe> {
	
	public MachineItemStackHandler input;
	public MachineItemStackHandler output;
	public MachineFluidTank tank;
	private TransferMode mode;
	
	private final IOConfigHandlerMachine ioConfigHandler;
	
	public TileEntityInjector() {
		super();
		this.ioConfigHandler = new IOConfigHandlerMachine(this, null, TooManyOresAPI.itemIoType, TooManyOresAPI.fluidIoType);
		this.input = new MachineItemStackHandler(this, 1, true, false);
		this.output = new MachineItemStackHandler(this, 1, false, true);
		
		this.tank = new MachineFluidTank(this, CAPACITY);
		this.mode = TransferMode.INJECTION;
	}
	
	@Override
	protected IOStatesBuilder getIOStatesBuilder() {
		
		IOStatesBuilder builder = new IOStatesBuilder();
		
		builder.addPair(TooManyOresAPI.itemIoType, Pair.of(IOState.INPUT, this.input));
		builder.addPair(TooManyOresAPI.itemIoType, Pair.of(IOState.OUTPUT, this.output));
		builder.addPair(TooManyOresAPI.itemIoType, Pair.of(IOState.ALL, new CombinerItemStackHandlerMachine(this.input, this.output)));
		
		builder.addPair(TooManyOresAPI.fluidIoType, Pair.of(IOState.ALL, this.tank));
		
		return builder;
	}
	
	@Override
	public IOConfigHandlerMachine getIoConfigHandler() {
		return this.ioConfigHandler;
	}

	@Override
	public ItemStackHandler getInventory() {
		return this.input;
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
		super.update();
		if(this.hasWorld() && !this.world.isRemote) {
			ItemStack stack = this.input.getStackInSlot(0).copy();
			if(currentRecipe == null) {
				currentRecipe = findRecipe();
			}
			if(currentRecipe != null) {
				
				if(currentRecipe.getRecipeTransferType() == this.mode && currentRecipe.isValid(new FluidTank(tank.getFluid(), tank.getCapacity()), stack.copy())) {
					if(progress >= this.getChangedCraftingTime(currentRecipe)) {
						Triple<FluidStack, ItemStack,ItemStack> p = currentRecipe.onFinish(new FluidTank(tank.getFluid() == null? null : tank.getFluid().copy(), tank.getCapacity()), stack, this.output.getStackInSlot(0).copy());
						ItemStack testStack = this.output.insertItem(0, p.getRight(), true);
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
	
	public EnumBlockSide[] getDisabledSides() {
		return new EnumBlockSide[]{EnumBlockSide.FRONT, EnumBlockSide.UP};
	}
	
}
