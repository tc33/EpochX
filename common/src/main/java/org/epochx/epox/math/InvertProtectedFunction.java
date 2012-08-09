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
import org.epochx.tools.*;

/**
 * A node which performs the multiplicative inverse (or
 * reciprocal), that is the inverse of x is 1/x, called INV. This version of the
 * function is protected, so if the input is 0.0 then the result will be 1.0 to
 * protect against divide by zero.
 */
public class InvertProtectedFunction extends Node {

	// The value returned in place of divide-by-zero.
	private Double protectionValue;
	
	/**
	 * Constructs an InvertFunction with one <tt>null</tt> child.
	 */
	public InvertProtectedFunction() {
		this(null);
	}

	/**
	 * Constructs an InvertFunction with one numerical child node.
	 * 
	 * @param child the child node.
	 */
	public InvertProtectedFunction(Node child) {
		this(child, 1.0);
	}
	
	/**
	 * Constructs an InvertFunction with one <tt>null</tt> child and the 
	 * given protection value which will be returned if the child evaluates to 
	 * 0.0.
	 * @param protectionValue the value to return for a child that evaluates to 
	 * 0.0.
	 */
	public InvertProtectedFunction(double protectionValue) {
		this(null);
	}

	/**
	 * Constructs an InvertFunction with one numerical child node and the 
	 * given protection value which will be returned if the child evaluates to 
	 * 0.0..
	 * 
	 * @param child the child node.
	 * @param protectionValue the value to return for a child that evaluates to 
	 * 0.0.
	 */
	public InvertProtectedFunction(Node child, double protectionValue) {
		super(child);
		
		this.protectionValue = protectionValue;
	}

	/**
	 * Evaluates this function. The child node is evaluated, the
	 * result of which must be a numeric type (one of Double, Float, Long,
	 * Integer). The value <tt>1</tt> is divided by this child value to be
	 * the result of this function. This function is protected, so if the child
	 * evaluates to a value of <tt>0.0</tt> then there is no finite
	 * reciprocal and so the value <tt>1.0</tt> will be returned.
	 */
	@Override
	public Double evaluate() {
		double c = NumericUtils.asDouble(getChild(0).evaluate());

		if (c == 0) {
			return protectionValue;
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
	 * be Double. In all other cases this method will return <tt>null</tt>
	 * to indicate that the inputs are invalid.
	 * 
	 * @return the Double class or null if the input type is invalid.
	 */
	@Override
	public Class<?> dataType(Class<?> ... inputTypes) {
		if ((inputTypes.length == 1) && DataTypeUtils.isNumericType(inputTypes[0])) {
			return Double.class;
		} else {
			return null;
		}
	}
	
	
	/**
	 * Sets the protection value that should be returned in case of 
	 * divide-by-zero.
	 * @param protectionValue the value to be returned if divide-by-zero is 
	 * attempted.
	 */
	public void setProtectionValue(Double protectionValue) {
		this.protectionValue = protectionValue;
	}
	
	/**
	 * Returns the protection value that will be returned in the case of 
	 * divide-by-zero.
	 * @return the protectionValue the value that will be returned in the case
	 * of divide-by-zero.
	 */
	public Double getProtectionValue() {
		return protectionValue;
	}
}