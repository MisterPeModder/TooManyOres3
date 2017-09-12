package misterpemodder.tmo.main.items;

import misterpemodder.hc.main.items.ItemMulti;
import net.minecraft.item.ItemStack;

public class ItemIngot extends ItemMulti<TMOItemVariants.IngotVariant>{

	public ItemIngot() {
		super(EnumItemsNames.INGOT, TMOItemVariants.IngotVariant.ingotVariants, "_ingot");
	}
	
	public boolean isBeaconPayment(ItemStack stack) {
        return true;
    }
	
}
