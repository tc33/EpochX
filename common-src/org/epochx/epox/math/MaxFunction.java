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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.epox.math;

import org.epochx.epox.Node;
import org.epochx.tools.util.*;

/**
 * A <code>FunctionNode</code> which performs the simple comparison function
 * of determining which of 2 numbers is larger, as per the boolean greater-than
 * function. This function can have a flexible number of children and as such 
 * cannot be parsed by the EpoxParser, look at using Max2Function instead.
 */
public class MaxFunction extends Node {

	/**
	 * Construct a MaxFunction which takes the given number of children.
	 */
	public MaxFunction(int n) {
		this((Node) null);
		
		setChildren(new Node[n]);
	}

	/**
	 * Construct a MaxFunction with two child. When evaluated, the children will
	 * both be evaluated with the larger of the two returned as the result.
	 * 
	 * @param child1 The first child node for comparison.
	 * @param child2 The second child node for comparison.
	 */
	public MaxFunction(final Node ... children) {
		super(children);
	}

	/**
	 * Evaluating a <code>MaxFunction</code> involves evaluating the children
	 * then comparing and returning which ever is the larger of the 2 evaluated
	 * children.
	 */
	@Override
	public Object evaluate() {
		int arity = getArity();
		Class<?> returnType = getReturnType();
		
		if (returnType == Double.class) {
			double max = Double.MIN_VALUE;
			for (int i=0; i<arity; i++) {
				double value = NumericUtils.asDouble(getChild(i).evaluate());
				max = Math.max(value, max);
			}
			return max;
		} else if (returnType == Float.class) {
			float max = Float.MIN_VALUE;
			for (int i=0; i<arity; i++) {
				float value = NumericUtils.asFloat(getChild(i).evaluate());
				max = Math.max(value, max);
			}
			return max;
		} else if (returnType == Long.class) {
			long max = Long.MIN_VALUE;
			for (int i=0; i<arity; i++) {
				long value = NumericUtils.asLong(getChild(i).evaluate());
				max = Math.max(value, max);
			}
			return max;
		} else if (TypeUtils.isNumericType(returnType)) {
			int max = Integer.MIN_VALUE;
			for (int i=0; i<arity; i++) {
				int value = NumericUtils.asInteger(getChild(i).evaluate());
				max = Math.max(value, max);
			}
			return max;
		} else {
			return null;
		}
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the MaxFunction which is MAX.
	 */
	@Override
	public String getIdentifier() {
		return "MAX";
	}
	
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		return TypeUtils.getNumericType(inputTypes);
	}
}
