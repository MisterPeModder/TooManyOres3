package misterpemodder.tmo.main;

import misterpemodder.tmo.main.init.ModEnchants;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TMOTab extends CreativeTabs {

	public TMOTab(String label) {
		super(label);
		this.setBackgroundImageName("tmo.png");
		this.setRelevantEnchantmentTypes(ModEnchants.FORGE_HAMMER_ENCHANTEMENT_TYPE);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(TheItems.TAB_ICON.getItem());
	}
	
	@Override
	public boolean hasSearchBar() {
		return true;
	}
	
	public int getSearchbarWidth() {
        return 69;
    }

}
