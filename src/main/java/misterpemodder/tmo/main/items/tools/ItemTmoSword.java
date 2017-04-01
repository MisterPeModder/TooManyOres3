package misterpemodder.tmo.main.items.tools;

import misterpemodder.tmo.main.items.EnumItemsNames;
import misterpemodder.tmo.main.items.materials.TmoToolMaterial;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ItemTmoSword extends ItemSword implements IItemTMOTool {
	
	protected EnumItemsNames itemRefs;
	private TmoToolMaterial material;

	public ItemTmoSword(EnumItemsNames itemRefs, TmoToolMaterial material) {
		super(material.material);
		this.itemRefs = itemRefs;
		this.material = material;
		setUnlocalizedName(itemRefs.getUnlocalizedName());
		setRegistryName(itemRefs.getRegistryName());
		if(isEnabled()) setCreativeTab(TMORefs.TMO_TAB);
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
