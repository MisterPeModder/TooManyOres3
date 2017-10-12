package misterpemodder.tmo.main.compat.jei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import misterpemodder.hc.main.blocks.IHexianBlock;
import misterpemodder.hc.main.items.IHexianItem;
import misterpemodder.hc.main.utils.ItemStackUtils;
import misterpemodder.hc.main.utils.StringUtils;
import misterpemodder.tmo.main.blocks.BlockMachineCasing.EnumMachineCasingVariant;
import misterpemodder.tmo.main.compat.jei.destabilizer.RecipeCategoryDestabilizer;
import misterpemodder.tmo.main.compat.jei.destabilizer.RecipeHandlerDestabilizer;
import misterpemodder.tmo.main.compat.jei.destabilizer.RecipeMakerDestabilizer;
import misterpemodder.tmo.main.compat.jei.destabilizer.RecipeTransferInfoDestabilizer;
import misterpemodder.tmo.main.compat.jei.dust.RecipeCategoryDustCrushing;
import misterpemodder.tmo.main.compat.jei.dust.RecipeHandlerDustCrushing;
import misterpemodder.tmo.main.compat.jei.dust.RecipeMakerDustCrushing;
import misterpemodder.tmo.main.compat.jei.endermatter.RecipeCategoryEnderMatter;
import misterpemodder.tmo.main.compat.jei.endermatter.RecipeHandlerEnderMatter;
import misterpemodder.tmo.main.compat.jei.endermatter.RecipeMakerEnderMatter;
import misterpemodder.tmo.main.compat.jei.injector.AdvancedGuiHandlerInjector;
import misterpemodder.tmo.main.compat.jei.injector.RecipeCategoryInjector;
import misterpemodder.tmo.main.compat.jei.injector.RecipeHandlerInjector;
import misterpemodder.tmo.main.compat.jei.injector.RecipeMakerInjector;
import misterpemodder.tmo.main.compat.jei.probeHelmet.ProbeHelmetRecipeHandler;
import misterpemodder.tmo.main.compat.jei.probeHelmet.ProbeHelmetRecipeMaker;
import misterpemodder.tmo.main.compat.top.TheOneProbeCompat;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.inventory.ContainerDestabilizer;
import misterpemodder.tmo.main.inventory.ContainerInjector;
import misterpemodder.tmo.main.inventory.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.inventory.ContainerTitaniumChest;
import misterpemodder.tmo.main.items.TMOItemVariants.LockVariant;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

@JEIPlugin
//TODO Remove use of deprecated methods
public class JeiPlugin implements IModPlugin {
	
	
	private List<RecipeMaker<? extends IRecipeWrapper>> recipeMakers = new ArrayList<>();

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {}

	@Override
	public void register(IModRegistry modRegistry) {
		
		IIngredientBlacklist blacklist = modRegistry.getJeiHelpers().getIngredientBlacklist();
		blacklist.addIngredientToBlacklist(new ItemStack(TheItems.TAB_ICON.getItem()));
		blacklist.addIngredientToBlacklist(new ItemStack(TheBlocks.WEAK_REDSTONE_TORCH_UNLIT.getBlock()));
		
		for(ModItems.TheItems item : ModItems.TheItems.values()) {
			if(!item.getHexianItem().isEnabled()) {
				blacklist.addIngredientToBlacklist(new ItemStack(item.getItem(), 1 , OreDictionary.WILDCARD_VALUE));
			}
		}
		
		this.addDescriptions(modRegistry, modRegistry.getIngredientRegistry());
		
		if(TheOneProbeCompat.topLoaded) {
			modRegistry.addRecipeHandlers(new ProbeHelmetRecipeHandler());
			this.addRecipeMaker(new ProbeHelmetRecipeMaker());
		}
		
		this.addRecipeMaker(new RecipeMakerInjector());	
		this.addRecipeMaker(new RecipeMakerEnderMatter());
		this.addRecipeMaker(new RecipeMakerDestabilizer());
		this.addRecipeMaker(new RecipeMakerDustCrushing());
		this.makeRecipes(modRegistry);
		
		DrawableItemStack.renderer = modRegistry.getIngredientRegistry().getIngredientRenderer(ItemStack.class);
		
		IGuiHelper guiHelper = modRegistry.getJeiHelpers().getGuiHelper();
		
		modRegistry.addRecipeCategories(new RecipeCategoryInjector(guiHelper), new RecipeCategoryEnderMatter(guiHelper), new RecipeCategoryDestabilizer(guiHelper), new RecipeCategoryDustCrushing(guiHelper));
		modRegistry.addRecipeHandlers(new RecipeHandlerInjector(), new RecipeHandlerEnderMatter(), new RecipeHandlerDestabilizer(), new RecipeHandlerDustCrushing());
		
		addRecipeCategoryMachine(modRegistry, TheBlocks.INJECTOR, RecipeCategoryInjector.UID);
		addRecipeCategoryMachine(modRegistry, TheBlocks.CRYSTAL_DESTABILIZER, RecipeCategoryEnderMatter.UID, RecipeCategoryDestabilizer.UID);
		
		modRegistry.addRecipeCategoryCraftingItem(new ItemStack(TheBlocks.TITANIUM_ANVIL.getBlock()), VanillaRecipeCategoryUid.ANVIL);
		
		modRegistry.addRecipeCategoryCraftingItem(new ItemStack(Blocks.CRAFTING_TABLE), RecipeCategoryDustCrushing.UID);
		
		modRegistry.addAnvilRecipe(ItemStackUtils.newVariantStack(TheItems.LOCK, LockVariant.BASIC_BROKEN), Arrays.asList(LockVariant.BASIC_BROKEN.getRepairItem()), Arrays.asList(LockVariant.BASIC_BROKEN.getOtherVariant()));
		modRegistry.addAnvilRecipe(ItemStackUtils.newVariantStack(TheItems.LOCK, LockVariant.REINFORCED_BROKEN), Arrays.asList(LockVariant.REINFORCED_BROKEN.getRepairItem()), Arrays.asList(LockVariant.REINFORCED_BROKEN.getOtherVariant()));
		modRegistry.addAdvancedGuiHandlers(new AdvancedGuiHandlerInjector());
		
		IRecipeTransferRegistry tr = modRegistry.getRecipeTransferRegistry();
		
		tr.addRecipeTransferHandler(ContainerTitaniumChest.class, VanillaRecipeCategoryUid.CRAFTING, 109, 9, 0, 107);
		tr.addRecipeTransferHandler(ContainerTitaniumAnvil.class, VanillaRecipeCategoryUid.CRAFTING, 46, 9, 0, 40);
		tr.addRecipeTransferHandler(ContainerInjector.class, VanillaRecipeCategoryUid.CRAFTING, 44, 9, 0, 40);
		tr.addRecipeTransferHandler(ContainerDestabilizer.class, VanillaRecipeCategoryUid.CRAFTING, 44, 9, 0, 40);
		
		tr.addRecipeTransferHandler(ContainerTitaniumAnvil.class, VanillaRecipeCategoryUid.ANVIL, 42, 2, 0, 40);
		tr.addRecipeTransferHandler(ContainerInjector.class, RecipeCategoryInjector.UID, 41, 1, 0, 40);
		tr.addRecipeTransferHandler(ContainerDestabilizer.class, RecipeCategoryEnderMatter.UID, 42, 1, 0, 40);
		tr.addRecipeTransferHandler(new RecipeTransferInfoDestabilizer());
	}
	
	private void addDescriptions(IModRegistry registry, IIngredientRegistry ingredientsRegistry) {
		List<ItemStack> ingredients = ingredientsRegistry.getIngredients(ItemStack.class);
		
		for(ItemStack ingredient : ingredients) {
			if(ingredient.getItem() instanceof IHexianItem || Block.getBlockFromItem(ingredient.getItem()) instanceof IHexianBlock) {
				String unlocName = ingredient.getItem().getUnlocalizedNameInefficiently(ingredient) + TMORefs.JEI_DESC_UNLOC_NAME;
				if(I18n.hasKey(unlocName)) {
					registry.addDescription(ingredient , StringUtils.translate(unlocName));
				}
			}
		}
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}
	
	private void addRecipeMaker(RecipeMaker<?> maker) {
		this.recipeMakers.add(maker);
	}
	
	private void makeRecipes(IModRegistry registry) {
		IIngredientRegistry ingredientRegistry = registry.getIngredientRegistry();
		for(RecipeMaker<? extends IRecipeWrapper> r : recipeMakers) {
			registry.addRecipes(r.makeRecipe(ingredientRegistry));
		}
	}
	
	private void addRecipeCategoryMachine(IModRegistry registry, TheBlocks block, String... recipeCategoryUids) {
		for(EnumMachineCasingVariant v : EnumMachineCasingVariant.values()) {
			registry.addRecipeCategoryCraftingItem(ItemStackUtils.newVariantStack(block, v), recipeCategoryUids);
		}
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {}
	
}
