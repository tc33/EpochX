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
package com.epochx.core.initialisation;

import java.util.*;

import com.epochx.core.GPModel;
import com.epochx.core.representation.*;
import com.epochx.semantics.*;

/**
 * 
 */
public class FullInitialiser<TYPE> implements Initialiser<TYPE> {
	
	private GPModel<TYPE> model;
	
	public FullInitialiser(GPModel<TYPE> model) {
		this(model, null);
	}
	
	public FullInitialiser(GPModel<TYPE> model, SemanticModule semMod) {
		this.model = model;
	}
	
	@Override
	public List<CandidateProgram<TYPE>> getInitialPopulation() {
		
		// Initialise population of candidate programs.
		int popSize = model.getPopulationSize();
		List<CandidateProgram<TYPE>> firstGen = new ArrayList<CandidateProgram<TYPE>>(popSize);
		
		// Build population		
		for(int i=0; i<popSize; i++) {
			CandidateProgram<TYPE> candidate;
			do {
            	candidate = new CandidateProgram<TYPE>(buildFullNodeTree(model.getInitialMaxDepth()), model);
			} while (firstGen.contains(candidate));
			firstGen.add(candidate);
        }
		
		// Return starting population.
		return firstGen;
	}
	
	public Node<TYPE> buildFullNodeTree(int depth) {		
        // define top node form functions
        int randomIndex = (int) Math.floor(Math.random() * model.getFunctions().size());
        Node<TYPE> top = (Node<TYPE>) model.getFunctions().get(randomIndex).clone();

        // recurse down each branch to depth
		fillChildren(top, 1, model.getInitialMaxDepth());      
        
        // return top node
        return top;
	}
	
	private void fillChildren(Node<?> topNode, int currentDepth, int maxDepth) {
		int arity = topNode.getArity();
		if(currentDepth<maxDepth-1) {
			// fill children with functions only
			for(int i = 0; i<arity; i++) {
				int randomIndex = (int) Math.floor(Math.random() * model.getFunctions().size());
				Node<?> child = (Node<?>) model.getFunctions().get(randomIndex).clone();

				topNode.setChild(child, i);
				fillChildren(child, (currentDepth+1), maxDepth);
			}
		} else {
			// fill children with terminals only
			for(int i = 0; i<arity; i++) {
				int randomIndex = (int) Math.floor(Math.random() * model.getTerminals().size());
				Node<?> child = (Node<?>) model.getTerminals().get(randomIndex).clone();

				topNode.setChild(child, i);
			}
		}
	}
}
