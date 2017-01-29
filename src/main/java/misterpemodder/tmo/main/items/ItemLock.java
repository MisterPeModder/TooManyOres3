package misterpemodder.tmo.main.items;

import java.util.List;
import java.util.Random;

import misterpemodder.tmo.api.item.IItemLock;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.items.ItemsVariants.Lock;
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

public class ItemLock extends ItemMulti<ItemsVariants.Lock> implements IItemLock {

	public ItemLock() {
		super(EnumItemsNames.LOCK, ItemsVariants.Lock.values(), "_lock");
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
		ItemsVariants.Lock variant = ((ItemLock)stack.getItem()).getVariant(stack.getMetadata());
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
		float breakChance;
		ItemsVariants.Lock variant = lock.getVariant(lockStack.getMetadata());
		if(variant == Lock.BASIC) {
			breakChance = 0.5F;
		} else {
			breakChance = 0.2F;
		}
		
		EnumActionResult result;
		
		if(random.nextInt(EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, lockStack)+1) == 0 || random.nextFloat()<breakChance) {
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
