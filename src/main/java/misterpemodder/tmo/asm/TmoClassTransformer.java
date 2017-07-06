package misterpemodder.tmo.asm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;

import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.launchwrapper.IClassTransformer;

public class TmoClassTransformer implements IClassTransformer {
	
	public static final Logger LOGGER = LogManager.getLogger(TMORefs.LOADING_PLUGIN_NAME);
	
	private static final ImmutableList<ClassPatcher> CLASS_PATCHERS;
	
	static {
		ImmutableList.Builder<ClassPatcher> builder = ImmutableList.builder();
		
		builder.add(new ClassPatcherRenderLivingBase());
		builder.add(new ClassPatcherEntityLivingBase());
		
		CLASS_PATCHERS = builder.build();
	}

	@Override
    public byte[] transform(String obfName, String transformedName, byte[] basicClass) {
		
		for(ClassPatcher classPatcher : CLASS_PATCHERS) {
			if(classPatcher.matches(transformedName)) {
				return classPatcher.makePatches(transformedName, basicClass);
			}
		}
		
        return basicClass;
    }

}
