package misterpemodder.tmo.main.utils;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.Fluid;

public class DamageSourceFluid extends DamageSource {
	
	public static final DamageSource FROZIUM = new DamageSourceFluid("frozium",4);
	public static final DamageSource IGNUM = new DamageSourceFluid("ignum",2);
			
	private final int messagesNum;

	public DamageSourceFluid(String damageTypeIn, int messagesNum) {
		super(damageTypeIn);
		this.setDamageBypassesArmor();
		this.messagesNum = messagesNum;
	}
	
	@Override
	public ITextComponent getDeathMessage(EntityLivingBase entity) {
		String str = "death."+TMORefs.MOD_ID+".fluid."+this.damageType;
		
		if(this == IGNUM && entity.getActivePotionEffect(MobEffects.FIRE_RESISTANCE) != null) {
			str += "."+messagesNum;
		}
		else if(this.messagesNum > 0) {
			str += "."+new Random().nextInt(messagesNum);
		}
		
		return new TextComponentTranslation(str, entity.getDisplayName());
	}
	
	public static DamageSource getDamageSourceForFluid(Fluid fluid) {
		switch(fluid.getName()) {
			case "liquidignum":
				return IGNUM;
			case "liquidfrozium":
				return FROZIUM;
			default:
				return DamageSource.LAVA;
		}
		
	}

}
