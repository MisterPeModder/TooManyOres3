package misterpemodder.tmo.main.client.gui;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.fml.client.config.HoverChecker;

/**
 * This class is a copy of {@link mezz.jei.gui.recipes.RecipeClickableArea} from JEI,
 * This has been done to avoid crash issues when JEI is not present,
 * I do not claim this class as my own.
 */
public class RecipeClickableAreaTMO extends HoverChecker {
	private final List<String> recipeCategoryUids;

	public RecipeClickableAreaTMO(int top, int bottom, int left, int right, String... recipeCategoryUids) {
		super(top, bottom, left, right, 0);
		this.recipeCategoryUids = Arrays.asList(recipeCategoryUids);
	}

	public List<String> getRecipeCategoryUids() {
		return recipeCategoryUids;
	}
}
