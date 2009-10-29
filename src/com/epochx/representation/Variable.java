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
package com.epochx.representation;

/**
 * A variable is a data type which can be used as a <code>TerminalNode</code>
 * in place of a predefined constant. Variables can thus have their values 
 * changed which will change the result of evaluation.
 */
public class Variable<TYPE> extends TerminalNode<TYPE> {

	// The name of the variable.
	private String label;
	
	/**
	 * Constructor for a new variable instance.
	 * 
	 * @param label the variable's name.
	 */
	public Variable(String label) {
		super(null);
		this.label = label;
	}
	
	/**
	 * Returns the label of the variable.
	 * 
	 * @return the label of the variable.
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Replaces the variable's label with a new name.
	 * 
	 * @param label the new label to assign to the variable.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/* This shouldn't be needed, because it extends from TerminalNode.
	@Override
	public TYPE evaluate() {
		return getValue();
	}
	*/

	/**
	 * Return a string representation of the variable.
	 * 
	 * @return a string representation of this variable.
	 */
	@Override
	public String toString() {
		return label;
	}
	
	/**
	 * Compares an object for equivalence to this instance of Variable. Two 
	 * variables are equal if they have equal labels and equal values.
	 * 
	 * @return true if the object is a variable equal to this one, false 
	 * otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Variable) 
					&& super.equals(obj) 
					&& this.label.equals(((Variable<TYPE>) obj).label);
	}
	
	/**
	 * Variables cannot be cloned. Calling this method returns the same 
	 * instance of Variable as this allows a variable to be treated in the same 
	 * way as other nodes without preventing variable values being updateable 
	 * throughout a program tree.
	 * 
	 * @return this instance of Variable.
	 */
	@Override
	public Object clone() {
		return this;
	}
}
