package misterpemodder.tmo.main.items.materials;

import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.config.ConfigValues.BoolValues;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.items.ItemVariant.GemVariant;
import misterpemodder.tmo.main.items.ItemVariant.IngotVariant;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class TmoToolMaterial {
	
	public static final TmoToolMaterial TITANIUM_MATERIAL = new TmoToolMaterial(EnumHelper.addToolMaterial("titanium", 4, 2755, 11, 5, 16), ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.TITANIUM_INGOT), BoolValues.TITANIUM_TOOLS);
	public static final TmoToolMaterial TITANIUM_DARK_MATERIAL = new TmoToolMaterial(EnumHelper.addToolMaterial("titanium_dark", 4, 2855, 12, 6, 17), ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.TITANIUM_INGOT_DARK), BoolValues.TITANIUM_DARK_TOOLS);
	public static final TmoToolMaterial COPPER_MATERIAL = new TmoToolMaterial(EnumHelper.addToolMaterial("copper", 2, 163, 5, 1, 8), ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.COPPER_INGOT), BoolValues.COPPER);
	public static final TmoToolMaterial FROZIUM_MATERIAL = new TmoToolMaterial(EnumHelper.addToolMaterial("frozium", 3, 310, 7, 2, 14), ItemStackUtils.newVariantStack(TheItems.GEM, GemVariant.FROZIUM_GEM), BoolValues.FROZIUM_TOOLS);
	public static final TmoToolMaterial IGNUM_MATERIAL = new TmoToolMaterial(EnumHelper.addToolMaterial("ignum", 3, 310, 7, 2, 14), ItemStackUtils.newVariantStack(TheItems.GEM, GemVariant.IGNUM_GEM), BoolValues.IGNUM_TOOLS);

	public ToolMaterial material;
	private ConfigValues.BoolValues enabled;
	
	public TmoToolMaterial(ToolMaterial material, ItemStack repairItem) {
		this(material, repairItem, null);
	}
	
	public TmoToolMaterial(ToolMaterial material, ItemStack repairItem, ConfigValues.BoolValues enabled) {
		this.material = material;
		this.material.setRepairItem(repairItem);
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled == null? true : enabled.currentValue;
	}
	
}
