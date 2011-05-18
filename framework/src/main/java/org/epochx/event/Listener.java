package org.epochx.event;

public interface Listener<T extends Event> {

	public void onEvent(T event);
	
}
