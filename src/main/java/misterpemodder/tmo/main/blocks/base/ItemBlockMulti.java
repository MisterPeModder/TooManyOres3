package misterpemodder.tmo.main.blocks.base;

import java.util.List;

import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMulti extends ItemBlockBase {
	
	protected int[] variantsMeta;
	
	public <V extends Enum<V> & IBlockVariant> ItemBlockMulti(BlockMulti<V> block) {
		super(block);
		this.variantsMeta = getVariantsMeta(block.getVariants());
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		
		
	}
	
	private static <V extends Enum<V> & IBlockVariant> int[] getVariantsMeta(V[] variants) {
		int len = variants.length;
		int[] v = new int[len];
		for(int i=0;i<len;i++) {
			v[i] = variants[i].getMeta();
		}
		return v;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for(int i = 0, len = variantsMeta.length; i<len; i++) {
			subItems.add(new ItemStack(itemIn, 1, variantsMeta[i]));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		int meta = stack.getMetadata();
		String type = ((BlockMulti)this.getBlock()).getVariant(meta).getUnlocalizedName();
		String name = Block.getBlockFromItem(stack.getItem()).getUnlocalizedName()+'.'+type;
		return ((BlockMulti) getBlock()).isValidVariant(meta)? name:TMOHelper.DEFAULT_ITEM_NAME;
	}
	
}
