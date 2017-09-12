package misterpemodder.tmo.main.utils;

import misterpemodder.hc.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.init.ModPotions.ThePotionTypes;
import net.minecraft.item.ItemStack;

public final class TMOItemStackUtils {
	
	public static ItemStack makePotion(ThePotionTypes type) {
		return ItemStackUtils.makePotion(type.getPotionType());
	}

	public static ItemStack makeLingeringPotion(ThePotionTypes type) {
		return ItemStackUtils.makeLingeringPotion(type.getPotionType());
	}
	
	public static ItemStack makeSplashPotion(ThePotionTypes type) {
		return ItemStackUtils.makeSplashPotion(type.getPotionType());
	}
	
}
