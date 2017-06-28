package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.MutablePair;

import misterpemodder.tmo.api.io.IIOConfigHandler;
import misterpemodder.tmo.api.io.IIOType;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.client.gui.GuiButtonToggle;
import misterpemodder.tmo.main.inventory.ContainerBase;
import misterpemodder.tmo.main.inventory.slot.IHidable;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.EnumBlockSide;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;

public class TabIO<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase {
	
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
	
	private final IIOConfigHandler configHandler;
	private static int ioIndex = 0;

	public TabIO(IIOConfigHandler configHandler) {
		super(TabPos.BOTTOM_LEFT);
		this.configHandler = configHandler;
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
					
					IIOType type = getSelectedIOType();
					EnumFacing facing = side.toFacing(getMachineFacing());
					boolean isInput = configHandler.isSideInput(facing, type);
					boolean isOutput = configHandler.isSideOutput(facing, type);
					
					int state = isInput == isOutput? (isInput? 3 : 0) : (isInput? 1 : 2);
					TextFormatting color = state == 0? TextFormatting.GRAY : state == 1? TextFormatting.AQUA : state == 2? TextFormatting.GOLD : TextFormatting.GREEN;
					
					guiContainer.drawString(fontrenderer, color + Tmo.proxy.translate("gui.tab.io.state."+state), 9, y_offset+60, 0xFFFFFF);
				}
			}
		}
	}
	
	private IIOType getSelectedIOType() {
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
	
	private EnumFacing getMachineFacing() {
		TileEntityContainerBase te = guiContainer.container.getTileEntity();
		IBlockState state = te.getWorld().getBlockState(te.getPos());
		if(state.getValue(BlockMachine.FACING) != null) {
			return state.getValue(BlockMachine.FACING);
		} else {
			return EnumFacing.NORTH;
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
		if(side == null) return;
		EnumFacing facing = side.toFacing(getMachineFacing());
		IIOType type = getSelectedIOType();
		boolean isInput = configHandler.isSideInput(facing, type);
		boolean isOutput = configHandler.isSideOutput(facing, type);
		
		if(GuiScreen.isShiftKeyDown()) {
			isInput = true;
			isOutput = true;
		}
		
		configHandler.setIOTypeConfig(facing , type, (isInput == isOutput?(isInput? false : true):(isInput? false : true)), (isInput == isOutput? false : true));
	}
	
	private void resetConfig(boolean resetAllTypes) {
		for(EnumFacing facing : EnumFacing.values()) {
			if(resetAllTypes) {
				for(IIOType type : configHandler.getValidIOTypes()) {
					configHandler.setIOTypeConfig(facing , type, false, false);
				}
			}
			else {
				configHandler.setIOTypeConfig(facing , getSelectedIOType(), false, false);
			}
		}
	}

}
