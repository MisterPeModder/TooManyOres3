package misterpemodder.tmo.main.compat.jei;

import java.util.Arrays;
import java.util.List;

import mezz.jei.api.IItemBlacklist;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.base.BlockTMO;
import misterpemodder.tmo.main.client.gui.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.client.gui.ContainerTitaniumChest;
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

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {}

	@Override
	public void register(IModRegistry registry) {
		//Hiding the 'tmo tab item'
		IItemBlacklist blacklist = registry.getJeiHelpers().getItemBlacklist();
		blacklist.addItemToBlacklist(new ItemStack(ModItems.TheItems.TAB_ICON.getItem()));
		
		for(ModItems.TheItems item : ModItems.TheItems.values()) {
			if(!item.getTMOItem().isEnabled()) {
				blacklist.addItemToBlacklist(new ItemStack(item.getItem(), 1 , OreDictionary.WILDCARD_VALUE));
			}
		}
		
		addDescriptions(registry, registry.getIngredientRegistry());
		
		if(TMORefs.topLoaded) {
			registry.addRecipeHandlers(new ProbeHelmetRecipeHandler());
			
			registry.addRecipes(ProbeHelmetRecipeMaker.getProbeRecipes(registry.getIngredientRegistry()));
			
		}
		
		registry.addRecipeCategoryCraftingItem(new ItemStack(TheBlocks.TITANIUM_ANVIL.getBlock()), VanillaRecipeCategoryUid.ANVIL);
		registry.addAnvilRecipe(ItemStackUtils.newVariantStack(TheItems.LOCK, LockVariant.BASIC_BROKEN), Arrays.asList(LockVariant.BASIC_BROKEN.getRepairItem()), Arrays.asList(LockVariant.BASIC_BROKEN.getOtherVariant()));
		registry.addAnvilRecipe(ItemStackUtils.newVariantStack(TheItems.LOCK, LockVariant.REINFORCED_BROKEN), Arrays.asList(LockVariant.REINFORCED_BROKEN.getRepairItem()), Arrays.asList(LockVariant.REINFORCED_BROKEN.getOtherVariant()));
		
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerTitaniumChest.class, VanillaRecipeCategoryUid.CRAFTING, 109, 9, 0, 107);
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerTitaniumAnvil.class, VanillaRecipeCategoryUid.CRAFTING, 46, 9, 0, 40);
		registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerTitaniumAnvil.class, VanillaRecipeCategoryUid.ANVIL, 42, 2, 0, 40);
	}
	
	private void addDescriptions(IModRegistry registry, IIngredientRegistry ingredientsRegistry) {
		List<ItemStack> ingredients = ingredientsRegistry.getIngredients(ItemStack.class);
		
		for(ItemStack ingredient : ingredients) {
			if(ingredient.getItem() instanceof ITMOItem || Block.getBlockFromItem(ingredient.getItem()) instanceof BlockTMO) {
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
	
}
