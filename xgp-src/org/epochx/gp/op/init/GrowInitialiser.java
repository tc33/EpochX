/*  
 *  Copyright 2007-2010 Tom Castle & Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming software for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 *  The latest version is available from: http:/www.epochx.org
 */
package org.epochx.gp.op.init;

import java.util.*;

import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.*;
import org.epochx.representation.*;


/**
 * Initialisation implementation which randomly grows program trees down to a 
 * maximum depth.
 */
public class GrowInitialiser implements GPInitialiser {
	
	// The current controlling model.
	private GPModel model;
	
	/**
	 * Constructor for the grow initialiser.
	 * 
	 * @param model The current controlling model. Run parameters such as the 
	 * population size will be obtained from this.
	 */
	public GrowInitialiser(GPModel model) {
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
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to be populated.
		int popSize = model.getPopulationSize();
		List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);
		
		// Create and add new programs to the population.
		for(int i=0; i<popSize; i++) {
			GPCandidateProgram candidate;
			
			do {
				// Grow a new node tree.
				Node nodeTree = buildGrowNodeTree(model.getInitialMaxDepth());
            	
				// Create a program around the node tree.
				candidate = new GPCandidateProgram(nodeTree, model);
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
	public Node buildGrowNodeTree(int maxDepth) {		
		// Randomly choose a root node.
		int randomIndex = model.getRNG().nextInt(model.getSyntax().size());
		Node root = (Node) model.getSyntax().get(randomIndex).clone();
        
		// Populate the root node with grown children with maximum depth-1.
        this.fillChildren(root, 0, maxDepth);
        
        return root;
	}
	
	/*
	 * Recursively fill the children of a node, to construct a grown tree down
	 * to at most a depth of maxDepth.
	 */
	private void fillChildren(Node currentNode, int currentDepth, int maxDepth) {
		int arity = currentNode.getArity();
		if(arity > 0) {
			if(currentDepth < maxDepth-1) {
				// Not near the maximum depth yet, use functions OR terminals.
				for(int i=0; i<arity; i++) {
					int randomIndex = model.getRNG().nextInt(model.getSyntax().size());
					Node child = (Node) model.getSyntax().get(randomIndex).clone();

					currentNode.setChild(i, child);
					this.fillChildren(child, (currentDepth+1), maxDepth);
				}
			} else {
				// At maximum depth-1, fill children with terminals.
				for(int i=0; i<arity; i++) {
					int randomIndex = model.getRNG().nextInt(model.getTerminals().size());
					Node child = (Node) model.getTerminals().get(randomIndex).clone();
					
					currentNode.setChild(i, child);
				}
			}
		}
	}
}
