package misterpemodder.tmo.main.blocks.properties;

import net.minecraft.item.EnumRarity;

public interface IBlockNames {
	public String getUnlocalizedName();
	public String getRegistryName();
	public String[] getOreDictNames();
	public EnumRarity getRarity();
}
