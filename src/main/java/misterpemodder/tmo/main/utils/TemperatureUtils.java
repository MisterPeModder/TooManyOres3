package misterpemodder.tmo.main.utils;

import java.util.function.Function;

import misterpemodder.hc.main.utils.StringUtils;
import misterpemodder.tmo.main.config.ConfigValues;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public final class TemperatureUtils {
	
	public static enum TemperatureUnit {
		KELVIN("K", t -> t),
		CELSIUS("°C", t -> t - 273.15F),
		FARENHEIT("°F", t -> t * 9/5 - 459.67F);
		
		public final String symbol;
		private final Function<Float, Float> fromKelvin;
		
		public float fromKelvin(float kelvins) {
			return this.fromKelvin.apply(kelvins);
		}
		
		TemperatureUnit(String symbol, Function<Float, Float> fromKelvin) {
			this.symbol = symbol;
			this.fromKelvin = fromKelvin;
		}
	}
	
	public static float getWaterTemperature(TemperatureUnit unit) {
		return (unit.fromKelvin(FluidRegistry.WATER.getTemperature()));
	}
	
	public static TemperatureUnit getTemperatureUnit() {
		return (TemperatureUnit.values()[ConfigValues.IntValues.TEMPERATURE_UNIT.currentValue % 3]);
	}
	
	public static String getTemperatureString(int kelvins, boolean addDesc) {
		TemperatureUnit unit = getTemperatureUnit();
		int temp = (int) unit.fromKelvin(kelvins);
		String desc = addDesc? TextFormatting.GRAY+StringUtils.translate("gui.temperature.desc")+": ": "";
			
		return (desc+(temp <= getWaterTemperature(unit)? TextFormatting.AQUA : TextFormatting.GOLD)+""+temp+" "+unit.symbol);
	}
	
	public static String getFluidTemperatureString(FluidStack stack, boolean addDesc) {
		if(stack != null)
			return getTemperatureString(stack.getFluid().getTemperature(stack), addDesc);
		return ("");
	}

}
