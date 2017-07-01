package misterpemodder.tmo.main.capability.item;

import misterpemodder.tmo.main.capability.HandlerContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

public class HandlerContainerItem extends HandlerContainer<IItemHandler> implements IItemHandler{

	public HandlerContainerItem() {
		this(EmptyHandler.INSTANCE);
	}
	
	public HandlerContainerItem(IItemHandler handler) {
		super(handler);
	}
	
	@Override
	public void setEmptyHandler() {
		this.containedHandler = EmptyHandler.INSTANCE;
	}

	@Override
	public int getSlots() {
		return this.containedHandler.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.containedHandler.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return this.containedHandler.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return this.containedHandler.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) {
		return this.containedHandler.getSlotLimit(slot);
	}

}
