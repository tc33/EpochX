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
 * A <code>GraphViewEvent</code> is used to notify listeners that a view model
 * has changed.
 */
public class GraphViewEvent extends EventObject {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -3419694219416332329L;

	/**
	 * A <code>Property</code> identifies the property that changed among the
	 * view model's fields.
	 */
	public enum Property {
		COMPARATOR, DIAMETER, HGAP, VGAP, MARGINS, SELECTED_VERTEX, HIGHLIGHTED_VERTEX, BOUND_ENABLE, BOUND_COLOR, HIGHLIGHT_COLOR,
		HIGHLIGHT_DEPTH, FITNESS
	}

	/**
	 * The property which identifies the view model's field that changed.
	 */
	private final Property property;

	/**
	 * The old value for the property.
	 */
	private final Object oldValue;

	/**
	 * The new value for the property.
	 */
	private final Object newValue;

	/**
	 * Constructs a <code>GraphViewEvent</code> with a null property and null
	 * values.
	 * 
	 * @param source the object that fired the event.
	 */
	public GraphViewEvent(Object source) {
		this(source, null, null, null);
	}

	/**
	 * Constructs a <code>GraphViewEvent</code> with a specified property.
	 * 
	 * @param source the object that fired the event.
	 * @param property the property which identifies the view model's field that
	 *        changed.
	 */
	public GraphViewEvent(Object source, Property property) {
		this(source, property, null, null);
	}

	/**
	 * Constructs a <code>GraphViewEvent</code>.
	 * 
	 * @param source the object that fired the event.
	 * @param property the property which identifies the view model's field that
	 *        changed.
	 * @param oldValue the old value of the property.
	 * @param newValue the new value of the property.
	 */
	public GraphViewEvent(Object source, Property property, Object oldValue, Object newValue) {
		super(source);
		this.property = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Gets the <code>Property</code> that was changed. Among this
	 * {@link Property enumeration}.
	 * 
	 * @return the <code>Property</code> that was changed. May be null if
	 *         multiple properties have changed.
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * Gets the old value for the property, expressed as an Object.
	 * 
	 * @return the old value for the property, expressed as an Object. May be
	 *         null if multiple properties have changed.
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * Gets the new value for the property, expressed as an Object.
	 * 
	 * @return the new value for the property, expressed as an Object. May be
	 *         null if multiple properties have changed.
	 */
	public Object getNewValue() {
		return newValue;
	}

}
