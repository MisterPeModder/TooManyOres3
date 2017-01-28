package misterpemodder.tmo.main.items;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLock extends ItemMulti<ItemsVariants.Lock> {
	
	private static final int ENCHANTABILITY = 0;

	public ItemLock() {
		super(EnumItemsNames.LOCK, ItemsVariants.Lock.values(), "_lock");
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment == Enchantments.UNBREAKING;
	}
	
	@Override
	public int getItemEnchantability() {
		return ENCHANTABILITY;
	}
	
	@Override
	public int getItemStackLimit() {
		return 1;
	}

	public static boolean isBroken(ItemStack stack) {
		if(!(stack.getItem() instanceof ItemLock)) {
			return false;
		}
		return ((ItemLock)stack.getItem()).getVariant(stack.getMetadata()).isBroken();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		ItemsVariants.Lock variant = ((ItemLock)stack.getItem()).getVariant(stack.getMetadata());
		if(isBroken(stack)) {
			tooltip.add(TextFormatting.RED+""+TextFormatting.BOLD+I18n.format("item.itemLock.broken.title"));
			tooltip.add(TextFormatting.RED+""+TextFormatting.ITALIC+I18n.format("item.itemLock.broken.desc", variant.getRepairItem().getCount(), variant.getRepairItem().getDisplayName()));
		} else {
			super.addInformation(stack, playerIn, tooltip, advanced);
		}
	}

}
