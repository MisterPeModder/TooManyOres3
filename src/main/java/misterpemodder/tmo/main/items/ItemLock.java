package misterpemodder.tmo.main.items;

import java.util.List;
import java.util.Random;

import misterpemodder.hc.api.item.IItemLock;
import misterpemodder.hc.main.items.ItemMulti;
import misterpemodder.hc.main.utils.StringUtils;
import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.items.TMOItemVariants.LockVariant;
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

public class ItemLock extends ItemMulti<TMOItemVariants.LockVariant> implements IItemLock {
	
	private static final int PERCENT_DECREASE_PER_UNBREAKING_LEVEL = 5;

	public ItemLock() {
		super(EnumItemsNames.LOCK, TMOItemVariants.LockVariant.lockVariants, "_lock");
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
		TMOItemVariants.LockVariant variant = ((ItemLock)stack.getItem()).getVariant(stack.getMetadata());
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
		TMOItemVariants.LockVariant variant = lock.getVariant(lockStack.getMetadata());
		if(variant == LockVariant.BASIC) {
			breakChance = ConfigValues.IntValues.BASIC_LOCK_BREAK_CHANCE.currentValue;
		} else {
			breakChance = ConfigValues.IntValues.REINFORCED_LOCK_BREAK_CHANCE.currentValue;
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
			((EntityPlayer)entity).sendMessage(new TextComponentString(TextFormatting.DARK_RED+StringUtils.translate("tile.blockTitaniumChest.lockBroken")));
		}
	}

	@Override
	public boolean isBroken(ItemStack stack) {
		return ((ItemLock)stack.getItem()).getVariant(stack.getMetadata()).isBroken();
	}
}
