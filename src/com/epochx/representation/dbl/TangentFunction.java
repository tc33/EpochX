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
package com.epochx.representation.dbl;

import com.epochx.representation.*;

/**
 * A <code>FunctionNode</code> which performs the trigonometric function of 
 * tangent.
 */
public class TangentFunction extends FunctionNode<Double> {

	/**
	 * Construct a TangentFunction with no children.
	 */
	public TangentFunction() {
		this(null);
	}
	
	/**
	 * Construct a TangentFunction with one child. When evaluated, the child
	 * will be evaluated with tangent performed on the result.
	 * @param child The child which tangent will be performed on.
	 */
	public TangentFunction(Node<Double> child) {
		super(child);
	}

	/**
	 * Evaluating a <code>TangentFunction</code> involves evaluating the child 
	 * then calculating the tangent of the result.
	 */
	@Override
	public Double evaluate() {
		double c = ((Double) getChild(0).evaluate()).doubleValue();
		
		return Math.tan(c);
	}
	
	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the TangentFunction which is TAN.
	 */
	@Override
	public String getFunctionName() {
		return "TAN";
	}
}
