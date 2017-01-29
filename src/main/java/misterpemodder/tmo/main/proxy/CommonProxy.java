package misterpemodder.tmo.main.proxy;

public interface CommonProxy {
	public void preInit();
	public void init();
	public void postInit();
	public String translate(String translateKey, Object... params);
}
