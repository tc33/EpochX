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
package com.epochx.op.crossover;

import com.epochx.representation.*;

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
public class KozaCrossover<TYPE> implements Crossover<TYPE> {

	// The probability of choosing a function node as the swap point.
	private double functionSwapProbability;

	/**
	 * Construct an instance of Koza crossover.
	 * 
	 * @param functionSwapProbability The probability of crossover operations 
	 * 								  choosing a function node as the swap point.
	 */
	public KozaCrossover(double functionSwapProbability) {
		this.functionSwapProbability = functionSwapProbability;
	}
	
	/**
	 * Default constructor for Koza standard crossover. The probability of a 
	 * function node being selected as the swap point will default to 90%.
	 */
	public KozaCrossover() {
		this(0.9);
	}
	
	/**
	 * Crossover the two <code>CandidatePrograms</code> provided as arguments 
	 * using Koza crossover. The crossover points will be chosen from the 
	 * function or terminal sets with the probability assigned at construction 
	 * or the default value of 90% function node.
	 * 
	 * @param program1 The first CandidateProgram selected to undergo Koza 
	 * 				   crossover.
	 * @param program2 The second CandidateProgram selected to undergo Koza 
	 * 				   crossover.
	 */
	@Override
	public CandidateProgram<TYPE>[] crossover(CandidateProgram<TYPE> program1, CandidateProgram<TYPE> program2) {
		// Get swap points.
		int swapPoint1 = getCrossoverPoint(program1);
		int swapPoint2 = getCrossoverPoint(program2);
		
		// Get copies of subtrees to swap.
		Node<TYPE> subtree1 = (Node<TYPE>) program1.getNthNode(swapPoint1);//.clone();
		Node<TYPE> subtree2 = (Node<TYPE>) program2.getNthNode(swapPoint2);//.clone();
		
		// Perform swap.
		program1.setNthNode(swapPoint1, subtree2);
		program2.setNthNode(swapPoint2, subtree1);

		return new CandidateProgram[]{program1, program2};
	}
	
	/*
	 * Choose the crossover point for the given CandidateProgram with respect 
	 * to the probabilities assigned for function and terminal node points.
	 */
	private int getCrossoverPoint(CandidateProgram<TYPE> program) {
		// Calculate numbers of terminal and function nodes.
		int length = program.getProgramLength();
		int noTerminals = program.getNoTerminals();
		int noFunctions = length - noTerminals;
		
		// Randomly decide whether to use a function or terminal node point.
		if ((noFunctions > 0) && (Math.random() < functionSwapProbability)) {
			// Randomly select a function node from the function set.
			int f = (int) Math.floor(Math.random()*noFunctions);
			int nthFunctionNode = getNthFunctionNode(f, program);
			
			return nthFunctionNode;
		} else {
			// Randomly select a terminal node from the terminal set.
			int t = (int) Math.floor(Math.random()*noTerminals);
			int nthTerminalNode = getNthTerminalNode(t, program);
			
			return nthTerminalNode;
		}
	}

	/*
	 * Recurse through the given CandidateProgram to find the nth function 
	 * node and return its node index.
	 * 
	 * TODO Consider moving all these functions to the CandidateProgram class 
	 * as first class citizens in some form - they might be useful for others.
	 */
	private int getNthFunctionNode(int n, CandidateProgram<TYPE> program) {
		return getNthFunctionNode(n, 0, 0, program.getRootNode());
	}
	
	/*
	 * Recursive helper function.
	 */
	private int getNthFunctionNode(int n, int functionCount, int nodeCount, Node<TYPE> current) {
		// Found the nth function node.
		if ((current instanceof FunctionNode) && (n == functionCount))
			return nodeCount;
		
		int result = -1;
		for (Node<TYPE> child: current.getChildren()) {
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
	 * Recurse through the given CandidateProgram to find the nth terminal 
	 * node and return its node index.
	 */
	private int getNthTerminalNode(int n, CandidateProgram<TYPE> program) {
		return getNthTerminalNode(n, 0, 0, program.getRootNode());
	}
	
	/*
	 * Recursive helper function.
	 */
	private int getNthTerminalNode(int n, int terminalCount, int nodeCount, Node<TYPE> current) {
		// Found the nth terminal node.
		if (current instanceof TerminalNode) {
			if (n == terminalCount++)
				return nodeCount;
		}
		
		int result = -1;
		for (Node<TYPE> child: current.getChildren()) {
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
}