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
package com.epochx.func.dbl;

import com.epochx.core.representation.*;

/**
 * A <code>FunctionNode</code> which performs the mathematical function of square 
 * root.
 */
public class SquareRootFunction extends FunctionNode<Double> {

	/**
	 * Construct a SquareRootFunction with no children.
	 */
	public SquareRootFunction() {
		this(null);
	}
	
	/**
	 * Construct a SquareRootFunction with one child. When evaluated, the child will 
	 * be first evaluated, with the result square-rooted.
	 * @param child The child which cube root will be performed on.
	 */
	public SquareRootFunction(Node<Double> child) {
		super(child);
	}

	/**
	 * Evaluating a <code>SquareRootFunction</code> involves evaluating the child 
	 * first then performing square root on the result.
	 */
	@Override
	public Double evaluate() {
		double c = ((Double) getChild(0).evaluate()).doubleValue();
		
		return Math.sqrt(c);
	}
	
	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the SquareRootFunction which is SQRT.
	 */
	@Override
	public String getFunctionName() {
		return "SQRT";
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && (obj instanceof SquareRootFunction);
	}

}
