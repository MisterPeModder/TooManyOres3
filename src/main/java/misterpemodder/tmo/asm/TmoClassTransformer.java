package misterpemodder.tmo.asm;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.launchwrapper.IClassTransformer;

public class TmoClassTransformer implements IClassTransformer {
	
	public static final Logger LOGGER = LogManager.getLogger(TMORefs.LOADING_PLUGIN_NAME);

	@Override
    public byte[] transform(String obfName, String transformedName, byte[] basicClass) {
		
		switch(transformedName) {
		case "net.minecraft.client.renderer.entity.RenderLivingBase" :
			return transformLivingRenderClass(basicClass);
		case "net.minecraft.entity.EntityLivingBase" :
			return transformLivingClass(basicClass,false);
		case "net.minecraft.entity.player.EntityPlayer" :
			return transformLivingClass(basicClass,true);
		}
        return basicClass;
    }
	
	private byte[] transformLivingRenderClass(byte[] basicClass) {
		LOGGER.info("Patching RenderLivingBase...");
		
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, ClassReader.SKIP_FRAMES);
        
        String methodName = TmoLoadingPlugin.runtimeDeobfuscation ? "setBrightness" : "func_177092_a";
        String deathTimeVarName = TmoLoadingPlugin.runtimeDeobfuscation? "deathTime" : "field_70725_aQ";
        for(MethodNode mn : classNode.methods) {
        	
        	if(mn != null && mn.name.equals(methodName) && mn.signature.equals("(TT;FZ)Z")) {
        		int st = 0;
        		for(int i=0; i<mn.instructions.size(); i++) {
        			AbstractInsnNode node = mn.instructions.get(i);
        			
        			if(st == 0 && node instanceof FieldInsnNode && node.getOpcode() == GETFIELD) {
        				FieldInsnNode n = (FieldInsnNode)node;
        				if(n.owner.equals("net/minecraft/entity/EntityLivingBase") && n.name.equals(deathTimeVarName) && n.desc.equals("I")) {
        					
        					InsnList newInstructions = new InsnList();
        					newInstructions.add(new VarInsnNode(ALOAD, 1));
        					newInstructions.add(new MethodInsnNode(INVOKESTATIC, "misterpemodder/tmo/asm/EntityLivingBaseHandler", "isEntityFrozen", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false));
        					newInstructions.add(new VarInsnNode(ISTORE, 8));
        					mn.instructions.insert(mn.instructions.get(i+8), newInstructions);
        					st++;
        				}
        			}
        			else if(st == 1 && node instanceof VarInsnNode && node.getOpcode() == ISTORE && ((VarInsnNode)node).var == 8) {
        				
        					InsnList newInstructions1 = new InsnList();
        					
        					newInstructions1.add(new VarInsnNode(ILOAD, 7));
        					LabelNode label1 = new LabelNode();
        					newInstructions1.add(new JumpInsnNode(IFNE, label1));
        					newInstructions1.add(new VarInsnNode(ILOAD, 8));
        					newInstructions1.add(new JumpInsnNode(IFNE, label1));
        					newInstructions1.add(new InsnNode(ICONST_0));
        					LabelNode label2 = new LabelNode();
        					newInstructions1.add(new JumpInsnNode(GOTO, label2));
        					newInstructions1.add(label1);
        					newInstructions1.add(new InsnNode(ICONST_1));
        					newInstructions1.add(label2);
        					newInstructions1.add(new VarInsnNode(ISTORE, 7));
        					
        					mn.instructions.insert(node, newInstructions1);
        					st++;
        			}
        			else if(st == 2 && node instanceof MethodInsnNode && node.getOpcode() == INVOKEVIRTUAL) {
        				MethodInsnNode n = (MethodInsnNode)node;
        				if(n.owner.equals("java/nio/FloatBuffer") && n.name.equals("flip") && n.desc.equals("()Ljava/nio/Buffer;")) {
        					InsnList newInstructions2 = new InsnList();
        					
        					newInstructions2.add(new VarInsnNode(ALOAD, 1));
        					newInstructions2.add(new VarInsnNode(ALOAD, 0));
        					newInstructions2.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/renderer/entity/RenderLivingBase", "brightnessBuffer", "Ljava/nio/FloatBuffer;"));
        					newInstructions2.add(new MethodInsnNode(INVOKESTATIC, "misterpemodder/tmo/asm/EntityLivingBaseHandler", "setBrightnessColor", "(Lnet/minecraft/entity/EntityLivingBase;Ljava/nio/FloatBuffer;)V", false));

        					mn.instructions.insert(node.getNext(), newInstructions2);
        					LOGGER.info("RenderLivingBase patching complete!");
        					break;
        				}
        			}
        			
        		}
        		
        	}
        	
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
	}
	
	private byte[] transformLivingClass(byte[] basicClass, boolean isPlayerClass) {
		String name = isPlayerClass? "EntityPlayer" : "EntityLivingBase";
		LOGGER.info("Patching "+name+"...");
		
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, ClassReader.SKIP_FRAMES);
        
        String methodName = TmoLoadingPlugin.runtimeDeobfuscation ? "isMovementBlocked" : "func_70610_aX";
        for(MethodNode mn : classNode.methods) {
        	
        	if(mn.access == ACC_PROTECTED && mn.name.equals(methodName) && mn.desc.equals("()Z") && mn.signature==null) {
        		for(int i=0; i<mn.instructions.size(); i++) {
        			AbstractInsnNode node = mn.instructions.get(i);
        			
        			if(node instanceof VarInsnNode && node.getOpcode() == ALOAD && ((VarInsnNode)node).var == 0) {
        				
        				AbstractInsnNode[] nodes = mn.instructions.toArray();
        				for(int j = i+1; j < nodes.length; j++) {
        					AbstractInsnNode n = nodes[j];
        					mn.instructions.remove(n);
        					if(nodes[j+1].getOpcode() == IRETURN) break;
        				}
        				
        				mn.instructions.insert(node, new MethodInsnNode(INVOKESTATIC, "misterpemodder/tmo/asm/EntityLivingBaseHandler", "isMovementBlocked", "(Lnet/minecraft/entity/EntityLivingBase;)Z", false));

        				LOGGER.info(name+" patching complete!");
        				break;
        			}
        		}
        		
        	}
        	
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
	}

}
