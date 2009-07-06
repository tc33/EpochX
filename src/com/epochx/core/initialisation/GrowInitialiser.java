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
 * Initialisation implementation which randomly grows program trees down to a 
 * maximum depth.
 */
public class GrowInitialiser<TYPE> implements Initialiser<TYPE> {
	
	// The current controlling model.
	private GPModel<TYPE> model;
	
	/**
	 * Constructor for the grow initialiser.
	 * 
	 * @param model The current controlling model. Run parameters such as the 
	 * population size will be obtained from this.
	 */
	public GrowInitialiser(GPModel<TYPE> model) {
		this.model = model;
	}
	
	/**
	 * Generate a population of new CandidatePrograms constructed from the 
	 * function and terminal sets defined by the model. The size of the 
	 * population will be equal to the result of calling getPopulationSize() on
	 * the controlling model. All programs in the population will be unique.
	 * Each candidate program will have a node tree with a maximum depth as 
	 * given by a call to getInitialMaxDepth() on the model.
	 * 
	 * @return A List of newly generated CandidatePrograms which will form the 
	 * initial population for a GP run.
	 */
	@Override
	public List<CandidateProgram<TYPE>> getInitialPopulation() {
		// Create population list to be populated.
		int popSize = model.getPopulationSize();
		List<CandidateProgram<TYPE>> firstGen = new ArrayList<CandidateProgram<TYPE>>(popSize);
		
		// Create and add new programs to the population.
		for(int i=0; i<popSize; i++) {
			CandidateProgram<TYPE> candidate;
			
			do {
				// Grow a new node tree.
				Node<TYPE> nodeTree = buildGrowNodeTree(model.getInitialMaxDepth());
            	
				// Create a program around the node tree.
				candidate = new CandidateProgram<TYPE>(nodeTree, model);
			} while (firstGen.contains(candidate));
            
			// Must be unique - add to the new population.
			firstGen.add(candidate);
        }
		
		return firstGen;
	}
	
	/**
	 * Build a grown node tree with a maximum depth as given. The internal 
	 * nodes and leaf nodes will be selected from the function and 
	 * terminal sets respectively, as provided by the model.
	 * 
	 * @param maxDepth The maximum depth of the node tree to be grown, where 
	 * the depth is the number of nodes from the root.
	 * @return The root node of a randomly generated node tree.
	 */
	public Node<TYPE> buildGrowNodeTree(int maxDepth) {		
		// Randomly choose a root node.
		int randomIndex = (int) Math.floor(Math.random() * model.getSyntax().size());
		Node<TYPE> root = (Node<TYPE>) model.getSyntax().get(randomIndex).clone();
        
		// Populate the root node with grown children with maximum depth-1.
        this.fillChildren(root, 0, maxDepth);
        
        return root;
	}
	
	/*
	 * Recursively fill the children of a node, to construct a grown tree down
	 * to at most a depth of maxDepth.
	 */
	private void fillChildren(Node<TYPE> currentNode, int currentDepth, int maxDepth) {
		int arity = currentNode.getArity();
		if(arity > 0) {
			if(currentDepth < maxDepth-1) {
				// Not near the maximum depth yet, use functions OR terminals.
				for(int i=0; i<arity; i++) {
					int randomIndex = (int) Math.floor(Math.random() * model.getSyntax().size());
					Node<TYPE> child = (Node<TYPE>) model.getSyntax().get(randomIndex).clone();

					currentNode.setChild(i, child);
					this.fillChildren(child, (currentDepth+1), maxDepth);
				}
			} else {
				// At maximum depth-1, fill children with terminals.
				for(int i=0; i<arity; i++) {
					int randomIndex = (int) Math.floor(Math.random() * model.getTerminals().size());
					Node<TYPE> child = (Node<TYPE>) model.getTerminals().get(randomIndex).clone();
					
					currentNode.setChild(i, child);
				}
			}
		}
	}
}
