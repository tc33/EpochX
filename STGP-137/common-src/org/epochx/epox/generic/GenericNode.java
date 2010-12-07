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
package org.epochx.epox.generic;

import org.epochx.epox.Node;



/**
 * 
 */
public abstract class GenericNode extends Node {
	
	/**
	 * 
	 */
	//public abstract Object evaluate();
	
	//public abstract int getArity();
	
	/*public abstract Class getReturnType(Class ... inputs);
	
	public abstract Class[] getValidArgTypes(Class returnType);*/
	
	/**
	 * Constructs a <code>GenericNode</code>.
	 * 
	 * @param child1
	 * @param child2
	 */
	public GenericNode(GenericNode ... children) {
		super(children);
	}

	/**
	 * This method may return null if any child nodes are missing, but if a full
	 * set of child nodes have been attached then it should always return a 
	 * valid type.
	 */
	public abstract Class<?> getReturnType();
	
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		int arity = getArity();
		
		// Validate the number of input types given against the arity.
		if (inputTypes.length != arity) {
			throw new IllegalArgumentException("The number of input types should match a node's arity.");
		}
		
		if (arity == 0) {
			return getReturnType();
		} else {
			return inputTypes[0];
		}
	}
}
