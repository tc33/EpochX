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
 * 
 */
public class TerminalNode<TYPE> extends Node<TYPE> {
	
	//private Variable<TYPE> variable;
	private TYPE value;
	
	/*public TerminalNode(Variable<TYPE> variable) {
		super();
		this.variable = variable;
	}*/
	
	public TerminalNode(TYPE value) {
		super();
		this.value = value;
	}

	/**
	 * @return the type
	 */
	public TYPE getValue() {
		return value;
	}

	/**
	 * @param value the type to set
	 */
	/*
	 * THIS IS COMMENTED OUT BECAUSE I'M NOT SURE IT'S NEEDED. MAYBE TERMINALS 
	 * SHOULD BE IMMUTABLE.
	 * public void setValue(TYPE value) {
		this.value = value;
		this.variable = null;
	}*/
	
	/*public Variable<TYPE> getVariable() {
		return variable;
	}*/

	@Override
	public TYPE evaluate() {
		//if (variable != null) {
		//	return variable.getValue();
		//} else {
			return value;
		//}
	}
	
	public String toString() {
		//if (variable != null) {
		//	return variable.toString();
		//} else {
			return value.toString();
		//}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		TerminalNode<TYPE> clone = (TerminalNode) super.clone();
		
		clone.value = this.value;
		//clone.variable = this.variable;
		
		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		boolean inst = (obj instanceof TerminalNode);
		if (!inst)
			return false;
		TYPE objVal = ((TerminalNode<TYPE>) obj).value;
		TYPE thisVal = this.value;
		//Variable<TYPE> objVar = ((TerminalNode<TYPE>) obj).variable;
		//Variable<TYPE> thisVar = this.variable;
		
		//if (((objVal == null) ^ (thisVal == null)) || ((objVar == null) ^ (thisVar == null))) {
		//	return false;
		//} else if (((thisVal == null) && (objVal == null)) || ((thisVar == null) && (objVar == null))) {
		//	return true;
		//} else {
			return (thisVal == objVal) || thisVal.equals(objVal);// && objVar.equals(thisVar);
		//}
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
