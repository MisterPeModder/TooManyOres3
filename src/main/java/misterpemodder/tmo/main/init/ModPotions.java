package misterpemodder.tmo.main.init;

import misterpemodder.tmo.main.potion.PotionBase;
import misterpemodder.tmo.main.potion.PotionFreezing;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@Mod.EventBusSubscriber(modid = TMORefs.MOD_ID)
public final class ModPotions {
	
	public static enum ThePotionTypes {
		FREEZING("freezing", new PotionType("tmo.freezing", new PotionEffect(ThePotions.FREEZING.getPotion(), 400, 0, false, true))),
		;
		
		private final PotionType type;
		
		public PotionType getPotionType() {
			return this.type;
		}

		ThePotionTypes(String registryName, PotionType potionType) {
			this.type = potionType;
			this.type.setRegistryName(new ResourceLocationTmo(registryName));
		}
		
	}
	
	public static enum ThePotions {
		FREEZING(new PotionFreezing()),
		;
		
		private final PotionBase potion;
		
		public PotionBase getPotion() {
			return this.potion;
		}

		ThePotions(PotionBase potion) {
			this.potion = potion;
		}
		
	}
	
	
	@SubscribeEvent
	public static void registerPotionsEvent(RegistryEvent.Register<Potion> event) {
		TMORefs.LOGGER.info("Registering Potions...");
		IForgeRegistry<Potion> registry = event.getRegistry();
		for(ThePotions p : ThePotions.values()) {
			registry.register(p.getPotion());
		}
	}
	
	@SubscribeEvent
	public static void registerPotionTypesEvent(RegistryEvent.Register<PotionType> event) {
		TMORefs.LOGGER.info("Registering PotionTypes...");
		IForgeRegistry<PotionType> registry = event.getRegistry();
		for(ThePotionTypes p : ThePotionTypes.values()) {
			registry.register(p.getPotionType());
		}
	}
}
