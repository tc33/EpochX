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
package org.epochx.representation;

/**
 * Defines a terminal node.
 */
public class TerminalNode extends Node {

	private TYPE value;
	
	/**
	 * Constructor for a new terminal node.
	 * 
	 * @param value the value of the terminal node.
	 */
	public TerminalNode(TYPE value) {
		super();
		this.value = value;
	}

	/**
	 * Returns the value of the terminal node.
	 * 
	 * @return the value of the terminal node.
	 */
	public TYPE getValue() {
		return value;
	}
	
	/**
	 * Replaces the value of the terminal node.
	 * 
	 * @param value the new value to store.
	 */
	public void setValue(TYPE value) {
		this.value = value;
	}

	/**
	 * Evaluation of a terminal node depends on if it is an action or not. If 
	 * the value is an Action, then the action is first executed, then a 
	 * DO_NOTHING Action value is returned, indicating that no further action 
	 * is required from this node. Alternatively, for all other terminal nodes, 
	 * the value is simply returned.
	 */
	@Override
	public TYPE evaluate() {
		return value;
	}
	
	/**
	 * Returns a string representation of this TerminalNode.
	 * 
	 * @return a string representation of this terminal node.
	 */
	@Override
	public String toString() {
		return value.toString();
	}
	
	/**
	 * Creates a copy of this terminal node. Values are shallow copied and so 
	 * the cloned value will point to the same reference.
	 * 
	 * @return a copy of this TerminalNode.
	 */
	@Override
	public TerminalNode clone() {
		TerminalNode clone = (TerminalNode) super.clone();
		
		clone.value = this.value;

		return clone;
	}

	/**
	 * Compares an object for equivalence to this terminal node. Another 
	 * terminal node is equal to this one if they share equal values.
	 * 
	 * @param obj an object to compare for equivalence.
	 * @return true if the objects are equal, false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		boolean inst = (obj instanceof TerminalNode);
		if (!inst)
			return false;
		TYPE objVal = ((TerminalNode) obj).value;
		TYPE thisVal = this.value;
		
		if ((objVal == null) ^ (thisVal == null)) {
			return false;
		}
		
		return (thisVal == objVal) || thisVal.equals(objVal);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
