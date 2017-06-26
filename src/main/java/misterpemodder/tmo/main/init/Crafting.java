package misterpemodder.tmo.main.init;

import misterpemodder.tmo.main.blocks.BlockDeco;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.items.ItemTmoArmor;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
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
		
		if(TMORefs.topLoaded) {
			RecipeSorter.register(TMORefs.PREFIX+"proberecipe", Crafting.ProbeRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
			GameRegistry.addRecipe(new ProbeRecipe());
		}
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
	
	public static class ProbeRecipe implements IRecipe {

		@Override
		public boolean matches(InventoryCrafting inv, World worldIn) {
			int helmetCount = 0;
			int probeCount = 0;
			
			for (int i = 0; i < inv.getSizeInventory(); ++i) {
                ItemStack stack = inv.getStackInSlot(i);
                
                if(stack.getItem() instanceof ItemTmoArmor && (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(TMORefs.TOP_TAG))) {
                	if(((ItemTmoArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD) {
                		helmetCount++;
                	}
                }
                else if(stack.getItem() == Item.getByNameOrId("theoneprobe:probe")) {
                	probeCount++;
                }
                else if(!stack.isEmpty()) {
                	return false;
                }
                
			}
			
			return helmetCount == 1 && probeCount == 1;
		}

		@Override
		public ItemStack getCraftingResult(InventoryCrafting inv) {
			ItemStack result = ItemStack.EMPTY;
			
			for (int i = 0; i < inv.getSizeInventory(); ++i) {
                ItemStack stack = inv.getStackInSlot(i);
                
                if(stack.getItem() instanceof ItemTmoArmor) {
                	if(((ItemTmoArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD) {
                		result = stack.copy();
                		NBTTagCompound compound;
                		if(stack.hasTagCompound()) {
                			compound = result.getTagCompound();
                		} else {
                			compound = new NBTTagCompound();
                		}
                		compound.setInteger(TMORefs.TOP_TAG, 1);
                		result.setTagCompound(compound);
                	}
                }
			}
			return result;
		}

		@Override
		public int getRecipeSize() {
			return 3;
		}

		@Override
		public ItemStack getRecipeOutput() {
			return ItemStack.EMPTY;
		}
		
		@Override
		public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
        {
            NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

            for (int i = 0; i < nonnulllist.size(); ++i) {
                ItemStack itemstack = inv.getStackInSlot(i).copy();
                if(!itemstack.isEmpty())
                	itemstack.shrink(1);
                nonnulllist.set(i, itemstack);
            }
            inv.clear();
            return nonnulllist;
        }
		
	}

}
