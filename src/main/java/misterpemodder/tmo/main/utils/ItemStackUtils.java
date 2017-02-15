package misterpemodder.tmo.main.utils;

import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.items.ItemMulti;
import misterpemodder.tmo.main.items.ItemVariant;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ItemStackUtils {
	
	public static <V extends ItemVariant> ItemStack newVariantStack(ItemMulti<V> item, V variant) {
		return ItemStackUtils.newVariantStack(item, variant, 1);
	}
	
	public static <V extends ItemVariant> ItemStack newVariantStack(ItemMulti<V> item, V variant, int amount) {
		return new ItemStack(item, amount, variant.getMeta());
	}
	
	public static  ItemStack newVariantStack(ModItems.TheItems item, ItemVariant variant) {
		return ItemStackUtils.newVariantStack(item, variant, 1);
	}
	
	public static ItemStack newVariantStack(ModItems.TheItems item, ItemVariant variant, int amount) {
		Item i = item.getItem();
		if(i instanceof ItemMulti && ((ItemMulti)i).getVariants().get(0).getClass().equals(variant.getClass())) {
			return newVariantStack((ItemMulti)i, variant, amount);
		} else {
			return new ItemStack(i, amount, 0);
		}
	}
	
}
