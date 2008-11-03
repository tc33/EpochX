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
public class NotFunction extends FunctionNode<Boolean> {
	
	public NotFunction(Node<Boolean> child) {
		super(child);
	}
	
	@Override
	public Boolean evaluate() {
		return !((Boolean) getChild(0).evaluate()).booleanValue();
	}
	
	public String toString() {
		return "NOT(" + getChild(0) + ')';
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && (obj instanceof NotFunction);
	}
}
