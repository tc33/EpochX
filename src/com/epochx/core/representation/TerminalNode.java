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

import com.epochx.action.Action;

/**
 * Defines a terminal node
 */
public class TerminalNode<TYPE> extends Node<TYPE> {

	private TYPE value;
	
	/**
	 * COnstructor for a new terminal node
	 * @param value The value of the terminal node
	 */
	public TerminalNode(TYPE value) {
		super();
		this.value = value;
	}

	/**
	 * @return the value of the terminal node
	 */
	public TYPE getValue() {
		return value;
	}
	
	/**
	 * Sets the value of the terminal node
	 * @param value The new value
	 */
	public void setValue(TYPE value) {
		this.value = value;
	}

	@Override
	public TYPE evaluate() {
		if(value instanceof Action) {
			((Action) value).execute();
			return (TYPE) Action.DO_NOTHING;
		} else {
			return value;
		}
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public Object clone() {
		TerminalNode<TYPE> clone = (TerminalNode<TYPE>) super.clone();
		
		clone.value = this.value;

		return clone;
	}

	@Override
	public boolean equals(Object obj) {
		boolean inst = (obj instanceof TerminalNode);
		if (!inst)
			return false;
		TYPE objVal = ((TerminalNode<TYPE>) obj).value;
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
