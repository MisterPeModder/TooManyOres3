package misterpemodder.tmo.api.io;

public enum EnumIOType implements IIOType {
	ITEM("item"),
	FLUID("fluid"),
	ENERGY("energy"),
	REDSTONE("redstone"),
	
	ENDER_MATTER("enderMatter", 0),
	;
	
	private final String id;
	private final int mainTypeIndex;
	
	@Override
	public String getID() {
		return this.id;
	}
	
	@Override
	public String getUnlocalizedName() {
		return "tmo.ioType."+getID();
	}
	
	@Override
	public IIOType getMainType() {
		return mainTypeIndex < 0 || mainTypeIndex >= EnumIOType.values().length? this : EnumIOType.values()[mainTypeIndex];
	}
	
	private EnumIOType(String id) {
		this(id, -1);
	}
	
	private EnumIOType(String id, int mainTypeIndex) {
		this.id = id;
		this.mainTypeIndex = mainTypeIndex;
	}
	
}
