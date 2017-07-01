package misterpemodder.tmo.main.capability;

public abstract class HandlerContainer<T> {

	protected T containedHandler;
	
	public HandlerContainer(T handler) {
		this.containedHandler = handler;
	}
	
	public T getHandler() {
		return this.containedHandler;
	}
	
	public void setHandler(T handler) {
		this.containedHandler = handler;
	}
	
	public abstract void setEmptyHandler();
	
}
