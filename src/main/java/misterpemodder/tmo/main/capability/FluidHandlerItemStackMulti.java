package misterpemodder.tmo.main.capability;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class FluidHandlerItemStackMulti extends FluidHandlerItemStack {

	public FluidHandlerItemStackMulti(ItemStack container, int capacity) {
		super(container, capacity*container.getItem().getItemStackLimit(container));
	}
	
	@Override
    public int fill(FluidStack resource, boolean doFill) {
        if (container.getCount() <= 0 || resource == null || resource.amount <= 0 || !canFillFluidType(resource)) {
            return 0;
        }

        FluidStack contained = getFluid();
        if (contained == null) {
            int fillAmount = Math.min(capacity, resource.amount);

            if (doFill) {
                FluidStack filled = resource.copy();
                filled.amount = fillAmount;
                setFluid(filled);
            }

            return fillAmount;
        }
        else {
            if (contained.isFluidEqual(resource)) {
                int fillAmount = Math.min(capacity - contained.amount, resource.amount);

                if (doFill && fillAmount > 0) {
                    contained.amount += fillAmount;
                    setFluid(contained);
                }

                return fillAmount;
            }

            return 0;
        }
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (container.getCount() <= 0 || resource == null || resource.amount <= 0 || !resource.isFluidEqual(getFluid())) {
            return null;
        }
        return drain(resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (container.getCount() <= 0 || maxDrain <= 0) {
            return null;
        }

        FluidStack contained = getFluid();
        if (contained == null || contained.amount <= 0 || !canDrainFluidType(contained)) {
            return null;
        }

        final int drainAmount = Math.min(contained.amount, maxDrain);

        FluidStack drained = contained.copy();
        drained.amount = drainAmount;

        if (doDrain) {
            contained.amount -= drainAmount;
            if (contained.amount == 0) {
                setContainerToEmpty();
            }
            else {
                setFluid(contained);
            }
        }

        return drained;
    }

}
