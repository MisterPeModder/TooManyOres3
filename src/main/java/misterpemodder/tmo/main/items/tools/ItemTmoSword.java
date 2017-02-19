package misterpemodder.tmo.main.items.tools;

import misterpemodder.tmo.main.items.EnumItemsNames;
import misterpemodder.tmo.main.items.base.TMOItem;
import misterpemodder.tmo.main.items.materials.TmoToolMaterial;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemSword;
import net.minecraftforge.client.model.ModelLoader;

public class ItemTmoSword extends ItemSword implements TMOItem {
	
	protected EnumItemsNames itemRefs;
	private TmoToolMaterial material;

	public ItemTmoSword(EnumItemsNames itemRefs, TmoToolMaterial material) {
		super(material.material);
		this.itemRefs = itemRefs;
		this.material = material;
		setUnlocalizedName(itemRefs.getUnlocalizedName());
		setRegistryName(itemRefs.getRegistryName());
		if(isEnabled()) setCreativeTab(TMORefs.TMO_TAB);
	}
	
	@Override
	public boolean isEnabled() {
		return material.isEnabled();
	}

	@Override
	public void registerRender() {
		ModelResourceLocation location = new ModelResourceLocation(TMORefs.PREFIX + itemRefs.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, location);
	}
	
}
