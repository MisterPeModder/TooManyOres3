package misterpemodder.tmo.main.enchant;

import java.util.Map;

import misterpemodder.tmo.api.item.IItemForgeHammer;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.init.ModEnchants;
import misterpemodder.tmo.main.init.ModEnchants.TheEnchants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnchantementXpCostReduction extends EnchantementBase {
	
	public static final int REDUCTION_PER_LEVEL = 5;

	public EnchantementXpCostReduction() {
		super("hammer_xp_cost_reduction", "hammerXpCostReduction", Rarity.COMMON, ModEnchants.FORGE_HAMMER_ENCHANTEMENT_TYPE, EntityEquipmentSlot.values());
	}
	
	@Override
	public int getMaxLevel() {
		return 4;
	}
	
	public int getMinEnchantability(int enchantmentLevel) {
        return 1 + 10 * (enchantmentLevel - 1);
    }

    /**
     * Returns the maximum value of enchantability nedded on the enchantment level passed.
     */
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public String getDescription() {
    	return Tmo.proxy.translate("enchantment.hammerXpCostReduction.desc", String.valueOf(REDUCTION_PER_LEVEL));
    }
    
    public static int getXPCost(ItemStack stack, int maximumCost) {
    	if(stack.getItem() instanceof IItemForgeHammer) {
    		
    		int e = 0;
    		Map<Enchantment, Integer> m = EnchantmentHelper.getEnchantments(stack);
    		if(m.containsKey(TheEnchants.XP_COST_REDUCTION.getEnchantement())) {
    			e = REDUCTION_PER_LEVEL * m.get(TheEnchants.XP_COST_REDUCTION.getEnchantement());
    		}
			int r = Math.abs(Math.min(90, ((IItemForgeHammer)stack.getItem()).getExperienceCostReduction(stack)+e));
			maximumCost -= Math.round(maximumCost*(r/100F));
		}
    	return Math.max(1, maximumCost);
    }

}
