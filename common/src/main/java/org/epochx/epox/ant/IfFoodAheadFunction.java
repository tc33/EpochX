/*
 * Copyright 2007-2013
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
package org.epochx.epox.ant;

import org.epochx.epox.Node;
import org.epochx.tools.ant.Ant;

/**
 * This class defines a function which conditionally evaluates one of two
 * children depending on the presence of food in the position in front of an
 * ant. The ant may be provided in one of two ways, depending on the constructor
 * used. The ant may be provided as a child node, in which case this function's
 * arity becomes 3. Otherwise, the ant must be provided at construction, and is
 * held internally, with the node's arity being 2.
 * 
 * @since 2.0
 */
public class IfFoodAheadFunction extends Node {

	// This may remain null, depending on the constructor used.
	private Ant ant;

	/**
	 * Constructs an <code>IfFoodAheadFunction</code> with three <code>null</code>
	 * children
	 */
	public IfFoodAheadFunction() {
		this((Node) null, null, null);
	}

	/**
	 * Constructs an <code>IfFoodAheadFunction</code> with three child nodes. The
	 * first child must have a return-type of <code>Ant</code>.
	 * 
	 * @param ant the ant child on which the condition is made
	 * @param child1 the first conditionally evaluated child node
	 * @param child2 the second conditionally evaluated child node
	 */
	public IfFoodAheadFunction(Node ant, Node child1, Node child2) {
		super(ant, child1, child2);
	}

	/**
	 * Constructs an <code>IfFoodAheadFunction</code> with two child nodes and
	 * an ant which will be held internally. This makes the function have arity
	 * of two. Note that this differs from the other constructors which
	 * take three child nodes, one of which has an <code>Ant</code> type.
	 * 
	 * @param ant the ant instance that should be operated upon when this node
	 *        is evaluated. An exception will be thrown if this argument is
	 *        <code>null</code>.
	 * @param child1 the first conditionally evaluated child node
	 * @param child2 the second conditionally evaluated child node
	 */
	public IfFoodAheadFunction(Ant ant, Node child1, Node child2) {
		super(child1, child2);

		if (ant == null) {
			throw new IllegalArgumentException("ant must not be null");
		}

		this.ant = ant;
	}

	/**
	 * Constructs an <code>IfFoodAheadFunction</code> with two <code>null</code> child
	 * nodes, and an ant which will be held internally. This makes the function
	 * have arity of two. Note that this differs from the other constructors
	 * which take three child nodes, one of which has an <code>Ant</code> type.
	 * 
	 * @param ant the <code>Ant</code> instance that should be operated on when this
	 *        node is evaluated. An exception will be thrown if this argument is
	 *        <code>null</code>.
	 */
	public IfFoodAheadFunction(final Ant ant) {
		this(ant, null, null);
	}

	/**
	 * Evaluates this function. The ant is checked for whether food is in the
	 * position ahead. If food is in the position directly in front of the ant
	 * then the first (non-ant) child node is evaluated, otherwise the second
	 * (non-ant) child node is evaluated. The return type of this function node
	 * is <code>Void</code>, and so the value returned from this method is
	 * undefined.
	 * 
	 * @return the return type of this function node is <code>Void</code>, and so
	 *         the value returned from this method is undefined
	 */
	@Override
	public Void evaluate() {
		Ant evalAnt;
		Node child1;
		Node child2;

		if (getArity() == 2) {
			evalAnt = ant;
			child1 = getChild(0);
			child2 = getChild(1);
		} else {
			evalAnt = (Ant) getChild(0).evaluate();
			child1 = getChild(1);
			child2 = getChild(2);
		}

		if (evalAnt.isFoodAhead()) {
			child1.evaluate();
		} else {
			child2.evaluate();
		}

		return null;
	}

	/**
	 * Returns the identifier of this function which is <code>IF-FOOD-AHEAD</code>
	 * 
	 * @return this node's identifier
	 */
	@Override
	public String getIdentifier() {
		return "IF-FOOD-AHEAD";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If the arity of this node is 2, and there are 2 inputs of type
	 * <code>Void</code>, then the return type will be <code>Void</code>. If the arity
	 * is 3, and there are 3 input types, the first of which is of an
	 * <code>Ant</code> type, then the return type of this function will be
	 * <code>Void</code>. In all other cases this method will return <code>null</code>
	 * to indicate that the inputs are invalid.
	 * 
	 * @return <code>Void</code> or otherwise <code>null</code> if the input type is
	 *         invalid
	 */
	@Override
	public Class<?> dataType(final Class<?> ... inputTypes) {
		if ((getArity() == 2) && (inputTypes.length == 2) && (inputTypes[0] == Void.class)
				&& (inputTypes[1] == Void.class)) {
			return Void.class;
		} else if ((getArity() == 3) && (inputTypes.length == 3) && Ant.class.isAssignableFrom(inputTypes[0])
				&& (inputTypes[1] == Void.class) && (inputTypes[2] == Void.class)) {
			return Void.class;
		} else {
			return null;
		}
	}
}
