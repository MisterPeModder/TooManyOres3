package misterpemodder.tmo.main;

import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TMOTab extends CreativeTabs {

	public TMOTab(String label) {
		super(label);
		setBackgroundImageName("tmo.png");
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(Item.getByNameOrId(TMORefs.PREFIX+"tab_icon"));
	}
	
	@Override
	public boolean hasSearchBar() {
		return true;
	}
	
	public int getSearchbarWidth() {
        return 69;
    }

}
