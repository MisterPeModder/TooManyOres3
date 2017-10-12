package misterpemodder.tmo.asm;

import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import misterpemodder.hc.asm.ClassPatcher;

public class ClassPatcherEntityLivingBase extends ClassPatcher {

	@Override
	public boolean matches(String transformedClassName) {
		return transformedClassName.equals("net.minecraft.entity.EntityLivingBase") || transformedClassName.equals("net.minecraft.entity.player.EntityPlayer");
	}

	@Override
	protected List<IMethodPatcher> getMethodPatchers() {
		return Collections.singletonList(new IMethodPatcher() {
			
			@Override
			public BiPredicate<Boolean, MethodNode> getMethodNodePredicate() {
				return (deObf, mn) -> mn.access == ACC_PROTECTED && mn.name.equals(deObf? "isMovementBlocked" : "func_70610_aX") && mn.desc.equals("()Z") && mn.signature==null;
			}

			@Override
			public List<IPatch> getPatches() {
				return Arrays.asList(new IPatch() {
					
					@Override
					public BiPredicate<Boolean, AbstractInsnNode> getNodePredicate() {
						return (deObf, node) -> node instanceof VarInsnNode && node.getOpcode() == ALOAD && ((VarInsnNode)node).var == 0;
					}
					
					@Override
					public void makePatch(MethodNode method, AbstractInsnNode targetNode, int nodeIndex) {
						AbstractInsnNode[] nodes = method.instructions.toArray();
						
        				for(int j = nodeIndex+1; j < nodes.length; j++) {
        					AbstractInsnNode n = nodes[j];
        					method.instructions.remove(n);
        					if(nodes[j+1].getOpcode() == IRETURN) break;
        				}
        				
        				method.instructions.insert(targetNode, new MethodInsnNode(INVOKESTATIC, "misterpemodder/tmo/asm/EntityLivingBaseHandler", "isMovementBlocked", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false));

					}
					
				});
			}
		});
	}

}
