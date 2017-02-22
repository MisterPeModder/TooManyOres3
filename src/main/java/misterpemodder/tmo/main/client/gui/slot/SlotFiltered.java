package misterpemodder.tmo.main.client.gui.slot;

import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

//TODO this class has been heavily modified. remove commented code later.
public class SlotFiltered extends SlotHidable {
	
	private Predicate<ItemStack> filter;

//	public SlotFiltered(IItemHandler itemHandler, int index, int xPosition, int yPosition, NonNullList<ItemStack> allowedItems, boolean enabled) {
//		super(itemHandler, index, xPosition, yPosition, enabled);
//		this.allowedItems = allowedItems;
//		Predicate<ItemStack> t = new Predicate<ItemStack>() {
//
//			@Override
//			public boolean test(ItemStack t) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		};
//	}
//	
//	public SlotFiltered(IItemHandler itemHandler, int index, int xPosition, int yPosition, NonNullList<ItemStack> allowedItems, boolean enabled, boolean ignoreMeta) {
//		this(itemHandler, index, xPosition, yPosition, allowedItems, enabled);
//		this.ignoreMeta = ignoreMeta;
//	}
	
	public SlotFiltered(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean enabled, Predicate<ItemStack> filter) {
		super(itemHandler, index, xPosition, yPosition, enabled);
		this.filter = filter;
	}
	
//	@Override
//	public boolean isItemValid(ItemStack stack) {
//		for(ItemStack st : allowedItems) {
//			if(ignoreMeta? st.getItem() == stack.getItem() : st.isItemEqual(stack)) {
//				return super.isItemValid(stack);
//			}
//		}
//		return false;
//	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return filter.test(stack)? super.isItemValid(stack) : false;
	}

}
