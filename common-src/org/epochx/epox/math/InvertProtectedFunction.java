/*
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.epox.math;

import org.epochx.epox.Node;
import org.epochx.tools.util.*;

/**
 * A function node which performs the multiplicative inverse (or
 * reciprocal), that is the inverse of x is 1/x, called INV. This version of the
 * function is protected, so if the input is 0.0 then the result will be 1.0 to
 * protect against divide by zero.
 */
public class InvertProtectedFunction extends Node {

	/**
	 * Constructs an InvertFunction with one <code>null</code> child.
	 */
	public InvertProtectedFunction() {
		this(null);
	}

	/**
	 * Constructs an InvertFunction with one numerical child node.
	 * 
	 * @param child the child node.
	 */
	public InvertProtectedFunction(final Node child) {
		super(child);
	}

	/**
	 * Evaluates this function. The child node is evaluated, the
	 * result of which must be a numeric type (one of Double, Float, Long,
	 * Integer). The value <code>1</code> is divided by this child value to be
	 * the result of this function. This function is protected, so if the child
	 * evaluates to a value of <code>0.0</code> then there is no finite
	 * reciprocal and so the value <code>1.0</code> will be returned.
	 */
	@Override
	public Double evaluate() {
		final double c = NumericUtils.asDouble(getChild(0).evaluate());

		if (c == 0) {
			return 1.0;
		} else {
			return 1 / c;
		}
	}

	/**
	 * Returns the identifier of this function which is INV.
	 */
	@Override
	public String getIdentifier() {
		return "INV";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If there is one input type of a numeric type then the return type will
	 * be Double. In all other cases this method will return <code>null</code>
	 * to indicate that the inputs are invalid.
	 * 
	 * @return the Double class or null if the input type is invalid.
	 */
	@Override
	public Class<?> getReturnType(final Class<?> ... inputTypes) {
		if ((inputTypes.length == 1) && TypeUtils.isNumericType(inputTypes[0])) {
			return Double.class;
		} else {
			return null;
		}
	}
}
