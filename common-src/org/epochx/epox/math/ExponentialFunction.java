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

import org.epochx.epox.*;

/**
 * A <code>FunctionNode</code> which performs the mathematical exponential 
 * function <code>e^x</code> where <code>e</code> is the constant known as 
 * Euler's number.
 */
public class ExponentialFunction extends Node {

	/**
	 * Construct a ExponentialFunction with no children.
	 */
	public ExponentialFunction() {
		this(null);
	}

	/**
	 * Construct a ExponentialFunction with 1 child. When evaluated, the 
	 * value of the constant e is raised to the power of the value after 
	 * evaluating the child.
	 * 
	 * @param base The child node - the exponent.
	 */
	public ExponentialFunction(final Node exponent) {
		super(exponent);
	}

	/**
	 * Evaluating a <code>ExponentialFunction</code> involves evaluating the
	 * child then raising e to that power. 
	 */
	@Override
	public Double evaluate() {
		Object c = getChild(0).evaluate();
		
		return Math.exp(NodeUtils.asDouble(c));
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the ExponentialFunction which is EXP.
	 */
	@Override
	public String getIdentifier() {
		return "EXP";
	}
	
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 1 && NodeUtils.isNumericalClass(inputTypes[0])) {
			return Double.class;
		} else {
			return null;
		}
	}
}
