package misterpemodder.tmo.main.items.materials;

import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.config.ConfigValues.BoolValues;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class TmoToolMaterial {
	
	public static final TmoToolMaterial TITANIUM_MATERIAL = new TmoToolMaterial(EnumHelper.addToolMaterial("titanium", 4, 2755, 11, 5, 16), BoolValues.TITANIUM_TOOLS);
	public static final TmoToolMaterial TITANIUM_DARK_MATERIAL = new TmoToolMaterial(EnumHelper.addToolMaterial("titanium_dark", 4, 2855, 12, 6, 17), BoolValues.TITANIUM_DARK_TOOLS);
	public static final TmoToolMaterial COPPER_MATERIAL = new TmoToolMaterial(EnumHelper.addToolMaterial("copper", 2, 163, 5, 1, 8), BoolValues.COPPER);
	public static final TmoToolMaterial FROZIUM_MATERIAL = new TmoToolMaterial(EnumHelper.addToolMaterial("frozium", 3, 310, 7, 2, 14), BoolValues.FROZIUM_TOOLS);
	public static final TmoToolMaterial IGNUM_MATERIAL = new TmoToolMaterial(EnumHelper.addToolMaterial("ignum", 3, 310, 7, 2, 14), BoolValues.IGNUM_TOOLS);

	public ToolMaterial material;
	private ConfigValues.BoolValues enabled;
	
	public TmoToolMaterial(ToolMaterial material) {
		this(material, null);
	}
	
	public TmoToolMaterial(ToolMaterial material, ConfigValues.BoolValues enabled) {
		this.material = material;
		this.enabled = enabled;
	}
	
	public boolean isEnabled() {
		return enabled == null? true : enabled.currentValue;
	}
	
}
