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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.event;

import java.util.*;

/**
 * Not thread-safe.
 */
public class EventManager {

	private static final EventManager singleton = new EventManager();

	private final HashMap<Class<?>, List<Listener<?>>> mapping = new HashMap<Class<?>, List<Listener<?>>>();

	private final HashMap<Class<?>, Object> listeners = new HashMap<Class<?>, Object>();

	private EventManager() {
	}

	public static EventManager getInstance() {
		return singleton;
	}

	public <T extends Event> void add(Class<? extends T> key, Listener<T> listener) {
		if (listeners.containsKey(listener.getClass())) {
			throw new IllegalArgumentException("Duplicated listener: " + listener.getClass());
		}

		if (!mapping.containsKey(key)) {
			mapping.put(key, new ArrayList<Listener<?>>());
		}

		listeners.put(listener.getClass(), listener);
		mapping.get(key).add(listener);
	}

	/**
	 * Removing a listener that has been added more than once will remove the
	 * only instance
	 * of the listener and so it will cease to listen for all parties.
	 * 
	 * @param <T>
	 * @param key
	 * @param listener
	 * @return
	 */
	public <T extends Event> boolean remove(Class<? extends T> key, Listener<T> listener) {
		return mapping.get(key).remove(listener);
	}

	@SuppressWarnings("unchecked")
	public <T extends Event, V extends T> void fire(Class<V> type, T event) {
		for (Class<?> key: mapping.keySet()) {
			if (key.isAssignableFrom(type)) {
				for (Listener<?> listener: mapping.get(key)) {
					((Listener<T>) listener).onEvent(event);
				}
			}
		}
	}

	public boolean contains(Class<? extends Listener<?>> listenerType) {
		return listeners.containsKey(listenerType);
	}

	public <T extends Listener<?>> T get(Class<T> type) {
		return type.cast(listeners.get(type));
	}
}
