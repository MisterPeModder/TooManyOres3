package misterpemodder.tmo.main.blocks.base;

import net.minecraft.item.ItemBlock;

public interface BlockTMO {
	
	void registerItemRender();
	void registerOreDict();
	
	ItemBlock getItemBlock();
	
}
