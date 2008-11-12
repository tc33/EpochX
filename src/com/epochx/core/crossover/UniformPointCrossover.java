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
public class UniformPointCrossover implements Crossover {
	
	private GPConfig config;
	
	public UniformPointCrossover(GPConfig config) {
		this.config = config;
	}

	@Override
	public CandidateProgram[] crossover(CandidateProgram parent1, CandidateProgram parent2) {
		CandidateProgram child1 = (CandidateProgram) parent1.clone();
		CandidateProgram child2 = (CandidateProgram) parent2.clone();

		// select swap and put points
		int swapPoint1 = (int) Math.floor(Math.random()*GPProgramAnalyser.getProgramLength(parent1));
		int swapPoint2 = (int) Math.floor(Math.random()*GPProgramAnalyser.getProgramLength(parent2));
		
		// do swap
		// get parts to swap

		// find Nth node
		// Do we actually need to make a clone of this if its a direct swap?
		Node subTree1 = (Node) child1.getNthNode(swapPoint1).clone();
		Node subTree2 = (Node) child2.getNthNode(swapPoint2).clone();
		// set Nth node
		child1.setNthNode(subTree2, swapPoint1);
		child2.setNthNode(subTree1, swapPoint2);

		// max depth reversion section
		int pDepth1 = GPProgramAnalyser.getProgramDepth(child1);
		int pDepth2 = GPProgramAnalyser.getProgramDepth(child2);
		// depth check on child one
		if(pDepth1>config.getMaxDepth()) {
			child1 = (CandidateProgram) parent1.clone();
		}
		// depth check on child two
		if(pDepth2>config.getMaxDepth()) {
			child2 = (CandidateProgram) parent2.clone();
		}

		// TODO state change section

		
		return new CandidateProgram[]{child1, child2};
	}
}
