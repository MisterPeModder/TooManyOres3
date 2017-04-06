package misterpemodder.tmo.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * A hammer that can be used in titanium anvils
 */
public interface IItemForgeHammer {
	
	/**
     * Called when the hammer is used in a titanium anvil
     * This is used to decrease durability or for other purposes.
     *
     * @param player
     *            - The player that is using the anvil
     * @param stack
     * 			  - The hammer itemStack
     * @param durability
     * 			  - The amount of durablity that should be taken form the itemstack
     */
	void onHammerUsed(EntityPlayer player, ItemStack stack, int durability);
	
	/**
     * How much should this hammer reduce XP cost?
     * 
     * @param stack
     * 			  - The hammer itemStack
     * @return an percentage between 0 and 90.
     */
	int getExperienceCostReduction(ItemStack stack);
	
}
