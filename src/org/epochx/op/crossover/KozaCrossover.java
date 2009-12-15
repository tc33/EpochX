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
package org.epochx.op.crossover;

import org.epochx.core.*;
import org.epochx.life.GenerationListener;
import org.epochx.representation.*;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * This class implements a Koza style crossover operation on two 
 * <code>CandidatePrograms</code>.
 * 
 * <p>Koza crossover performs a one point exchange with the choice of a swap 
 * point being either a function or terminal node with assigned probabilities.
 * This class provides a constructor for specifying a probability of selecting 
 * a function swap point. The default constructor uses the typical rates of 90% 
 * function node swap point and 10% terminal node swap points.
 */
public class KozaCrossover implements GPCrossover, GenerationListener {

	// The current controlling model.
	private GPModel model;
	
	// The probability of choosing a function node as the swap point.
	private double functionSwapProbability;
	
	// The random number generator for controlling random behaviour.
	private RandomNumberGenerator rng;
	
	/**
	 * Construct an instance of Koza crossover.
	 * 
	 * @param functionSwapProbability The probability of crossover operations 
	 * 								  choosing a function node as the swap point.
	 */
	public KozaCrossover(GPModel model, double functionSwapProbability) {
		this.model = model;
		this.functionSwapProbability = functionSwapProbability;
		
		Controller.getLifeCycleManager().addGenerationListener(this);
	}
	
	/**
	 * Default constructor for Koza standard crossover. The probability of a 
	 * function node being selected as the swap point will default to 90%.
	 */
	public KozaCrossover(GPModel model) {
		this(model, 0.9);
	}
	
	/**
	 * GPCrossover the two <code>CandidatePrograms</code> provided as arguments 
	 * using Koza crossover. The crossover points will be chosen from the 
	 * function or terminal sets with the probability assigned at construction 
	 * or the default value of 90% function node.
	 * 
	 * @param program1 The first GPCandidateProgram selected to undergo Koza 
	 * 				   crossover.
	 * @param program2 The second GPCandidateProgram selected to undergo Koza 
	 * 				   crossover.
	 */
	@Override
	public GPCandidateProgram[] crossover(CandidateProgram p1, CandidateProgram p2) {		
		GPCandidateProgram program1 = (GPCandidateProgram) p1;
		GPCandidateProgram program2 = (GPCandidateProgram) p2;
		
		// Get swap points.
		int swapPoint1 = getCrossoverPoint(program1);
		int swapPoint2 = getCrossoverPoint(program2);
		
		// Get copies of subtrees to swap.
		Node subtree1 = program1.getNthNode(swapPoint1);//.clone();
		Node subtree2 = program2.getNthNode(swapPoint2);//.clone();
		
		// Perform swap.
		program1.setNthNode(swapPoint1, subtree2);
		program2.setNthNode(swapPoint2, subtree1);

		return new GPCandidateProgram[]{program1, program2};
	}
	
	/*
	 * Choose the crossover point for the given GPCandidateProgram with respect 
	 * to the probabilities assigned for function and terminal node points.
	 */
	private int getCrossoverPoint(GPCandidateProgram program) {
		// Calculate numbers of terminal and function nodes.
		int length = program.getProgramLength();
		int noTerminals = program.getNoTerminals();
		int noFunctions = length - noTerminals;
		
		// Randomly decide whether to use a function or terminal node point.
		if ((noFunctions > 0) && (rng.nextDouble() < functionSwapProbability)) {
			// Randomly select a function node from the function set.
			int f = rng.nextInt(noFunctions);
			int nthFunctionNode = getNthFunctionNode(f, program);
			
			return nthFunctionNode;
		} else {
			// Randomly select a terminal node from the terminal set.
			int t = rng.nextInt(noTerminals);
			int nthTerminalNode = getNthTerminalNode(t, program);
			
			return nthTerminalNode;
		}
	}

	/*
	 * Recurse through the given GPCandidateProgram to find the nth function 
	 * node and return its node index.
	 * 
	 * TODO Consider moving all these functions to the GPCandidateProgram class 
	 * as first class citizens in some form - they might be useful for others.
	 */
	private int getNthFunctionNode(int n, GPCandidateProgram program) {
		return getNthFunctionNode(n, 0, 0, program.getRootNode());
	}
	
	/*
	 * Recursive helper function.
	 */
	private int getNthFunctionNode(int n, int functionCount, int nodeCount, Node current) {
		// Found the nth function node.
		if ((current instanceof FunctionNode) && (n == functionCount))
			return nodeCount;
		
		int result = -1;
		for (Node child: current.getChildren()) {
			int noNodes = child.getLength();
			int noFunctions = child.getNoFunctions();
			
			// Only look at the subtree if it contains the right range of nodes.
			if (n <= noFunctions + functionCount) {
				int childResult = getNthFunctionNode(n, functionCount+1, nodeCount+1, child);
				if (childResult != -1) 
					return childResult;
			}
			
			// Skip the correct number of nodes from the subtree.
			functionCount += noFunctions;
			nodeCount += noNodes;
		}
		
		return result;
	}
	
	/*
	 * Recurse through the given GPCandidateProgram to find the nth terminal 
	 * node and return its node index.
	 */
	private int getNthTerminalNode(int n, GPCandidateProgram program) {
		return getNthTerminalNode(n, 0, 0, program.getRootNode());
	}
	
	/*
	 * Recursive helper function.
	 */
	private int getNthTerminalNode(int n, int terminalCount, int nodeCount, Node current) {
		// Found the nth terminal node.
		if (current instanceof TerminalNode) {
			if (n == terminalCount++)
				return nodeCount;
		}
		
		int result = -1;
		for (Node child: current.getChildren()) {
			int noNodes = child.getLength();
			int noTerminals = child.getNoTerminals();
			
			// Only look at the subtree if it contains the right range of nodes.
			if (n <= noTerminals + terminalCount) {
				int childResult = getNthTerminalNode(n, terminalCount, nodeCount+1, child);
				if (childResult != -1) 
					return childResult;
			}
			
			// Skip the correct number of nodes from the subtree.
			terminalCount += noTerminals;
			nodeCount += noNodes;
		}
		
		return result;
	}

	@Override
	public void onGenerationStart() {
		rng = model.getRNG();
	}

	@Override
	public Object[] getOperatorStats() {
		return null;
	}
}
