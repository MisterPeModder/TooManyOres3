package misterpemodder.tmo.main.client.render;

import misterpemodder.hc.main.client.render.TileEntityCustomChestRenderer;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityTitaniumChestRenderer extends TileEntityCustomChestRenderer<TileEntityTitaniumChest> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocationTmo("textures/entity/titanium_chest.png");
	
	public TileEntityTitaniumChestRenderer() {}
	
	@Override
	protected ResourceLocation getTexture() {
		return TEXTURE;
	}
	
}
