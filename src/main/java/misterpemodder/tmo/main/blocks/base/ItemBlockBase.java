package misterpemodder.tmo.main.blocks.base;

import net.minecraft.item.ItemBlock;

public class ItemBlockBase extends ItemBlock {
	
	protected BlockBase block;
	
	public ItemBlockBase(BlockBase block) {
		super(block);
		this.block = block;
		this.setHasSubtypes(false);
		this.setMaxDamage(0);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
}
