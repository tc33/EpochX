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
package org.epochx.monitor.tree;

import java.util.EventObject;

/**
 * A <code>TreeEvent</code> is used to notify listeners that a tree
 * has changed.
 */
public class TreeEvent extends EventObject {

	/**
	 * Generated serial UID.
	 */
	private static final long serialVersionUID = -3419694219416332329L;

	/**
	 * A <code>ViewProperty</code> identifies the treeProperty that changed among the
	 * tree's fields.
	 */
	public enum TreeProperty {
		ROOT, SELECTED_NODE, HIGHLIGHTED_NODE
	}

	/**
	 * The property which identifies the tree's field that changed.
	 */
	private final TreeProperty treeProperty;

	/**
	 * The old value for the property.
	 */
	private final Object oldValue;

	/**
	 * The new value for the property.
	 */
	private final Object newValue;

	/**
	 * Constructs a <code>TreeEvent</code> with a null property and null
	 * values.
	 * 
	 * @param source the object that fired the event.
	 */
	public TreeEvent(Object source) {
		this(source, null, null, null);
	}

	/**
	 * Constructs a <code>TreeEvent</code> with a specified property.
	 * 
	 * @param source the object that fired the event.
	 * @param treeProperty the property which identifies the tree's field that
	 *        changed.
	 */
	public TreeEvent(Object source, TreeProperty treeProperty) {
		this(source, treeProperty, null, null);
	}

	/**
	 * Constructs a <code>TreeEvent</code>.
	 * 
	 * @param source the object that fired the event.
	 * @param treeProperty the property which identifies the tree's field that
	 *        changed.
	 * @param oldValue the old value of the property.
	 * @param newValue the new value of the property.
	 */
	public TreeEvent(Object source, TreeProperty treeProperty, Object oldValue, Object newValue) {
		super(source);
		this.treeProperty = treeProperty;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Gets the <code>TreeProperty</code> that was changed. Among this
	 * {@link TreeProperty enumeration}.
	 * 
	 * @return the <code>TreeProperty</code> that was changed. May be null if
	 *         multiple properties have changed.
	 */
	public TreeProperty getProperty() {
		return treeProperty;
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