package misterpemodder.tmo.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.ImmutableListMultimap;

public abstract class ClassPatcher {
	
	private final ImmutableListMultimap<BiPredicate<Boolean, MethodNode>, Pair<BiPredicate<Boolean, AbstractInsnNode>, IPatch>> methodPatchers;
	
	public ClassPatcher() {
		
		ImmutableListMultimap.Builder<BiPredicate<Boolean, MethodNode>, Pair<BiPredicate<Boolean, AbstractInsnNode>, IPatch>> builder = ImmutableListMultimap.builder();
		
		List<IMethodPatcher> methodPatches = getMethodPatchers();
		if(methodPatches != null && !methodPatches.isEmpty()) {
			for(IMethodPatcher methodPatch : methodPatches) {
				List<IPatch> patches = methodPatch.getPatches();
				if(patches != null && !patches.isEmpty()) {
					List<Pair<BiPredicate<Boolean, AbstractInsnNode>, IPatch>> list = new ArrayList<>();
					for(IPatch p : patches) {
						list.add(Pair.of(p.getNodePredicate(), p));
					}
					builder.putAll(methodPatch.getMethodNodePredicate(), list);
				}
			}
		}
		
		this.methodPatchers = builder.build();
		
	}
	
	public abstract boolean matches(String transformedClassName);
	
	protected abstract List<IMethodPatcher> getMethodPatchers();
	
	public final byte[] makePatches(String className, byte[] basicClass) {
		
		if(methodPatchers != null && !methodPatchers.isEmpty()) {
			
			TmoClassTransformer.LOGGER.info("Patching " + className + "...");
			
			ClassNode classNode = new ClassNode();
	        ClassReader classReader = new ClassReader(basicClass);
	        classReader.accept(classNode, ClassReader.SKIP_FRAMES);
			
			for(BiPredicate<Boolean, MethodNode> mPredicate : methodPatchers.keySet()) {
				for(MethodNode mn : classNode.methods) {
	        		if(mn != null && mPredicate.test(TmoLoadingPlugin.runtimeDeobfuscation, mn)) {
	        			
	        			List<Pair<BiPredicate<Boolean, AbstractInsnNode>, IPatch>> list = methodPatchers.get(mPredicate);
	        			if(list != null && !list.isEmpty()) {
	        				for(int i=0; i<mn.instructions.size(); i++) {
	                			AbstractInsnNode node = mn.instructions.get(i);
	                			for(Pair<BiPredicate<Boolean, AbstractInsnNode>, IPatch> pair : list) {
	                				if(pair.getLeft().test(TmoLoadingPlugin.runtimeDeobfuscation, node)) {
	                					pair.getRight().makePatch(mn, node, i);
	                				}
	                			}
	        				}
	        			}
	        			break;
	        		}
	        	}
			}
	        
	        TmoClassTransformer.LOGGER.info(className+" patching complete!");
	        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
	        classNode.accept(writer);
	        return writer.toByteArray();
		}
		
		return basicClass;
	}
	
	protected static interface IMethodPatcher {
		
		public BiPredicate<Boolean, MethodNode> getMethodNodePredicate();
		
		public List<IPatch> getPatches();
		
	}
	
	protected interface IPatch {
		
		public BiPredicate<Boolean, AbstractInsnNode> getNodePredicate();
		
		public void makePatch(MethodNode method, AbstractInsnNode targetNode, int nodeIndex);
		
	}
	
}
