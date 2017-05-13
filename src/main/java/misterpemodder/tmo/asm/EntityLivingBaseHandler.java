package misterpemodder.tmo.asm;

import java.nio.FloatBuffer;

import misterpemodder.tmo.main.capability.CapabilityFreezing;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityLivingBaseHandler {
	
	public static <T extends EntityLivingBase> boolean isEntityFrozen(T entity) {
		if(entity.hasCapability(CapabilityFreezing.FREEZING_CAPABILITY, null)) {
			return entity.getCapability(CapabilityFreezing.FREEZING_CAPABILITY, null).isFreezing();
		}
		return false;
	}
	
	public static <T extends EntityLivingBase> boolean isMovementBlocked(T entity) {
		return entity.getHealth() <= 0.0F || EntityLivingBaseHandler.isEntityFrozen(entity) || (entity instanceof EntityPlayer && ((EntityPlayer)entity).isPlayerSleeping());
	}
	
	public static <T extends EntityLivingBase> void setBrightnessColor(T entity, FloatBuffer brightnessBuffer) {
		if(isEntityFrozen(entity)) {
			float colorMultiplier = entity.getEntityWorld() != null? ((float) entity.getEntityWorld().getTotalWorldTime())%20/(19):0F;
			colorMultiplier = colorMultiplier >= 0.5? (1F-colorMultiplier) : colorMultiplier;
			brightnessBuffer.clear();
			brightnessBuffer.position(0);
			brightnessBuffer.put(1.0F);
			brightnessBuffer.put(colorMultiplier*6.0F);
			brightnessBuffer.put(colorMultiplier*8.0F);
			brightnessBuffer.put(0.1F);
			brightnessBuffer.flip();
		}
	}
	
}
