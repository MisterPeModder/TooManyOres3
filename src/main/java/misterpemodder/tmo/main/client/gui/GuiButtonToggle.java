package misterpemodder.tmo.main.client.gui;

import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonToggle extends GuiButton {
	
	public static final ResourceLocation BUTTON_TEXTURE = new ResourceLocationTmo("textures/gui/container/misc.png");

	public boolean toggled;
	
	public GuiButtonToggle(int buttonId, int x, int y, boolean toggled) {
		this(buttonId, x, y, toggled, true);
	}
	
	public GuiButtonToggle(int buttonId, int x, int y, boolean toggled, boolean enabled) {
		super(buttonId, x, y, 8, 8, "");
		this.toggled = toggled;
		this.enabled = enabled;
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(BUTTON_TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            
            Gui.drawModalRectWithCustomSizedTexture(xPosition, yPosition, 80+i*width+(toggled? 3*width:0), 11, width, height, 128, 128);
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

}
