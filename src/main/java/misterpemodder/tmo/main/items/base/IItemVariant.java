package misterpemodder.tmo.main.items.base;

import misterpemodder.tmo.main.items.ItemMulti;

public interface IItemVariant {
	
	public String getName();
	public String getUnlocalizedName();
	public int getMeta();
	public ItemMulti getItem();
	public default String[] getOreDictNames() {
		return new String[0];
	}
	
}
