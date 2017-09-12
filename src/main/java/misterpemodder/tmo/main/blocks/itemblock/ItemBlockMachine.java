package misterpemodder.tmo.main.blocks.itemblock;

import misterpemodder.hc.main.blocks.itemblock.ItemBlockBase;
import misterpemodder.tmo.main.blocks.BlockMachineCasing.EnumMachineCasingVariant;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMachine extends ItemBlockBase {

	public ItemBlockMachine(BlockMachine<?> block) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for(EnumMachineCasingVariant v : EnumMachineCasingVariant.values()) {
			subItems.add(new ItemStack(itemIn, 1, v.getMeta()));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getBlock().getUnlocalizedName() + '.' + EnumMachineCasingVariant.fromMeta(stack.getMetadata()).getName();
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumMachineCasingVariant.fromMeta(stack.getMetadata()).getRarity();
	}

}
