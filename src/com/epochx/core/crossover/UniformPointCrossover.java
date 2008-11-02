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
package com.epochx.core.crossover;

import com.epochx.core.representation.*;
import core.SemanticModule;

/**
 * 
 */
public class UniformPointCrossover extends Crossover {
	
	private Node child1;
	private Node child2;

	/**
	 * @param parent1
	 * @param parent2
	 * @param semMod
	 */
	public UniformPointCrossover(Node parent1, Node parent2,
			SemanticModule semMod) {
		super(parent1, parent2, semMod);
		this.doCrossover();
	}
	
	public void doCrossover() {
		// copy parents to children
		
		// select swap and put points
		
		// do swap
		
		// max depth 17
		
		// state change section
		
		// set children
		
	}
	
	public Node getChild1() {
		return child1;
	}
	
	public Node getChild2() {
		return child2;
	}
}
