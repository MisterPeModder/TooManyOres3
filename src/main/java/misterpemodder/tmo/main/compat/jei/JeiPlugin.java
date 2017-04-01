package misterpemodder.tmo.main.compat.jei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.base.IBlockTMO;
import misterpemodder.tmo.main.client.gui.ContainerInjector;
import misterpemodder.tmo.main.client.gui.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.client.gui.ContainerTitaniumChest;
import misterpemodder.tmo.main.compat.jei.injector.AdvancedGuiHandlerInjector;
import misterpemodder.tmo.main.compat.jei.injector.InjectorRecipeMaker;
import misterpemodder.tmo.main.compat.jei.injector.RecipeCategoryInjector;
import misterpemodder.tmo.main.compat.jei.injector.RecipeHandlerInjector;
import misterpemodder.tmo.main.compat.jei.probeHelmet.ProbeHelmetRecipeHandler;
import misterpemodder.tmo.main.compat.jei.probeHelmet.ProbeHelmetRecipeMaker;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.items.ItemVariant.LockVariant;
import misterpemodder.tmo.main.items.base.ITMOItem;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@JEIPlugin
public class JeiPlugin implements IModPlugin {
	
	public static IJeiRuntime jeiRuntime;
	private List<RecipeMaker> recipeMakers = new ArrayList<>();

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {}

	@Override
	public void register(IModRegistry registry) {
		//Hiding the 'tmo tab item'
		IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
		blacklist.addIngredientToBlacklist(new ItemStack(ModItems.TheItems.TAB_ICON.getItem()));
		
		for(ModItems.TheItems item : ModItems.TheItems.values()) {
			if(!item.getTMOItem().isEnabled()) {
				blacklist.addIngredientToBlacklist(new ItemStack(item.getItem(), 1 , OreDictionary.WILDCARD_VALUE));
			}
		}
		
		this.addDescriptions(registry, registry.getIngredientRegistry());
		
		if(TMORefs.topLoaded) {
			registry.addRecipeHandlers(new ProbeHelmetRecipeHandler());
			this.addRecipeMaker(new ProbeHelmetRecipeMaker());
		}
		
		this.addRecipeMaker(new InjectorRecipeMaker());	
		this.makeRecipes(registry);
		

		registry.addRecipeCategories(new RecipeCategoryInjector(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeHandlers(new RecipeHandlerInjector());
		registry.addRecipeCategoryCraftingItem(new ItemStack(TheBlocks.INJECTOR.getBlock()), RecipeCategoryInjector.UID);
		
		registry.addRecipeCategoryCraftingItem(new ItemStack(TheBlocks.TITANIUM_ANVIL.getBlock()), VanillaRecipeCategoryUid.ANVIL);
		registry.addAnvilRecipe(ItemStackUtils.newVariantStack(TheItems.LOCK, LockVariant.BASIC_BROKEN), Arrays.asList(LockVariant.BASIC_BROKEN.getRepairItem()), Arrays.asList(LockVariant.BASIC_BROKEN.getOtherVariant()));
		registry.addAnvilRecipe(ItemStackUtils.newVariantStack(TheItems.LOCK, LockVariant.REINFORCED_BROKEN), Arrays.asList(LockVariant.REINFORCED_BROKEN.getRepairItem()), Arrays.asList(LockVariant.REINFORCED_BROKEN.getOtherVariant()));
		registry.addAdvancedGuiHandlers(new AdvancedGuiHandlerInjector());
		
		IRecipeTransferRegistry tr = registry.getRecipeTransferRegistry();
		tr.addRecipeTransferHandler(ContainerTitaniumChest.class, VanillaRecipeCategoryUid.CRAFTING, 109, 9, 0, 107);
		tr.addRecipeTransferHandler(ContainerTitaniumAnvil.class, VanillaRecipeCategoryUid.CRAFTING, 46, 9, 0, 40);
		tr.addRecipeTransferHandler(ContainerInjector.class, VanillaRecipeCategoryUid.CRAFTING, 44, 9, 0, 40);
		tr.addRecipeTransferHandler(ContainerTitaniumAnvil.class, VanillaRecipeCategoryUid.ANVIL, 42, 2, 0, 40);
		tr.addRecipeTransferHandler(ContainerInjector.class, RecipeCategoryInjector.UID, 41, 1, 0, 40);
	}
	
	private void addDescriptions(IModRegistry registry, IIngredientRegistry ingredientsRegistry) {
		List<ItemStack> ingredients = ingredientsRegistry.getIngredients(ItemStack.class);
		
		for(ItemStack ingredient : ingredients) {
			if(ingredient.getItem() instanceof ITMOItem || Block.getBlockFromItem(ingredient.getItem()) instanceof IBlockTMO) {
				String unlocName = ingredient.getItem().getUnlocalizedNameInefficiently(ingredient) + TMORefs.JEI_DESC_UNLOC_NAME;
				if(I18n.hasKey(unlocName)) {
					registry.addDescription(ingredient , Tmo.proxy.translate(unlocName));
				}
			}
		}
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		JeiPlugin.jeiRuntime = jeiRuntime;
	}
	
	private void addRecipeMaker(RecipeMaker<?> maker) {
		this.recipeMakers.add(maker);
	}
	
	private void makeRecipes(IModRegistry registry) {
		IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
		for(RecipeMaker r : recipeMakers) {
			registry.addRecipes(r.makeRecipe(ingredientRegistry));
		}
	}
	
}
