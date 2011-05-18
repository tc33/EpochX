package org.epochx.event.stat;

import java.lang.reflect.*;
import java.util.*;

import org.epochx.event.*;

public abstract class AbstractStat<T extends Event> implements Listener<T> {

	public static final List<Class<? extends AbstractStat<Event>>> NO_DEPENDENCIES = new ArrayList<Class<? extends AbstractStat<Event>>>(0);
	
	public <V extends Event> AbstractStat(List<Class<? extends AbstractStat<V>>> dependencies) {
		for (Class<? extends AbstractStat<V>> dependency: dependencies) {
			if (!EventManager.getInstance().contains(dependency)) {
				try {
					AbstractStat<V> stat = (AbstractStat<V>) dependency.newInstance();
					EventManager.getInstance().add(stat.getEvent(), stat);
				} catch (Exception e) {
					throw new RuntimeException("Could not create dependency " + dependency, e);
				}
			}
		}
	}
	
	/**
	 * Returns the Class of the generic type T.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<T> getEvent() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
			.getActualTypeArguments()[0];
	}
}
