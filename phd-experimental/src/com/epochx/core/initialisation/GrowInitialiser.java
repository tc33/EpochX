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

/**
 * Grow initialisation method
 */
public class GrowInitialiser<TYPE> implements Initialiser<TYPE> {
	
	private GPModel<TYPE> model;
	
	/**
	 * Constructor for the GROW initialisation method
	 * @param model The GP model in use
	 */
	public GrowInitialiser(GPModel<TYPE> model) {
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
	
	/**
	 * Builds a GROW generated node tree
	 * @param depth The maximum depth of the node tree
	 * @return The node tree
	 */
	public Node<TYPE> buildGrowNodeTree(int depth) {		
        // define top node
		int randomIndex = (int) Math.floor(Math.random() * model.getSyntax().size());
		Node<TYPE> top = (Node<TYPE>) model.getSyntax().get(randomIndex).clone();
        
        // recurse down each branch to depth using Grow mechanism
        this.fillChildren(top, 1, depth);
        
        // return top node
        return top;
	}
	
	private void fillChildren(Node<TYPE> topNode, int currentDepth, int maxDepth) {
		int arity = topNode.getArity();
		if(arity>0) {
			if(currentDepth<maxDepth) {
				// fill children with functions or terminals
				for(int i = 0; i<arity; i++) {
					int randomIndex = (int) Math.floor(Math.random() * model.getSyntax().size());
					Node<TYPE> child = (Node<TYPE>) model.getSyntax().get(randomIndex).clone();

					topNode.setChild(child, i);
					this.fillChildren(child, (currentDepth+1), maxDepth);
				}
			} else {
				// fill children with terminals only
				for(int i = 0; i<arity; i++) {
					int randomIndex = (int) Math.floor(Math.random() * model.getTerminals().size());
					Node<TYPE> child = (Node<TYPE>) model.getTerminals().get(randomIndex).clone();
					topNode.setChild(child, i);
				}
			}
		}
	}
}
