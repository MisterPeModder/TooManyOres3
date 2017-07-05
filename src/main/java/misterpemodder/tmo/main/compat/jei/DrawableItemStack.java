package misterpemodder.tmo.main.compat.jei;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

public class DrawableItemStack implements IDrawable {
	
	public static IIngredientRenderer<ItemStack> renderer;
	private final ItemStack stack;
	
	public DrawableItemStack(ItemStack stack) {
		this.stack = stack;
	}

	@Override
	public int getWidth() {
		return 16;
	}

	@Override
	public int getHeight() {
		return 16;
	}

	@Override
	public void draw(Minecraft minecraft) {
		this.draw(minecraft, 0, 0);
	}

	@Override
	public void draw(Minecraft minecraft, int xOffset, int yOffset) {
		GlStateManager.enableDepth();
		renderer.render(minecraft, xOffset, yOffset, this.stack);
		GlStateManager.enableAlpha();
		GlStateManager.disableDepth();
	}

}
