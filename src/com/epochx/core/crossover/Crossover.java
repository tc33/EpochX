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
import core.*;

/**
 * 
 */
public abstract class Crossover {
	
	private Node parent1;
	private Node parent2;
	private SemanticModule semMod;
	
	public Crossover(Node parent1, Node parent2, SemanticModule semMod) {
		this.parent1 = parent1;
		this.parent2 = parent2;
		this.semMod = semMod;
	}
	
	public Node getParent1() {
		return parent1;
	}
	
	public Node getParent2() {
		return parent2;
	}
	
	public SemanticModule getSemanticModule() {
		return semMod;
	}
	
	public abstract Node getChild1();
	
	public abstract Node getChild2();

}
