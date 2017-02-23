package misterpemodder.tmo.main.proxy;

public abstract class CommonProxy {
	
	public abstract void preInit();
	public abstract void init();
	public abstract void postInit();
	public abstract String translate(String translateKey, Object... params);
	
	public String translateFormatted(String format, String translateKey, Object... params) {
		return String.format(format, this.translate(translateKey, params));
	}
}
