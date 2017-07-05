package misterpemodder.tmo.main.compat.jei.endermatter;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiIngredientGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.compat.jei.DrawableItemStack;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.items.ItemVariant.IngotVariant;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RecipeCategoryEnderMatter extends BlankRecipeCategory<RecipeWrapperEnderMatter> {
	
	private final String localizedName;
	public static final String UID = "tmo.enderMatter";
	
	public static final int X_OFFSET = 7;
	public static final int Y_OFFSET = 33;
	public final static ResourceLocation LOCATION = new ResourceLocationTmo("textures/gui/container/destabilizer_main.png");

	private final IDrawable background;
	private final IDrawable slot;
	private final IDrawable bar;
	private final IDrawable enderMatterIngotIcon;
	
	@Override
	public String getUid() {
		return UID;
	}
	
	public RecipeCategoryEnderMatter(IGuiHelper helper) {
		this.localizedName = Tmo.proxy.translate("gui.jei.category.enderMatter");
		this.background = helper.createBlankDrawable(148, 45);
		this.slot = helper.createDrawable(LOCATION, 11, 35, 26, 26, 256, 128);
		this.bar = helper.createDrawable(LOCATION, 11, 82, 140, 8, 256, 128);
		this.enderMatterIngotIcon = new DrawableItemStack(ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.ENDER_MATTER_INGOT));
	}

	@Override
	public String getTitle() {
		return this.localizedName;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		this.slot.draw(minecraft, 11-X_OFFSET, 37-Y_OFFSET);
		this.bar.draw(minecraft, 11-X_OFFSET, 66-Y_OFFSET);
	}
	
	@Override
	public IDrawable getIcon() {
		return this.enderMatterIngotIcon;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperEnderMatter recipeWrapper,IIngredients ingredients) {
		recipeLayout.setRecipeTransferButton(138-X_OFFSET, 51-Y_OFFSET);
		
		IGuiIngredientGroup<ItemStack> guiItemStacks = recipeLayout.getIngredientsGroup(ItemStack.class);
		
		guiItemStacks.init(0, true, 15-X_OFFSET, 42-Y_OFFSET);
		List<List<ItemStack>> itemInputs = ingredients.getInputs(ItemStack.class);
		List<ItemStack> itemInput = itemInputs.isEmpty()? null : itemInputs.get(0);
		guiItemStacks.set(0, itemInput);
	}

}
