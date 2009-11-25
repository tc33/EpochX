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
package org.epochx.representation;


/**
 *
 */
public abstract class Action extends TerminalNode<Object> {
	
	public Action() {
		super(null);
	}

	public abstract String getActionName();

	@Override
	public abstract Object evaluate();

	/**
	 * Returns a string representation of the function node. Since the function 
	 * node is dependent upon its children, their string representations will 
	 * form part of this.
	 * 
	 * @return a string representation of this function node.
	 */
	@Override
	public String toString() {
		return getActionName() + "()";
	}
}
