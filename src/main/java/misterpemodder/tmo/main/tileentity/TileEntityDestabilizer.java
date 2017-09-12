package misterpemodder.tmo.main.tileentity;

import java.util.Arrays;

import org.apache.commons.lang3.tuple.Pair;

import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.recipe.IDestabilizerRecipe;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.capability.fluid.MachineFluidTank;
import misterpemodder.tmo.main.capability.io.IOConfigHandlerMachine;
import misterpemodder.tmo.main.capability.io.IOState;
import misterpemodder.tmo.main.capability.item.CombinerItemStackHandlerMachine;
import misterpemodder.tmo.main.capability.item.MachineItemStackHandler;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityDestabilizer extends TileEntityCraftingMachine<IDestabilizerRecipe> {
	
	private MachineItemStackHandler ender;
	private MachineItemStackHandler input;
	private MachineFluidTank tank;
	private int enderMatterAmount;
	
	public static final int MAX_ENDER_MATTER = 1000;
	
	private final IOConfigHandlerMachine ioConfigHandler;
	
	@SideOnly(Side.CLIENT)
	private boolean hadEnderMatter;
	
	public TileEntityDestabilizer() {
		super();
		this.ioConfigHandler = new IOConfigHandlerMachine(this, Arrays.asList(IOState.ENDER_MATTER), TooManyOresAPI.itemIoType, TooManyOresAPI.fluidIoType);
		this.input = new MachineItemStackHandler(this, 1, true, false);
		this.ender = new MachineItemStackHandler(this, 1, true, false) {
			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				if(!TooManyOresAPI.methodHandler.isEnderMatterItem(stack)) {
					return stack;
				}
				return super.insertItem(slot, stack, simulate);
			}
			
		};
		this.tank = new MachineFluidTank(this, CAPACITY, false, true);
	}
	
	@Override
	protected IOStatesBuilder getIOStatesBuilder() {
		
		IOStatesBuilder builder = new IOStatesBuilder();
		
		builder.addPair(TooManyOresAPI.itemIoType, Pair.of(IOState.INPUT, this.input));
		builder.addPair(TooManyOresAPI.itemIoType, Pair.of(IOState.ENDER_MATTER, this.ender));
		builder.addPair(TooManyOresAPI.itemIoType, Pair.of(IOState.ALL, new CombinerItemStackHandlerMachine(this.ender, this.input)));
		
		builder.addPair(TooManyOresAPI.fluidIoType, Pair.of(IOState.OUTPUT, this.tank));
		
		return builder;
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
		super.update();
		if(this.hasWorld()) {
			if(!this.world.isRemote) {
				ItemStack stack = this.input.getStackInSlot(0).copy();
				if(currentRecipe == null) {
					currentRecipe = findRecipe();
				}
				if(currentRecipe != null) {
					if(currentRecipe.isValid(stack.copy()) && this.enderMatterAmount >= currentRecipe.getEnderMaterNeeded()) {
						if(progress >= this.getChangedCraftingTime(currentRecipe)) {
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
			} else if(enderMatterAmount > 0 ^ hadEnderMatter) {
				IBlockState state = world.getBlockState(pos);
				this.world.notifyBlockUpdate(pos, state, state.getActualState(world, pos), 3);
				hadEnderMatter = enderMatterAmount > 0;
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
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(facing == null && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(facing == null && capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this.tank);
		}
		return super.getCapability(capability, facing);
	}
	
}
