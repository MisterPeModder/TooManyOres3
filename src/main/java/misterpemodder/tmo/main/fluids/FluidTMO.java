package misterpemodder.tmo.main.fluids;

import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.item.EnumRarity;
import net.minecraftforge.fluids.Fluid;

public class FluidTMO extends Fluid {
	
	private final IBlockNames names;
	private int color = 0xFF000079;

	public FluidTMO(IBlockNames names, EnumRarity rarity, int temperature, int color, int luminosity, int viscosity) {
		this(names);
		this.rarity = rarity;
		this.temperature = temperature;
		this.color = color;
		this.luminosity = luminosity;
		this.viscosity = viscosity;
	}
	
	public FluidTMO(IBlockNames names) {
		super(names.getUnlocalizedName(), new ResourceLocationTmo("blocks/"+names.getRegistryName()+"_still"), new ResourceLocationTmo("blocks/"+names.getRegistryName()+"_flowing"));
		this.names = names;
	}
	
	public int getItemColor() {
		return this.color;
	}
	
	public IBlockNames getNames() {
		return this.names;
	}
	
}
