package misterpemodder.tmo.main.utils;

import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.init.ModPotions.ThePotionTypes;
import misterpemodder.tmo.main.items.ItemMulti;
import misterpemodder.tmo.main.items.ItemVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ItemStackUtils {
	
	public static <V extends ItemVariant> ItemStack newVariantStack(ItemMulti<V> item, V variant) {
		return ItemStackUtils.newVariantStack(item, variant, 1);
	}
	
	public static <V extends ItemVariant> ItemStack newVariantStack(ItemMulti<V> item, V variant, int amount) {
		return new ItemStack(item, amount, variant.getMeta());
	}
	
	public static  ItemStack newVariantStack(ModItems.TheItems item, ItemVariant variant) {
		return ItemStackUtils.newVariantStack(item, variant, 1);
	}
	
	public static ItemStack newVariantStack(ModItems.TheItems item, ItemVariant variant, int amount) {
		Item i = item.getItem();
		if(i instanceof ItemMulti && ((ItemMulti)i).getVariants().get(0).getClass().equals(variant.getClass())) {
			return newVariantStack((ItemMulti)i, variant, amount);
		} else {
			return new ItemStack(i, amount, 0);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static int blinkColorOnLowDurability(int color, ItemStack stack) {
		if((stack.getItemDamage()*100)/stack.getMaxDamage() > 80) {
		return Minecraft.getMinecraft().world.getTotalWorldTime() % 20 < 10? color : 0;
		} else {
			return color;
		}
	}
	
	public static ItemStack makePotion(ThePotionTypes type) {
		return makePotion(type.getPotionType());
	}
	
	public static ItemStack makePotion(PotionType type) {
		return PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), type);
	}
	
	public static ItemStack makeSplashPotion(ThePotionTypes type) {
		return makeSplashPotion(type.getPotionType());
	}
	
	public static ItemStack makeSplashPotion(PotionType type) {
		return PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), type);
	}
	
	public static ItemStack makeLingeringPotion(ThePotionTypes type) {
		return makeLingeringPotion(type.getPotionType());
	}
	
	public static ItemStack makeLingeringPotion(PotionType type) {
		return PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), type);
	}
	
}
