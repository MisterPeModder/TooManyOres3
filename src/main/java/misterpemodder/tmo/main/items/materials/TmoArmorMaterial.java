package misterpemodder.tmo.main.items.materials;

import com.typesafe.config.ConfigValueType;

import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.config.ConfigValues.BoolValues;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.EnumHelper;

public class TmoArmorMaterial {
	
	//(String name, String textureName, int durability, int[] reductionAmounts, int enchantability, SoundEvent soundOnEquip, float toughness)
	//DIAMOND("diamond", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);
	//IRON("iron", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F),
	public static final TmoArmorMaterial TITANIUM = new TmoArmorMaterial(EnumHelper.addArmorMaterial("TITANIUM", TMOHelper.PREFIX+"titanium", 33, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F), ConfigValues.BoolValues.TITANIUM_ARMOR);
	public static final TmoArmorMaterial TITANIUM_DARK = new TmoArmorMaterial(EnumHelper.addArmorMaterial("TITANIUM_DARK", TMOHelper.PREFIX+"dark_titanium", 34, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F), ConfigValues.BoolValues.DARK_TITANIUM_ARMOR);
	public static final TmoArmorMaterial COPPER = new TmoArmorMaterial(EnumHelper.addArmorMaterial("COPPER", TMOHelper.PREFIX+"copper", 15, new int[]{2, 4, 5, 2}, 8, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F), ConfigValues.BoolValues.COPPER_ARMOR);
	public static final TmoArmorMaterial FROZIUM = new TmoArmorMaterial(EnumHelper.addArmorMaterial("FROZIUM", TMOHelper.PREFIX+"frozium", 15, new int[]{2, 6, 7, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.5F), ConfigValues.BoolValues.FROZIUM_ARMOR);
	public static final TmoArmorMaterial IGNUM = new TmoArmorMaterial(EnumHelper.addArmorMaterial("IGNUM", TMOHelper.PREFIX+"ignum", 15, new int[]{2, 6, 7, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 0.5F), ConfigValues.BoolValues.IGNUM_ARMOR);
	public static final TmoArmorMaterial DARKANIUM = new TmoArmorMaterial(EnumHelper.addArmorMaterial("DARKANIUM", TMOHelper.PREFIX+"darkanium", 36, new int[]{3, 6, 8, 3}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.5F), ConfigValues.BoolValues.DARKANIUM_ARMOR);
	public static final TmoArmorMaterial HALLOWED = new TmoArmorMaterial(EnumHelper.addArmorMaterial("HALLOWED", TMOHelper.PREFIX+"hallowed", 36, new int[]{3, 6, 8, 3}, 10, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 2.5F), ConfigValues.BoolValues.HALLOWED_ARMOR);
	public ArmorMaterial material;
	private ConfigValues.BoolValues enabled;
	
	public TmoArmorMaterial(ArmorMaterial material) {
		this(material, null);
	}
	
	public TmoArmorMaterial(ArmorMaterial material, ConfigValues.BoolValues enabled) {
		this.material = material;
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled == null? true : enabled.currentValue;
	}
	
}
