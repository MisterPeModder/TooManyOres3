package misterpemodder.tmo.main.compat.jei.endermatter;

import java.awt.Color;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import misterpemodder.hc.main.utils.StringUtils;
import misterpemodder.tmo.main.inventory.elements.ContainerElementEnderMatterBar;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

public class RecipeWrapperEnderMatter extends BlankRecipeWrapper {
	
	public final ItemStack stack;
	public final int value;
	
	public static final ContainerElementEnderMatterBar BAR = new ContainerElementEnderMatterBar();
	
	public RecipeWrapperEnderMatter(ItemStack stack, int value) {
		this.stack = stack;
		this.value = value;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, stack);
		ingredients.setOutput(ItemStack.class, stack);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		
		int p = (value*140)/TileEntityDestabilizer.MAX_ENDER_MATTER;
		BAR.drawBar(11-RecipeCategoryEnderMatter.X_OFFSET, 66-RecipeCategoryEnderMatter.Y_OFFSET, p<=0? 2:p);
	
		String str = StringUtils.translate("gui.jei.category.enderMatter.value")+": ";
		FontRenderer fr = minecraft.fontRendererObj;
		
		int x = 40-RecipeCategoryEnderMatter.X_OFFSET;
		int y = 40-RecipeCategoryEnderMatter.Y_OFFSET;
		
		fr.drawString(str, x, y, 0);
		fr.drawString(String.valueOf(value), x+fr.getStringWidth(str), y, Color.CYAN.getRGB());
	}

}
