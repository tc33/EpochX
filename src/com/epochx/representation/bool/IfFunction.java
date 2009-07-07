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
package com.epochx.representation.bool;

import com.epochx.representation.*;

/**
 * A <code>FunctionNode</code> which represents the conditional if-then-else 
 * statement.
 */
public class IfFunction extends FunctionNode<Boolean> {
	
	/**
	 * Construct an IfFunction with no children.
	 */
	public IfFunction() {
		this(null, null, null);
	}
	
	/**
	 * Construct an IfFunction with three children. When evaluated, if the first 
	 * child evaluates to true then the second child is evaluated and return, 
	 * otherwise the third child is evaluated and returned.
	 * @param condition The first child node.
	 * @param ifStatement The second child node.
	 * @param elseStatement The third child node.
	 */
	public IfFunction(Node<Boolean> condition, Node<Boolean> ifStatement, Node<Boolean> elseStatement) {
		super(condition, ifStatement, elseStatement);
	}

	/**
	 * Evaluating an <code>IfFunction</code> involves evaluating the first child, 
	 * if it evaluates to true then the second child is evaluated as the result. 
	 * Otherwise the third child is evaluated and returned.
	 */
	@Override
	public Boolean evaluate() {
		boolean c1 = ((Boolean) getChild(0).evaluate()).booleanValue();

		if(c1) {
			return ((Boolean) getChild(1).evaluate()).booleanValue();
		} else {
			return ((Boolean) getChild(2).evaluate()).booleanValue();
		}
	}
	
	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the IfFunction which is IF.
	 */
	@Override
	public String getFunctionName() {
		return "IF";
	}
}
