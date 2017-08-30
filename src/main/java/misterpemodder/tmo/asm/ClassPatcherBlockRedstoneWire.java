package misterpemodder.tmo.asm;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.INSTANCEOF;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.PUTSTATIC;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import misterpemodder.hc.asm.ClassPatcher;

public class ClassPatcherBlockRedstoneWire extends ClassPatcher {


	@Override
	public boolean matches(String transformedClassName) {
		return transformedClassName.equals("net.minecraft.block.BlockRedstoneWire");
	}

	@Override
	protected List<IMethodPatcher> getMethodPatchers() {
		return Arrays.asList(new IMethodPatcher() {
			
			@Override
			public BiPredicate<Boolean, MethodNode> getMethodNodePredicate() {
				return (deObf, mn) -> mn.access == ACC_PROTECTED + ACC_STATIC && mn.name.equals(deObf? "canConnectTo" : "func_176343_a") && mn.desc.equals("(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Z") && mn.signature==null;
			}
			
			@Override
			public List<IPatch> getPatches() {
				return Arrays.asList(new IPatch() {

					@Override
					public BiPredicate<Boolean, AbstractInsnNode> getNodePredicate() {
						return (obf, node) -> node instanceof VarInsnNode && node.getOpcode() == ALOAD && ((VarInsnNode)node).var == 0;
					}

					@Override
					public void makePatch(MethodNode method, AbstractInsnNode targetNode, int nodeIndex) {
						
						int c = 0;
						while(c < 6) {
							method.instructions.remove(targetNode.getNext());
							c++;
						}
						
						((JumpInsnNode)targetNode.getNext()).setOpcode(IFEQ);
						
						InsnList newInstructions = new InsnList();
						newInstructions.add(new VarInsnNode(ALOAD, 1));
						newInstructions.add(new VarInsnNode(ALOAD, 2));
						newInstructions.add(new VarInsnNode(ALOAD, 3));
						newInstructions.add(new MethodInsnNode(INVOKESTATIC, "misterpemodder/tmo/main/blocks/redstone/BlockSpecialRedstoneWire", "canConnectTo", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/EnumFacing;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Z", false));
					
						method.instructions.insert(targetNode, newInstructions);
						
						for(LocalVariableNode varNode : method.localVariables) {
							if(varNode.desc.equals("Lnet/minecraft/block/Block;")) {
								method.localVariables.remove(varNode);
								break;
							}
						}
					}
					
				});
			}
		}, new IMethodPatcher() {
			
			@Override
			public BiPredicate<Boolean, MethodNode> getMethodNodePredicate() {
				return (deObf, mn) -> mn.access == ACC_PRIVATE && mn.name.equals(deObf? "notifyWireNeighborsOfStateChange" : "func_176344_d") && mn.desc.equals("(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V") && mn.signature==null;
			}
			
			@Override
			public List<IPatch> getPatches() {
				return Collections.singletonList(new IPatch() {

					@Override
					public BiPredicate<Boolean, AbstractInsnNode> getNodePredicate() {
						return getPredicate();
					}

					@Override
					public void makePatch(MethodNode method, AbstractInsnNode targetNode, int nodeIndex) {
						method.instructions.remove(targetNode.getNext());
						method.instructions.insert(targetNode, new TypeInsnNode(INSTANCEOF, "net/minecraft/block/BlockRedstoneWire"));
						((JumpInsnNode)method.instructions.get(nodeIndex+2)).setOpcode(IFEQ);
					}
					
				});
			}
		}, new IMethodPatcher() {
			
			@Override
			public BiPredicate<Boolean, MethodNode> getMethodNodePredicate() {
				return (deObf, mn) -> mn.access == ACC_PRIVATE && mn.name.equals(deObf? "getMaxCurrentStrength" : "func_176342_a") && mn.desc.equals("(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;I)I") && mn.signature==null;
			}
			
			@Override
			public List<IPatch> getPatches() {
				return Collections.singletonList(new IPatch() {

					@Override
					public BiPredicate<Boolean, AbstractInsnNode> getNodePredicate() {
						return getPredicate();
					}

					@Override
					public void makePatch(MethodNode method, AbstractInsnNode targetNode, int nodeIndex) {
						method.instructions.remove(targetNode.getNext());
						method.instructions.insert(targetNode, new TypeInsnNode(INSTANCEOF, "net/minecraft/block/BlockRedstoneWire"));
						
						((JumpInsnNode)method.instructions.get(nodeIndex+2)).setOpcode(IFNE);
					}
					
				});
			}
		}, new IMethodPatcher() {
			
			@Override
			public BiPredicate<Boolean, MethodNode> getMethodNodePredicate() {
				return (deObf, mn) -> !mn.name.equals("<init>");
			}
			
			@Override
			public List<IPatch> getPatches() {
				return Collections.singletonList(new IPatch() {

					@Override
					public BiPredicate<Boolean, AbstractInsnNode> getNodePredicate() {
						return (obf, node) -> node instanceof FieldInsnNode && node.getOpcode() == GETFIELD && ((FieldInsnNode)node).owner.equals("net/minecraft/block/BlockRedstoneWire") && ((FieldInsnNode)node).name.equals(obf? "canProvidePower" : "field_150181_a") && ((FieldInsnNode)node).desc.equals("Z");
					}

					@Override
					public void makePatch(MethodNode method, AbstractInsnNode targetNode, int nodeIndex) {
						method.instructions.remove(targetNode.getPrevious());
						method.instructions.insert(targetNode, new FieldInsnNode(GETSTATIC, "misterpemodder/tmo/main/blocks/redstone/BlockSpecialRedstoneWire", "canWireProvidePower", "Z"));
						method.instructions.remove(targetNode);
					}
					
					@Override
					public boolean alwaysPatch() {
						return true;
					}
					
				});
			}
		}, new IMethodPatcher() {
			
			@Override
			public BiPredicate<Boolean, MethodNode> getMethodNodePredicate() {
				return (deObf, mn) -> !mn.name.equals("<init>");
			}
			
			@Override
			public List<IPatch> getPatches() {
				return Collections.singletonList(new IPatch() {

					@Override
					public BiPredicate<Boolean, AbstractInsnNode> getNodePredicate() {
						return (obf, node) -> node instanceof FieldInsnNode && node.getOpcode() == PUTFIELD && ((FieldInsnNode)node).owner.equals("net/minecraft/block/BlockRedstoneWire") && ((FieldInsnNode)node).name.equals(obf? "canProvidePower" : "field_150181_a") && ((FieldInsnNode)node).desc.equals("Z");
					}

					@Override
					public void makePatch(MethodNode method, AbstractInsnNode targetNode, int nodeIndex) {
						method.instructions.remove(targetNode.getPrevious().getPrevious());
						method.instructions.insert(targetNode, new FieldInsnNode(PUTSTATIC, "misterpemodder/tmo/main/blocks/redstone/BlockSpecialRedstoneWire", "canWireProvidePower", "Z"));
						method.instructions.remove(targetNode);
					}
					
					@Override
					public boolean alwaysPatch() {
						return true;
					}
					
				});
			}
		});
	}
	
	private static BiPredicate<Boolean, AbstractInsnNode> getPredicate() {
		return (obf, node) -> node instanceof MethodInsnNode && node.getOpcode() == INVOKEINTERFACE;
	}

}
