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

import org.epochx.epox.*;
import org.epochx.tools.util.*;

/**
 * A function node which performs the simple comparison function of determining
 * which of a set of numbers is larger, as per the boolean greater-than
 * function. This function can have a flexible number of children and as such
 * cannot be parsed by the {@link EpoxParser}, look at using
 * {@link Max2Function} or {@link Max3Function} instead.
 */
public class MaxFunction extends Node {

	/**
	 * Constructs a MaxFunction with the given number of <code>null</code>
	 * children.
	 * @param n the number of <code>null</code> children to set this function up
	 * for.
	 */
	public MaxFunction(final int n) {
		this((Node) null);

		setChildren(new Node[n]);
	}

	/**
	 * Constructs a MaxFunction with given numerical child nodes.
	 * 
	 * @param children the numeric child nodes.
	 */
	public MaxFunction(final Node ... children) {
		super(children);
	}

	/**
	 * Evaluates this function. The child nodes are evaluated, the
	 * results of which must be numerically typed (any of Double, Float, Long,
	 * Integer). The largest of the child values will be returned as the result
	 * as the widest of the numeric types.
	 */
	@Override
	public Object evaluate() {
		final int arity = getArity();
		
		Object[] childValues = new Object[arity];
		Class<?>[] types = new Class<?>[arity];
		for (int i=0; i<arity; i++) {
			childValues[i] = getChild(i).evaluate();
			types[i] = childValues[i].getClass();
		}
		final Class<?> returnType = TypeUtils.getNumericType(types);

		if (returnType == Double.class) {
			double max = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < arity; i++) {
				final double value = NumericUtils.asDouble(childValues[i]);
				max = Math.max(value, max);
			}
			return max;
		} else if (returnType == Float.class) {
			float max = Float.NEGATIVE_INFINITY;
			for (int i = 0; i < arity; i++) {
				final float value = NumericUtils.asFloat(childValues[i]);
				max = Math.max(value, max);
			}
			return max;
		} else if (returnType == Long.class) {
			long max = Long.MIN_VALUE;
			for (int i = 0; i < arity; i++) {
				final long value = NumericUtils.asLong(childValues[i]);
				max = Math.max(value, max);
			}
			return max;
		} else if (TypeUtils.isNumericType(returnType)) {
			int max = Integer.MIN_VALUE;
			for (int i = 0; i < arity; i++) {
				final int value = NumericUtils.asInteger(childValues[i]);
				max = Math.max(value, max);
			}
			return max;
		} else {
			return null;
		}
	}

	/**
	 * Returns the identifier of this function which is MAX.
	 */
	@Override
	public String getIdentifier() {
		return "MAX";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If there is the correct number of numeric input types then the return
	 * type will be the widest of those types. In all other cases this method
	 * will return <code>null</code> to indicate that the inputs are invalid.
	 * 
	 * @return the widest numeric type or null if the input types are invalid.
	 */
	@Override
	public Class<?> getReturnType(final Class<?> ... inputTypes) {
		if (inputTypes.length == getArity()) {
			return TypeUtils.getNumericType(inputTypes);
		}
		return null;
	}
}
