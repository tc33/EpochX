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
 * A <code>FunctionNode</code> which performs the mathematical sign function
 * that extracts the sign of a number.
 */
public class SignumFunction extends Node {

	/**
	 * Construct a SignumFunction with no children.
	 */
	public SignumFunction() {
		this(null);
	}

	/**
	 * Construct a SignumFunction with one child. When evaluated, the child
	 * will be evaluated with signum performed on the result.
	 * 
	 * @param child The child which signum will be performed on.
	 */
	public SignumFunction(final Node child) {
		super(child);
	}

	/**
	 * Evaluating a <code>SignumFunction</code> involves evaluating the child
	 * then the result will be zero if it resolves to zero, 1.0 if greater than
	 * zero and -1.0 if less than zero.
	 */
	@Override
	public Object evaluate() {
		Object c = getChild(0).evaluate();
		
		double result = Math.signum(NodeUtils.asDouble(c));
		
		if (c instanceof Double) {
			return result;
		} else if (c instanceof Integer) {
			return (int) result;
		} else if (c instanceof Long) {
			return (long) result;
		}
		
		return null;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the SignumFunction which is SGN.
	 */
	@Override
	public String getIdentifier() {
		return "SGN";
	}
	
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 1 && NodeUtils.isNumericalClass(inputTypes[0])) {
			return inputTypes[0];
		} else {
			return null;
		}
	}
}
