package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.MutablePair;

import misterpemodder.hc.main.client.gui.GuiContainerBase;
import misterpemodder.hc.main.client.gui.tabs.TabBase;
import misterpemodder.hc.main.client.gui.tabs.TabMain;
import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.inventory.slot.IHidableSlot;
import misterpemodder.hc.main.utils.StringUtils;
import misterpemodder.tmo.api.capability.io.IIOType;
import misterpemodder.tmo.main.capability.io.IOConfigHandlerMachine;
import misterpemodder.tmo.main.capability.io.IOState;
import misterpemodder.tmo.main.client.gui.GuiButtonToggle;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import misterpemodder.tmo.main.utils.EnumBlockSide;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class TabIO<C extends ContainerBase<TE>, TE extends TileEntityMachine> extends TabBase<C, TE> {
	
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
	
	public static final String ID = TMORefs.MOD_ID + ".io";
	
	private final IOConfigHandlerMachine configHandler;
	private static int ioIndex = 0;

	public TabIO(IOConfigHandlerMachine configHandler) {
		super(TabPos.BOTTOM_LEFT);
		this.configHandler = configHandler;
	}
	
	@Override
	public String getTabID() {
		return ID;
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
		return new TabTexture(TMORefs.TAB_LOCATION, new Point(96, 28), new Point(64, 28), new ResourceLocationTmo("textures/gui/container/io.png"), new Dimension(212, 100));
	}
	
	@Override
	public boolean shouldDisplaySlot(IHidableSlot slot) {
		return false;
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		FontRenderer fontrenderer = guiContainer.mc.fontRendererObj;
		boolean ioInfoDrawn = false;
		int y_offset = guiContainer.getBottomPartPos()-guiContainer.getGuiTop();
		
		guiContainer.drawString(fontrenderer, StringUtils.translate("gui.tab.io.autoPush"), 129, y_offset+16, 0xFFFFFF);
		guiContainer.drawString(fontrenderer, StringUtils.translate("gui.tab.io.autoPull"), 129, y_offset+36, 0xFFFFFF);
		
		for(GuiButton button: this.buttons) {
			if(button.id == RESET_BUTTON_ID) {
				if(button.isMouseOver()) {
					String key = "gui.tab.io.reset.desc.";
					List<String> text = Arrays.asList(TextFormatting.GRAY+""+TextFormatting.ITALIC+StringUtils.translate(key+"1"), TextFormatting.GRAY+""+TextFormatting.ITALIC+StringUtils.translate(key+"2"));
					int textWidth = Math.max(fontrenderer.getStringWidth(text.get(0)), fontrenderer.getStringWidth(text.get(1)));
					GuiContainerBase.addHoveringText(text, Math.min(textWidth, 250));
				}
			}
			EnumBlockSide side = getButtonSide(button.id);
			if(side != null) {
				if(button.enabled && button.isMouseOver()) {
					
					guiContainer.drawString(fontrenderer, side.getLocalizedName(), 9, y_offset+9, 0xFFFFFF);
					
					IIOType<?> type = getSelectedIOType();
					guiContainer.drawString(fontrenderer, configHandler.getIOStateConfig(side, type).getLocalizedNameColored(), 9, y_offset+60, 0xFFFFFF);
					ioInfoDrawn = true;
				}
			}
		}
		
		if(!ioInfoDrawn) {
			guiContainer.drawString(fontrenderer, "---", 9, y_offset+9, 0xFFFFFF);
			guiContainer.drawString(fontrenderer, TextFormatting.GRAY+"n/a", 9, y_offset+60, 0xFFFFFF);
		}
	}
	
	private IIOType<?> getSelectedIOType() {
		if(ioIndex < 0 || ioIndex >= configHandler.getValidIOTypes().length) {
			ioIndex = 0;
		}
		return configHandler.getValidIOTypes()[ioIndex];
	}
	
	private String getTypeButtonText() {
		return StringUtils.translate(getSelectedIOType().getUnlocalizedName());
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
		this.buttons.add(new GuiButton(RESET_BUTTON_ID, x+119, y+75, 88, 20, TextFormatting.RED+StringUtils.translate("gui.tab.io.reset")));
		
		this.buttons.add(new GuiButtonToggle(AUTO_PUSH_BUTTON_ID, x+119, y+16, getTileEntity().autoPush));
		this.buttons.add(new GuiButtonToggle(AUTO_PULL_BUTTON_ID, x+119, y+36, getTileEntity().autoPull));
		
		this.buttons.add(newIOButton(IO_UP_BUTTON_ID, x+71, y+9, 20, 20));
		this.buttons.add(newIOButton(IO_DOWN_BUTTON_ID, x+71, y+49, 20, 20));
		this.buttons.add(newIOButton(IO_FRONT_BUTTON_ID, x+71, y+29, 20, 20));
		this.buttons.add(newIOButton(IO_BACK_BUTTON_ID, x+91, y+49, 20, 20));
		this.buttons.add(newIOButton(IO_LEFT_BUTTON_ID, x+51, y+29, 20, 20));
		this.buttons.add(newIOButton(IO_RIGHT_BUTTON_ID, x+91, y+29, 20, 20));
	}
	
	private GuiButton newIOButton(int buttonId, int x, int y, int width, int height) {
		GuiButton button = new GuiButton(buttonId, x, y, width, height, "");
		EnumBlockSide side = getButtonSide(button.id);
		
		if(side != null && getTileEntity().isSideDisabled(side)) {
			button.enabled = false;
		}
		
		return button;
	}
	
	@Override
	public void updateButtons() {
		for(GuiButton b : buttons) {
			if(b.id == INPUT_TYPE_BUTTON_ID) {
				b.displayString	= getTypeButtonText();
			}
			else if(b.id == RESET_BUTTON_ID) {
				if(GuiScreen.isShiftKeyDown()) {
					b.displayString = TextFormatting.DARK_RED+StringUtils.translate("gui.tab.io.reset.all");
				} else {
					b.displayString = TextFormatting.RED+StringUtils.translate("gui.tab.io.reset");
				}
			}
			else if(b.id == AUTO_PUSH_BUTTON_ID) {
				((GuiButtonToggle)b).toggled = getTileEntity().autoPush;
			}
			else if(b.id == AUTO_PULL_BUTTON_ID) {
				((GuiButtonToggle)b).toggled = getTileEntity().autoPull;
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
					NBTTagCompound data = new NBTTagCompound();
					boolean newMode = !((GuiButtonToggle) button).toggled;
					data.setBoolean("autoPushPull", newMode);
					TabBase.sendButtonPacket(getTabID(), button.id, guiContainer.mc.world, getTileEntity().getPos(), data);
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
		if(side == null || getTileEntity() == null) return;
		IIOType<?> type = getSelectedIOType();
		boolean shift = GuiScreen.isShiftKeyDown();
		
		List<IOState> states = getTileEntity().getIOStatesForIOType().get(type);
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
