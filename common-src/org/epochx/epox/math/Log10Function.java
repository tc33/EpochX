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
 * A <code>FunctionNode</code> which performs the common (base 10) logarithm.
 * 
 * @see LogFunction
 */
public class Log10Function extends Node {

	/**
	 * Construct a Log10Function with no children.
	 */
	public Log10Function() {
		this(null);
	}

	/**
	 * Construct a Log10Function with one child. When evaluated, the logarithm
	 * of the evaluated child will be calculated.
	 * 
	 * @param child The child of which the base 10 logarithm will be calculated.
	 */
	public Log10Function(final Node child) {
		super(child);
	}

	/**
	 * Evaluating a <code>Log10Function</code> involves evaluating the child
	 * then calculating it's base 10 logarithm.
	 */
	@Override
	public Double evaluate() {
		double c = NumericUtils.asDouble(getChild(0).evaluate());

		return Math.log10(c);
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the Log10Function which is LOG-10.
	 */
	@Override
	public String getIdentifier() {
		return "LOG-10";
	}
	
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 2 && TypeUtils.isAllNumericType(inputTypes)) {
			return Double.class;
		}
		
		return null;
	}
}
