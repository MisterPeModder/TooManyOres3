package misterpemodder.tmo.main.compat.jei;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredientRegistry;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.items.ItemTmoArmor;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public final class ProbeHelmetRecipeMaker {
	
	public static List<ProbeHelmetRecipeWrapper> getProbeRecipes(IIngredientRegistry ingredientRegistry) {
		List<ProbeHelmetRecipeWrapper> recipes = new ArrayList<>();
		List<ItemStack> ingredients = ingredientRegistry.getIngredients(ItemStack.class);
		
		for(ItemStack stack : ingredients) {
			if(stack.getItem() instanceof ItemTmoArmor && (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(TMOHelper.TOP_TAG))) {
            	if(((ItemTmoArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD) {
            		recipes.add(new ProbeHelmetRecipeWrapper(stack));
            	}
            }
		}
		
		return recipes;
	}
	
}
