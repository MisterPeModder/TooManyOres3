package misterpemodder.tmo.main.apiimpl;

import static misterpemodder.tmo.api.TooManyOresAPI.registryHandler;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import misterpemodder.hc.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.blocks.BlockStorage;
import misterpemodder.tmo.main.compat.aa.ActAddCompat;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.items.TMOItemVariants.IngotVariant;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class EnderMatterItems {
	
	public static void register() {
		
		registryHandler.registerEnderMatterItem(new ItemStack(Blocks.END_STONE, 10), 1);
		registryHandler.registerEnderMatterItem(new ItemStack(Items.CHORUS_FRUIT), 4);
		registryHandler.registerEnderMatterItem(new ItemStack(Items.ENDER_PEARL), 32);
		registryHandler.registerEnderMatterItem(new ItemStack(Items.ENDER_EYE), 32);
		registryHandler.registerEnderMatterItem(new ItemStack(Items.END_CRYSTAL), 48);
		registryHandler.registerEnderMatterItem(ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.ENDER_MATTER_INGOT), 64);
		registryHandler.registerEnderMatterItem(new ItemStack(TheBlocks.STORAGE_BLOCK.getBlock(),1,BlockStorage.EnumVariant.ENDER_MATTER_BLOCK.getMeta()), 576);
		
		if(ActAddCompat.actAddLoaded) {
			registerModItem(ActuallyAdditionsAPI.MOD_ID, "item_misc", 1, 19, 1000);
			registerModItem(ActuallyAdditionsAPI.MOD_ID, "block_misc", 1, 6, 128);
		}
	}
	
	private static void registerModItem(String modId, String registryName, int count, int meta, int enderAmount) {
		ItemStack stack = ItemStackUtils.getModItemStack(modId, registryName, count, meta);
		if(stack != ItemStack.EMPTY) {
			registryHandler.registerEnderMatterItem(stack, enderAmount);
		}
	}
	
}
