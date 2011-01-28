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
 * A function node which performs the modulo operation, that is
 * it finds the remainder of division. This version of the function is
 * protected, so if the divisor input is 0.0 then the result will be value of
 * the dividend.
 */
public class ModuloProtectedFunction extends Node {

	/**
	 * Constructs a ModuloProtectedFunction with two <code>null</code> children.
	 */
	public ModuloProtectedFunction() {
		this(null, null);
	}

	/**
	 * Constructs a ModuloProtectedFunction with two numeric <code>null</code>
	 * children.
	 * 
	 * @param child1 The first child node - the dividend.
	 * @param child2 The second child node - the divisor.
	 */
	public ModuloProtectedFunction(final Node child1, final Node child2) {
		super(child1, child2);
	}

	/**
	 * Evaluates this function. Both child nodes are evaluated, the result of
	 * both must be of numeric type. If necessary, the inputs are widened to
	 * both be of the same type, then division is performed with the remainder
	 * used as the result of this function. The return value will be of the
	 * wider input data type. If the divisor resolves to zero then the result
	 * returned will be the first (dividend) child.
	 */
	@Override
	public Object evaluate() {
		final Object c1 = getChild(0).evaluate();
		final Object c2 = getChild(1).evaluate();

		final Class<?> returnType = TypeUtils.getNumericType(c1.getClass(), c2.getClass());

		if (returnType == Double.class) {
			final double d1 = NumericUtils.asDouble(c1);
			final double d2 = NumericUtils.asDouble(c2);

			return (d2 == 0) ? d1 : (d1 % d2);
		} else if (returnType == Float.class) {
			final float f1 = NumericUtils.asFloat(c1);
			final float f2 = NumericUtils.asFloat(c2);

			return (f2 == 0) ? f1 : (f1 % f2);
		} else if (returnType == Integer.class) {
			final int i1 = NumericUtils.asInteger(c1);
			final int i2 = NumericUtils.asInteger(c2);

			return (i2 == 0) ? i1 : (i1 % i2);
		} else if (returnType == Long.class) {
			final long l1 = NumericUtils.asLong(c1);
			final long l2 = NumericUtils.asLong(c2);

			return (l2 == 0) ? l1 : (l1 % l2);
		} else {
			return null;
		}
	}

	/**
	 * Returns the identifier of this function which is MOD.
	 */
	@Override
	public String getIdentifier() {
		return "MOD";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If there are two input types of numeric type then the return type will
	 * be the wider of those numeric types. In all other cases this method will
	 * return <code>null</code> to indicate that the inputs are invalid.
	 * 
	 * @return A numeric class or null if the input type is invalid.
	 */
	@Override
	public Class<?> getReturnType(final Class<?> ... inputTypes) {
		if (inputTypes.length == 2) {
			return TypeUtils.getNumericType(inputTypes);
		}
		return null;
	}
}
