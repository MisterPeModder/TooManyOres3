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
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.capability.HandlerContainer;
import misterpemodder.tmo.main.capability.IMachineElementHandler;
import misterpemodder.tmo.main.capability.fluid.HandlerContainerFluid;
import misterpemodder.tmo.main.capability.io.CapabilityIOConfig;
import misterpemodder.tmo.main.capability.io.IOConfigHandlerMachine;
import misterpemodder.tmo.main.capability.io.IOState;
import misterpemodder.tmo.main.capability.item.HandlerContainerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

public abstract class TileEntityMachine<V extends IMachineRecipe<V>> extends TileEntityContainerBase implements ITickable{
	
	public static final int CAPACITY = 8000;
	protected int progress;
	protected V currentRecipe;
	
	private ImmutableListMultimap<IIOType<?>, Pair<IOState, IMachineElementHandler<?>>> elementsForIoStates;
	private ImmutableListMultimap<IIOType<?>, IOState> ioTypeToIoStates;
	private ListMultimap<EnumFacing, Pair<Capability<?>, HandlerContainer<?>>> handlerContainers = ArrayListMultimap.create();
	
	public int getProgress() {
		return this.progress;
	}
	
	@Nullable
	public V getCurrentRecipe() {
		return this.currentRecipe;
	}
	
	protected abstract IForgeRegistry<V> getRecipeRegistry();
	
	public abstract IOConfigHandlerMachine getIoConfigHandler();
	
	@Nonnull
	protected abstract IOStatesBuilder getIOStatesBuilder();
	
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
