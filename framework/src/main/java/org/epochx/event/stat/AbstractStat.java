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

package org.epochx.event.stat;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.epochx.event.Event;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;

/**
 * Gathers data and statistics about events.
 */
public abstract class AbstractStat<T extends Event> {

	/**
	 * An empty list of dependencies.
	 */
	public static final List<Class<? extends AbstractStat<?>>> NO_DEPENDENCIES = new ArrayList<Class<? extends AbstractStat<?>>>(
			0);

	/**
	 * The central repository of <code>AbstractStat</code> objects.
	 */
	private static final HashMap<Class<?>, Object> REPOSITORY = new HashMap<Class<?>, Object>();

	/**
	 * This stat listener. When the stat is registered, its listener is added to
	 * the {@link EventManager}.
	 */
	private Listener<T> listener = new Listener<T>() {

		public void onEvent(T event) {
			AbstractStat.this.onEvent(event);
		}
	};

	/**
	 * Constructs an <code>AbstractStat</code>.
	 * 
	 * @param dependency the dependency of this stat.
	 */
	@SuppressWarnings("unchecked")
	public AbstractStat(Class<? extends AbstractStat<?>> dependency) {
		this(Arrays.<Class<? extends AbstractStat<?>>> asList(dependency));
	}

	/**
	 * Constructs an <code>AbstractStat</code>.
	 * 
	 * @param dependencies the array of dependencies of this stat.
	 */
	public AbstractStat(Class<? extends AbstractStat<?>> ... dependencies) {
		this(Arrays.asList(dependencies));
	}

	/**
	 * Constructs an <code>AbstractStat</code>. The list of dependencies can be
	 * empty, in case this stat has no dependencies.
	 * 
	 * @param dependencies the list of dependencies of this stat.
	 */
	public AbstractStat(List<Class<? extends AbstractStat<?>>> dependencies) {
		for (Class<? extends AbstractStat<?>> dependency: dependencies) {
			AbstractStat.register(dependency);
		}
	}

	/**
	 * Returns the class of the generic type T.
	 */
	@SuppressWarnings("unchecked")
	private Class<T> getEvent() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * Gathers the information about the event.
	 * 
	 * @param event the event
	 */
	public abstract void onEvent(T event);

	/**
	 * Registers the specified <code>AbstractStat</code> in the reposiroty, if
	 * it is not already registered.
	 * 
	 * @param type the class of <code>AbstractStat</code> to be registered.
	 */
	public static <E extends Event, V extends AbstractStat<E>> void register(Class<V> type) {
		// if the repository already contains an instance of the specified stat,
		// we do not create a new one; otherwise, we create a new instance and
		// register its listener in the EventManager
		if (!REPOSITORY.containsKey(type)) {
			try {
				AbstractStat<E> stat = type.newInstance();
				REPOSITORY.put(type, stat);
				EventManager.getInstance().add(stat.getEvent(), stat.listener);
			} catch (Exception e) {
				throw new RuntimeException("Could not create an instance of " + type, e);
			}
		}
	}

	/**
	 * Removes the specified <code>AbstractStat</code> from the repository.
	 * 
	 * @param type the class of <code>AbstractStat</code> to be removed.
	 */
	public static <E extends Event, V extends AbstractStat<E>> void remove(Class<V> type) {
		if (REPOSITORY.containsKey(type)) {
			AbstractStat<E> stat = type.cast(REPOSITORY.remove(type));
			EventManager.getInstance().remove(stat.getEvent(), stat.listener);
		}
	}

	/**
	 * Returns the <code>AbstractStat</code> object of the specified class. If
	 * the <code>AbstractStat</code> has been registered, it returns
	 * <code>null</code>.
	 * 
	 * @return the <code>AbstractStat</code> object of the specified class;
	 *         <code>null</code> if the <code>AbstractStat</code> has not been
	 *         registered.
	 */
	public static <V extends AbstractStat<?>> V get(Class<V> type) {
		return type.cast(REPOSITORY.get(type));
	}

}