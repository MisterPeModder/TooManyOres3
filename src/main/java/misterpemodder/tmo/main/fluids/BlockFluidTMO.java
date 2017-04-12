package misterpemodder.tmo.main.fluids;

import misterpemodder.tmo.main.init.ModFluids.TheFluids;
import misterpemodder.tmo.main.init.ModPotions.ThePotions;
import misterpemodder.tmo.main.utils.DamageSourceFluid;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

public class BlockFluidTMO extends BlockFluidClassic {
	
	private boolean fluidRegistered = false;

	public BlockFluidTMO(FluidTMO fluid, Material material) {
		super(fluid, material);
	}
	
	public boolean isFluidRegistered() {
		return this.fluidRegistered;
	}
	
	public FluidTMO getDefinedFluid() {
		if(this.fluidRegistered == false) {
			fluidRegistered = true;
			return (FluidTMO)this.definedFluid;
		} else {
			TMORefs.LOGGER.warn("Trying to get the declared fluid instance for "+this.getClass().getName()+" too late!");
		}
		return null;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
		if(entityIn instanceof EntityLivingBase) {
			entityIn.attackEntityFrom(DamageSourceFluid.getDamageSourceForFluid(this.getFluid()), 6F);
			if(entityIn instanceof EntityPlayer && this.getFluid() == TheFluids.FROZIUM_FUEL.getFluid())
				((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(ThePotions.FREEZING.getPotion(), 60));
		}
		if(this.getFluid() == TheFluids.IGNUM_FUEL.getFluid()) {
			entityIn.setFire(10);
		}
	}

}
