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
 * A <code>FunctionNode</code> which performs the hyperbolic trigonometric 
 * function of hyperbolic cosine.
 */
public class HyperbolicCosineFunction extends FunctionNode<Double> {

	/**
	 * Construct a HyperbolicCosineFunction with no children.
	 */
	public HyperbolicCosineFunction() {
		this(null);
	}
	
	/**
	 * Construct a HyperbolicCosineFunction with one child. When evaluated, the 
	 * child will first be evaluated with hyperbolic cosine performed on the result.
	 * @param child The child which hyperbolic cosine will be performed on.
	 */
	public HyperbolicCosineFunction(Node<Double> child) {
		super(child);
	}

	/**
	 * Evaluating an <code>HyperbolicCosineFunction</code> involves calculating the 
	 * hyperbolic cosine of the child once it has also been evaluated.
	 */
	@Override
	public Double evaluate() {
		double c = ((Double) getChild(0).evaluate()).doubleValue();
		
		return Math.cosh(c);
	}
	
	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the HyperbolicCosineFunction which is COSH.
	 */
	@Override
	public String getFunctionName() {
		return "COSH";
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && (obj instanceof HyperbolicCosineFunction);
	}
}
