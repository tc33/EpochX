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
package org.epochx.epox.ant;

import org.epochx.epox.*;
import org.epochx.tools.ant.Ant;

/**
 * This class defines a function which requires one child with a data-type of 
 * Ant. When evaluated, this function will evaluate its child and cause the
 * returned Ant to move on one time-step without moving its position in the 
 * landscape.
 * 
 * @see AntMoveFunction
 * @see AntTurnLeftFunction
 * @see AntTurnRightFunction
 */
public class AntSkipFunction extends Node {

	/**
	 * Constructs an AntSkipFunction with one <code>null</code> child.
	 */
	public AntSkipFunction() {
		this(null);
	}
	
	/**
	 * Constructs an AntSkipFunction with one child node. The given child
	 * must have a return-type of Ant.
	 * 
	 * @param child this node's only child.
	 */
	public AntSkipFunction(final Node child) {
		super(child);
	}

	/**
	 * Evaluates this function. The Ant returned by evaluating this node's child
	 * is made to skip one time step without moving its position within the 
	 * landscape. The return type of this function node is Void, and so the 
	 * value returned from this method is undefined.
	 */
	@Override
	public Void evaluate() {
		Ant ant = (Ant) getChild(0).evaluate();
		
		ant.skip();

		return null;
	}
	
	/**
	 * Returns the identifier of this function which is SKIP.
	 */
	@Override
	public String getIdentifier() {
		return "SKIP";
	}
	
	/**
	 * Returns this function node's return type for the given child input types.
	 * If there is only one input type which is a sub-type of Ant then the 
	 * return type of this function will be Void. In all other cases this method
	 * will return <code>null</code> to indicate that the inputs are invalid.
	 * 
	 * @return The Void class or null if the input type is invalid.
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 1 && Ant.class.isAssignableFrom(inputTypes[0])) {
			return Void.class;
		} else {
			return null;
		}
	}
}
