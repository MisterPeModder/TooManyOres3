package misterpemodder.tmo.main.compat.jei;

import java.util.List;

import mezz.jei.api.IItemBlacklist;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import misterpemodder.tmo.Tmo;
import misterpemodder.tmo.main.blocks.base.BlockTMO;
import misterpemodder.tmo.main.init.ModBlocks;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.items.base.TMOItem;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {}

	@Override
	public void register(IModRegistry registry) {
		//Hiding the 'tmo tab item'
		IItemBlacklist blacklist = registry.getJeiHelpers().getItemBlacklist();
		blacklist.addItemToBlacklist(new ItemStack(ModItems.Items.TAB_ICON.getItem()));
		blacklist.addItemToBlacklist(new ItemStack(ModBlocks.Blocks.TEST_BLOCK.getBlock().getItemBlock()));
		blacklist.addItemToBlacklist(new ItemStack(ModBlocks.Blocks.KEEPER_BLOCK.getBlock().getItemBlock()));
		
		for(ModItems.Items item : ModItems.Items.values()) {
			if(!item.getTMOItem().isEnabled()) {
				blacklist.addItemToBlacklist(new ItemStack(item.getItem(), 1 , OreDictionary.WILDCARD_VALUE));
			}
		}
		
		addDescriptions(registry, registry.getIngredientRegistry());
		
		if(TMOHelper.topLoaded) {
			registry.addRecipeHandlers(new ProbeHelmetRecipeHandler());
			
			registry.addRecipes(ProbeHelmetRecipeMaker.getProbeRecipes(registry.getIngredientRegistry()));
			
		}
		
	}
	
	private void addDescriptions(IModRegistry registry, IIngredientRegistry ingredientsRegistry) {
		List<ItemStack> ingredients = ingredientsRegistry.getIngredients(ItemStack.class);
		
		for(ItemStack ingredient : ingredients) {
			if(ingredient.getItem() instanceof TMOItem || Block.getBlockFromItem(ingredient.getItem()) instanceof BlockTMO) {
				String unlocName = ingredient.getItem().getUnlocalizedNameInefficiently(ingredient) + TMOHelper.JEI_DESC_UNLOC_NAME;
				if(I18n.hasKey(unlocName)) {
					registry.addDescription(ingredient , Tmo.proxy.translate(unlocName));
				}
			}
		}
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}
	
}
