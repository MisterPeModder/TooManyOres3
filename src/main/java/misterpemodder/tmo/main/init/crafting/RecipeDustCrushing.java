package misterpemodder.tmo.main.init.crafting;

import java.util.Random;

import misterpemodder.tmo.api.item.IItemForgeHammer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeDustCrushing implements IRecipe {
	
	public final NonNullList<ItemStack> ores = NonNullList.create();
	public final NonNullList<ItemStack> dusts = NonNullList.create();
	
	private final Random rand;

	public RecipeDustCrushing(String oreOreDict, String dustOreDict) {
		ores.addAll(OreDictionary.getOres(oreOreDict));
		dusts.addAll(OreDictionary.getOres(dustOreDict));
		this.rand = new Random();
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		if(ores.isEmpty() || dusts.isEmpty()) return false;
		
		int oreCount = 0;
		int hammerCount = 0;
		
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack stack = inv.getStackInSlot(i);
			
			if(stack.getItem() instanceof IItemForgeHammer) {
				hammerCount++;
			}
			else if(OreDictionary.containsMatch(true, ores, stack)) {
				oreCount ++;
			}
			else if(!stack.isEmpty()){
				return false;
			}
			
		}
		
		return hammerCount == 1 && oreCount == 1;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return dusts.isEmpty()? ItemStack.EMPTY : dusts.get(0).copy();
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
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i).copy();
            
            if(!itemstack.isEmpty()) {
            	if(itemstack.getItem() instanceof IItemForgeHammer) {
            		if(itemstack.getItemDamage() < itemstack.getMaxDamage()) {
            			itemstack.attemptDamageItem(2, rand);
            		} else {
            			itemstack = ItemStack.EMPTY;
            		}
    			}
    			else {
    				itemstack.shrink(1);
    			}
            }
            nonnulllist.set(i, itemstack);
        }
        inv.clear();
        return nonnulllist;
	}
	
}
