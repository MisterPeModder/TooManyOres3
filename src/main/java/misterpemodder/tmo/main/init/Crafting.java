package misterpemodder.tmo.main.init;

import misterpemodder.tmo.main.items.ItemTmoArmor;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

public final class Crafting {

	public static void registerRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.Items.PICKAXE_TITANIUM.getItem(), "III", " S ", " S ", 'I', "ingotTitanium", 'S', "stickWood"));
		
		if(TMOHelper.topLoaded) {
			RecipeSorter.register(TMOHelper.PREFIX+"proberecipe", Crafting.ProbeRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
			GameRegistry.addRecipe(new ProbeRecipe());
		}
	}
	
	public static class ProbeRecipe implements IRecipe {

		@Override
		public boolean matches(InventoryCrafting inv, World worldIn) {
			int helmetCount = 0;
			int probeCount = 0;
			
			for (int i = 0; i < inv.getSizeInventory(); ++i) {
                ItemStack stack = inv.getStackInSlot(i);
                
                if(stack.getItem() instanceof ItemTmoArmor && (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(TMOHelper.TOP_TAG))) {
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
                		compound.setInteger(TMOHelper.TOP_TAG, 1);
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
