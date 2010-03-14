/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.gp.op.init;

import java.util.*;

import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.*;
import org.epochx.life.GenerationAdapter;
import org.epochx.life.LifeCycleManager;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;


/**
 * Initialisation implementation which produces full program trees.
 */
public class FullInitialiser implements GPInitialiser {
	
	// The current controlling model.
	private GPModel model;
	
	private List<Node> terminals;
	private List<Node> functions;
	private List<Node> syntax;
	
	private RandomNumberGenerator rng;
	
	private int popSize;
	private int maxInitialDepth;
	
	/**
	 * Constructor for the full initialiser.
	 * 
	 * @param model The current controlling model. Run parameters such as the 
	 * population size will be obtained from this.
	 */
	public FullInitialiser(GPModel model) {
		this.model = model;
		
		LifeCycleManager.getLifeCycleManager().addGenerationListener(new GenerationAdapter() {
			@Override
			public void onGenerationStart() {
				reset();
			}
		});
	}
	
	/*
	 * Update the initialisers parameters from the model.
	 */
	private void reset() {
		rng = model.getRNG();
		
		terminals.clear();
		functions.clear();		
		syntax = model.getSyntax();
		
		maxInitialDepth = model.getInitialMaxDepth();
		
		for (Node n: syntax) {
			if (n.getArity() == 0) {
				terminals.add(n);
			} else {
				functions.add(n);
			}
		}
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
		List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);
		
		// Create and add new programs to the population.
		for(int i=0; i<popSize; i++) {
			GPCandidateProgram candidate;
			
			do {
				// Build a new full node tree.
				Node nodeTree = buildFullNodeTree(maxInitialDepth);
            	
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
			int randomIndex = rng.nextInt(terminals.size());
			root = terminals.get(randomIndex).clone();
		} else {
			// Randomly choose a root function node.
	        int randomIndex = rng.nextInt(functions.size());
	        root = functions.get(randomIndex).clone();
	        
	        // Populate the root node with full children of depth-1.
			fillChildren(root, 0, depth);
		}
        
        return root;
	}
	
	/*
	 * Recursively fill the children of a node, to construct a full tree down
	 * to a depth of maxDepth.
	 */
	private void fillChildren(Node currentNode, int currentDepth, int maxDepth) {
		int arity = currentNode.getArity();
		
		if(currentDepth<maxDepth-1) {
			// Not near the maximum depth yet, fill children with functions only.
			for(int i = 0; i<arity; i++) {
				int randomIndex = rng.nextInt(functions.size());
				Node child = functions.get(randomIndex).clone();

				currentNode.setChild(i, child);
				fillChildren(child, (currentDepth+1), maxDepth);
			}
		} else {
			// At maximum depth-1, fill children with terminals.
			for(int i = 0; i<arity; i++) {
				int randomIndex = rng.nextInt(terminals.size());
				Node child = terminals.get(randomIndex).clone();

				currentNode.setChild(i, child);
			}
		}
	}
}
