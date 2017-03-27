package misterpemodder.tmo.main.items;

import javax.annotation.Nonnull;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.items.base.ItemBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public abstract class FluidContainerItem extends ItemBase {
	
	public static final String EMPTY_UNLOC_NAME = ".empty";
	public static final String FILLED_UNLOC_NAME = ".filled";

	public FluidContainerItem(EnumItemsNames names) {
		super(names);
	}
	
	@Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        FluidStack fluidStack = FluidUtil.getFluidContained(stack);
        String unloc = this.getUnlocalizedNameInefficiently(stack);
        if (fluidStack == null) {
        	return Tmo.proxy.translate(unloc + EMPTY_UNLOC_NAME);
        }

        return Tmo.proxy.translate(unloc + FILLED_UNLOC_NAME, fluidStack.amount, fluidStack.getFluid().getLocalizedName(fluidStack));
    }
	
	public abstract int getCapacity();

}
