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
 * A function node which performs the logical operation of NOR
 * that is equivalent to the negation of logical OR or NOT OR.
 */
public class NorFunction extends Node {

	/**
	 * Constructs a NorFunction with two <code>null</code> children.
	 */
	public NorFunction() {
		this(null, null);
	}

	/**
	 * Constructs a NorFunction with two boolean child nodes.
	 * 
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 */
	public NorFunction(final Node child1, final Node child2) {
		super(child1, child2);
	}

	/**
	 * Evaluates this function lazily. The first child node is evaluated, the
	 * result of which must be a <code>Boolean</code> instance. If the result
	 * is a false value then the second child is also evaluated. The result of
	 * this function will be true if both children evaluate to false, otherwise
	 * the result will be false.
	 */
	@Override
	public Boolean evaluate() {
		boolean result = ((Boolean) getChild(0).evaluate()).booleanValue();

		if (!result) {
			result = ((Boolean) getChild(1).evaluate()).booleanValue();
		}

		return !result;
	}

	/**
	 * Returns the identifier of this function which is NOR.
	 */
	@Override
	public String getIdentifier() {
		return "NOR";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If there are two children, both of which have a return type of Boolean,
	 * then the return type of this function will also be Boolean. In all other
	 * cases this method will return <code>null</code> to indicate that the
	 * inputs are invalid.
	 * 
	 * @return The Boolean class or null if the input type is invalid.
	 */
	@Override
	public Class<?> getReturnType(final Class<?> ... inputTypes) {
		if ((inputTypes.length == 2) && TypeUtils.allEqual(inputTypes, Boolean.class)) {
			return Boolean.class;
		} else {
			return null;
		}
	}
}
