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
package com.epochx.core.representation;

/**
 * A variable is a data type which can be used for a <code>TerminalNode</code>
 * inplace of a predefined constant. Variables can thus have their values 
 * changed which will change the result of evaluation.
 */
public class Variable<TYPE> extends TerminalNode<TYPE> {

	private String label;
	//private TYPE value = null;
	
	public Variable(String label) {
		super(null);
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	/*public TYPE getValue() {
		return value;
	}
	
	public void setValue(TYPE value) {
		this.value = value;
	}*/
	
	@Override
	public TYPE evaluate() {
		return getValue();
	}

	@Override
	public String toString() {
		//if (value != null) {
		//	return value.toString();
		//} else {
			return label;
		//}
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Variable) 
					&& super.equals(obj) 
					&& this.label.equals(((Variable) obj).label);
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.core.representation.TerminalNode#clone()
	 */
	@Override
	public Object clone() {
		return this;
	}
}
