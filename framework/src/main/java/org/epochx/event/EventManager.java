/*
 * Copyright 2007-2011
 * Lawrence Beadle, Tom Castle and Fernando Otero
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */

package org.epochx.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The <code>EventManager</code> class provides event-related functionality.
 * It is a singleton, which is obtainable with the <code>getInstance</code>
 * method. It provides methods for registering listeners and firing events.
 * 
 * <p>
 * <b>Note:</b> The current implementation is not thread-safe.
 * </p>
 * 
 * @see Listener
 * @see Event
 */
public class EventManager {

	/**
	 * The singleton instance.
	 */
	private static final EventManager singleton = new EventManager();

	/**
	 * The mapping of listeners per event.
	 */
	private final HashMap<Class<?>, List<Listener<?>>> mapping = new HashMap<Class<?>, List<Listener<?>>>();

	/**
	 * Constructs a <code>EventManager</code>.
	 */
	private EventManager() {
	}

	/**
	 * Returns the singleton instance.
	 * 
	 * @return the singleton instance.
	 */
	public static EventManager getInstance() {
		return singleton;
	}

	/**
	 * Registers a listener for the specified event.
	 * 
	 * @param key the class of the event.
	 * @param listener the listener object.
	 */
	public <T extends Event> void add(Class<? extends T> key, Listener<T> listener) {

		if (!mapping.containsKey(key)) {
			mapping.put(key, new ArrayList<Listener<?>>());
		}

		mapping.get(key).add(listener);
	}

	/**
	 * Removes a listener from the specified event. This effectively makes the
	 * listener stop receiveing notifications of the event.
	 * 
	 * @param key the class of the event.
	 * @param listener the listener object.
	 * 
	 * @return <code>true</code> if the event's listener mapping contained the
	 *         specified listener.
	 */
	public <T extends Event> boolean remove(Class<? extends T> key, Listener<T> listener) {
		List<Listener<?>> listeners = mapping.get(key);
		return listeners == null ? false : listeners.remove(listener);
	}

	/**
	 * Fires the specified event by notifying all registered listeners.
	 * 
	 * @param event the event object.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Event, V extends T> void fire(T event) {
		for (Class<?> key: mapping.keySet()) {
			if (key.isAssignableFrom(event.getClass())) {
				for (Listener<?> listener: mapping.get(key)) {
					((Listener<T>) listener).onEvent(event);
				}
			}
		}
	}

	/**
	 * Removes all events listener mapping. The <code>EventManager</code> will
	 * be empty this call returns.
	 */
	public void reset() {
		mapping.clear();
	}

}