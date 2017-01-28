package misterpemodder.tmo.main.items.tools;

import misterpemodder.tmo.main.items.EnumItemsNames;
import misterpemodder.tmo.main.items.base.TMOItem;
import misterpemodder.tmo.main.items.materials.TmoToolMaterial;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemAxe;
import net.minecraftforge.client.model.ModelLoader;

public class ItemTmoAxe extends ItemAxe implements TMOItem {
	
	protected EnumItemsNames itemRefs;
	private TmoToolMaterial material;
	
	public ItemTmoAxe(EnumItemsNames itemRefs, TmoToolMaterial material, float damage, float speed) {
		//TODO Adjust Axes speed
		super(material.material, damage, speed);
		this.material = material;
		this.itemRefs = itemRefs;
		setUnlocalizedName(itemRefs.getUnlocalizedName());
		setRegistryName(itemRefs.getRegistryName());
		if(isEnabled()) setCreativeTab(TMOHelper.TMO_TAB);
	}
	
	@Override
	public boolean isEnabled() {
		return material.isEnabled();
	}

	@Override
	public void registerRender() {
		ModelResourceLocation location = new ModelResourceLocation(TMOHelper.PREFIX + itemRefs.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, location);
	}

}
