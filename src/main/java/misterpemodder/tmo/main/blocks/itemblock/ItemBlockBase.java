package misterpemodder.tmo.main.blocks.itemblock;

import misterpemodder.tmo.main.blocks.base.IBlockTMO;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBase extends ItemBlock {
	
	protected IBlockTMO block;
	
	public <B extends Block & IBlockTMO> ItemBlockBase(B block) {
		super(block);
		this.block = block;
		this.setHasSubtypes(false);
		this.setMaxDamage(0);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return block.getNames().getRarity();
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return super.hasEffect(stack) || block.hasEffect(stack);
	}
	
}
