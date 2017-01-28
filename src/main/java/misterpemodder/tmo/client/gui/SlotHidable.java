package misterpemodder.tmo.client.gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotHidable extends SlotItemHandler {
	
	public boolean enabled;

	public SlotHidable(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean enabled) {
		super(itemHandler, index, xPosition, yPosition);
		this.enabled = enabled;
	}
	
	@Override
	public boolean canBeHovered() {
		return enabled;
	}

}
