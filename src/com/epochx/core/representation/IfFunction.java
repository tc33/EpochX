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

public class IfFunction extends FunctionNode<Boolean> {
	
	public IfFunction(Node<Boolean> child1, Node<Boolean> child2, Node<Boolean> child3) {
		super(child1, child2, child3);
	}

	@Override
	public Boolean evaluate() {
		boolean c1 = ((Boolean) getChild(0).evaluate()).booleanValue();

		if(c1) {
			return ((Boolean) getChild(1).evaluate()).booleanValue();
		} else {
			return ((Boolean) getChild(2).evaluate()).booleanValue();
		}
	}
	
	public String toString() {
		return "IF(" + getChild(0) + ' ' + getChild(1) + ' ' + getChild(2) + ')';
	}
}
