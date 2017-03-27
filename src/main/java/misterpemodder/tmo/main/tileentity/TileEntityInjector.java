package misterpemodder.tmo.main.tileentity;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Triple;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.recipe.IInjectorRecipe;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.containers.BlockInjector;
import misterpemodder.tmo.main.capability.ComparatorSyncedItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityInjector extends TileEntityContainerBase implements ITickable{
	
	public static final int CAPACITY = 8000;
	public static final int TRANSFER_RATE = 50;
	private int progress;
	private IInjectorRecipe currentRecipe;
	
	private ItemStackHandler input;
	private ItemStackHandler output;
	private FluidTank tank;
	private TransferMode mode;
	
	public TileEntityInjector() {
		super();
		this.input = new ComparatorSyncedItemHandler(this,1);
		this.output = new ComparatorSyncedItemHandler(this, 1);
		this.tank = new FluidTank(CAPACITY);
		this.mode = TransferMode.INJECTION;
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
	
	public int getProgress() {
		return this.progress;
	}
	
	@Nullable
	public IInjectorRecipe getCurrentRecipe() {
		return this.currentRecipe;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", this.input.serializeNBT());
		compound.setTag("output", this.output.serializeNBT());
		compound.setTag("tank", this.tank.writeToNBT(new NBTTagCompound()));
		compound.setInteger("mode", this.mode.ordinal());
		if(this.currentRecipe != null) {
			compound.setString("current_recipe", this.currentRecipe.getRecipeId());
		}
		compound.setInteger("progress", this.progress);
		NBTTagCompound tag = super.writeToNBT(compound);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.input.deserializeNBT(compound.getCompoundTag("inventory"));
		this.output.deserializeNBT(compound.getCompoundTag("output"));
		this.tank.readFromNBT(compound.getCompoundTag("tank"));
		this.mode = TransferMode.values()[compound.getInteger("mode")];
		if(compound.hasKey("current_recipe")) {
			this.currentRecipe = getRecipeFromId(compound.getString("current_recipe"));
		}
		super.readFromNBT(compound);
	}
	
	private IInjectorRecipe getRecipeFromId(String id) {
		for(IInjectorRecipe r : TooManyOresAPI.INJECTOR_RECIPES) {
			if(id == r.getRecipeId()) {
				return r;
			}
		}
		return null;
	}

	@Override
	public void onInvOpen(EntityPlayer player) {}

	@Override
	public void onInvClose(EntityPlayer player) {}
	
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
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		this.sync();
	}
	
	private IInjectorRecipe findRecipe() {
		ItemStack s = this.input.getStackInSlot(0);
		for(IInjectorRecipe r : TooManyOresAPI.INJECTOR_RECIPES) {
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
	public ITextComponent getDisplayName() {
		return new TextComponentString(TextFormatting.getTextWithoutFormattingCodes(Tmo.proxy.translate("tile.blockInjector.name")));
	}
}
