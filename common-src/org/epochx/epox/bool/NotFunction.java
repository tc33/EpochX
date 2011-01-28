/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
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
package org.epochx.epox.bool;

import org.epochx.epox.Node;
import org.epochx.tools.util.TypeUtils;

/**
 * A function node which performs logical negation.
 */
public class NotFunction extends Node {

	/**
	 * Constructs a NotFunction with one <code>null</code> child.
	 */
	public NotFunction() {
		this(null);
	}

	/**
	 * Constructs a NotFunction with one boolean child node.
	 * 
	 * @param child The first child node.
	 */
	public NotFunction(final Node child) {
		super(child);
	}

	/**
	 * Evaluates this function. The child node is evaluated, the
	 * result of which must be a <code>Boolean</code> instance. The result is
	 * negated and returned as the result.
	 */
	@Override
	public Boolean evaluate() {
		return !((Boolean) getChild(0).evaluate()).booleanValue();
	}

	/**
	 * Returns the identifier of this function which is NOT.
	 */
	@Override
	public String getIdentifier() {
		return "NOT";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If there is one child with a return type of Boolean, then the return type
	 * of this function will also be Boolean. In all other cases this method
	 * will return <code>null</code> to indicate that the inputs are invalid.
	 * 
	 * @return The Boolean class or null if the input type is invalid.
	 */
	@Override
	public Class<?> getReturnType(final Class<?> ... inputTypes) {
		if ((inputTypes.length == 1) && TypeUtils.allEqual(inputTypes, Boolean.class)) {
			return Boolean.class;
		} else {
			return null;
		}
	}
}
