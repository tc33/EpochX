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
 * This class implements crossover with uniform swap points.
 */
public class UniformPointCrossover<TYPE> implements Crossover<TYPE> {

	@Override
	public CandidateProgram<TYPE>[] crossover(CandidateProgram<TYPE> program1, CandidateProgram<TYPE> program2) {
		// Select swap points.
		int swapPoint1 = (int) Math.floor(Math.random()*GPProgramAnalyser.getProgramLength(program1));
		int swapPoint2 = (int) Math.floor(Math.random()*GPProgramAnalyser.getProgramLength(program2));

		// Get copies of subtrees to swap.
		// We NEED to clone these because otherwise you risk copying crossed over
		// programs back into the breeding pool 
		Node<?> subTree1 = (Node<?>) program1.getNthNode(swapPoint1).clone();
		Node<?> subTree2 = (Node<?>) program2.getNthNode(swapPoint2).clone();
		
		// Perform swap.
		program1.setNthNode(subTree2, swapPoint1);
		program2.setNthNode(subTree1, swapPoint2);		
		
		return new CandidateProgram[]{program1, program2};
	}
}
