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
package org.epochx.epox.dbl;

import org.epochx.epox.*;

/**
 * A <code>FunctionNode</code> which performs the mathematical function of
 * subtraction.
 */
public class SubtractFunction extends Node {

	/**
	 * Construct an SubtractFunction with no children.
	 */
	public SubtractFunction() {
		this(null, null);
	}

	/**
	 * Construct a SubtractFunction with 2 children. When evaluated, both
	 * children will be evaluated, with the second subtracted from the first.
	 * 
	 * @param child1 The first child node.
	 * @param child2 The second child node, to be subtracted from the first.
	 */
	public SubtractFunction(final Node child1, final Node child2) {
		super(child1, child2);
	}

	/**
	 * Evaluating a <code>SubtractFunction</code> involves subtracting the
	 * result of the second child from the first.
	 */
	@Override
	public Object evaluate() {
		Object c1 = getChild(0).evaluate();
		Object c2 = getChild(1).evaluate();
		
		Class<?> returnType = getReturnType();
		
		if (returnType == Double.class) {
			// Subtract as doubles.
			double d1 = NodeUtils.asDouble(c1);
			double d2 = NodeUtils.asDouble(c2);
			
			return d1 - d2;
		} else if (returnType == Long.class) {
			// Subtract as longs.
			long l1 = NodeUtils.asLong(c1);
			long l2 = NodeUtils.asLong(c2);
			
			return l1 - l2;
		} else if (returnType == Integer.class) {
			// Subtract as integers.
			int i1 = NodeUtils.asInteger(c1);
			int i2 = NodeUtils.asInteger(c2);
			
			return i1 - i2;
		}
		
		return null;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the SubtractFunction which is SUB.
	 */
	@Override
	public String getIdentifier() {
		return "SUB";
	}
	
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		return NodeUtils.getWidestNumericalClass(inputTypes);
	}
}
