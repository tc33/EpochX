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

import org.epochx.epox.Node;


/**
 * A Node which provides the facility to sequence a specific number of 
 * instructions, specified at construction. Each of the instructions may be any 
 * other function or terminal node with a Void return type. This is the same
 * function that Koza calls progN in his work.
 */
public class SeqNFunction extends Node {

	/**
	 * Construct a SeqNFunction with no children, but which will be able to have
	 * the given number of children.
	 */
	public SeqNFunction(int n) {
		this((Node) null);
		
		setChildren(new Node[n]);
	}

	/**
	 * Construct a SeqNFunction with the given children. When evaluated, each
	 * child will be evaluated in order. As such they will have been
	 * executed in sequence.
	 * 
	 * @param children The child nodes to be executed in sequence.
	 */
	public SeqNFunction(final Node ... children) {
		super(children);
	}

	/**
	 * Evaluating a <code>SeqNFunction</code> involves evaluating each of the
	 * children in sequence. After evaluating its children, this method will 
	 * return null.
	 */
	@Override
	public Void evaluate() {
		int arity = getArity();
		for (int i=0; i<arity; i++) {
			getChild(i).evaluate();
		}

		return null;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the SeqNFunction is SEQn, where n is the 
	 * arity of the function.
	 */
	@Override
	public String getIdentifier() {
		return "SEQN";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		return Void.class;
	}
}
