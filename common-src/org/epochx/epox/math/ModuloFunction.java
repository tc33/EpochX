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
 * A <code>FunctionNode</code> which performs the modulo operation, that is
 * it finds the remainder of division.
 */
public class ModuloFunction extends Node {

	/**
	 * Construct a ModuloFunction with no children.
	 */
	public ModuloFunction() {
		this(null, null);
	}

	/**
	 * Construct a ModuloFunction with two children. When evaluated, the modulo
	 * of the evaluated children will be calculated.
	 * 
	 * @param child1 The first child node - the dividend.
	 * @param child2 The second child node - the divisor.
	 */
	public ModuloFunction(final Node child1, final Node child2) {
		super(child1, child2);
	}

	/**
	 * Evaluating a <code>ModuloFunction</code> involves dividing the evaluated
	 * first child, by the second child with the result being the remainder. If
	 * the second child evaluates to <code>0</code>, then the result will be 
	 * whatever the first child evaluated to.
	 */
	@Override
	public Object evaluate() {
		Object c1 = getChild(0).evaluate();
		Object c2 = getChild(1).evaluate();

		Class<?> returnType = getReturnType();
		
		if (returnType == Double.class) {
			double d1 = NumericUtils.asDouble(c1);
			double d2 = NumericUtils.asDouble(c2);
			
			return (d2 == 0) ? d1 : (d1 % d2);
		} else if (returnType == Float.class) {
			double f1 = NumericUtils.asFloat(c1);
			double f2 = NumericUtils.asFloat(c2);
			
			return (f2 == 0) ? f1 : (f1 % f2);
		} else if (returnType == Integer.class) {
			int i1 = NumericUtils.asInteger(c1);
			int i2 = NumericUtils.asInteger(c2);
			
			return (i2 == 0) ? i1 : (i1 % i2);
		} else if (returnType == Long.class) {
			long l1 = NumericUtils.asLong(c1);
			long l2 = NumericUtils.asLong(c2);
			
			return (l2 == 0) ? l1 : (l1 % l2);
		} else {
			return null;
		}
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the ModuloFunction which is MOD.
	 */
	@Override
	public String getIdentifier() {
		return "MOD";
	}
	
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		return TypeUtils.getNumericType(inputTypes);
	}
}
