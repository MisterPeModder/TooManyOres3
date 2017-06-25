package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.GuiButtonToggle;
import misterpemodder.tmo.main.inventory.ContainerBase;
import misterpemodder.tmo.main.inventory.slot.IHidable;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;

public class TabIO<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase {
	
	public static final int INPUT_TYPE_BUTTON_ID = 30;
	public static final int INPUT_CHANGE_LEFT_BUTTON_ID = 31;
	public static final int INPUT_CHANGE_RIGHT_BUTTON_ID = 32;
	public static final int RESET_BUTTON_ID = 33;
	public static final int AUTO_PUSH_BUTTON_ID = 34;
	public static final int AUTO_PULL_BUTTON_ID = 35;
	
	private static String[] text = {
		"all",
		"items",
		"fluids",
		"redstone",
		"energy"
	};
	
	private static int ioIndex = 0;

	public TabIO() {
		super(TabPos.BOTTOM_LEFT);
	}
	
	@Override
	public TabID getTabID() {
		return TabID.IO;
	}
	
	@Override
	public MutablePair<TabBase,TabBase> forceTabConfig() {
		for(TabBase tab : (List<TabBase>)guiContainer.getRegisteredTabs()) {
			if(tab instanceof TabMain) {
				return MutablePair.of(tab, (TabBase) this);
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
	
	private String getTypeButtonText() {
		if(ioIndex < 0 || ioIndex >= text.length) {
			ioIndex = 0;
		}
		return Tmo.proxy.translate("gui.tab.io.type."+text[ioIndex]);
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
		}
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		for(Object button: this.buttons) {
			if(((GuiButton)button).id == RESET_BUTTON_ID) {
				if(((GuiButton)button).isMouseOver()) {
					String key = "gui.tab.io.reset.desc.";
					List<String> text = Arrays.asList(TextFormatting.GRAY+""+TextFormatting.ITALIC+Tmo.proxy.translate(key+"1"), TextFormatting.GRAY+""+TextFormatting.ITALIC+Tmo.proxy.translate(key+"2"));
					GuiUtils.drawHoveringText(text, mouseX-guiContainer.getGuiLeft(), mouseY-guiContainer.getGuiTop(), guiContainer.width, guiContainer.height, 200, guiContainer.getFontRenderer());
				}
			}
		}
	}

}
