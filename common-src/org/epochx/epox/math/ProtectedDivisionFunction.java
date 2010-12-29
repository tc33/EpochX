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
 * A <code>FunctionNode</code> which performs the arithmetic function of
 * division. The division is protected to avoid the scenario where division
 * by zero is attempted - which is undefined. Division by zero evaluates to
 * zero.
 */
public class ProtectedDivisionFunction extends Node {

	private final Double protectionValue;

	/**
	 * Construct a ProtectedDivisionFunction with no children. A default
	 * protection value that is returned in the case of divide-by-zero is set
	 * as 0.0.
	 */
	public ProtectedDivisionFunction() {
		this(null, null);
	}

	/**
	 * Construct a ProtectedDivisionFunction with a protection value to assign
	 * during divide-by-zero. This overrides the default protection value of
	 * 0.0.
	 * 
	 * @param protectionValue a double value to return in the case of
	 *        divide-by-zeros.
	 */
	public ProtectedDivisionFunction(final double protectionValue) {
		this(null, null, protectionValue);
	}

	/**
	 * Construct a ProtectedDivisionFunction with 2 children. When evaluated,
	 * the evaluation of the first child will be divided by the evaluation of
	 * the second. A default protection value that is returned in the case of
	 * divide-by-zero is set as 0.0.
	 * 
	 * @param dividend The first child node - the dividend.
	 * @param divisor The second child node - the divisor.
	 */
	public ProtectedDivisionFunction(final Node dividend, final Node divisor) {
		this(dividend, divisor, 0.0);
	}

	/**
	 * Construct a ProtectedDivisionFunction with 2 children and a
	 * divide-by-zero protection value. When evaluated, the evaluation of the
	 * first child will be divided by the evaluation of the second. If the
	 * second (divisor) child evaluates to zero, then no division takes place
	 * and the specified protectionValue is returned.
	 * 
	 * @param dividend The first child node - the dividend.
	 * @param divisor The second child node - the divisor.
	 * @param protectionValue a double value to return in the case of
	 *        divide-by-zeros.
	 */
	public ProtectedDivisionFunction(final Node dividend,
			final Node divisor, final double protectionValue) {
		super(dividend, divisor);

		this.protectionValue = protectionValue;
	}

	/**
	 * Evaluating a <code>ProtectedDivisionFunction</code> involves dividing
	 * the result of evaluating both children. If the divisor resolves to zero
	 * then the result returned will be zero to avoid the divide by zero issue.
	 * For performance, this function is evaluated lazily. The divisor child is
	 * evaluated first, if it evaluates to <code>0.0</code> then the first child
	 * will not be evaluated at all.
	 */
	@Override
	public Object evaluate() {
		Object c1 = getChild(0).evaluate();
		Object c2 = getChild(1).evaluate();
		Class<?> returnType = getReturnType();
		
		if (returnType == Double.class) {
			// Divide as doubles.
			double d1 = NumericUtils.asDouble(c1);
			double d2 = NumericUtils.asDouble(c2);
			
			return (d2 == 0) ? NumericUtils.asDouble(protectionValue) : (d1 / d2);
		} else if (returnType == Float.class) {
			// Divide as floats.
			float f1 = NumericUtils.asFloat(c1);
			float f2 = NumericUtils.asFloat(c2);
			
			return (f2 == 0) ? NumericUtils.asFloat(protectionValue) : (f1 / f2);
		} else if (returnType == Long.class) {
			// Divide as longs.
			long l1 = NumericUtils.asLong(c1);
			long l2 = NumericUtils.asLong(c2);
			
			return (l2 == 0) ? NumericUtils.asLong(protectionValue) : (l1 / l2);
		} else if (returnType == Integer.class) {
			// Divide as intgers.
			int i1 = NumericUtils.asInteger(c1);
			int i2 = NumericUtils.asInteger(c2);
			
			return (i2 == 0) ? NumericUtils.asInteger(protectionValue) : (i1 / i2);
		}
		
		return null;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the ProtectedDivisionFunction which is PDIV.
	 */
	@Override
	public String getIdentifier() {
		return "PDIV";
	}
	
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		return TypeUtils.getNumericType(inputTypes);
	}
}
