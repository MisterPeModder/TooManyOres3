package misterpemodder.tmo.main.compat.jei.dust;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.plugins.vanilla.crafting.CraftingRecipeCategory;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.compat.jei.DrawableItemStack;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RecipeCategoryDustCrushing extends CraftingRecipeCategory {
	
	public static final String UID = "tmo.crafting.crushing";
	public static final ResourceLocation LOCATION = new ResourceLocationTmo("textures/gui/container/injector_main.png");
	
	private final String localizedName;
	private final IDrawable hammerIcon;

	public RecipeCategoryDustCrushing(IGuiHelper guiHelper) {
		super(guiHelper);
		this.localizedName = Tmo.proxy.translate("gui.jei.category.crafting.crushing");
		this.hammerIcon = new DrawableItemStack(new ItemStack(TheItems.HAMMER_TITANIUM.getItem()));
	}
	
	@Override
	public String getUid() {
		return UID;
	}
	
	@Override
	public String getTitle() {
		return this.localizedName;
	}
	
	@Override
	public IDrawable getIcon() {
		return this.hammerIcon;
	}

}
