/*
 * Copyright 2007-2013
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

package org.epochx;

import org.epochx.Config.ConfigKey;
import org.epochx.Config.Template;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;

/**
 * <code>ProxyComponent</code> provides a mechanism to wrap objects and view them
 * as {@link Component}s. The wrapped object is specified by a <code>ConfigKey</code>
 * and changes in the configuration are monitored.
 */
public abstract class ProxyComponent<T> implements Component, Listener<ConfigEvent> {

	/**
	 * The <code>ConfigKey</code> of the proxied object.
	 */
	protected ConfigKey<T> key;

	/**
	 * The proxied object.
	 */
	protected T handler;

	/**
	 * Constructs a <code>ProxyComponent</code>.
	 * 
	 * @param key the <code>ConfigKey</code> of the proxied object.
	 */
	public ProxyComponent(ConfigKey<T> key) {
		this.key = key;
		setup();
		EventManager.getInstance().add(ConfigEvent.class, this);
	}

	/**
	 * Receives configuration events and refresh its configuration if the
	 * <code>ConfigEvent</code> is for the parameter specified by the
	 * <code>key</code> object.
	 * 
	 * @param event the fired event object.
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(Template.TEMPLATE, key)) {
			setup();
		}
	}

	/**
	 * Sets up this proxy with the appropriate configuration settings. This
	 * method is called whenever a <code>ConfigEvent</code> occurs for a
	 * change in the parameter specified by the <code>key</code> object.
	 */
	protected void setup() {
		handler = Config.getInstance().get(key);
	}

}