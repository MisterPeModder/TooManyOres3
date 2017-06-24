package misterpemodder.tmo.asm;

import java.util.Map;

import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name(TMORefs.LOADING_PLUGIN_NAME)
@IFMLLoadingPlugin.MCVersion("1.11.2")
@IFMLLoadingPlugin.SortingIndex(1043)
public class TmoLoadingPlugin implements IFMLLoadingPlugin {
	
	public static boolean runtimeDeobfuscation = false;

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{TmoClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
    public void injectData(Map<String, Object> data) {
		runtimeDeobfuscation = !(Boolean)data.get("runtimeDeobfuscationEnabled");
    }

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
