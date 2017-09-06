package misterpemodder.tmo.asm;

import java.util.HashSet;
import java.util.Set;

import misterpemodder.hc.asm.ClassPatcher;
import misterpemodder.hc.asm.HCClassTransformer;
import misterpemodder.tmo.main.utils.TMORefs;

public class TmoClassTransformer extends HCClassTransformer {

	@Override
	public Set<ClassPatcher> getClassPatchers() {
		Set<ClassPatcher> patchers = new HashSet<>();
		
		patchers.add(new ClassPatcherRenderLivingBase());
		patchers.add(new ClassPatcherEntityLivingBase());
		patchers.add(new ClassPatcherBlockRedstoneWire());
		return patchers;
	}

	@Override
	protected String getName() {
		return TMORefs.LOADING_PLUGIN_NAME;
	}

}
