package misterpemodder.tmo.main.compat.jei.injector;

import mezz.jei.api.gui.BlankAdvancedGuiHandler;
import misterpemodder.tmo.main.client.gui.GuiContainerInjector;

public class AdvancedGuiHandlerInjector extends BlankAdvancedGuiHandler<GuiContainerInjector>{

	@Override
	public Class<GuiContainerInjector> getGuiContainerClass() {
		return GuiContainerInjector.class;
	}
	
	@Override
	public Object getIngredientUnderMouse(GuiContainerInjector guiContainer, int mouseX, int mouseY) {
		if(!guiContainer.isPointInTheRegion(40, 10, 11, 11, mouseX, mouseY) && guiContainer.isPointInTheRegion(11, 10, 40, 80, mouseX, mouseY)) {
			return guiContainer.container.getTileEntity().getTank().getFluid();
		}
		if(guiContainer.getSlotUnderMouse() != null) {
			return guiContainer.getSlotUnderMouse().getStack();
		}
		return null;
	}

}
