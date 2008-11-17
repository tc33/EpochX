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
 * 
 */
public class KozaCrossover<TYPE> implements Crossover<TYPE> {

	private double internalProbability;
	
	private GPModel<?> model;
	
	public KozaCrossover(GPModel<?> model, double internalProbability) {
		this.model = model;
		this.internalProbability = internalProbability;
	}
	
	public KozaCrossover(GPModel<?> model) {
		this(model, 0.9);
	}
	
	@Override
	public CandidateProgram<TYPE>[] crossover(CandidateProgram<TYPE> parent1, CandidateProgram<TYPE> parent2) {
		int c1 = getCrossoverPoint(parent1);
		int c2 = getCrossoverPoint(parent2);
		
		CandidateProgram<TYPE> child1 = (CandidateProgram<TYPE>) parent1.clone();
		CandidateProgram<TYPE> child2 = (CandidateProgram<TYPE>) parent2.clone();
		
		Node<?> subtree1 = child1.getNthNode(c1);
		Node<?> subtree2 = child2.getNthNode(c2);
		
		child1.setNthNode(subtree2, c1);
		child2.setNthNode(subtree1, c2);
		
		// max depth reversion section
		int pDepth1 = GPProgramAnalyser.getProgramDepth(child1);
		int pDepth2 = GPProgramAnalyser.getProgramDepth(child2);
		// depth check on child one
		if(pDepth1>model.getMaxDepth()) {
			child1 = (CandidateProgram<TYPE>) parent1.clone();
		}
		// depth check on child two
		if(pDepth2>model.getMaxDepth()) {
			child2 = (CandidateProgram<TYPE>) parent2.clone();
		}
		
		return new CandidateProgram[]{child1, child2};
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
