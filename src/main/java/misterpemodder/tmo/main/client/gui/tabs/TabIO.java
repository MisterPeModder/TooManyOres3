package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.MutablePair;

import misterpemodder.tmo.api.capability.io.IIOType;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.capability.io.IOConfigHandlerMachine;
import misterpemodder.tmo.main.capability.io.IOState;
import misterpemodder.tmo.main.client.gui.GuiButtonToggle;
import misterpemodder.tmo.main.inventory.ContainerBase;
import misterpemodder.tmo.main.inventory.slot.IHidable;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import misterpemodder.tmo.main.utils.EnumBlockSide;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;

public class TabIO<C extends ContainerBase<TE>, TE extends TileEntityMachine<?>> extends TabBase<C, TE> {
	
	public static final int INPUT_TYPE_BUTTON_ID = 30;
	public static final int INPUT_CHANGE_LEFT_BUTTON_ID = 31;
	public static final int INPUT_CHANGE_RIGHT_BUTTON_ID = 32;
	public static final int RESET_BUTTON_ID = 33;
	public static final int AUTO_PUSH_BUTTON_ID = 34;
	public static final int AUTO_PULL_BUTTON_ID = 35;
	
	public static final int IO_UP_BUTTON_ID = 36;
	public static final int IO_DOWN_BUTTON_ID = 37;
	public static final int IO_FRONT_BUTTON_ID = 38;
	public static final int IO_BACK_BUTTON_ID = 39;
	public static final int IO_LEFT_BUTTON_ID = 40;
	public static final int IO_RIGHT_BUTTON_ID = 41;
	
	private final IOConfigHandlerMachine configHandler;
	private static int ioIndex = 0;

	public TabIO(IOConfigHandlerMachine configHandler) {
		super(TabPos.BOTTOM_LEFT);
		this.configHandler = configHandler;
	}
	
	@Override
	public TabID getTabID() {
		return TabID.IO;
	}
	
	@Override
	public MutablePair<TabBase<C, TE>, TabBase<C, TE>> forceTabConfig() {
		for(TabBase<C, TE> tab : guiContainer.getRegisteredTabs()) {
			if(tab instanceof TabMain) {
				return MutablePair.of(tab, this);
			}
		}
		return super.forceTabConfig();
	}

	@Override
	public String getUnlocalizedName() {
		return "gui.tab.io.name";
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Blocks.HOPPER);
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(96,28), new Point(64, 28), new ResourceLocationTmo("textures/gui/container/io.png"), new Dimension(212, 100));
	}
	
	@Override
	public boolean shouldDisplaySlot(IHidable slot) {
		return false;
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		FontRenderer fontrenderer = guiContainer.mc.fontRendererObj;
		for(Object button: this.buttons) {
			if(((GuiButton)button).id == RESET_BUTTON_ID) {
				if(((GuiButton)button).isMouseOver()) {
					String key = "gui.tab.io.reset.desc.";
					List<String> text = Arrays.asList(TextFormatting.GRAY+""+TextFormatting.ITALIC+Tmo.proxy.translate(key+"1"), TextFormatting.GRAY+""+TextFormatting.ITALIC+Tmo.proxy.translate(key+"2"));
					int textWidth = Math.max(fontrenderer.getStringWidth(text.get(0)), fontrenderer.getStringWidth(text.get(1)));
					GuiUtils.drawHoveringText(text, mouseX-guiContainer.getGuiLeft(), mouseY-guiContainer.getGuiTop(), guiContainer.width, guiContainer.height, textWidth, guiContainer.getFontRenderer());
				}
			}
			EnumBlockSide side = getButtonSide(((GuiButton)button).id);
			if(side != null) {
				if(((GuiButton)button).isMouseOver()) {
					int y_offset = guiContainer.getBottomPartPos()-guiContainer.getGuiTop();
					guiContainer.drawString(fontrenderer, side.getLocalizedName(), 9, y_offset+9, 0xFFFFFF);
					
					IIOType<?> type = getSelectedIOType();
					guiContainer.drawString(fontrenderer, configHandler.getIOStateConfig(side, type).getLocalizedNameColored(), 9, y_offset+60, 0xFFFFFF);
				}
			}
		}
	}
	
	private IIOType<?> getSelectedIOType() {
		if(ioIndex < 0 || ioIndex >= configHandler.getValidIOTypes().length) {
			ioIndex = 0;
		}
		return configHandler.getValidIOTypes()[ioIndex];
	}
	
	private String getTypeButtonText() {
		return Tmo.proxy.translate(getSelectedIOType().getUnlocalizedName());
	}
	
	@Override
	public void initButtons(int topX, int topY) {
		int x = guiContainer.getGuiLeft();
		int y = guiContainer.getBottomPartPos();
		this.buttons.add(new GuiButton(INPUT_TYPE_BUTTON_ID, x+26, y+75, 68, 20, getTypeButtonText()) {
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				return false;
			}
		});
		this.buttons.add(new GuiButton(INPUT_CHANGE_LEFT_BUTTON_ID, x+5, y+75, 20, 20, "<"));
		this.buttons.add(new GuiButton(INPUT_CHANGE_RIGHT_BUTTON_ID, x+95, y+75, 20, 20, ">"));
		this.buttons.add(new GuiButton(RESET_BUTTON_ID, x+119, y+75, 88, 20, TextFormatting.RED+Tmo.proxy.translate("gui.tab.io.reset")));
		
		this.buttons.add(new GuiButtonToggle(AUTO_PUSH_BUTTON_ID, x+119, y+16, false));
		this.buttons.add(new GuiButtonToggle(AUTO_PULL_BUTTON_ID, x+119, y+36, false, true));
		
		this.buttons.add(new GuiButton(IO_UP_BUTTON_ID, x+71, y+9, 20, 20, ""));
		this.buttons.add(new GuiButton(IO_DOWN_BUTTON_ID, x+71, y+49, 20, 20, ""));
		this.buttons.add(new GuiButton(IO_FRONT_BUTTON_ID, x+71, y+29, 20, 20, ""));
		this.buttons.add(new GuiButton(IO_BACK_BUTTON_ID, x+91, y+49, 20, 20, ""));
		this.buttons.add(new GuiButton(IO_LEFT_BUTTON_ID, x+51, y+29, 20, 20, ""));
		this.buttons.add(new GuiButton(IO_RIGHT_BUTTON_ID, x+91, y+29, 20, 20, ""));
	}
	
	@Override
	public void updateButtons() {
		for(GuiButton b : (List<GuiButton>)buttons) {
			if(b.id == INPUT_TYPE_BUTTON_ID) {
				b.displayString	= getTypeButtonText();
			}
		}
	}
	
	@Override
	public void onButtonClicked(GuiButton button) {
		
		switch(button.id) {
			case INPUT_CHANGE_LEFT_BUTTON_ID:
				ioIndex--;
			break;
			case INPUT_CHANGE_RIGHT_BUTTON_ID:
				ioIndex++;
			break;
			case AUTO_PUSH_BUTTON_ID:
			case AUTO_PULL_BUTTON_ID:
				if(button instanceof GuiButtonToggle) {
					GuiButtonToggle b = (GuiButtonToggle)button;
					b.toggled = !b.toggled;
				}
			break;
			case RESET_BUTTON_ID:
				resetConfig(GuiScreen.isShiftKeyDown());
			break;
			default:
				changeIOConfig(button.id);
		}
	}
	
	@Nullable
	private EnumBlockSide getButtonSide(int buttonID) {
		EnumBlockSide side = null;
		switch(buttonID) {
			case IO_UP_BUTTON_ID:
				side = EnumBlockSide.UP;
			break;
			case IO_DOWN_BUTTON_ID:
				side = EnumBlockSide.DOWN;
			break;
			case IO_FRONT_BUTTON_ID:
				side = EnumBlockSide.FRONT;
			break;
			case IO_BACK_BUTTON_ID:
				side = EnumBlockSide.BACK;
			break;
			case IO_LEFT_BUTTON_ID:
				side = EnumBlockSide.LEFT;
			break;
			case IO_RIGHT_BUTTON_ID:
				side = EnumBlockSide.RIGHT;
			break;
		}
		return side;
	}
	
	private void changeIOConfig(int buttonID) {
		EnumBlockSide side = getButtonSide(buttonID);
		if(side == null || guiContainer.container.getTileEntity() == null) return;
		IIOType<?> type = getSelectedIOType();
		boolean shift = GuiScreen.isShiftKeyDown();
		
		List<IOState> states = ((TileEntityMachine<?>)guiContainer.container.getTileEntity()).getIOStatesForIOType().get(type);
		IOState state = configHandler.getIOStateConfig(side, type);
		IOState nextState = IOState.DISABLED;
			
		if(!shift && !states.isEmpty()) {
			if(states.contains(state)) {
				int i = states.indexOf(state)+1;
				nextState = i >= states.size()? IOState.DISABLED : states.get(i);
			} else if(state == IOState.DISABLED) {
				nextState = states.get(0);
			}
		}
		configHandler.setIOStateConfig(side, type, nextState);
	}
	
	private void resetConfig(boolean resetAllTypes) {
		for(EnumBlockSide side : EnumBlockSide.values()) {
			if(resetAllTypes) {
				for(IIOType<?> type : configHandler.getValidIOTypes()) {
					configHandler.setIOStateConfig(side , type, IOState.DISABLED);
				}
			}
			else {
				configHandler.setIOStateConfig(side , getSelectedIOType(), IOState.DISABLED);
			}
		}
	}

}
