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
package org.epochx.epox.ant;

import org.epochx.epox.*;
import org.epochx.tools.ant.Ant;

/**
 * This class defines a function which requires three children, the first of 
 * which has the data-type of Ant. When evaluated, this function will evaluate 
 * its first child and then conditionally evaluate either the second or third
 * child depending on whether the ant has a food item in the position directly
 * in front of it.
 */
public class IfFoodAheadFunction extends Node {

	/**
	 * Constructs an IfFoodAheadFunction with three <code>null</code> children.
	 */
	public IfFoodAheadFunction() {
		this(null, null, null);
	}

	/**
	 * Constructs an IfFoodAheadFunction with three child nodes. The first child
	 * must have a return-type of Ant.
	 * 
	 * @param ant the ant child upon which the condition is made.
	 * @param child1 The first conditionally evaluated child node.
	 * @param child2 The second conditionally evaluated child node.
	 */
	public IfFoodAheadFunction(final Node ant, final Node child1, final Node child2) {
		super(ant, child1, child2);
	}

	/**
	 * Evaluates this function. The Ant returned by evaluating this node's first
	 * child is checked for whether food is in the position ahead. If food is in
	 * the position directly in front of the ant then the second child node is
	 * evaluated, otherwise the third child node is evaluated. The return type 
	 * of this function node is Void, and so the value returned from this method
	 * is undefined.
	 */
	@Override
	public Void evaluate() {
		Ant ant = (Ant) getChild(0).evaluate();
		
		if (ant.isFoodAhead()) {
			getChild(1).evaluate();
		} else {
			getChild(2).evaluate();
		}

		return null;
	}

	/**
	 * Returns the identifier of this function which is IF-FOOD-AHEAD.
	 */
	@Override
	public String getIdentifier() {
		return "IF-FOOD-AHEAD";
	}
	
	/**
	 * Returns this function node's return type for the given child input types.
	 * If there are three input types, the first of which is a sub-type of Ant 
	 * then the return type of this function will be Void. In all other cases 
	 * this method will return <code>null</code> to indicate that the inputs are
	 * invalid.
	 * 
	 * @return The Void class or null if the input type is invalid.
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 3 && Ant.class.isAssignableFrom(inputTypes[0])) {
			return Void.class;
		} else {
			return null;
		}
	}
}
