package misterpemodder.tmo.main.compat.jei.destabilizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import misterpemodder.tmo.main.client.gui.ContainerDestabilizer;
import net.minecraft.inventory.Slot;

public class RecipeTransferInfoDestabilizer implements IRecipeTransferInfo<ContainerDestabilizer> {

	@Override
	public Class<ContainerDestabilizer> getContainerClass() {
		return ContainerDestabilizer.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return RecipeCategoryDestabilizer.UID;
	}

	@Override
	public boolean canHandle(ContainerDestabilizer container) {
		return true;
	}

	@Override
	public List<Slot> getRecipeSlots(ContainerDestabilizer container) {
		return Arrays.asList(container.getSlot(42), container.getSlot(41));
	}

	@Override
	public List<Slot> getInventorySlots(ContainerDestabilizer container) {
		List<Slot> slots = new ArrayList<>();
		for(int i=0; i<=40; i++) {
			slots.add(container.getSlot(i));
		}
		return slots;
	}

}
