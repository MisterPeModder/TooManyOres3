package misterpemodder.tmo.main.compat.jei.probeHelmet;

import java.util.Arrays;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;


public class ProbeHelmetRecipeWrapper implements IRecipeWrapper {
	
	private final List<ItemStack> inputs;
	private final ItemStack output;
	
	public ProbeHelmetRecipeWrapper(ItemStack helmetStack) {
		ItemStack probeStack = new ItemStack(Item.getByNameOrId("theoneprobe:probe"));
		
		this.inputs = Arrays.asList(helmetStack, probeStack);
		
		NBTTagCompound compound = new NBTTagCompound();
		compound.setInteger(TMORefs.TOP_TAG, 1);
		
		output = helmetStack.copy();
		output.setTagCompound(compound);
		
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, this.inputs);
		ingredients.setOutput(ItemStack.class, this.output);
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return null;
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}

}
