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
	
	public KozaCrossover(double internalProbability) {
		this.internalProbability = internalProbability;
	}
	
	public KozaCrossover() {
		this(0.9);
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
		
		return new CandidateProgram[]{child1, child2};
	}
	
	private int getCrossoverPoint(CandidateProgram program) {
		int length = GPProgramAnalyser.getProgramLength(program);
		int noTerminals = GPProgramAnalyser.getNoTerminals(program);
		int noFunctions = length - noTerminals;
		
		if ((noFunctions > 0) && (Math.random() < internalProbability)) {
			
			
			int f = (int) Math.floor(Math.random()*noFunctions);
			
			int functionCount = -1;
			for (int i=0; i<length; i++) {
				// Definitely should NOT be using getNthNode - WAY too slow for this.
				Node nth = program.getNthNode(i);
				if (nth instanceof FunctionNode) {
					if (++functionCount == f) {
						return i;
					}
				}
			}
		} else {
			int t = (int) Math.floor(Math.random()*noTerminals);
			
			int terminalCount = -1;
			for (int i=0; i<length; i++) {
				Node nth = program.getNthNode(i);
				if (nth instanceof TerminalNode) {
					if (++terminalCount == t) {
						return i;
					}
				}
			}	
		}
		
		// If we get to here then something has broken - need to do something more sensible (exception?).
		return -1;
	}
}
