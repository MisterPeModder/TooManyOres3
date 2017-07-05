package misterpemodder.tmo.main.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.capability.io.IIOType;
import misterpemodder.tmo.api.recipe.IMachineRecipe;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.BlockMachineCasing.EnumMachineCasingVariant;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.capability.HandlerContainer;
import misterpemodder.tmo.main.capability.IMachineElementHandler;
import misterpemodder.tmo.main.capability.fluid.HandlerContainerFluid;
import misterpemodder.tmo.main.capability.io.CapabilityIOConfig;
import misterpemodder.tmo.main.capability.io.IOConfigHandlerMachine;
import misterpemodder.tmo.main.capability.io.IOState;
import misterpemodder.tmo.main.capability.item.HandlerContainerItem;
import misterpemodder.tmo.main.utils.EnumBlockSide;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class TileEntityMachine<V extends IMachineRecipe<V>> extends TileEntityContainerBase implements ITickable{
	
	public static final int CAPACITY = 8000;
	protected int progress;
	protected V currentRecipe;
	
	private ImmutableListMultimap<IIOType<?>, Pair<IOState, IMachineElementHandler<?>>> elementsForIoStates;
	private ImmutableListMultimap<IIOType<?>, IOState> ioTypeToIoStates;
	private ListMultimap<EnumFacing, Pair<Capability<?>, HandlerContainer<?>>> handlerContainers = ArrayListMultimap.create();
	
	public boolean autoPush;
	public boolean autoPull;
	
	public int getProgress() {
		return this.progress;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	@Nullable
	public V getCurrentRecipe() {
		return this.currentRecipe;
	}
	
	protected abstract IForgeRegistry<V> getRecipeRegistry();
	
	public abstract IOConfigHandlerMachine getIoConfigHandler();
	
	@Nonnull
	protected abstract IOStatesBuilder getIOStatesBuilder();
	
	public final int getChangedCraftingTime(IMachineRecipe<?> recipe) {
		if(this.hasWorld()) {
			EnumMachineCasingVariant casing = this.world.getBlockState(this.pos).getValue(BlockMachine.CASING);
			if(casing != null) {
				return (int) (recipe.getTotalTime() * casing.speedModifier);
			}
		}
		return recipe.getTotalTime();
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	private <T> HandlerContainer<T> getHandlerContainerForCapability(Capability<T> capability) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (HandlerContainer<T>) new HandlerContainerItem();
		} else if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return (HandlerContainer<T>) new HandlerContainerFluid();
		}
		return null;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if(this.currentRecipe != null) {
			compound.setString("current_recipe", this.currentRecipe.getRegistryName().toString());
		}
		compound.setBoolean("auto_push", autoPush);
		compound.setBoolean("auto_pull", autoPull);
		compound.setInteger("progress", this.progress);
		compound.setTag("io_config", getIoConfigHandler().serializeNBT());
		NBTTagCompound tag = super.writeToNBT(compound);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if(compound.hasKey("current_recipe")) {
			this.currentRecipe = getRecipeRegistry().getValue(new ResourceLocation(compound.getString("current_recipe")));
		}
		if(compound.hasKey("io_config")) {
			getIoConfigHandler().deserializeNBT((NBTTagList)compound.getTag("io_config"));			
		}
		if(compound.hasKey("auto_push")) {
			autoPush = compound.getBoolean("auto_push");
		}
		if(compound.hasKey("auto_pull")) {
			autoPull = compound.getBoolean("auto_pull");
		}
		this.progress = compound.getInteger("progress");
		super.readFromNBT(compound);
	}
	
	@Override
	public void onInvOpen(EntityPlayer player) {}

	@Override
	public void onInvClose(EntityPlayer player) {}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		this.sync();
	}
	
	@Nullable
	protected abstract V findRecipe();
	
	protected abstract IBlockNames getBlockNames();
	
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(TextFormatting.getTextWithoutFormattingCodes(Tmo.proxy.translate("tile."+getBlockNames().getUnlocalizedName()+".name")));
	}
	
	public void emptyTank(short tankId) {
		
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		if(oldState.getBlock() == newState.getBlock() && newState.getBlock() instanceof BlockMachine) {
			boolean flag1 = oldState.getValue(BlockMachine.CASING) == newState.getValue(BlockMachine.CASING);
			boolean flag2 = oldState.getValue(BlockMachine.FACING) == newState.getValue(BlockMachine.FACING);
			return flag1 && flag2;
		}
		return true;
	}
	
	@Override
	public void update() {
		if(this.hasWorld() && !this.world.isRemote && (autoPull || autoPush)) {
			IOConfigHandlerMachine h = this.getIoConfigHandler();
			if(h != null) {
				for(EnumFacing facing : EnumFacing.values()) {
					if(!isSideDisabled(facing)) {
						TileEntity te = this.world.getTileEntity(this.pos.offset(facing));
						if(te != null) {
							IIOType<?>[] types = h.getIOTypes(facing);
							if(types != null && types.length > 0) {
								for(IIOType<?> type : types) {
									IOState state = h.getIOStateConfig(facing, type);
									if(state != IOState.DISABLED) {
										Capability<?> cap = type.getCapabilityInstance();
										if(this.hasCapability(cap, facing) && te.hasCapability(cap, facing.getOpposite())) {
											handleAutoPushPull(cap, facing, te);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void handleAutoPushPull(Capability<?> cap, EnumFacing facing, TileEntity otherTileEntity) {
		Object handler = this.getCapability(cap, facing);
		Object otherHandler = otherTileEntity.getCapability(cap, facing.getOpposite());
		
		boolean b1 = autoPush;
		boolean b2 = autoPull;
		
		if(handler == null || otherHandler == null) return;
		
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			IItemHandler h = (IItemHandler) handler;
			IItemHandler oh = (IItemHandler) otherHandler;
			
			int s = h.getSlots();
			int os = oh.getSlots();
			
			if(s > 0 && os > 0) {
				while(b1 || b2) {
					IItemHandler h1;
					IItemHandler h2;
					int s1;
					int s2;
					
					if(b1) {
						h1 = h;
						h2 = oh;
						s1 = s;
						s2 = os;
						b1 = false;
					} else {
						h1 = oh;
						h2 = h;
						s1 = os;
						s2 = s;
						b2 = false;
					}
					
					int itemCount = 0;
					for(int i=0;i<s1;i++) {
						ItemStack stack = h1.extractItem(i, h1.getSlotLimit(i), true);
						if(stack.isEmpty()) continue;
						if(itemCount >= 2) break;
						for(int j=0;j<s2;j++) {
							ItemStack otherStack = h2.insertItem(j, stack, true);
							int toInsert = stack.getCount() - otherStack.getCount();
							if(toInsert <= 0) continue;
							itemCount += toInsert;
							if(h2.insertItem(j, h1.extractItem(i, Math.min(toInsert, 2), false), false).getCount() > 0) {
								stack = h1.extractItem(i, h1.getSlotLimit(i), true);
							}
							if(itemCount >= 2) break;
						}
					}
					
				}
			}
			
		}
		else if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			IFluidHandler h = (IFluidHandler) handler;
			IFluidHandler oh = (IFluidHandler) otherHandler;
			
			while(b1 || b2) {
				IFluidHandler h1;
				IFluidHandler h2;
				
				if(b1) {
					h1 = h;
					h2 = oh;
					b1 = false;
				} else {
					h1 = oh;
					h2 = h;
					b2 = false;
				}
				
				FluidStack fs1 = h1.drain(100, false);
				
				if(fs1 != null) {
					int toFill = h2.fill(fs1, false);
					if(toFill > 0) {
						h2.fill(h1.drain(toFill, true), true);
					}
				}
			}
		}
	}
	
	public EnumBlockSide[] getDisabledSides() {
		return new EnumBlockSide[]{EnumBlockSide.FRONT};
	}
	
	public final boolean isSideDisabled(EnumBlockSide side) {
		EnumBlockSide[] disabledSides = this.getDisabledSides();
		if(disabledSides != null && disabledSides.length > 0) {
			for(EnumBlockSide d : disabledSides) {
				if(side == d) {
					return true;
				}
			}
		}
		return false;
	}
	
	public final boolean isSideDisabled(EnumFacing side) {
		EnumFacing facing = null;
		if(this.hasWorld()) {
			facing = this.getWorld().getBlockState(this.getPos()).getValue(BlockMachine.FACING);
		}
		return isSideDisabled(EnumBlockSide.fromFacing(facing == null? EnumFacing.NORTH : facing, side));
	}
	
	public ListMultimap<IIOType<?>, Pair<IOState, IMachineElementHandler<?>>> getElementsForIOType() {
		if(this.elementsForIoStates == null) {
			this.elementsForIoStates = getIOStatesBuilder().build();
		}
		return this.elementsForIoStates;
	}
	
	public ListMultimap<IIOType<?>, IOState> getIOStatesForIOType() {
		if(ioTypeToIoStates == null) {
			ImmutableListMultimap.Builder<IIOType<?>, IOState> builder = new ImmutableListMultimap.Builder<>();
			
			ListMultimap<IIOType<?>, Pair<IOState, IMachineElementHandler<?>>> elements = getElementsForIOType();
			if(!elements.isEmpty()) {
				for(IIOType<?> key : elements.keySet()) {
					List<IOState> list = new ArrayList<>();
					elements.get(key).forEach(p -> list.add(p.getLeft()));
					builder.putAll(key, list);
				}
			}
			
			ioTypeToIoStates = builder.build();
		}
		return ioTypeToIoStates;
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	private <T> HandlerContainer<T> getHandlderContainer(Capability<T> capability, EnumFacing facing) {
		if(!handlerContainers.containsKey(facing)) {
			HandlerContainer<T> hc = getHandlerContainerForCapability(capability);
			if(hc == null) return null;
			handlerContainers.put(facing, Pair.of(capability, hc));
			return hc;
		} else {
			List<Pair<Capability<?>, HandlerContainer<?>>> list = handlerContainers.get(facing);
			
			for(Pair<Capability<?>, HandlerContainer<?>> p : list) {
				if(p.getLeft() == capability) {
					return (HandlerContainer<T>) p.getRight();
				}
			}
			
			HandlerContainer<T> hc = getHandlerContainerForCapability(capability);
			if(hc == null) return null;
			list.add(Pair.of(capability, hc));
			return hc;
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityIOConfig.CONFIG_HANLDER_CAPABILITY) {
			return true;
		}
		else if(facing != null){
			IIOType<?> type = TooManyOresAPI.methodHandler.getIOTypeForCapability(capability);
			return (type != null && getIoConfigHandler().isIOTypeValid(type)) || super.hasCapability(capability, facing);
		}
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityIOConfig.CONFIG_HANLDER_CAPABILITY) {
			return CapabilityIOConfig.CONFIG_HANLDER_CAPABILITY.cast(getIoConfigHandler());
		}
		else if(facing != null) {
			IIOType<T> type = TooManyOresAPI.methodHandler.getIOTypeForCapability(capability);
			if(type != null) {
				IOConfigHandlerMachine h = getIoConfigHandler();
				
				if(h.isIOTypeValid(type)) {
					
					T c = updateHandlerContainers(type, facing, h);
					
					if(c != null) {
						return c;
					}
				}
			}
		}
		else if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.getInventory());
		}
		
		return super.getCapability(capability, facing);
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	public <T> T updateHandlerContainers(IIOType<T> type, EnumFacing facing, IOConfigHandlerMachine h) {
		HandlerContainer<T> hc = getHandlderContainer(type.getCapabilityInstance(), facing);
		if(hc != null) {
			if(h.hasIOType(facing, type)) {
				IOState state = h.getIOStateConfig(facing, type);
				if(state != null && state != IOState.DISABLED) {
					ListMultimap<IIOType<?>, Pair<IOState, IMachineElementHandler<?>>> map = getElementsForIOType();
					if(map.containsKey(type)) {
						for(Pair<IOState, IMachineElementHandler<?>> p : map.get(type)) {
							if(p.getLeft() == state) {
								hc.setHandler((T)p.getRight().getSideHandler(facing));
								break;
							}
						}
					}
				} else if(state == IOState.DISABLED) {
					hc.setEmptyHandler();
				}
			} else {
				hc.setEmptyHandler();
			}
			return (T) hc;
		}
		return null;
	}
	
	protected static class IOStatesBuilder {
		
		private ListMultimap<IIOType<?>, Pair<IOState, IMachineElementHandler<?>>> map = ArrayListMultimap.create();
		
		protected void addPair(IIOType<?> key, Pair<IOState, IMachineElementHandler<?>> pair) {
			if(map.containsKey(key)) {
				map.get(key).add(pair);
			} else {
				map.put(key, pair);
			}
		}
		
		protected ImmutableListMultimap<IIOType<?>, Pair<IOState, IMachineElementHandler<?>>> build() {
			return ImmutableListMultimap.copyOf(map);
		}
		
	}

}
