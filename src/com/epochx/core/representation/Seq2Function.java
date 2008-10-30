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
public class Seq2Function extends FunctionNode<Action> {

	private Node<Action> action1;
	private Node<Action> action2;
	
	public Seq2Function(Node<Action> action1, Node<Action> action2) {
		this.action1 = action1;
		this.action2 = action2;
	}
	
	@Override
	public Action evaluate() {
		action1.evaluate().execute();
		action2.evaluate().execute();
		
		return Action.DO_NOTHING;
	}
}
