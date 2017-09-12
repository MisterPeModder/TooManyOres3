package misterpemodder.tmo.main.client.gui.tabs;

import java.util.function.Predicate;

import misterpemodder.hc.api.block.ILockable;
import misterpemodder.hc.main.client.gui.tabs.ButtonClickHandler;
import misterpemodder.hc.main.client.gui.tabs.IButtonClickConsumer;
import misterpemodder.hc.main.client.gui.tabs.TabBase;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.inventory.elements.ContainerElementTank;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import net.minecraftforge.common.util.Constants;

public enum TMOButtonClickHandlers {
	SECURITY(TabBase.SECURITY_TAB_ID, (bId, world, pos, te, info) -> {
		if(bId == TabSecurity.LOCK_BUTTON_ID) {
			if(te instanceof ILockable && info.hasKey("locked")) {
				((ILockable)te).setLocked(info.getBoolean("locked"));
			}
			return true;
		}
		return false;
	}),
	MAIN_INJECTOR(TabMainInjector.ID, (bId, world, pos, te, info) -> {
		if(bId == TabMainInjector.TOGGLE_MODE_BUTTON_ID) {
			if(te instanceof TileEntityInjector && info.hasKey("mode", Constants.NBT.TAG_INT)) {
				TransferMode m = TransferMode.values()[info.getInteger("mode")];
				((TileEntityInjector)te).setTransferMode(m);
			}
			return true;
		}
		return false;
	}),
	IO(TabBase.IO_TAB_ID, (bId, world, pos, te, info) -> {
		if(te instanceof TileEntityMachine && info.hasKey("autoPushPull")) {
			boolean autoPushPull = info.getBoolean("autoPushPull");
			if(bId == TabIO.AUTO_PUSH_BUTTON_ID) {
				((TileEntityMachine)te).autoPush = autoPushPull;
			}
			else if(bId == TabIO.AUTO_PULL_BUTTON_ID) {
				((TileEntityMachine)te).autoPull = autoPushPull;
			}
			((TileEntityMachine)te).sync();
			return true;
		}
		return false;
	}),
	MISC(TabBase.MISC_TAB_ID, (bId, world, pos, te, info) -> {
		if(bId == ContainerElementTank.CLEAR_TANK_BUTTON_ID) {
			if(te instanceof TileEntityMachine && info.hasKey("tank_id", Constants.NBT.TAG_SHORT)) {
				((TileEntityMachine)te).emptyTank(info.getShort("tank_id"));
			}
			return true;
		}
		return false;
	}),
	;
	
	private final Predicate<String> condition;
	private final IButtonClickConsumer handler;
	
	public static void registerHandlers() {
		for(TMOButtonClickHandlers h : TMOButtonClickHandlers.values()) {
			ButtonClickHandler.registerHandler(h.condition, h.handler);
		}
	}
	
	private TMOButtonClickHandlers(String tabId, IButtonClickConsumer handler) {
		this(id -> id.equals(tabId), handler);
	}
	
	private TMOButtonClickHandlers(Predicate<String> condition, IButtonClickConsumer handler) {
		this.condition = condition;
		this.handler = handler; 
	}

}
