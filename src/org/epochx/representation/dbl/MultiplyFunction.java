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
package org.epochx.representation.dbl;

import org.epochx.representation.*;

/**
 * A <code>FunctionNode</code> which performs the mathematical function of 
 * multiplication.
 */
public class MultiplyFunction extends DoubleNode {
	
	/**
	 * Construct a MultiplyFunction with no children.
	 */
	public MultiplyFunction() {
		this(null, null);
	}
	
	/**
	 * Construct a MultiplyFunction with 2 children. When evaluated, both children will 
	 * be evaluated and then multiplied together.
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 */
	public MultiplyFunction(DoubleNode child1, DoubleNode child2) {
		super(child1, child2);
	}

	/**
	 * Evaluating a <code>MultiplyFunction</code> involves multiplying the result of 
	 * evaluating both children.
	 */
	@Override
	public Double evaluate() {
		double c1 = ((Double) getChild(0).evaluate()).doubleValue();
		double c2 = ((Double) getChild(1).evaluate()).doubleValue();
		
		return c1 * c2;
	}
	
	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the MultiplyFunction which is MUL.
	 */
	@Override
	public String getIdentifier() {
		return "MUL";
	}
}
