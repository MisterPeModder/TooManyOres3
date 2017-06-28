package misterpemodder.tmo.main.tileentity;

import org.apache.commons.lang3.tuple.Pair;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.io.EnumIOType;
import misterpemodder.tmo.api.recipe.IDestabilizerRecipe;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.blocks.containers.BlockInjector;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.capability.IOConfigHandlerMachine;
import misterpemodder.tmo.main.capability.SyncedFluidTank;
import misterpemodder.tmo.main.capability.SyncedItemHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityDestabilizer extends TileEntityMachine<IDestabilizerRecipe> {
	
	private ItemStackHandler ender;
	private ItemStackHandler input;
	private FluidTank tank;
	private int enderMatterAmount;
	
	public static final int MAX_ENDER_MATTER = 1000;
	
	private final IOConfigHandlerMachine ioConfigHandler;
	
	public TileEntityDestabilizer() {
		super();
		this.input = new SyncedItemHandler(this,1);
		this.ender = new SyncedItemHandler(this, 1) {
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				if(!TooManyOresAPI.methodHandler.isEnderMatterItem(stack)) {
					return stack;
				}
				return super.insertItem(slot, stack, simulate);
			}
			
		};
		this.tank = new SyncedFluidTank(CAPACITY);
		this.ioConfigHandler = new IOConfigHandlerMachine(this, EnumIOType.ITEM, EnumIOType.FLUID, EnumIOType.ENDER_MATTER);
	}
	
	@Override
	public IOConfigHandlerMachine getIoConfigHandler() {
		return this.ioConfigHandler;
	}
	
	public ItemStackHandler getEnder() {
		return this.ender;
	}

	@Override
	public ItemStackHandler getInventory() {
		return this.input;
	}
	
	public FluidTank getTank() {
		return this.tank;
	}
	
	public int getEnderMatterAmount() {
		return this.enderMatterAmount;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", this.input.serializeNBT());
		compound.setTag("ender", this.ender.serializeNBT());
		compound.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
		compound.setInteger("ender_matter_amount", this.enderMatterAmount);
		NBTTagCompound tag = super.writeToNBT(compound);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.input.deserializeNBT(compound.getCompoundTag("inventory"));
		this.ender.deserializeNBT(compound.getCompoundTag("ender"));
		this.tank.readFromNBT(compound.getCompoundTag("tank"));
		this.enderMatterAmount = compound.getInteger("ender_matter_amount");
		super.readFromNBT(compound);
	}
	
	protected boolean isValidSide(EnumFacing side) {
		EnumFacing facing;
		if(this.hasWorld() && (facing = this.world.getBlockState(this.pos).getValue(BlockInjector.FACING)) != null) {
			return !(side == facing);
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
				return (T) (facing == EnumFacing.UP? this.input : this.ender);
			}
			else if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
				return (T) this.tank;
			}
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	protected IForgeRegistry<IDestabilizerRecipe> getRecipeRegistry() {
		return TooManyOresAPI.registryHandler.getCrystalDestabilizerRecipesRegistry();
	}
	
	@Override
	protected IDestabilizerRecipe findRecipe() {
		ItemStack s = this.input.getStackInSlot(0).copy();
		for(IDestabilizerRecipe r : getRecipeRegistry().getValues()) {
			if(r.isValid(s)) {
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
				if(currentRecipe.isValid(stack.copy()) && this.enderMatterAmount >= currentRecipe.getEnderMaterNeeded()) {
					if(progress >= currentRecipe.getTotalTime()) {
						Pair<ItemStack, FluidStack> p = currentRecipe.onFinish(stack, new FluidTank(tank.getFluid() == null? null : tank.getFluid().copy(), tank.getCapacity()));
						if(this.tank.fill(p.getRight(), false) == p.getRight().amount) {				
							this.tank.fill(p.getRight(), true);
							this.input.setStackInSlot(0, p.getLeft());
							this.enderMatterAmount -= currentRecipe.getEnderMaterNeeded();
							currentRecipe = null;
							progress = 0;
							sync();
						}
					}
					else {
						progress++;
					}
				} else {
					currentRecipe = null;
					progress = 0;
				}
			}
			
			ItemStack enderStack = this.ender.getStackInSlot(0).copy();
			if(TooManyOresAPI.methodHandler.isEnderMatterItem(enderStack) && this.enderMatterAmount < MAX_ENDER_MATTER) {
				Pair<Integer, Integer> p = TooManyOresAPI.methodHandler.getEnderMatterValue(enderStack);
				if(p.getLeft() != 0 && this.enderMatterAmount + p.getLeft() <= MAX_ENDER_MATTER) {
					this.enderMatterAmount += p.getLeft();
					enderStack.shrink(p.getRight());
					this.ender.setStackInSlot(0, enderStack);
				}
			}
			
			if(this.tank.getFluid() != null) {
				IBlockState state = world.getBlockState(this.pos);
				for(EnumFacing side : EnumFacing.VALUES) {
					if(side != state.getValue(BlockMachine.FACING)) {
						TileEntity te = world.getTileEntity(pos.offset(side));
						if(te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite())) {
							IFluidHandler h = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
							
							int amount = h.fill(this.tank.drain(TileEntityDestabilizer.CAPACITY, false), false);
							h.fill(this.tank.drain(amount, true), true);
							
							if(this.tank.getFluidAmount() == 0) break;
						}
					}
				}
			}
			
		}
	}
	
	@Override
	protected IBlockNames getBlockNames() {
		return EnumBlocksNames.CRYSTAL_DESTABILIZER;
	}
	
	@Override
	public void emptyTank(short tankId) {
		if(tankId == 0) this.tank.drain(TileEntityDestabilizer.CAPACITY, true);
	}
	
}
