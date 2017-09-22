package misterpemodder.tmo.main.inventory.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import misterpemodder.hc.main.client.gui.GuiContainerBase;
import misterpemodder.hc.main.client.gui.tabs.TabBase;
import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.inventory.elements.ISyncedContainerElement;
import misterpemodder.hc.main.utils.StringUtils;
import misterpemodder.tmo.main.client.render.RenderTank;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import misterpemodder.tmo.main.utils.TemperatureUtils;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerElementTank extends Gui implements ISyncedContainerElement {

	public static final int CLEAR_TANK_BUTTON_ID = 101;
	public static final int HEIGHT = 80;
	public static final int WIDTH = 40;

	public static final ResourceLocation TANK_TEXTURE = new ResourceLocationTmo("textures/gui/container/misc.png");
	
	private FluidTank tank;
	private FluidStack lastFluid;
	private ContainerBase<?> container;

	private final int x;
	private final int y;
	private final boolean invertedTank;
	private final boolean isButtonLeft;

	public final short id;
	
	public ContainerElementTank(int id, ContainerBase<?> container, int x, int y, FluidTank tank) {
		this(id, container, x, y, tank, false, false);
	}

	public ContainerElementTank(int id, ContainerBase<?> container, int x, int y, FluidTank tank, boolean invertedTank, boolean isButtonLeft) {
		this.id = (short) id;
		this.container = container;
		this.tank = tank;
		this.x = x;
		this.y = y;
		this.invertedTank = invertedTank;
		this.isButtonLeft = isButtonLeft;
	}

	public void drawTank(int mouseX, int mouseY, GuiContainerBase<?,?> c) {
		RenderTank.renderTankInGui(tank.getFluid(), tank.getCapacity(), c.getGuiLeft() + x, c.getGuiTop() + y, WIDTH, HEIGHT);
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TANK_TEXTURE);
		Gui.drawModalRectWithCustomSizedTexture(c.getGuiLeft() + x, c.getGuiTop() + y, invertedTank? WIDTH : 0, 0, WIDTH, HEIGHT, 128, 128);

		Gui.drawModalRectWithCustomSizedTexture(c.getGuiLeft() + x + (isButtonLeft? 0 : 29), c.getGuiTop() + y, 80 + (isButtonHovered(mouseX, mouseY, c) ? 11 : 0), 0, 11, 11, 128, 128);
		GlStateManager.disableBlend();
	}
	
	public List<String> getHoverDesc(int mouseX, int mouseY, GuiContainerBase<?,?> c) {
		return this.getHoverDesc(mouseX, mouseY, c, false);
	}

	public List<String> getHoverDesc(int mouseX, int mouseY, GuiContainerBase<?,?> c, boolean showTemperature) {
		List<String> strs = new ArrayList<>();
		if (isButtonHovered(mouseX, mouseY, c)) {
			strs.add(TextFormatting.RED + "" + TextFormatting.BOLD + StringUtils.translate("gui.tank.clear"));
			if (GuiScreen.isShiftKeyDown()) {
				strs.add(TextFormatting.RED + StringUtils.translate("gui.tank.clear.confirm"));
			} else {
				strs.add(TextFormatting.GRAY + "" + TextFormatting.ITALIC + "-" + StringUtils.translate("gui.tank.clear.hint") + "-");
			}
		} else if (isTankHovered(mouseX, mouseY, c)) {
			FluidStack stack = tank.getFluid();

			if (stack == null || stack.getFluid() == null || stack.amount <= 0) {
				strs = Arrays.asList(StringUtils.translate("gui.tank.empty"));
			} else {
				strs = Arrays.asList(stack.getFluid().getRarity(stack).rarityColor + stack.getLocalizedName(), StringUtils.translate("gui.tank.contents", stack.amount, tank.getCapacity()), TemperatureUtils.getFluidTemperatureString(stack, true));
			}
		}
		return strs;
	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton, GuiContainerBase<?,?> c) {
		if (isButtonHovered(mouseX, mouseY, c) && GuiScreen.isShiftKeyDown() && mouseButton == 0) {

			if (this.tank.getFluidAmount() > 0) {
				SoundEvent soundevent = tank.getFluid().getFluid().getEmptySound(tank.getFluid());
				EntityPlayerSP player = Minecraft.getMinecraft().player;
				player.playSound(soundevent, 1.0F, 1.0F);
				tank.drain(TileEntityInjector.CAPACITY, true);
				NBTTagCompound data = new NBTTagCompound();
				data.setShort("tank_id", id);
				TabBase.sendButtonPacket(TabBase.MISC_TAB_ID, CLEAR_TANK_BUTTON_ID, c.mc.world, container.getTileEntity().getPos(), data);
			}

			return true;
		}
		return false;
	}

	private boolean isButtonHovered(int mouseX, int mouseY, GuiContainerBase<?,?> c) {
		return c.isPointInTheRegion(x + (isButtonLeft? 0 : 29), y, 11, 11, mouseX, mouseY);
	}
	
	public boolean isTankHovered(int mouseX, int mouseY, GuiContainerBase<?,?> c) {
		return c.isPointInTheRegion(x, y, WIDTH, HEIGHT, mouseX, mouseY);
	}

	@Override
	public boolean shouldSendDataToClient() {
		boolean b = false;
		if(tank.getFluid() != null) {
			b = !tank.getFluid().isFluidStackIdentical(lastFluid);
		} else if(lastFluid != null){
			b = true;
		}
		lastFluid = tank.getFluid() == null? null : tank.getFluid().copy();
		return b;
	}

	@Override
	public NBTTagCompound writeData(NBTTagCompound c) {
		tank.writeToNBT(c);
		return c;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void procData(NBTTagCompound data) {
		tank.readFromNBT(data);
	}
	
	@Nullable
	public FluidStack getFluid() {
		if(tank.getFluidAmount() > 0) {
			return tank.getFluid().copy();
		}
		return null;
	}

}
