package misterpemodder.tmo.main.capability.io;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.Nullable;

import misterpemodder.tmo.main.Tmo;
import net.minecraft.util.text.TextFormatting;

public enum IOState implements Comparable<IOState> {
	
	ALL(TextFormatting.GREEN, "all", true, true),
	DISABLED(TextFormatting.GRAY, "disabled", false, false),
	INPUT(TextFormatting.AQUA, "input", true),
	OUTPUT(TextFormatting.GOLD, "output", false),
	ENDER_MATTER(TextFormatting.LIGHT_PURPLE, "enderMatter", true),
	LEFT_TANK(TextFormatting.RED, "leftTank", true),
	RIGHT_TANK(TextFormatting.BLUE, "rightTank", true),
	;
	
	private final TextFormatting color;
	private final String id;
	private boolean insert;
	private boolean extract;
	
	public String getId() {
		return this.id;
	}
	
	public String getLocalizedName() {
		return Tmo.proxy.translate("gui.tab.io.state."+getId());
	}
	
	public String getLocalizedNameColored() {
		return this.color + Tmo.proxy.translate("gui.tab.io.state."+getId());
	}
	
	public boolean canInsert() {
		return this.insert;
	}
	
	public boolean canExtract() {
		return this.extract;
	}
	
	IOState(TextFormatting color, String id, boolean insert, boolean extract) {
		this(color, id, insert);
		this.extract = extract;
	}

	IOState(TextFormatting color, String id, boolean insert) {
		this.color = color;
		this.id = id;
		this.insert = insert;
		this.extract = !insert;
	}
	
	//Adds default IOStates to the list and sorts it.
	public static List<IOState> completeStateList(@Nullable List<IOState> list) {
		TreeSet<IOState> set = new TreeSet<>();
		if(list != null) {
			set.addAll(list);
			if(list.size() > 1) set.add(ALL);
		}
		set.add(DISABLED);
		return new ArrayList<>(set);
	}
	
}
