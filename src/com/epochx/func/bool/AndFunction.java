/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.func.bool;

import com.epochx.core.representation.*;

/**
 * A <code>FunctionNode</code> which performs logical conjunction or the 
 * boolean function of AND.
 */
public class AndFunction extends FunctionNode<Boolean> {
	
	/**
	 * Construct an AndFunction with no children.
	 */
	public AndFunction() {
		this(null, null);
	}
	
	/**
	 * Construct an AndFunction with two children. When evaluated, if both 
	 * children evaluate to true then the result will be true. All other 
	 * combinations will return a result of false.
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 */
	public AndFunction(Node<Boolean> child1, Node<Boolean> child2) {
		super(child1, child2);
	}
	
	/**
	 * Evaluating an <code>AndFunction</code> involves combining the evaluation 
	 * of the children according to the rules of AND where if both children 
	 * evaluate to true then the result will be true. All other combinations 
	 * will return a result of false.
	 */
	@Override
	public Boolean evaluate() {
		boolean c1 = ((Boolean) getChild(0).evaluate()).booleanValue();
		boolean c2 = ((Boolean) getChild(1).evaluate()).booleanValue();
		
		return c1 && c2;
	}

	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the AndFunction which is AND.
	 */
	@Override
	public String getFunctionName() {
		return "AND";
	}
}