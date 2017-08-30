package misterpemodder.tmo.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.ISTORE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import misterpemodder.hc.asm.ClassPatcher;

public class ClassPatcherRenderLivingBase extends ClassPatcher {
	
	@Override
	public boolean matches(String transformedClassName) {
		return transformedClassName.equals("net.minecraft.client.renderer.entity.RenderLivingBase");
	}

	@Override
	protected List<IMethodPatcher> getMethodPatchers() {
		return Collections.singletonList(new IMethodPatcher() {
			
			@Override
			public BiPredicate<Boolean, MethodNode> getMethodNodePredicate() {
				return (deObf, mn) -> mn.name.equals(deObf? "setBrightness" : "func_177092_a") && mn.signature.equals("(TT;FZ)Z");
			}

			@Override
			public List<IPatch> getPatches() {
				return Arrays.asList(new IPatch() {
					
					@Override
					public BiPredicate<Boolean, AbstractInsnNode> getNodePredicate() {
						return new BiPredicate<Boolean, AbstractInsnNode>() {
							@Override
							public boolean test(Boolean deObf, AbstractInsnNode node) {
								if(node instanceof FieldInsnNode && node.getOpcode() == GETFIELD) {
									FieldInsnNode n = (FieldInsnNode) node;
									String deathTimeVarName = deObf? "deathTime" : "field_70725_aQ";
									return n.owner.equals("net/minecraft/entity/EntityLivingBase") && n.name.equals(deathTimeVarName) && n.desc.equals("I");
								}
								return false;
							}
						};
					}
					
					@Override
					public void makePatch(MethodNode method, AbstractInsnNode targetNode, int nodeIndex) {
						InsnList newInstructions = new InsnList();
    					newInstructions.add(new VarInsnNode(ALOAD, 1));
    					newInstructions.add(new MethodInsnNode(INVOKESTATIC, "misterpemodder/tmo/asm/EntityLivingBaseHandler", "isEntityFrozen", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false));
    					newInstructions.add(new VarInsnNode(ISTORE, 8));
    					method.instructions.insert(method.instructions.get(nodeIndex+8), newInstructions);
					}
					
				}, new IPatch() {
					
					@Override
					public BiPredicate<Boolean, AbstractInsnNode> getNodePredicate() {
						return (deObf, node) -> node instanceof VarInsnNode && node.getOpcode() == ISTORE && ((VarInsnNode)node).var == 8;
					}
					
					@Override
					public void makePatch(MethodNode method, AbstractInsnNode targetNode, int nodeIndex) {
						InsnList newInstructions = new InsnList();
    					
    					newInstructions.add(new VarInsnNode(ILOAD, 7));
    					LabelNode label1 = new LabelNode();
    					newInstructions.add(new JumpInsnNode(IFNE, label1));
    					newInstructions.add(new VarInsnNode(ILOAD, 8));
    					newInstructions.add(new JumpInsnNode(IFNE, label1));
    					newInstructions.add(new InsnNode(ICONST_0));
    					LabelNode label2 = new LabelNode();
    					newInstructions.add(new JumpInsnNode(GOTO, label2));
    					newInstructions.add(label1);
    					newInstructions.add(new InsnNode(ICONST_1));
    					newInstructions.add(label2);
    					newInstructions.add(new VarInsnNode(ISTORE, 7));
    					
    					method.instructions.insert(targetNode, newInstructions);
					}
				}, new IPatch() {

					@Override
					public BiPredicate<Boolean, AbstractInsnNode> getNodePredicate() {
						return (deObf, node) -> node instanceof MethodInsnNode && node.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode)node).owner.equals("java/nio/FloatBuffer") && ((MethodInsnNode)node).name.equals("flip") && ((MethodInsnNode)node).desc.equals("()Ljava/nio/Buffer;");
					}

					@Override
					public void makePatch(MethodNode method, AbstractInsnNode targetNode, int nodeIndex) {
						InsnList newInstructions = new InsnList();
    					
    					newInstructions.add(new VarInsnNode(ALOAD, 1));
    					newInstructions.add(new VarInsnNode(ALOAD, 0));
    					newInstructions.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/entity/RenderLivingBase", "brightnessBuffer", "Ljava/nio/FloatBuffer;"));
    					newInstructions.add(new MethodInsnNode(INVOKESTATIC, "misterpemodder/tmo/asm/EntityLivingBaseHandler", "setBrightnessColor", "(Lnet/minecraft/entity/EntityLivingBase;Ljava/nio/FloatBuffer;)V", false));

    					method.instructions.insert(targetNode.getNext(), newInstructions);
					}
					
				}
				);
			}
		});
	}

}
