package misterpemodder.tmo.main.blocks.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;

import misterpemodder.tmo.api.capability.io.IIOType;
import misterpemodder.tmo.main.capability.io.IOConfigHandlerMachine;
import misterpemodder.tmo.main.capability.io.IOState;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import misterpemodder.tmo.main.utils.EnumBlockSide;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public class PropertyIOPortState extends PropertyEnum<PropertyIOPortState.IOPortState> {

	public static enum IOPortState implements IStringSerializable {
		OPEN,
		CLOSED,
		DISABLED,;

		@Override
		public String getName() {
			return this.toString().toLowerCase(Locale.ROOT);
		}
	}
	
	public static final PropertyIOPortState UP = new PropertyIOPortState(EnumBlockSide.UP);
	public static final PropertyIOPortState DOWN = new PropertyIOPortState(EnumBlockSide.DOWN);
	public static final PropertyIOPortState FRONT = new PropertyIOPortState(EnumBlockSide.FRONT);
	public static final PropertyIOPortState BACK = new PropertyIOPortState(EnumBlockSide.BACK);
	public static final PropertyIOPortState LEFT = new PropertyIOPortState(EnumBlockSide.LEFT);
	public static final PropertyIOPortState RIGHT = new PropertyIOPortState(EnumBlockSide.RIGHT);
	
	private static final ImmutableList<PropertyIOPortState> ALL;
	
	static {
		ALL = ImmutableList.of(UP, DOWN, FRONT, BACK, LEFT, RIGHT);
	}
	
	public final EnumBlockSide side;
	
	protected PropertyIOPortState(EnumBlockSide side) {
		super(side.getName() + "_ioport", IOPortState.class, Arrays.asList(IOPortState.values()));
		this.side = side;
	}
	
	public static List<PropertyIOPortState> getAll() {
		return ALL;
	}
	
	public static List<PropertyIOPortState> getAllWithout(PropertyIOPortState... portStates) {
		ArrayList<PropertyIOPortState> list = new ArrayList<>();
		list.addAll(ALL);
		list.removeAll(Arrays.asList(portStates));
		return list;
	}
	
	public IOPortState getValue(@Nonnull TileEntityMachine<?> te) {
		
		if(!te.isSideDisabled(this.side)) {
			IOConfigHandlerMachine handler = te.getIoConfigHandler();
			if(handler != null && handler.getIOTypes(this.side) != null) {
				for(IIOType<?> type : handler.getIOTypes(this.side)) {
					if(type != null && handler.getIOStateConfig(this.side, type) != IOState.DISABLED) {
						return IOPortState.OPEN;					
					}
				}
			}
			return IOPortState.CLOSED;
		}
		return IOPortState.DISABLED;
	}

}
