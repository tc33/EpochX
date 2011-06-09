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
import java.util.*;

import org.epochx.event.*;

public abstract class AbstractStat<T extends Event> implements Listener<T> {

	public static final List<Class<? extends AbstractStat<Event>>> NO_DEPENDENCIES = new ArrayList<Class<? extends AbstractStat<Event>>>(
			0);

	public <V extends Event> AbstractStat(List<Class<? extends AbstractStat<V>>> dependencies) {
		for (Class<? extends AbstractStat<V>> dependency: dependencies) {
			if (!EventManager.getInstance().contains(dependency)) {
				try {
					AbstractStat<V> stat = dependency.newInstance();
					EventManager.getInstance().add(stat.getEvent(), stat);
				} catch (Exception e) {
					throw new RuntimeException("Could not create dependency " + dependency, e);
				}
			}
		}
	}

	/**
	 * Returns the Class of the generic type T.
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getEvent() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
