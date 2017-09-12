package misterpemodder.tmo.main.items.tools;

import misterpemodder.hc.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.items.EnumItemsNames;
import misterpemodder.tmo.main.items.materials.TmoToolMaterial;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;

public class ItemTmoHoe extends ItemHoe implements IItemTMOTool{
	
	protected EnumItemsNames itemRefs;
	private TmoToolMaterial material;
	
	public ItemTmoHoe(EnumItemsNames itemRefs, TmoToolMaterial material) {
		super(material.material);
		this.itemRefs = itemRefs;
		this.material = material;
		setUnlocalizedName(itemRefs.getUnlocalizedName());
		setRegistryName(itemRefs.getRegistryName());
	}

	@Override
	public EnumItemsNames getNames() {
		return this.itemRefs;
	}

	@Override
	public TmoToolMaterial getMaterial() {
		return this.material;
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return ItemStackUtils.blinkColorOnLowDurability(super.getRGBDurabilityForDisplay(stack), stack);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return this.getNames().getRarity();
	}
	
}
