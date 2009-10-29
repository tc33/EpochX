/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.representation.dbl;

import com.epochx.representation.*;

/**
 * A <code>FunctionNode</code> which performs the hyperbolic trigonometric 
 * function of hyperbolic sine.
 */
public class HyperbolicSineFunction extends FunctionNode<Double> {

	/**
	 * Construct a HyperbolicSineFunction with no children.
	 */
	public HyperbolicSineFunction() {
		this(null);
	}
	
	/**
	 * Construct a HyperbolicSineFunction with one child. When evaluated, the 
	 * child will first be evaluated with hyperbolic sine performed on the result.
	 * @param child The child which hyperbolic sine will be performed on.
	 */
	public HyperbolicSineFunction(Node<Double> child) {
		super(child);
	}

	/**
	 * Evaluating an <code>HyperbolicSineFunction</code> involves calculating the 
	 * hyperbolic sine of the child once it has also been evaluated.
	 */
	@Override
	public Double evaluate() {
		double c = ((Double) getChild(0).evaluate()).doubleValue();
		
		return Math.sinh(c);
	}
	
	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the HyperbolicSineFunction which is SINH.
	 */
	@Override
	public String getFunctionName() {
		return "SINH";
	}
}
