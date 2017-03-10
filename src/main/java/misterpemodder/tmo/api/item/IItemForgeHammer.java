package misterpemodder.tmo.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IItemForgeHammer {
	
	void onHammerUsed(EntityPlayer player, ItemStack stack, int durability);
	
}
