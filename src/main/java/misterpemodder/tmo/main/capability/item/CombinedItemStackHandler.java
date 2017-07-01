package misterpemodder.tmo.main.capability.item;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class CombinedItemStackHandler implements IItemHandler {
	
	protected final ImmutableList<IItemHandler> handlers;
	
	public CombinedItemStackHandler(IItemHandler first, IItemHandler... handlers) {
		ImmutableList.Builder<IItemHandler> builder = new ImmutableList.Builder<>();
		builder.add(first);
		builder.add(handlers);
		this.handlers = builder.build();
	}
	
	public ImmutableList<IItemHandler> getItemHandlers() {
		return this.handlers;
	}
	
	@Override
	public int getSlots() {
		int s = 0;
		for(IItemHandler h : handlers) {
			s += h.getSlots();
		}
		return s;
	}
	
	public IItemHandler getHandlerForSlot(int slot) {
		int totalSlots = 0;
		for(IItemHandler h : handlers) {
			totalSlots += h.getSlots();
			if(slot < totalSlots) {
				return h;
			}
		}
		return handlers.get(0);
	}

	public int getSlotForHandler(int slot) {
		int t = getSlots();
		for(int i=handlers.size()-1;i>=0;i--) {
			t -= handlers.get(i).getSlots();
			if(slot >= t) {
				return slot-t;
			}
		}
		return slot;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return getHandlerForSlot(slot).getStackInSlot(getSlotForHandler(slot));
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return getHandlerForSlot(slot).insertItem(getSlotForHandler(slot), stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return getHandlerForSlot(slot).extractItem(getSlotForHandler(slot), amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		return getHandlerForSlot(slot).getSlotLimit(getSlotForHandler(slot));
	}

}
