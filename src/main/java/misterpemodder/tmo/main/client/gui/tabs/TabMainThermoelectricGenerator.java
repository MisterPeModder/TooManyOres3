package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import misterpemodder.hc.main.client.gui.GuiContainerBase;
import misterpemodder.hc.main.client.gui.tabs.TabMain;
import misterpemodder.hc.main.inventory.elements.ContainerElementVerticalEnergyBar;
import misterpemodder.hc.main.inventory.slot.IHidableSlot;
import misterpemodder.hc.main.inventory.slot.SlotHidable;
import misterpemodder.hc.main.utils.StringUtils;
import misterpemodder.tmo.main.blocks.BlockMachineCasing.EnumMachineCasingVariant;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.inventory.ContainerThermoelectricGenerator;
import misterpemodder.tmo.main.inventory.elements.ContainerElementTank;
import misterpemodder.tmo.main.tileentity.TileEntityThemoelectricGenerator;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import misterpemodder.tmo.main.utils.TemperatureUtils;
import misterpemodder.tmo.main.utils.TemperatureUtils.TemperatureUnit;
import net.minecraft.client.gui.Gui;
import net.minecraft.inventory.Slot;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

public class TabMainThermoelectricGenerator extends TabMain<ContainerThermoelectricGenerator, TileEntityThemoelectricGenerator> {
	
	public static final String ID = "tmo.main.thermoElectricGenerator";
	
	@Override
	public String getTabID() {
		return ID;
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,0), new Point(32, 0), new ResourceLocationTmo("textures/gui/container/thermo_generator.png"), new Dimension(212, 100));
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		FluidStack leftStack = null;
		FluidStack rightStack = null;
		
		if(getLeftTank() != null) {
			getLeftTank().drawTank(mouseX, mouseY, guiContainer);
			leftStack = getLeftTank().getFluid();
		}
		if(getRightTank() != null) {
			getRightTank().drawTank(mouseX, mouseY, guiContainer);
			rightStack = getRightTank().getFluid();
		}
		if(getEnergyBar() != null) {
			getEnergyBar().drawEnergyBar(guiContainer.getGuiLeft()+68, guiContainer.getGuiTop()+13);
		}
		
		guiContainer.mc.getTextureManager().bindTexture(this.getTabTexture().screenTexture);
		int p = (guiContainer.container.getSmallEnergyBarFillPercent()*16)/100;
		if(p > 0) {
			Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+186, guiContainer.getGuiTop()+90, 60, 100, p, 4, 256, 128);
		}
		
		if(leftStack != null && rightStack != null && guiContainer.container.getTileEntity().isMachineWorking()) {
			
			int lt = leftStack.getFluid().getTemperature(leftStack);
			int rt = rightStack.getFluid().getTemperature(rightStack);
			
			Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+51, guiContainer.getGuiTop()+47, lt > rt? 0 : 15, 100, 15, 15, 256, 128);
			Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+88, guiContainer.getGuiTop()+47, rt > lt? 30 : 45, 100, 15, 15, 256, 128);
		}
		
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		int ox = guiContainer.getGuiLeft();
		int oy = guiContainer.getGuiTop();

		FluidStack leftStack = getLeftTank().getFluid();
		FluidStack rightStack = getRightTank().getFluid();
		
		TileEntityThemoelectricGenerator te = guiContainer.container.getTileEntity();
		EnumMachineCasingVariant casing = te.getWorld().getBlockState(te.getPos()).getValue(BlockMachine.CASING);
		
		int diff = 0;
		int p = 0;
		int c = te.isMachineWorking()? TileEntityThemoelectricGenerator.getFluidConsumption(casing) : 0;
		TemperatureUnit unit = TemperatureUtils.getTemperatureUnit();
		
		if(leftStack != null && rightStack != null) {
			diff = (int) Math.abs(unit.fromKelvin(leftStack.getFluid().getTemperature(leftStack)) - unit.fromKelvin(rightStack.getFluid().getTemperature(rightStack)));
			if(te.isMachineWorking())p = TileEntityThemoelectricGenerator.calcultatePower(leftStack, rightStack, casing);
		}
		guiContainer.drawString(guiContainer.getFontRenderer(),(diff <= 0? TextFormatting.GRAY : TextFormatting.WHITE) + "" +  diff+ " " + unit.symbol, 156, 16, 0xFFFFFF);
		guiContainer.drawString(guiContainer.getFontRenderer(),(p <= 0? TextFormatting.GRAY : TextFormatting.WHITE) + "" +  p+" RF/t", 156, 33, 0xFFFFFF);
		guiContainer.drawString(guiContainer.getFontRenderer(),(c <= 0? TextFormatting.GRAY : TextFormatting.WHITE) + "" + c+" Mb/t", 156, 50, 0xFFFFFF);
		
		List<String> strs = new ArrayList<>();
		
		Slot slot = guiContainer.getSlotUnderMouse();
		if(slot != null && guiContainer.container.inventorySlots.contains(slot)) {
			int i = guiContainer.container.inventorySlots.indexOf(slot);
			String str = "";
			if(i == 41) {
				str = "gui.thermoElectricGenerator.slot.charging";
			} else if(i == 43) {
				str = "gui.thermoElectricGenerator.slot.leftTank";
			} else if(i == 44) {
				str = "gui.thermoElectricGenerator.slot.rightTank";
			}
			
			if(!str.isEmpty()) {
				strs.add(StringUtils.translate(str));
			}
		} 
		
		if(strs.isEmpty()) {
			ContainerElementTank tank = getLeftTank() != null && getLeftTank().isTankHovered(mouseX, mouseY, guiContainer)? getLeftTank() : getRightTank().isTankHovered(mouseX, mouseY, guiContainer)? getRightTank() : null;
			
			if(tank != null) {
				strs.addAll(tank.getHoverDesc(mouseX, mouseY, guiContainer));
			}
			if(getEnergyBar() != null && guiContainer.isPointInRegion(68, 13, 18, 82, mouseX, mouseY)) {
				strs.addAll(getEnergyBar().getDisplayText());
			}
			else if(guiContainer.isPointInRegion(184, 88, 20, 8, mouseX, mouseY)) {
				strs.add(guiContainer.container.getSmallEnergyBarFillPercent() + "%");
			}
		}
		
		if(strs.isEmpty() && mouseX > ox+156 && mouseX < ox+204 && mouseY > oy+13 && mouseY < 65+oy) {
			if(mouseY < 33+oy) {
				strs.add(StringUtils.translate("gui.thermoElectricGenerator.text.temperature.desc"));
			} else if(mouseY < 50+oy) {
				strs.add(StringUtils.translate("gui.thermoElectricGenerator.text.power.desc.1"));
				float powerBonus = TileEntityThemoelectricGenerator.getPowerBonus(casing);
				if(powerBonus > 1.0F) {
					float f = Math.round((powerBonus-1.0F)*100.0F);
					strs.add(String.format(TextFormatting.GRAY+"%s: "+TextFormatting.GREEN+"+%d", StringUtils.translate("gui.thermoElectricGenerator.text.power.desc.2"), (int)f)+"%");
				}
			} else {
				strs.add(StringUtils.translate("gui.thermoElectricGenerator.text.fluid.desc.1"));
				int fluidConsumption = TileEntityThemoelectricGenerator.getFluidConsumption(casing);
				if(fluidConsumption > 1) {
					int i = (fluidConsumption-1)*100;
					strs.add(String.format(TextFormatting.GRAY+"%s: "+TextFormatting.RED+"+%d", StringUtils.translate("gui.thermoElectricGenerator.text.fluid.desc.2"), i)+"%");
				}
			}
		}
		
		if(!strs.isEmpty()) {
			GuiContainerBase.addHoveringText(strs, 250);
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		if(getLeftTank() != null) {
			getLeftTank().mouseClicked(mouseX, mouseY, mouseButton, guiContainer);
		}
		if(getRightTank() != null) {
			getRightTank().mouseClicked(mouseX, mouseY, mouseButton, guiContainer);
		}
	}
	
	@Override
	public boolean shouldDisplaySlot(IHidableSlot slot) {
		if(slot instanceof SlotHidable) {
			TileEntityThemoelectricGenerator te = getTileEntity();
			IItemHandler h = ((SlotHidable)slot).getItemHandler();
			return h == te.getInventory() || h == te.getTankBucketSlots();
		}
		return false;
	}
	
	private ContainerElementTank getLeftTank() {
		return guiContainer.container.leftTank;
	}
	
	private ContainerElementTank getRightTank() {
		return guiContainer.container.rightTank;
	}
	
	private ContainerElementVerticalEnergyBar getEnergyBar() {
		return guiContainer.container.energyBar;
	}
	
	protected int getTitleX() {
		return 6;
	}
	
	protected int getTitleY() {
		return 5;
	}
	
}
