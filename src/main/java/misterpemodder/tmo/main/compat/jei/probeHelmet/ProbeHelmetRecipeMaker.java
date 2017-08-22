package misterpemodder.tmo.main.compat.jei.probeHelmet;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredientRegistry;
import misterpemodder.tmo.main.compat.jei.RecipeMaker;
import misterpemodder.tmo.main.items.ItemTmoArmor;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ProbeHelmetRecipeMaker extends RecipeMaker<ProbeHelmetRecipeWrapper> {

	@Override
	public List<ProbeHelmetRecipeWrapper> makeRecipe(IIngredientRegistry ingredientRegistry) {
		List<ProbeHelmetRecipeWrapper> recipes = new ArrayList<>();
		List<ItemStack> ingredients = ingredientRegistry.getIngredients(ItemStack.class);
		
		for(ItemStack stack : ingredients) {
			if(stack.getItem() instanceof ItemTmoArmor && (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(TMORefs.TOP_TAG))) {
            	if(((ItemTmoArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD) {
            		recipes.add(new ProbeHelmetRecipeWrapper(stack));
            	}
            }
		}
		
		return recipes;
	}
	
}
