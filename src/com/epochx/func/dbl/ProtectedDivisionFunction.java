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
 * A <code>FunctionNode</code> which performs the arithmetic function of 
 * division. The division is protected to avoid the scenario where division 
 * by zero is attempted - which is undefined. Division by zero evaluates to 
 * zero.
 */
public class ProtectedDivisionFunction extends FunctionNode<Double> {
	
	/**
	 * Construct a ProtectedDivisionFunction with no children.
	 */
	public ProtectedDivisionFunction() {
		this(null, null);
	}
	
	/**
	 * Construct a ProtectedDivisionFunction with 2 children. When evaluated, 
	 * the evaluation of the first child will be divided by the evaluation of 
	 * the second.
	 * @param dividend The first child node - the dividend.
	 * @param divisor The second child node - the divisor.
	 */
	public ProtectedDivisionFunction(Node<Double> dividend, Node<Double> divisor) {
		super(dividend, divisor);
	}

	/**
	 * Evaluating a <code>ProtectedDivisionFunction</code> involves dividing 
	 * the result of evaluating both children. If the divisor resolves to zero 
	 * then the result returned will be zero to avoid the divide by zero issue.
	 */
	@Override
	public Double evaluate() {
		double c1 = ((Double) getChild(0).evaluate()).doubleValue();
		double c2 = ((Double) getChild(1).evaluate()).doubleValue();
		
		if(c2==0) {
			//TODO It might be useful to have a version of the constructor which takes a value to return for divide by zeros.
			return 0d;
		} else {
			return c1 / c2;
		}
	}
	
	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the ProtectedDivisionFunction which is PDIV.
	 */
	@Override
	public String getFunctionName() {
		return "PDIV";
	}
}
