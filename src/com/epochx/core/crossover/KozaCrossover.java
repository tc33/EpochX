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

import com.epochx.core.*;
import com.epochx.core.representation.*;

/**
 * This class implements Koza style crossover with 90% bias on
 * function swap points and 10% on terminal swap points
 */
public class KozaCrossover<TYPE> implements Crossover<TYPE> {

	private double internalProbability;

	/**
	 * Constructor for Koza standard crossover object
	 * @param internalProbability The probability of crossover
	 */
	public KozaCrossover(double internalProbability) {
		this.internalProbability = internalProbability;
	}
	
	/**
	 * Default constructor for Koza standard crossover
	 */
	public KozaCrossover() {
		this(0.9);
	}
	
	@Override
	public CandidateProgram<TYPE>[] crossover(CandidateProgram<TYPE> program1, CandidateProgram<TYPE> program2) {
		// Get swap points.
		int swapPoint1 = getCrossoverPoint(program1);
		int swapPoint2 = getCrossoverPoint(program2);
		
		// Get copies of subtrees to swap.
		Node<TYPE> subtree1 = (Node<TYPE>) program1.getNthNode(swapPoint1).clone();
		Node<TYPE> subtree2 = (Node<TYPE>) program2.getNthNode(swapPoint2).clone();
		
		// Perform swap.
		program1.setNthNode(subtree2, swapPoint1);
		program2.setNthNode(subtree1, swapPoint2);

		return new CandidateProgram[]{program1, program2};
	}
	
	private int getCrossoverPoint(CandidateProgram<?> program) {
		int length = GPProgramAnalyser.getProgramLength(program);
		int noTerminals = GPProgramAnalyser.getNoTerminals(program);
		int noFunctions = length - noTerminals;
		
		if ((noFunctions > 0) && (Math.random() < internalProbability)) {

			int f = (int) Math.floor(Math.random()*noFunctions);
			
			int nthFunctionNode = getNthFunctionNode(f, program);
			
			return nthFunctionNode;
		} else {
			int t = (int) Math.floor(Math.random()*noTerminals);
			
			int nthTerminalNode = getNthTerminalNode(t, program);
			
			return nthTerminalNode;
		}
	}

	private int getNthFunctionNode(int n, CandidateProgram<?> program) {
		return getNthFunctionNode(n, 0, 0, program.getRootNode());
	}
	
	private int getNthFunctionNode(int n, int fc, int nc, Node<?> current) {
		if ((current instanceof FunctionNode) && (n == fc))
			return nc;
		
		int result = -1;
		for (Node<?> child: current.getChildren()) {
			int noNodes = GPProgramAnalyser.getProgramLength(child);
			int noFunctions = GPProgramAnalyser.getNoFunctions(child);
			
			// Only look at the subtree if it contains the right range of nodes.
			if (n <= noFunctions + fc) {
				int childResult = getNthFunctionNode(n, fc+1, nc+1, child);
				if (childResult != -1) 
					return childResult;
			}
			
			fc += noFunctions;
			nc += noNodes;
		}
		
		return result;
	}
	
	private int getNthTerminalNode(int n, CandidateProgram<?> program) {
		return getNthTerminalNode(n, 0, 0, program.getRootNode());
	}
	
	private int getNthTerminalNode(int n, int tc, int nc, Node<?> current) {
		if (current instanceof TerminalNode) {
			if (n == tc++)
				return nc;
		}
		
		int result = -1;
		for (Node<?> child: current.getChildren()) {
			int noNodes = GPProgramAnalyser.getProgramLength(child);
			int noTerminals = GPProgramAnalyser.getNoTerminals(child);
			
			// Only look at the subtree if it contains the right range of nodes.
			if (n <= noTerminals + tc) {
				int childResult = getNthTerminalNode(n, tc, nc+1, child);
				if (childResult != -1) 
					return childResult;
			}
			
			tc += noTerminals;
			nc += noNodes;
		}
		
		return result;
	}
}
