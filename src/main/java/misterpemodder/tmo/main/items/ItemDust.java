package misterpemodder.tmo.main.items;

import misterpemodder.tmo.main.items.ItemVariant.DustVariant;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ItemDust extends ItemMulti<ItemVariant.DustVariant> {

	public ItemDust() {
		super(EnumItemsNames.DUST, DustVariant.dustVariants, "_dust");
	}
	
	@Override
	public void registerRender() {
		for(DustVariant v : variants) {
			String name = v.getName();
			ModelResourceLocation location = new ModelResourceLocation(TMORefs.PREFIX + name + suffix + (v.isDirty? "_dirty" : ""), name);
			ModelLoader.setCustomModelResourceLocation(this,  v.getMeta(), location);
		}
	}

}
