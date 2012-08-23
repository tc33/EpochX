/*
 * Copyright 2007-2012
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
package org.epochx.monitor.graph;

import java.util.EventObject;

/**
 * 
 */
public class GraphViewEvent extends EventObject {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -3419694219416332329L;

	public enum Property {
		COMPARATOR, DIAMETER, HGAP, VGAP, MARGINS, SELECTED_VERTEX, HIGHLIGHTED_VERTEX, BOUND_COLOR, HIGHLIGHT_COLOR,
		HIGHLIGHT_DEPTH, FITNESS
	}

	private final Property property;

	private final Object oldValue;

	private final Object newValue;

	/**
	 * Constructs a <code>GraphViewEvent</code>.
	 * 
	 * @param source
	 */
	public GraphViewEvent(Object source, Property property) {
		this(source, property, null, null);
	}

	/**
	 * Constructs a <code>GraphViewEvent</code>.
	 * 
	 * @param source
	 * @param property
	 * @param oldValue
	 * @param newValue
	 */
	public GraphViewEvent(Object source, Property property, Object oldValue, Object newValue) {
		super(source);
		this.property = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * @return the property
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * @return the oldValue
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}

}
