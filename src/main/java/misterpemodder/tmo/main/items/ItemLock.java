package misterpemodder.tmo.main.items;

import java.util.List;
import java.util.Random;

import misterpemodder.tmo.api.item.IItemLock;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.items.ItemVariant.LockVariant;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLock extends ItemMulti<ItemVariant.LockVariant> implements IItemLock {
	
	private static final int PERCENT_DECREASE_PER_UNBREAKING_LEVEL = 5;

	public ItemLock() {
		super(EnumItemsNames.LOCK, ItemVariant.LockVariant.lockVariants, "_lock");
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment == Enchantments.UNBREAKING;
	}
	
	@Override
	public int getItemStackLimit() {
		return 1;
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		ItemVariant.LockVariant variant = ((ItemLock)stack.getItem()).getVariant(stack.getMetadata());
		if(isBroken(stack)) {
			tooltip.add(TextFormatting.RED+""+TextFormatting.BOLD+I18n.format("item.itemLock.broken.title"));
			tooltip.add(TextFormatting.RED+""+TextFormatting.ITALIC+I18n.format("item.itemLock.broken.desc", variant.getRepairItem().getCount(), variant.getRepairItem().getDisplayName()));
		} else {
			super.addInformation(stack, playerIn, tooltip, advanced);
		}
	}

	@Override
	public EnumActionResult attemptBreak(ItemStack lockStack, Random random) {
		ItemLock lock = (ItemLock)lockStack.getItem();
		int breakChance;
		ItemVariant.LockVariant variant = lock.getVariant(lockStack.getMetadata());
		if(variant == LockVariant.BASIC) {
			breakChance = 50;
		} else {
			breakChance = 20;
		}
		breakChance -= EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, lockStack)*PERCENT_DECREASE_PER_UNBREAKING_LEVEL;
		EnumActionResult result;
		if(random.nextInt(100) < breakChance) {
			lockStack.setItemDamage(variant.getOtherVariant().getMetadata());
			result = EnumActionResult.SUCCESS;
		} else {
			result = EnumActionResult.FAIL;
		}
		
		return result;
	}

	@Override
	public void onLockBroken(World world, BlockPos pos, EntityLivingBase entity) {
		if(entity != null && entity instanceof EntityPlayer) {
			((EntityPlayer)entity).sendMessage(new TextComponentString(TextFormatting.DARK_RED+Tmo.proxy.translate("tile.blockTitaniumChest.lockBroken")));
		}
	}

	@Override
	public boolean isBroken(ItemStack stack) {
		return ((ItemLock)stack.getItem()).getVariant(stack.getMetadata()).isBroken();
	}
}
