package misterpemodder.tmo.main.init;

import java.util.Locale;

import com.google.common.base.Predicate;

import misterpemodder.tmo.api.item.IItemForgeHammer;
import misterpemodder.tmo.main.enchant.EnchantementXpCostReduction;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TMORefs.MOD_ID)
public class ModEnchants {
	
	public static EnumEnchantmentType FORGE_HAMMER_ENCHANTEMENT_TYPE = addEnchantType("forge_hammer", new Predicate<Item>() {
		@Override
		public boolean apply(Item input) {
			return input instanceof IItemForgeHammer;	
		}
	});
	
	public static enum TheEnchants {
		
		XP_COST_REDUCTION(new EnchantementXpCostReduction()),
		;
		
		private final Enchantment enchant;
		
		public Enchantment getEnchantement() {
			return this.enchant;
		}
		
		TheEnchants(Enchantment enchant) {
			this.enchant = enchant;
		}
		
	}
	
	@SubscribeEvent
	public static void registerBlocksEvent(RegistryEvent.Register<Enchantment> event) {
		TMORefs.LOGGER.info("Registering Enchantements...");
		for(TheEnchants ench : TheEnchants.values()) {
			event.getRegistry().register(ench.getEnchantement());
		}
		
	}
	
	private static EnumEnchantmentType addEnchantType(String name, Predicate<Item> predicate) {
		return EnumHelper.addEnchantmentType(name.toUpperCase(Locale.ROOT), predicate);
	}
	
}
