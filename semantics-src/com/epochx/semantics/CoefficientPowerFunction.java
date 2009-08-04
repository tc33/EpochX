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
package com.epochx.semantics;

import com.epochxge.grammar.TerminalSymbol;
import com.epochxge.representation.*;

/**
 * The CoefficientPowerFunction is equivalent to a <code>PowerFunction</code> 
 * combined with a <code>MultiplyFunction</code>. It allows a succinct way of 
 * representing a variable with an exponent and a coefficient.
 * 
 * An example:
 *    3x^2, which is equivalent to 3*(x^2)
 *    CVP 3 x 2, which is equivalent to MUL(POW x 2)
 */
public class CoefficientPowerFunction {
	
	TerminalSymbol coefficient;
	TerminalSymbol variable;
	TerminalSymbol power;

	/**
	 * Construct an CoefficientPowerFunction with no children.
	 */
	public CoefficientPowerFunction() {
		coefficient = new TerminalSymbol("0");
		variable = new TerminalSymbol("X");
		power = new TerminalSymbol("0");
	}
	
	/**
	 * Construct a CoefficientPowerFunction with three children. When evaluated, 
	 * all children will first be evaluated. Then the second child will be raised 
	 * to the power of the third child, and multiplied by the first.
	 * @param coefficient will be multiplied by the result of the term raised to 
	 * the exponent.
	 * @param term will be raised to the power of the exponent and multiplied by 
	 * the coefficient.
	 * @param exponent the power the term will be raised to.
	 */
	public CoefficientPowerFunction(TerminalSymbol coefficient, TerminalSymbol variable, TerminalSymbol power) {
		this.coefficient = coefficient;
		this.variable = variable;
		this.power = power;
	}
	
	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the CoefficientPowerFunction which is CVP.
	 */
	public String getFunctionName() {
		return "CVP";
	}
	
	public TerminalSymbol getCoefficient() {
		return coefficient;
	}
	
	public TerminalSymbol getVariable() {
		return variable;
	}
	
	public TerminalSymbol getPower() {
		return power;
	}
}
