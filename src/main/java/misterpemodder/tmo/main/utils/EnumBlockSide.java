package misterpemodder.tmo.main.utils;

import java.util.Locale;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import misterpemodder.tmo.main.Tmo;
import net.minecraft.util.EnumFacing;

public enum EnumBlockSide {
	
	UP,
	DOWN,
	FRONT,
	BACK,
	LEFT,
	RIGHT;
	
	private static final ImmutableMap<String, EnumBlockSide> NAME_TO_SIDE_LOOKUP;
	
	static {
		ImmutableMap.Builder<String, EnumBlockSide> builder = new ImmutableMap.Builder<>();
		for(EnumBlockSide side : EnumBlockSide.values()) {
			builder.put(side.getName(), side);
		}
		NAME_TO_SIDE_LOOKUP = builder.build();
	}
	
	public static EnumBlockSide fromFacing(EnumFacing facing, EnumFacing side) {
		if(side == EnumFacing.UP) {
			return EnumBlockSide.UP;
		}
		else if(side == EnumFacing.DOWN) {
			return EnumBlockSide.DOWN;
		}
		else if(facing == side) {
			return FRONT;
		}
		else if(facing == side.getOpposite()) {
			return BACK;
		}
		else if(facing == side.rotateYCCW()) {
			return LEFT;
		}
		return RIGHT;
	}
	
	public EnumFacing toFacing(EnumFacing facing) {
		
		switch(this) {
			case UP:
				return EnumFacing.UP;
			case DOWN:
				return EnumFacing.DOWN;
			case FRONT:
				return facing;
			case BACK:
				return facing.getOpposite();
			case LEFT:
				return facing.rotateY();
			default:
				return facing.rotateYCCW();
		}
	}
	
	public String getName() {
		return this.toString().toLowerCase(Locale.ROOT);
	}
	
	public String getLocalizedName() {
		return Tmo.proxy.translate("tmo.blockSide."+getName());
	}
	
	@Nullable
	public static EnumBlockSide forName(String name) {
		return name != null? NAME_TO_SIDE_LOOKUP.get(name) : null;
	}
	
}
