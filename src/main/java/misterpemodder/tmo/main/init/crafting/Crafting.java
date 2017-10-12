package misterpemodder.tmo.main.init.crafting;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.hc.main.items.properties.ItemVariant;
import misterpemodder.hc.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.blocks.BlockDeco;
import misterpemodder.tmo.main.compat.top.TheOneProbeCompat;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.items.TMOItemVariants.DustVariant;
import misterpemodder.tmo.main.items.TMOItemVariants.GemVariant;
import misterpemodder.tmo.main.items.TMOItemVariants.IngotVariant;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class Crafting {

	public static void registerRecipes() {
		
		GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.TheItems.PICKAXE_TITANIUM.getItem(), "III", " S ", " S ", 'I', "ingotTitaniumBlue", 'S', "stickWood"));
		
		addStorageRecipe("ingotTitaniumBlue", "blockTitaniumBlue");
		addStorageRecipe("ingotTitaniumDark", "blockTitaniumDark");
		addStorageRecipe("ingotTitanium", "blockTitanium");
		addStorageRecipe("ingotTitanite", "blockTitanite");
		addStorageRecipe("ingotCopper", "blockCopper");
		addStorageRecipe("gemFrozium", "blockFrozium");
		addStorageRecipe("gemIgnum", "blockIgnum");
		addStorageRecipe("ingotEnderMatter", "blockEnderMatter");
		addStorageRecipe("ingotDarkanium", "blockDarkanium");
		addStorageRecipe("ingotGoldAncient", "blockGoldAncient");
		addStorageRecipe("ingotPlatinum", "blockPlatinum");
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TheBlocks.DECORATION.getBlock(), 16, BlockDeco.EnumVariant.TITANIUM_PLATING.getMeta()), "TT ", "TT ", "   ", 'T', "plateTitanium"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TheBlocks.DECORATION.getBlock(), 16, BlockDeco.EnumVariant.COPPER_DECO_BLOCK.getMeta()), "CC ", "CC ", "   ", 'C', "ingotCopper"));
		
		if(TheOneProbeCompat.topLoaded) {
			RecipeSorter.register(TMORefs.PREFIX+"proberecipe", RecipeProbe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
			GameRegistry.addRecipe(new RecipeProbe());
		}
		
		RecipeSorter.register(TMORefs.PREFIX+"dustrecipe", RecipeDustCrushing.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
		
		for(RecipeDustCrushing recipe : getCrushingRecipes()) {
			GameRegistry.addRecipe(recipe);
		}
		
		addDustSmeltingRecipe(DustVariant.TITANIUM, TheItems.INGOT, IngotVariant.TITANIUM_INGOT);
		addDustSmeltingRecipe(DustVariant.TITANIUM_POOR, TheItems.INGOT, IngotVariant.TITANIUM_INGOT_POOR);
		addDustSmeltingRecipe(DustVariant.TITANITE, TheItems.INGOT, IngotVariant.TITANITE_INGOT);
		addDustSmeltingRecipe(DustVariant.COPPER, TheItems.INGOT, IngotVariant.COPPER_INGOT);
		addDustSmeltingRecipe(DustVariant.ANCIENT_GOLD, TheItems.INGOT, IngotVariant.ANCIENT_GOLD_INGOT);
		addDustSmeltingRecipe(DustVariant.PLATINUM, TheItems.INGOT, IngotVariant.PLATINUM_INGOT);
		addDustSmeltingRecipe(DustVariant.FROZIUM, TheItems.GEM, GemVariant.FROZIUM_GEM);
		addDustSmeltingRecipe(DustVariant.IGNUM, TheItems.GEM, GemVariant.IGNUM_GEM);
		
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TheBlocks.TITANIUM_REDSTONE.getBlock(), 8), "RRR", "RTR", "RRR", 'R', new ItemStack(Items.REDSTONE), 'T', "dustTitaniumBlueClean"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TheBlocks.COPPER_REDSTONE.getBlock(), 8), "RRR", "RCR", "RRR", 'R', new ItemStack(Items.REDSTONE), 'C', "dustCopper"));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TheBlocks.STRONG_REDSTONE_BLOCK.getBlock()), "IRI", "RBR", "IRI", 'I', "ingotIron", 'R', new ItemStack(TheBlocks.COPPER_REDSTONE.getBlock()), 'B', "blockRedstone"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(TheBlocks.WEAK_REDSTONE_TORCH.getBlock()), " R ", " S ", "   ", 'R', new ItemStack(TheBlocks.TITANIUM_REDSTONE.getBlock()), 'S', "stickWood"));

	}
	
	private static void addStorageRecipe(String ressourceOreDict, String storageBlockOreDict) {
		
		NonNullList<ItemStack> ressources = OreDictionary.getOres(ressourceOreDict);
		NonNullList<ItemStack> blocks = OreDictionary.getOres(storageBlockOreDict);
		
		if(!ressources.isEmpty() && !blocks.isEmpty()) {
			GameRegistry.addRecipe(new ShapedOreRecipe(blocks.get(0), "III", "III", "III", 'I', ressourceOreDict));
			ItemStack ressource = ressources.get(0).copy();
			ressource.setCount(9);
			GameRegistry.addRecipe(new ShapelessOreRecipe(ressource, storageBlockOreDict));
		}
	}
	
	public static List<RecipeDustCrushing> getCrushingRecipes() {
		List<RecipeDustCrushing> list = new ArrayList<>();
		
		list.add(new RecipeDustCrushing("oreTitaniumBlue", "dustTitaniumBlueDirty"));
		list.add(new RecipeDustCrushing("oreTitanium", "dustTitaniumDirty"));
		list.add(new RecipeDustCrushing("oreCopper", "dustCopperDirty"));
		list.add(new RecipeDustCrushing("oreGoldAncient", "dustGoldAncientDirty"));
		list.add(new RecipeDustCrushing("orePlatinum", "dustPlatinumDirty"));
		
		list.add(new RecipeDustCrushing("ingotTitanium", "dustTitaniumClean"));
		list.add(new RecipeDustCrushing("ingotTitaniumBlue", "dustTitaniumBlue"));
		list.add(new RecipeDustCrushing("ingotTitanite", "dustTitanite"));
		list.add(new RecipeDustCrushing("ingotCopper", "dustCopper"));
		list.add(new RecipeDustCrushing("ingotGoldAncient", "dustGoldAncientClean"));
		list.add(new RecipeDustCrushing("ingotPlatinum", "dustPlatinum"));
		list.add(new RecipeDustCrushing("gemFrozium", "dustFrozium"));
		list.add(new RecipeDustCrushing("gemIgnum", "dustIgnum"));
		return list;
	}
	
	private static void addDustSmeltingRecipe(DustVariant dust, TheItems outputItem,  ItemVariant variant) {
		GameRegistry.addSmelting(ItemStackUtils.newVariantStack(TheItems.DUST, dust), ItemStackUtils.newVariantStack(outputItem, variant), 0.0F);
	}

}
