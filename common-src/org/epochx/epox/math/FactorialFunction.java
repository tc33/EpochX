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
 * A function node which performs the mathematical function of
 * factorial, which is normally expressed with an exclamation mark !
 * 
 * For example:
 * 5! = 5 x 4 x 3 x 2 x 1 = FACTORIAL 5
 */
public class FactorialFunction extends Node {

	/**
	 * Constructs a FactorialFunction with one <code>null</code> child.
	 */
	public FactorialFunction() {
		this(null);
	}

	/**
	 * Constructs a FactorialFunction with one numerical child node.
	 * 
	 * @param child the child node.
	 */
	public FactorialFunction(final Node child) {
		super(child);
	}

	/**
	 * Evaluates this function. The child node is evaluated, the
	 * result of which must be of an integer type (one of Byte, Short, Integer,
	 * Long). If the value is negative, then its absolute value is used to avoid
	 * a divide by zero. The factorial function is performed on this value
	 * and the result returned as an Integer if the input was one of Byte,
	 * Short or Integer and returned as a Long if the input is a Long.
	 */
	@Override
	public Object evaluate() {
		final Object c = getChild(0).evaluate();

		final long cint = Math.abs(NumericUtils.asLong(c));

		long factorial = 1;
		for (long i = 1; i <= cint; i++) {
			factorial = factorial * i;
		}

		if (c instanceof Long) {
			return factorial;
		} else {
			return (int) factorial;
		}
	}

	/**
	 * Returns the identifier of this function which is FACTORIAL.
	 */
	@Override
	public String getIdentifier() {
		return "FACTORIAL";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If there is one input type of Byte, Short or Integer then the return type
	 * will be Integer, if it is Long then the return type will also be Long. In
	 * all other cases this method will return <code>null</code> to indicate
	 * that the inputs are invalid.
	 * 
	 * @return the Integer or Long class or null if the input type is invalid.
	 */
	@Override
	public Class<?> getReturnType(final Class<?> ... inputTypes) {
		if ((inputTypes.length == 1) && TypeUtils.isIntegerType(inputTypes[0])) {
			return TypeUtils.getNumericType(inputTypes[0]);
		}

		return null;
	}
}
