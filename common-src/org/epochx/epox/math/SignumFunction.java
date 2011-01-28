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
 * A function node which performs the mathematical sign function
 * that extracts the sign of a number.
 */
public class SignumFunction extends Node {

	/**
	 * Constructs a SignumFunction with one <code>null</code> child.
	 */
	public SignumFunction() {
		this(null);
	}

	/**
	 * Constructs a SignumFunction with one numerical child node.
	 * 
	 * @param child the child node.
	 */
	public SignumFunction(final Node child) {
		super(child);
	}

	/**
	 * Evaluates this function. The child node is evaluated, the
	 * result of which must be a numeric type (one of Double, Float, Long,
	 * Integer). Then the result will be -1, if the value is negative, +1, if
	 * the value is positive and 0 if the value is zero. The type of the value
	 * returned will be the same as the input type.
	 */
	@Override
	public Object evaluate() {
		final Object c = getChild(0).evaluate();

		final double result = Math.signum(NumericUtils.asDouble(c));

		if (c instanceof Double) {
			return result;
		} else if (c instanceof Float) {
			return (float) result;
		} else if (c instanceof Integer) {
			return (int) result;
		} else if (c instanceof Long) {
			return (long) result;
		}

		return null;
	}

	/**
	 * Returns the identifier of this function which is SGN.
	 */
	@Override
	public String getIdentifier() {
		return "SGN";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If there is one input type of a numeric type then the return type will
	 * be that same numeric type. In all other cases this method will return
	 * <code>null</code> to indicate that the inputs are invalid.
	 * 
	 * @return A numeric class or null if the input type is invalid.
	 */
	@Override
	public Class<?> getReturnType(final Class<?> ... inputTypes) {
		if ((inputTypes.length == 1) && TypeUtils.isNumericType(inputTypes[0])) {
			return inputTypes[0];
		} else {
			return null;
		}
	}
}
