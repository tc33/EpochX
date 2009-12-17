/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.op.initialisation;

import java.util.*;

import org.epochx.core.GPModel;
import org.epochx.representation.*;


/**
 * Initialisation implementation which produces full program trees.
 */
public class FullInitialiser implements GPInitialiser {
	
	// The current controlling model.
	private GPModel model;

	/**
	 * Constructor for the full initialiser.
	 * 
	 * @param model The current controlling model. Run parameters such as the 
	 * population size will be obtained from this.
	 */
	public FullInitialiser(GPModel model) {
		this.model = model;
	}
	
	/**
	 * Generate a population of new CandidatePrograms constructed from the 
	 * function and terminal sets defined by the model. The size of the 
	 * population will be equal to the result of calling getPopulationSize() on
	 * the controlling model. All programs in the population will be unique.
	 * Each candidate program will have a full node tree with a depth as given 
	 * by a call to getInitialMaxDepth() on the model.
	 * 
	 * @return A List of newly generated CandidatePrograms which will form the 
	 * initial population for a GP run.
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to be populated.
		int popSize = model.getPopulationSize();
		List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);
		
		// Create and add new programs to the population.
		for(int i=0; i<popSize; i++) {
			GPCandidateProgram candidate;
			
			do {
				// Build a new full node tree.
				Node nodeTree = buildFullNodeTree(model.getInitialMaxDepth());
            	
				// Create a program around the node tree.
				candidate = new GPCandidateProgram(nodeTree, model);
			} while (firstGen.contains(candidate));
			
			// Must be unique - add to the new population.
			firstGen.add(candidate);
        }
		
		return firstGen;
	}
	
	/**
	 * Build a full node tree with a given depth. As the node tree will be full 
	 * the maximum and minimum depths of the returned node tree should be equal 
	 * to the depth argument. The internal and leaf nodes will be selected from 
	 * the function and terminal sets respectively, as provided by the model.
	 * 
	 * @param depth The depth of the full node tree, where the depth is the 
	 * number of nodes from the root.
	 * @return The root node of a randomly generated full node tree of the 
	 * requested depth.
	 */
	public Node buildFullNodeTree(int depth) {
		Node root;
		if (depth == 0) {
			// Randomly choose a terminal node as our root.
			int randomIndex = model.getRNG().nextInt(model.getTerminals().size());
			root = model.getTerminals().get(randomIndex).clone();
		} else {
			// Randomly choose a root function node.
	        int randomIndex = model.getRNG().nextInt(model.getFunctions().size());
	        root = model.getFunctions().get(randomIndex).clone();
	        
	        // Populate the root node with full children of depth-1.
			fillChildren(root, 0, depth);
		}
        
        return root;
	}
	
	/*
	 * Recursively fill the children of a node, to construct a full tree down
	 * to a depth of maxDepth.
	 * TODO These model calls should not be being made multiple times.
	 */
	private void fillChildren(Node currentNode, int currentDepth, int maxDepth) {
		int arity = currentNode.getArity();
		
		if(currentDepth<maxDepth-1) {
			// Not near the maximum depth yet, fill children with functions only.
			for(int i = 0; i<arity; i++) {
				int randomIndex = model.getRNG().nextInt(model.getFunctions().size());
				Node child = model.getFunctions().get(randomIndex).clone();

				currentNode.setChild(i, child);
				fillChildren(child, (currentDepth+1), maxDepth);
			}
		} else {
			// At maximum depth-1, fill children with terminals.
			for(int i = 0; i<arity; i++) {
				int randomIndex = model.getRNG().nextInt(model.getTerminals().size());
				Node child = model.getTerminals().get(randomIndex).clone();

				currentNode.setChild(i, child);
			}
		}
	}
}
