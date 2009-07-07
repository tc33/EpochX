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
 * A <code>FunctionNode</code> which performs the arithmetic function of cube, 
 * that is - raising to the third power. It is equivalent to the 
 * <code>PowerFunction</code> where the second child is the double literal 3.0.
 */
public class CubeFunction extends FunctionNode<Double> {

	/**
	 * Construct a CubeFunction with no children.
	 */
	public CubeFunction() {
		this(null);
	}
	
	/**
	 * Construct a CubeFunction with one child. When evaluated, the child will 
	 * be evaluated with the result then raised to the power of 3.
	 * @param child The child which will be cubed.
	 */
	public CubeFunction(Node<Double> child) {
		super(child);
	}

	/**
	 * Evaluating a <code>CubeFunction</code> involves evaluating the child 
	 * then raising the result to the power of 3.
	 */
	@Override
	public Double evaluate() {
		double c = ((Double) getChild(0).evaluate()).doubleValue();
		
		return Math.pow(c,3);
	}
	
	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the CubeFunction which is CUBE.
	 */
	@Override
	public String getFunctionName() {
		return "CUBE";
	}
}
