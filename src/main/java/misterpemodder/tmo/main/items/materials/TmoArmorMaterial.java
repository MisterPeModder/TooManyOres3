package misterpemodder.tmo.main.items.materials;

import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.items.ItemVariant.GemVariant;
import misterpemodder.tmo.main.items.ItemVariant.IngotVariant;
import misterpemodder.tmo.main.items.ItemVariant.PlateVariant;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class TmoArmorMaterial {
	
	public static final TmoArmorMaterial TITANIUM = new TmoArmorMaterial(EnumHelper.addArmorMaterial("TITANIUM", TMORefs.PREFIX+"titanium", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F), EnumRarity.RARE, ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.TITANIUM_INGOT), ConfigValues.BoolValues.TITANIUM_ARMOR);
	public static final TmoArmorMaterial TITANIUM_DARK = new TmoArmorMaterial(EnumHelper.addArmorMaterial("TITANIUM_DARK", TMORefs.PREFIX+"dark_titanium", 34, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F), EnumRarity.EPIC, ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.TITANIUM_INGOT_DARK), ConfigValues.BoolValues.DARK_TITANIUM_ARMOR);
	public static final TmoArmorMaterial COPPER = new TmoArmorMaterial(EnumHelper.addArmorMaterial("COPPER", TMORefs.PREFIX+"copper", 15, new int[]{2, 4, 5, 2}, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F), ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.COPPER_INGOT), ConfigValues.BoolValues.COPPER_ARMOR);
	public static final TmoArmorMaterial FROZIUM = new TmoArmorMaterial(EnumHelper.addArmorMaterial("FROZIUM", TMORefs.PREFIX+"frozium", 15, new int[]{2, 6, 7, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.5F), ItemStackUtils.newVariantStack(TheItems.GEM, GemVariant.FROZIUM_GEM), ConfigValues.BoolValues.FROZIUM_ARMOR);
	public static final TmoArmorMaterial IGNUM = new TmoArmorMaterial(EnumHelper.addArmorMaterial("IGNUM", TMORefs.PREFIX+"ignum", 15, new int[]{2, 6, 7, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.5F), ItemStackUtils.newVariantStack(TheItems.GEM, GemVariant.IGNUM_GEM), ConfigValues.BoolValues.IGNUM_ARMOR);
	public static final TmoArmorMaterial DARKANIUM = new TmoArmorMaterial(EnumHelper.addArmorMaterial("DARKANIUM", TMORefs.PREFIX+"darkanium", 36, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.5F), EnumRarity.EPIC,  ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.DARKANIUM_INGOT), ConfigValues.BoolValues.DARKANIUM_ARMOR);
	public static final TmoArmorMaterial HALLOWED = new TmoArmorMaterial(EnumHelper.addArmorMaterial("HALLOWED", TMORefs.PREFIX+"hallowed", 36, new int[]{3, 6, 8, 3}, 10, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 2.5F), EnumRarity.UNCOMMON, ItemStackUtils.newVariantStack(TheItems.PLATE, PlateVariant.HALLOWED_PLATE), ConfigValues.BoolValues.HALLOWED_ARMOR);
	
	public ArmorMaterial material;
	public EnumRarity rarity;
	private ConfigValues.BoolValues enabled;
	
	public TmoArmorMaterial(ArmorMaterial material) {
		this(material, ItemStack.EMPTY, null);
	}
	
	public TmoArmorMaterial(ArmorMaterial material, ItemStack repairItem, ConfigValues.BoolValues enabled) {
		this(material, EnumRarity.COMMON, repairItem, enabled);
	}
	
	public TmoArmorMaterial(ArmorMaterial material, EnumRarity rarity, ItemStack repairItem, ConfigValues.BoolValues enabled) {
		this.material = material;
		this.material.setRepairItem(repairItem);
		this.enabled = enabled;
		this.rarity = rarity;
	}
	
	public boolean isEnabled() {
		return enabled == null? true : enabled.currentValue;
	}
	
}
