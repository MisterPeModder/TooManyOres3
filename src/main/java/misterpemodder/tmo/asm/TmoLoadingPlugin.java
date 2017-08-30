package misterpemodder.tmo.asm;

import java.util.HashSet;
import java.util.Set;

import misterpemodder.hc.asm.ClassPatcher;
import misterpemodder.hc.asm.HCLoadingPlugin;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(TMORefs.LOADING_PLUGIN_NAME)
@IFMLLoadingPlugin.MCVersion("1.11.2")
@IFMLLoadingPlugin.SortingIndex(1042)
public class TmoLoadingPlugin extends HCLoadingPlugin {

	@Override
	public Set<ClassPatcher> getClassPatchers() {
		Set<ClassPatcher> patchers = new HashSet<>();
		
		patchers.add(new ClassPatcherRenderLivingBase());
		patchers.add(new ClassPatcherEntityLivingBase());
		patchers.add(new ClassPatcherBlockRedstoneWire());
		return patchers;
	}

}
