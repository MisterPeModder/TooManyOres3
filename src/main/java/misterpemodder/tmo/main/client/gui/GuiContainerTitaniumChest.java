package misterpemodder.tmo.main.client.gui;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Mouse;

import misterpemodder.tmo.main.client.gui.GuiTabs.EnumTabs;
import misterpemodder.tmo.main.init.ModBlocks;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.main.utils.TMOGuiUtils;
import misterpemodder.tmo.main.utils.TMOHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

public class GuiContainerTitaniumChest extends GuiContainerBase<ContainerTitaniumChest> {
	
	private static final String MAIN_TAB = "textures/gui/container/titanium_chest/main.png";
	
	public GuiContainerTitaniumChest(PlayerMainInvWrapper playerInv, TileEntityTitaniumChest te) {
        super(new ContainerTitaniumChest(te, playerInv));
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	}
	
	@Override
	protected void drawTeInv(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	    this.mc.getTextureManager().bindTexture(new ResourceLocation(TMOHelper.PREFIX + GuiContainerTitaniumChest.MAIN_TAB));
	    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.INV_SIZE_TE.height);
	}
	
	public void initGui() {
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {

	}
	

	@Override
	public List<EnumTabs> getTabs() {
		return EnumTabs.getTabs();
	}
	
}
