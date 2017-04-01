package misterpemodder.tmo.main.items.tools;

import misterpemodder.tmo.main.items.EnumItemsNames;
import misterpemodder.tmo.main.items.base.ITMOItem;
import misterpemodder.tmo.main.items.materials.TmoToolMaterial;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public interface IItemTMOTool extends ITMOItem {
	
	public EnumItemsNames getNames();
	public TmoToolMaterial getMaterial();
	
	@Override
	public default boolean isEnabled() {
		return getMaterial().isEnabled();
	}

	public default void registerRender() {
		ModelResourceLocation location = new ModelResourceLocation(TMORefs.PREFIX + getNames().getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(((Item)this), 0, location);
	}
	
	public default int getRGBDurabilityForDisplay(ItemStack stack) {
		return ItemStackUtils.blinkColorOnLowDurability(((Item)this).getRGBDurabilityForDisplay(stack), stack);
	}
	
	public default EnumRarity getRarity(ItemStack stack) {
		return this.getNames().getRarity();
	}
	
}
