package misterpemodder.tmo.client.gui;

import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFiltered extends SlotHidable {
	
	public NonNullList<ItemStack> allowedItems;
	private boolean ignoreMeta = false;

	public SlotFiltered(IItemHandler itemHandler, int index, int xPosition, int yPosition, NonNullList<ItemStack> allowedItems, boolean enabled) {
		super(itemHandler, index, xPosition, yPosition, enabled);
		this.allowedItems = allowedItems;
	}
	
	public SlotFiltered(IItemHandler itemHandler, int index, int xPosition, int yPosition, NonNullList<ItemStack> allowedItems, boolean enabled, boolean ignoreMeta) {
		this(itemHandler, index, xPosition, yPosition, allowedItems, enabled);
		this.ignoreMeta = ignoreMeta;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		for(ItemStack st : allowedItems) {
			if(ignoreMeta? st.getItem() == stack.getItem() : st.isItemEqual(stack)) {
				return super.isItemValid(stack);
			}
		}
		return false;
	}

}
