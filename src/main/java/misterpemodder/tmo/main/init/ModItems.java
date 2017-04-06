package misterpemodder.tmo.main.init;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.tmo.main.items.EnumItemsNames;
import misterpemodder.tmo.main.items.ItemLock;
import misterpemodder.tmo.main.items.ItemMulti;
import misterpemodder.tmo.main.items.ItemTitaniumBucket;
import misterpemodder.tmo.main.items.ItemTmoArmor;
import misterpemodder.tmo.main.items.ItemVariant;
import misterpemodder.tmo.main.items.base.ITMOItem;
import misterpemodder.tmo.main.items.base.ItemBase;
import misterpemodder.tmo.main.items.materials.TmoArmorMaterial;
import misterpemodder.tmo.main.items.materials.TmoToolMaterial;
import misterpemodder.tmo.main.items.tools.ItemHammer;
import misterpemodder.tmo.main.items.tools.ItemTmoAxe;
import misterpemodder.tmo.main.items.tools.ItemTmoHoe;
import misterpemodder.tmo.main.items.tools.ItemTmoPickaxe;
import misterpemodder.tmo.main.items.tools.ItemTmoShovel;
import misterpemodder.tmo.main.items.tools.ItemTmoSword;
import misterpemodder.tmo.main.items.tools.ItemWrench;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@Mod.EventBusSubscriber(modid = TMORefs.MOD_ID)
public class ModItems {
	
	static  List<ItemBlock> ITEM_BLOCKS = new ArrayList<>();
	
	public enum TheItems  {
		TAB_ICON(new ItemBase(EnumItemsNames.TAB_ICON, false)),
		INGOT(new ItemMulti<ItemVariant.IngotVariant>(EnumItemsNames.INGOT, ItemVariant.IngotVariant.ingotVariants, "_ingot")),
		GEM(new ItemMulti<ItemVariant.GemVariant>(EnumItemsNames.GEM, ItemVariant.GemVariant.gemVariants, "_gem")),
		PLATE(new ItemMulti<ItemVariant.PlateVariant>(EnumItemsNames.PLATE, ItemVariant.PlateVariant.plateVariants, "_plate")),
		LOCK(new ItemLock()),
		TITANIUM_BUCKET(new ItemTitaniumBucket()),
		
		PICKAXE_TITANIUM(new ItemTmoPickaxe(EnumItemsNames.PICKAXE_TITANIUM, TmoToolMaterial.TITANIUM_MATERIAL)),
		PICKAXE_TITANIUM_DARK(new ItemTmoPickaxe(EnumItemsNames.PICKAXE_TITANIUM_DARK, TmoToolMaterial.TITANIUM_DARK_MATERIAL)),
		PICKAXE_COPPER(new ItemTmoPickaxe(EnumItemsNames.PICKAXE_COPPER, TmoToolMaterial.COPPER_MATERIAL)),
		PICKAXE_IGNUM(new ItemTmoPickaxe(EnumItemsNames.PICKAXE_IGNUM, TmoToolMaterial.IGNUM_MATERIAL)),
		PICKAXE_FROZIUM(new ItemTmoPickaxe(EnumItemsNames.PICKAXE_FROZIUM, TmoToolMaterial.FROZIUM_MATERIAL)),
		
		AXE_TITANIUM(new ItemTmoAxe(EnumItemsNames.AXE_TITANIUM, TmoToolMaterial.TITANIUM_MATERIAL, 6.0F, -3.0F)),
		AXE_TITANIUM_DARK(new ItemTmoAxe(EnumItemsNames.AXE_TITANIUM_DARK, TmoToolMaterial.TITANIUM_DARK_MATERIAL, 6.0F, -3.0F)),
		AXE_COPPER(new ItemTmoAxe(EnumItemsNames.AXE_COPPER, TmoToolMaterial.COPPER_MATERIAL, 6.0F, -3.0F)),
		AXE_IGNUM(new ItemTmoAxe(EnumItemsNames.AXE_IGNUM, TmoToolMaterial.IGNUM_MATERIAL, 6.0F, -3.0F)),
		AXE_FROZIUM(new ItemTmoAxe(EnumItemsNames.AXE_FROZIUM, TmoToolMaterial.FROZIUM_MATERIAL, 6.0F, -3.0F)),
		
		SHOVEL_TITANIUM(new ItemTmoShovel(EnumItemsNames.SHOVEL_TITANIUM, TmoToolMaterial.TITANIUM_MATERIAL)),
		SHOVEL_TITANIUM_DARK(new ItemTmoShovel(EnumItemsNames.SHOVEL_TITANIUM_DARK, TmoToolMaterial.TITANIUM_DARK_MATERIAL)),
		SHOVEL_COPPER(new ItemTmoShovel(EnumItemsNames.SHOVEL_COPPER, TmoToolMaterial.COPPER_MATERIAL)),
		SHOVEL_IGNUM(new ItemTmoShovel(EnumItemsNames.SHOVEL_IGNUM, TmoToolMaterial.IGNUM_MATERIAL)),
		SHOVEL_FROZIUM(new ItemTmoShovel(EnumItemsNames.SHOVEL_FROZIUM, TmoToolMaterial.FROZIUM_MATERIAL)),
		
		SWORD_TITANIUM(new ItemTmoSword(EnumItemsNames.SWORD_TITANIUM, TmoToolMaterial.TITANIUM_MATERIAL)),
		SWORD_TITANIUM_DARK(new ItemTmoSword(EnumItemsNames.SWORD_TITANIUM_DARK, TmoToolMaterial.TITANIUM_DARK_MATERIAL)),
		SWORD_COPPER(new ItemTmoSword(EnumItemsNames.SWORD_COPPER, TmoToolMaterial.COPPER_MATERIAL)),
		SWORD_IGNUM(new ItemTmoSword(EnumItemsNames.SWORD_IGNUM, TmoToolMaterial.IGNUM_MATERIAL)),
		SWORD_FROZIUM(new ItemTmoSword(EnumItemsNames.SWORD_FROZIUM, TmoToolMaterial.FROZIUM_MATERIAL)),
		
		HOE_TITANIUM(new ItemTmoHoe(EnumItemsNames.HOE_TITANIUM, TmoToolMaterial.TITANIUM_MATERIAL)),
		HOE_TITANIUM_DARK(new ItemTmoHoe(EnumItemsNames.HOE_TITANIUM_DARK, TmoToolMaterial.TITANIUM_DARK_MATERIAL)),
		HOE_COPPER(new ItemTmoHoe(EnumItemsNames.HOE_COPPER, TmoToolMaterial.COPPER_MATERIAL)),
		HOE_IGNUM(new ItemTmoHoe(EnumItemsNames.HOE_IGNUM, TmoToolMaterial.IGNUM_MATERIAL)),
		HOE_FROZIUM(new ItemTmoHoe(EnumItemsNames.HOE_FROZIUM, TmoToolMaterial.FROZIUM_MATERIAL)),
		
		WRENCH_COPPER(new ItemWrench(EnumItemsNames.WRENCH_COPPER, TmoToolMaterial.COPPER_MATERIAL)),
		WRENCH_ADMIN(new ItemWrench(EnumItemsNames.WRENCH_ADMIN, TmoToolMaterial.TITANIUM_MATERIAL, true, true)),
		
		HAMMER_TITANIUM(new ItemHammer(EnumItemsNames.HAMMER_TITANIUM, TmoToolMaterial.TITANIUM_MATERIAL)),
		HAMMER_TITANIUM_DARK(new ItemHammer(EnumItemsNames.HAMMER_TITANIUM_DARK, TmoToolMaterial.TITANIUM_DARK_MATERIAL)),
		HAMMER_COPPER(new ItemHammer(EnumItemsNames.HAMMER_COPPER, TmoToolMaterial.COPPER_MATERIAL)),
		HAMMER_FROZIUM(new ItemHammer(EnumItemsNames.HAMMER_FROZIUM, TmoToolMaterial.FROZIUM_MATERIAL)),
		HAMMER_IGNUM(new ItemHammer(EnumItemsNames.HAMMER_IGNUM, TmoToolMaterial.IGNUM_MATERIAL)),
		
		TITANIUM_HELMET(new ItemTmoArmor(EnumItemsNames.HELMET_TITANIUM, TmoArmorMaterial.TITANIUM, EntityEquipmentSlot.HEAD)),
		TITANIUM_CHESTPLATE(new ItemTmoArmor(EnumItemsNames.CHESTPLATE_TITANIUM, TmoArmorMaterial.TITANIUM, EntityEquipmentSlot.CHEST)),
		TITANIUM_LEGGINGS(new ItemTmoArmor(EnumItemsNames.LEGGINGS_TITANIUM, TmoArmorMaterial.TITANIUM, EntityEquipmentSlot.LEGS)),
		TITANIUM_BOOTS(new ItemTmoArmor(EnumItemsNames.BOOTS_TITANIUM, TmoArmorMaterial.TITANIUM, EntityEquipmentSlot.FEET)),
		
		TITANIUM_HELMET_DARK(new ItemTmoArmor(EnumItemsNames.HELMET_TITANIUM_DARK, TmoArmorMaterial.TITANIUM_DARK, EntityEquipmentSlot.HEAD)),
		TITANIUM_CHESTPLATE_DARK(new ItemTmoArmor(EnumItemsNames.CHESTPLATE_TITANIUM_DARK, TmoArmorMaterial.TITANIUM_DARK, EntityEquipmentSlot.CHEST)),
		TITANIUM_LEGGINGS_DARK(new ItemTmoArmor(EnumItemsNames.LEGGINGS_TITANIUM_DARK, TmoArmorMaterial.TITANIUM_DARK, EntityEquipmentSlot.LEGS)),
		TITANIUM_BOOTS_DARK(new ItemTmoArmor(EnumItemsNames.BOOTS_TITANIUM_DARK, TmoArmorMaterial.TITANIUM_DARK, EntityEquipmentSlot.FEET)),
		
		COPPER_HELMET(new ItemTmoArmor(EnumItemsNames.HELMET_COPPER, TmoArmorMaterial.COPPER, EntityEquipmentSlot.HEAD)),
		COPPER_CHESTPLATE(new ItemTmoArmor(EnumItemsNames.CHESTPLATE_COPPER, TmoArmorMaterial.COPPER, EntityEquipmentSlot.CHEST)),
		COPPER_LEGGINGS(new ItemTmoArmor(EnumItemsNames.LEGGINGS_COPPER, TmoArmorMaterial.COPPER, EntityEquipmentSlot.LEGS)),
		COPPER_BOOTS(new ItemTmoArmor(EnumItemsNames.BOOTS_COPPER, TmoArmorMaterial.COPPER, EntityEquipmentSlot.FEET)),
		
		FROZIUM_HELMET(new ItemTmoArmor(EnumItemsNames.HELMET_FROZIUM, TmoArmorMaterial.FROZIUM, EntityEquipmentSlot.HEAD)),
		FROZIUM_CHESTPLATE(new ItemTmoArmor(EnumItemsNames.CHESTPLATE_FROZIUM, TmoArmorMaterial.FROZIUM, EntityEquipmentSlot.CHEST)),
		FROZIUM_LEGGINGS(new ItemTmoArmor(EnumItemsNames.LEGGINGS_FROZIUM, TmoArmorMaterial.FROZIUM, EntityEquipmentSlot.LEGS)),
		FROZIUM_BOOTS(new ItemTmoArmor(EnumItemsNames.BOOTS_FROZIUM, TmoArmorMaterial.FROZIUM, EntityEquipmentSlot.FEET)),
		
		IGNUM_HELMET(new ItemTmoArmor(EnumItemsNames.HELMET_IGNUM, TmoArmorMaterial.IGNUM, EntityEquipmentSlot.HEAD)),
		IGNUM_CHESTPLATE(new ItemTmoArmor(EnumItemsNames.CHESTPLATE_IGNUM, TmoArmorMaterial.IGNUM, EntityEquipmentSlot.CHEST)),
		IGNUM_LEGGINGS(new ItemTmoArmor(EnumItemsNames.LEGGINGS_IGNUM, TmoArmorMaterial.IGNUM, EntityEquipmentSlot.LEGS)),
		IGNUM_BOOTS(new ItemTmoArmor(EnumItemsNames.BOOTS_IGNUM, TmoArmorMaterial.IGNUM, EntityEquipmentSlot.FEET)),
		
		DARKANIUM_HELMET(new ItemTmoArmor(EnumItemsNames.HELMET_DARKANIUM, TmoArmorMaterial.DARKANIUM, EntityEquipmentSlot.HEAD)),
		DARKANIUM_CHESTPLATE(new ItemTmoArmor(EnumItemsNames.CHESTPLATE_DARKANIUM, TmoArmorMaterial.DARKANIUM, EntityEquipmentSlot.CHEST)),
		DARKANIUM_LEGGINGS(new ItemTmoArmor(EnumItemsNames.LEGGINGS_DARKANIUM, TmoArmorMaterial.DARKANIUM, EntityEquipmentSlot.LEGS)),
		DARKANIUM_BOOTS(new ItemTmoArmor(EnumItemsNames.BOOTS_DARKANIUM, TmoArmorMaterial.DARKANIUM, EntityEquipmentSlot.FEET)),
		
		HALLOWED_HELMET(new ItemTmoArmor(EnumItemsNames.HELMET_HALLOWED, TmoArmorMaterial.HALLOWED, EntityEquipmentSlot.HEAD)),
		HALLOWED_CHESTPLATE(new ItemTmoArmor(EnumItemsNames.CHESTPLATE_HALLOWED, TmoArmorMaterial.HALLOWED, EntityEquipmentSlot.CHEST)),
		HALLOWED_LEGGINGS(new ItemTmoArmor(EnumItemsNames.LEGGINGS_HALLOWED, TmoArmorMaterial.HALLOWED, EntityEquipmentSlot.LEGS)),
		HALLOWED_BOOTS(new ItemTmoArmor(EnumItemsNames.BOOTS_HALLOWED, TmoArmorMaterial.HALLOWED, EntityEquipmentSlot.FEET)),
		;
		
		private ITMOItem item;
		
		public Item getItem() {
			return (Item) this.item;
		}
		
		public ITMOItem getTMOItem() {
			return this.item;
		}
		
		TheItems(ITMOItem item) {
			this.item = item;
		}
		
	}
	
	@SubscribeEvent
	public static void registerBlocksEvent(RegistryEvent.Register<Item> event) {
		register(event.getRegistry());
	}
	
	private static void register(IForgeRegistry<Item> registry) {
		TMORefs.LOGGER.info("Registering Items...");
		for(TheItems i : TheItems.values()) {
			registry.register(i.getItem());
		}
		if(!ITEM_BLOCKS.isEmpty()) {
			TMORefs.LOGGER.info("Registering ItemsBlocks...");
			for(ItemBlock i : ITEM_BLOCKS) {
				registry.register(i);
			}
		}
	}
	
	public static void registerOreDict() {
		for (TheItems i : TheItems.values()) {
			if(i.getItem() instanceof ItemBase) {
				((ItemBase) i.getItem()).registerOreDict();
			}
		}
	}
	
	public static void registerRenders() {
		for(TheItems i : TheItems.values()) {
			i.getTMOItem().registerRender();
		}
	}
	
}
