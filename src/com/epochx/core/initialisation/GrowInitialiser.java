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

import com.epochx.core.*;
import com.epochx.core.representation.CandidateProgram;
import com.epochx.core.representation.Node;
import com.epochx.semantics.*;

/**
 * 
 */
public class GrowInitialiser<TYPE> implements Initialiser<TYPE> {
	
	private GPModel<TYPE> model;
	
	public GrowInitialiser(GPModel<TYPE> model, SemanticModule semMod) {
		this.model = model;
	}
	
	@Override
	public List<CandidateProgram<TYPE>> getInitialPopulation() {
		
		// initialise population of candidate programs
		int popSize = model.getPopulationSize();
		List<CandidateProgram<TYPE>> firstGen = new ArrayList<CandidateProgram<TYPE>>(popSize);
		
		// build population
		for(int i=0; i<popSize; i++) {
            CandidateProgram<TYPE> candidate = new CandidateProgram<TYPE>(buildGrowNodeTree(model.getInitialMaxDepth()), model);
            while(firstGen.contains(candidate)) {
                candidate = new CandidateProgram<TYPE>(buildGrowNodeTree(model.getInitialMaxDepth()), model);
            }
            firstGen.add(candidate);
        }
		
		// return starting population
		return firstGen;
	}
	
	public Node<TYPE> buildGrowNodeTree(int depth) {		
        // define top node
		int randomIndex = (int) Math.floor(Math.random() * model.getSyntax().size());
		Node<TYPE> top = (Node<TYPE>) model.getSyntax().get(randomIndex).clone();
        
        // recurse down each branch to depth using Grow mechanism
        this.fillChildren(top, 1, model.getInitialMaxDepth());
        
        // return top node
        return top;
	}
	
	public void fillChildren(Node<?> topNode, int currentDepth, int maxDepth) {
		int arity = topNode.getArity();
		if(arity>0) {
			if(currentDepth<maxDepth-1) {
				// fill children with functions or terminals
				for(int i = 0; i<arity; i++) {
					int randomIndex = (int) Math.floor(Math.random() * model.getSyntax().size());
					Node<?> child = (Node<?>) model.getSyntax().get(randomIndex).clone();

					topNode.setChild(child, i);
					this.fillChildren(child, (currentDepth+1), maxDepth);
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
}
