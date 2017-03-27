package misterpemodder.tmo.main.items;

import misterpemodder.tmo.main.capability.FluidHandlerItemStackMulti;
import misterpemodder.tmo.main.config.ConfigValues;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemTitaniumCan extends FluidContainerItem {

	public ItemTitaniumCan() {
		super(EnumItemsNames.TITANIUM_CAN);
		this.setMaxStackSize(16);
	}
	
	@Override
	public int getCapacity() {
		return (int)Math.ceil(ConfigValues.IntValues.BUCKET_CAPACITY.currentValue/2);
	}
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new FluidHandlerItemStackMulti(stack, getCapacity());
	}

}
