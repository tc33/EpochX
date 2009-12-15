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
 * This class implements standard crossover with uniform swap points.
 */
public class UniformPointCrossover implements GPCrossover, GenerationListener {

	// The current controlling model.
	private GPModel model;
	
	// The random number generator for controlling random behaviour.
	private RandomNumberGenerator rng;
	
	public UniformPointCrossover(GPModel model) {
		this.model = model;
		
		Controller.getLifeCycleManager().addGenerationListener(this);
	}
	
	/**
	 * GPCrossover the two <code>CandidatePrograms</code> provided as arguments 
	 * using uniform swap points. Random crossover points are chosen at random 
	 * in both programs, the genetic material at the points are then exchanged.
	 * The resulting programs are returned as new GPCandidateProgram objects.
	 * 
	 * @param program1 The first GPCandidateProgram selected to undergo uniform  
	 * 			       point crossover.
	 * @param program2 The second GPCandidateProgram selected to undergo uniform  
	 * 				   point crossover.
	 */
	@Override
	public GPCandidateProgram[] crossover(CandidateProgram p1, CandidateProgram p2) {
		GPCandidateProgram program1 = (GPCandidateProgram) p1;
		GPCandidateProgram program2 = (GPCandidateProgram) p2;
		
		// Select swap points.
		int swapPoint1 = rng.nextInt(program1.getProgramLength());
		int swapPoint2 = rng.nextInt(program2.getProgramLength());

		// Get copies of subtrees to swap.
		Node subTree1 = program1.getNthNode(swapPoint1);//.clone();
		Node subTree2 = program2.getNthNode(swapPoint2);//.clone();
		
		// Perform swap.
		program1.setNthNode(swapPoint1, subTree2);
		program2.setNthNode(swapPoint2, subTree1);		
		
		return new GPCandidateProgram[]{program1, program2};
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
