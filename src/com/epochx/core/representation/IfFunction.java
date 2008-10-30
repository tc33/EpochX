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
	
	private Node<Boolean> child1;
	private Node<Boolean> child2;
	private Node<Boolean> child3;
	
	public IfFunction(Node<Boolean> child1, Node<Boolean> child2, Node<Boolean> child3) {
		this.child1 = child1;
		this.child2 = child2;
		this.child3 = child3;
	}

	@Override
	public Boolean evaluate() {
		Boolean c1 = child1.evaluate();
		Boolean c2 = child2.evaluate();
		Boolean c3 = child3.evaluate();
		if(c1.booleanValue()) {
			return c2.booleanValue();
		} else {
			return c3.booleanValue();
		}
	}

}
