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
 * 
 * 
 */
public class AbsoluteFunction extends Node {

	/**
	 * Construct an AbsoluteFunction with no children.
	 */
	public AbsoluteFunction() {
		this(null);
	}

	/**
	 * 
	 */
	public AbsoluteFunction(final Node child) {
		super(child);
	}

	/**
	 * 
	 */
	@Override
	public Object evaluate() {
		Object c = getChild(0).evaluate();
		
		Class<?> returnType = getReturnType();
		
		if (returnType == Double.class) {
			// Perform absolute on double.
			return Math.abs(NodeUtils.asDouble(c));
		} else if (returnType == Long.class) {
			// Perform absolute on long.
			long l = NodeUtils.asLong(c);
			
			return Math.abs(l);
		} else if (returnType == Integer.class) {
			// Perform absolute on integer.
			int i = NodeUtils.asInteger(c);
			
			return Math.abs(i);
		}

		return null;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AbsoluteFunction which is ABS.
	 */
	@Override
	public String getIdentifier() {
		return "ABS";
	}

	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		return NodeUtils.getWidestNumericalClass(inputTypes);
	}
}
