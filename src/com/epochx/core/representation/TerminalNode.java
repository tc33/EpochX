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
	
	private TYPE value;
	
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
	public void setValue(TYPE value) {
		this.value = value;
	}

	@Override
	public TYPE evaluate() {
		return value;
	}
	
	public String toString() {
		return (String) value;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		TerminalNode<TYPE> clone = (TerminalNode) super.clone();
		
		clone.setValue(value);
		
		return clone;
	}
}
