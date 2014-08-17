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
 * This class defines a function which causes an <code>Ant</code> instance to turn
 * right from its current orientation in its landscape. Although named as a
 * function, this node may operate as either a function or a terminal depending
 * on how the ant is provided. It may optionally be provided at construction, in
 * which case it becomes a terminal which operates on its internal ant.
 * Alternatively, it can require one child node with a data-type of <code>Ant</code>
 * . In this case, when evaluated it will first evaluate its child
 * to obtain its ant.
 * 
 * @see AntSkipFunction
 * @see AntMoveFunction
 * @see AntTurnLeftFunction
 */
public class AntTurnRightFunction extends Node {

	// This may remain null, depending on the constructor used.
	private Ant ant;

	/**
	 * Constructs an <code>AntTurnRightFunction</code> with one <code>null</code> child
	 */
	public AntTurnRightFunction() {
		this((Node) null);
	}

	/**
	 * Constructs an <code>AntTurnRightFunction</code> with one child node. The
	 * given child must have a return-type of <code>Ant</code>.
	 * 
	 * @param child this node's only child
	 */
	public AntTurnRightFunction(Node child) {
		super(child);
	}

	/**
	 * Constructs an <code>AntTurnRightFunction</code> with no child nodes, but
	 * the given ant which will be held internally. This makes the function a
	 * terminal node with arity 0. Note that this differs from the other
	 * constructors which take a child node with an <code>Ant</code> return type.
	 * 
	 * @param ant the ant that should be operated on when this node is
	 *        evaluated. An exception will be thrown if this argument is
	 *        <code>null</code>.
	 */
	public AntTurnRightFunction(Ant ant) {
		super();

		if (ant == null) {
			throw new IllegalArgumentException("ant must not be null");
		}

		this.ant = ant;
	}

	/**
	 * Evaluates this function. The ant is made to turn left from its current
	 * orientation in its landscape. The return type of this function node is 
	 * <code>Void</code>, and so the value returned from this method is
	 * undefined.
	 */
	@Override
	public Void evaluate() {
		Ant evalAnt = ant;
		if (getArity() > 0) {
			evalAnt = (Ant) getChild(0).evaluate();
		}

		evalAnt.turnRight();

		return null;
	}

	/**
	 * Returns the identifier of this function which is <code>TURN-RIGHT</code>
	 */
	@Override
	public String getIdentifier() {
		return "TURN-RIGHT";
	}

	/**
	 * Returns this function node's return type for the given child input types.
	 * If the arity of this node is 0, and the <code>inputTypes</code> array is 
	 * empty then the return type of this node will be <code>Void</code>. If the 
	 * arity is 1, and there is only one input type which is of an <code>Ant</code>
	 * type, then the return type of this function will be <code>Void</code>. In all
	 * other cases this method will return <code>null</code> to indicate that the 
	 * inputs are invalid.
	 * 
	 * @return <code>Void</code> or otherwise <code>null</code> if the input type is 
	 * invalid
	 */
	@Override
	public Class<?> dataType(Class<?> ... inputTypes) {
		if ((getArity() == 0) && (inputTypes.length == 0)) {
			return Void.class;
		} else if ((getArity() == 1) && (inputTypes.length == 1) && Ant.class.isAssignableFrom(inputTypes[0])) {
			return Void.class;
		} else {
			return null;
		}
	}
}
