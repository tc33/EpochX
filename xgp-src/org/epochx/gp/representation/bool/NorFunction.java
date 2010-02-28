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
package org.epochx.gp.representation.bool;

import org.epochx.gp.representation.BooleanNode;

/**
 * A <code>FunctionNode</code> which performs the logical operation of NOR 
 * that is equivalent to the negation of logical OR or NOT OR. 
 */
public class NorFunction extends BooleanNode {
	
	/**
	 * Construct a NorFunction with no children.
	 */
	public NorFunction() {
		this(null, null);
	}
	
	/**
	 * Construct a NorFunction with two children. When evaluated, if both 
	 * children evaluate to false then the result will be true. All other 
	 * combinations will return a result of false.
	 * @param child1 The first child node.
	 * @param child2 The second child node.
	 */
	public NorFunction(BooleanNode child1, BooleanNode child2) {
		super(child1, child2);
	}
	
	/**
	 * Evaluating a <code>NorFunction</code> involves combining the evaluation 
	 * of the children according to the rules of NOR where if both children 
	 * evaluate to false then the result will be true. All other combinations 
	 * will return a result of false.
	 */
	@Override
	public Boolean evaluate() {
		boolean c1 = ((Boolean) getChild(0).evaluate()).booleanValue();
		boolean c2 = ((Boolean) getChild(1).evaluate()).booleanValue();
		
		return !(c1 || c2);
	}
	
	/**
	 * Get the unique name that identifies this function.
	 * @return the unique name for the NorFunction which is NOR.
	 */
	@Override
	public String getIdentifier() {
		return "NOR";
	}
}
