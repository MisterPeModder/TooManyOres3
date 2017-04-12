package misterpemodder.tmo.main.potion;

import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionBase extends Potion {
	
	protected final int iconX;
	protected final int iconY;
	
	public static final int TEXTURE_SIZE = 128;
	public static final int ICON_SIZE = 18;
	public static final ResourceLocation ICONS_LOCATION = new ResourceLocationTmo("textures/gui/potion_icons.png");

	public PotionBase(String registryName, String unlocalizedName, boolean isBadEffectIn, int liquidColorIn, int iconX, int iconY) {
		super(isBadEffectIn, liquidColorIn);
		this.iconX = iconX;
		this.iconY = iconY;
		this.setRegistryName(new ResourceLocationTmo(registryName));
		this.setPotionName("effect.tmo."+unlocalizedName);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
		mc.getTextureManager().bindTexture(ICONS_LOCATION);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 64+(isBeneficial()?32:0), 120, 32, TEXTURE_SIZE, TEXTURE_SIZE);
		Gui.drawModalRectWithCustomSizedTexture(x+6, y+7, getIconTextureX(), getIconTextureY(), ICON_SIZE, ICON_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
	}

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(int x, int y, PotionEffect effect, Minecraft mc, float alpha) { 
        mc.getTextureManager().bindTexture(ICONS_LOCATION);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 80+(isBeneficial()?24:0), 0, 24, 24, TEXTURE_SIZE, TEXTURE_SIZE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
		Gui.drawModalRectWithCustomSizedTexture(x+3, y+3, getIconTextureX(), getIconTextureY(), ICON_SIZE, ICON_SIZE, TEXTURE_SIZE, TEXTURE_SIZE);
    	
    }
    
    public int getIconTextureX() {
    	return iconY*ICON_SIZE+iconY*2+1;
    }
    
    public int getIconTextureY() {
    	return iconX*ICON_SIZE+iconX*2+1;
    }
    
    @Override
    public boolean hasStatusIcon() {
    	return iconX>0 && iconY>0;
    }
    
}
