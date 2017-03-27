package misterpemodder.tmo.main.apiimpl.recipe;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Triple;

import misterpemodder.tmo.api.recipe.IInjectorRecipe;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class InjectorFillDrainRecipe implements IInjectorRecipe {
	
	private final TransferMode mode;
	
	public InjectorFillDrainRecipe(TransferMode mode) {
		this.mode = mode;
	}

	@Override
	public TransferMode getRecipeTransferType() {
		return this.mode;
	}

	@Override
	public String getRecipeId() {
		return TMORefs.PREFIX+(this.mode == TransferMode.INJECTION? "fill_item":"drain_item");
	}

	@Override
	public int getTotalTime() {
		return 20;
	}
	@Override
	public boolean isValid(@Nonnull FluidTank tank, ItemStack stack) {
		stack.setCount(1);
		if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			IFluidHandlerItem handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			FluidStack r = null;
			if(this.mode == TransferMode.INJECTION && tank.getFluidAmount() > 0) {
				r = FluidUtil.tryFluidTransfer(handler, tank, Fluid.BUCKET_VOLUME, false);
			}
			else if(this.mode == TransferMode.EXTRACTION && !(tank.getFluidAmount() >= tank.getCapacity())) {
				r = FluidUtil.tryFluidTransfer(tank, handler, Fluid.BUCKET_VOLUME, false);
			}
			
			return r != null && r.amount >= 0;
		}
		return false;
	}

	@Override
	public Triple<FluidStack, ItemStack,ItemStack> onFinish(@Nonnull FluidTank tankIn, ItemStack itemStackIn, ItemStack itemStackOut) {
		ItemStack itemStackIn2 = itemStackIn.copy();
		itemStackIn.setCount(1);
		IFluidHandlerItem handler = itemStackIn.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if(this.mode == TransferMode.INJECTION) {
			FluidStack transfered = FluidUtil.tryFluidTransfer(handler, tankIn, Fluid.BUCKET_VOLUME, true);
			if(handler.fill(new FluidStack(transfered.getFluid(), Fluid.BUCKET_VOLUME), false) <= 0) {
				itemStackIn2.shrink(1);
				return Triple.of(tankIn.getFluid(), itemStackIn2, handler.getContainer());
			}
		}
		else {
			FluidUtil.tryFluidTransfer(tankIn, handler, Fluid.BUCKET_VOLUME, true);
			FluidStack f = handler.drain(Fluid.BUCKET_VOLUME, false);
			if(f == null || f.amount == 0) {
				itemStackIn2.shrink(1);
				return Triple.of(tankIn.getFluid(), itemStackIn2, handler.getContainer());
			}
		}
		return Triple.of(tankIn.getFluid(), handler.getContainer(), ItemStack.EMPTY);
	}

}
