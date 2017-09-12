package misterpemodder.tmo.main.potion;

import misterpemodder.hc.main.utils.StringUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class PotionFreezing extends PotionBase{

	public PotionFreezing() {
		super("freezing", "freezing", true, 0xa1feff, 0, 0);
		this.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "1107DE6E-7B48-A030-940E-598C1F160C41", -1.0D, 2);
	}
	
	@Override
    public boolean isReady(int duration, int amplifier) {
	    int k = 20 >> amplifier;
        return k > 0 ? duration % k == 0 : true;
    }
	
	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int num) {
        if (entityLivingBaseIn instanceof EntityPlayer) {
        	entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 20, 255, false, false));
        	entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 20, 255, false, false));
        	((EntityPlayer) entityLivingBaseIn).sendStatusMessage(new TextComponentString(TextFormatting.AQUA+""+TextFormatting.BOLD+StringUtils.translate("effect.tmo.freezing.message")), true);
        }
    }

}
