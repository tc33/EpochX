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
package org.epochx.epox.math;

import org.epochx.epox.Node;
import org.epochx.tools.util.*;

/**
 * A function node which performs the numerical greater than comparison of two
 * numeric inputs, called GT.
 */
public class GreaterThanFunction extends Node {

	/**
	 * Constructs a GreaterThanFunction with two <code>null</code> children.
	 */
	public GreaterThanFunction() {
		this(null, null);
	}

	/**
	 * Constructs a GreaterThanFunction with two numerical child nodes.
	 * 
	 * @param child1 The first child which is being tested if it is greater than
	 *        the second child.
	 * @param child2 The second child which the first child is being tested
	 *        against.
	 */
	public GreaterThanFunction(final Node child1, final Node child2) {
		super(child1, child2);
	}

	/**
	 * Evaluates this function. The child nodes are evaluated, the
	 * results of which must be numerically type (any of Double, Float, Long,
	 * Integer). If the result of the first child is larger than the result of
	 * the second child, then a boolean value of <code>true</code> will be
	 * returned, otherwise a <code>false</code> value will be returned.
	 */
	@Override
	public Boolean evaluate() {
		final Object c1 = getChild(0).evaluate();
		final Object c2 = getChild(1).evaluate();

		final double value1 = NumericUtils.asDouble(c1);
		final double value2 = NumericUtils.asDouble(c2);

		return (value1 > value2);
	}

	/**
	 * Returns the identifier of this function which is GT.
	 */
	@Override
	public String getIdentifier() {
		return "GT";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If there is two numeric input types then the return type will be Boolean.
	 * In all other cases this method will return <code>null</code> to indicate
	 * that the inputs are invalid.
	 * 
	 * @return the Boolean class or null if the input type is invalid.
	 */
	@Override
	public Class<?> getReturnType(final Class<?> ... inputTypes) {
		if ((inputTypes.length == 2) && TypeUtils.isAllNumericType(inputTypes)) {
			return Boolean.class;
		}

		return null;
	}
}
