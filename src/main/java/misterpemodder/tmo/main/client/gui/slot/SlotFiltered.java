package misterpemodder.tmo.main.client.gui.slot;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class SlotFiltered extends SlotHidable {
	
	private Predicate<ItemStack> filter;
	
	public SlotFiltered(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean enabled, Predicate<ItemStack> filter) {
		super(itemHandler, index, xPosition, yPosition, enabled);
		this.filter = filter;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return filter.test(stack)? super.isItemValid(stack) : false;
	}

}
