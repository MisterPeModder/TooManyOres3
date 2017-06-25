package misterpemodder.tmo.main.compat.craftingtweaks;

import misterpemodder.tmo.main.inventory.ContainerBase;
import misterpemodder.tmo.main.inventory.ContainerInjector;
import misterpemodder.tmo.main.inventory.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.inventory.ContainerTitaniumChest;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public final class CraftingTweaksCompat {
	
	public static final String MOD_ID = "craftingtweaks";
	public static boolean isModLoaded = false;
	public static Class<?> guiTweakButtonClass = null;
	
	public static void init() {
		isModLoaded = Loader.isModLoaded(MOD_ID);
		if(isModLoaded) {
			TMORefs.LOGGER.info("Found CraftingTweaks: Registering containers...");
			
			try {
				guiTweakButtonClass = Class.forName("net.blay09.mods.craftingtweaks.client.GuiTweakButton");
			} catch (ClassNotFoundException e) {
				TMORefs.LOGGER.info("Tweak Button class not found, Crafting Tweaks buttons wont be hidden inside containers!");
			}
			
			registerContainer(ContainerTitaniumAnvil.class, 46);
			registerContainer(ContainerTitaniumChest.class, 109);
			registerContainer(ContainerInjector.class, 44);
			
		} else {
			TMORefs.LOGGER.info("CraftingTweaks not found: integration not loading");
		}
	}
	
	private static void registerContainer(Class<? extends ContainerBase> clazz, int gridSlotNumber) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setString("ContainerClass", clazz.getName());

		tagCompound.setInteger("GridSlotNumber", gridSlotNumber);
		tagCompound.setInteger("GridSize", 9);
		tagCompound.setBoolean("HideButtons", false);
		tagCompound.setString("AlignToGrid", "left");

		FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", tagCompound);
	}

}
