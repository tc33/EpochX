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
 * A <code>FunctionNode</code> which performs the mathematical function of
 * addition.
 * 
 * Addition can be performed on inputs of the following types: 
 * <ul>
 * <li>Integer</li>
 * <li>Long</li>
 * <li>Double</li>
 * </ul>
 * 
 * Addition can be performed between mixed types, with a widening operation 
 * performed and the result being of the wider of the two types.
 */
public class AddFunction extends Node {

	/**
	 * Construct an AddFunction with no children.
	 */
	public AddFunction() {
		this(null, null);
	}

	/**
	 * Construct an AddFunction with 2 children. When evaluated, both children
	 * will
	 * be evaluated and added together.
	 * 
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 */
	public AddFunction(final Node child1, final Node child2) {
		super(child1, child2);
	}

	/**
	 * Evaluating an <code>AddFunction</code> involves summing the result of
	 * evaluating both children.
	 */
	@Override
	public Object evaluate() {
		Object c1 = getChild(0).evaluate();
		Object c2 = getChild(1).evaluate();
		
		Class<?> returnType = getReturnType();
		
		if (returnType == Double.class) {
			// Add as doubles.
			double d1 = NumericUtils.asDouble(c1);
			double d2 = NumericUtils.asDouble(c2);
			
			return d1 + d2;
		} else if (returnType == Float.class) {
			// Add as floats.
			float f1 = NumericUtils.asFloat(c1);
			float f2 = NumericUtils.asFloat(c2);
			
			return f1 + f2;
		} else if (returnType == Long.class) {
			// Add as longs.
			long l1 = NumericUtils.asLong(c1);
			long l2 = NumericUtils.asLong(c2);
			
			return l1 + l2;
		} else if (returnType == Integer.class) {
			// Add as intgers.
			int i1 = NumericUtils.asInteger(c1);
			int i2 = NumericUtils.asInteger(c2);
			
			return i1 + i2;
		}
		
		return null;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AddFunction which is ADD.
	 */
	@Override
	public String getIdentifier() {
		return "ADD";
	}
	
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		return TypeUtils.getNumericType(inputTypes);
	}
}
