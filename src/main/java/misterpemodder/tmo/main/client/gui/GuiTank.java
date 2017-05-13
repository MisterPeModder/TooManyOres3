package misterpemodder.tmo.main.client.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.tabs.TabBase;
import misterpemodder.tmo.main.client.gui.tabs.TabBase.TabID;
import misterpemodder.tmo.main.client.render.RenderTank;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class GuiTank extends Gui {
	
	public static final int CLEAR_TANK_BUTTON_ID = 101;
	public static final int HEIGHT = 80;
	public static final int WIDTH = 40;
	
	public static final ResourceLocation TANK_TEXTURE = new ResourceLocationTmo("textures/gui/container/misc.png");
	
	private FluidTank tank;
	private GuiContainerBase container;
	
	private final int x;
	private final int y;
	
	public final short id;
	
	public GuiTank(int id, FluidTank tank, GuiContainerBase container, int x, int y) {
		this.id = (short) id;
		this.tank = tank;
		this.container = container;
		this.x = x;
		this.y = y;
	}
	
	public void drawTank(int mouseX, int mouseY) {
		RenderTank.renderTankInGui(tank.getFluid(), tank.getCapacity(), container.getGuiLeft()+x, container.getGuiTop()+y, WIDTH, HEIGHT);
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TANK_TEXTURE);
		Gui.drawModalRectWithCustomSizedTexture(container.getGuiLeft()+x, container.getGuiTop()+y, 0, 0, WIDTH, HEIGHT, 128, 128);

		Gui.drawModalRectWithCustomSizedTexture(container.getGuiLeft()+x+29, container.getGuiTop()+y, 40+(isButtonHovered(mouseX, mouseY)?11:0), 0, 11, 11, 128, 128);
		GlStateManager.disableBlend();
	}
	
	public List<String> getHoverDesc(int mouseX, int mouseY) {
		List<String> strs = new ArrayList<>();
		if(isButtonHovered(mouseX, mouseY)) {
			strs.add(TextFormatting.RED+""+TextFormatting.BOLD+Tmo.proxy.translate("gui.tank.clear"));
			if(GuiScreen.isShiftKeyDown()) {
				strs.add(TextFormatting.RED+Tmo.proxy.translate("gui.tank.clear.confirm"));
			} else {
				strs.add(TextFormatting.GRAY+""+TextFormatting.ITALIC+"-"+Tmo.proxy.translate("gui.tank.clear.hint")+"-");
			}
		} else if(container.isPointInRegion(x, y, WIDTH, HEIGHT, mouseX, mouseY)) {
			FluidStack stack = tank.getFluid();
		
			if(stack == null || stack.getFluid() == null || stack.amount <= 0) {
				strs = Arrays.asList(Tmo.proxy.translate("gui.tank.empty"));
			} else {
				strs = Arrays.asList(stack.getFluid().getRarity(stack).rarityColor+stack.getLocalizedName(), Tmo.proxy.translate("gui.tank.contents", stack.amount, tank.getCapacity()));
			}
    	}
		return strs;
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(isButtonHovered(mouseX, mouseY) && GuiScreen.isShiftKeyDown() && mouseButton == 0) {
			
			if(this.tank.getFluidAmount() > 0) {
				SoundEvent soundevent = tank.getFluid().getFluid().getEmptySound(tank.getFluid());
				EntityPlayerSP player = Minecraft.getMinecraft().player;
				player.playSound(soundevent, 1.0F, 1.0F);
				tank.drain(TileEntityInjector.CAPACITY, true);
				NBTTagCompound data = new NBTTagCompound();
				data.setShort("tank_id", id);
				TabBase.sendButtonPacket(TabID.MISC, CLEAR_TANK_BUTTON_ID, container.mc.world, container.container.getTileEntity().getPos(), data);
			}
			
			return true;
		}
		return false;
	}
	
	private boolean isButtonHovered(int mouseX, int mouseY) {
		return container.isPointInRegion(x+29, y, 11, 11, mouseX, mouseY);
	}

}
