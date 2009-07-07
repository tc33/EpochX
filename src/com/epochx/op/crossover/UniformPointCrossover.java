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
 * This class implements standard crossover with uniform swap points.
 */
public class UniformPointCrossover<TYPE> implements Crossover<TYPE> {

	/**
	 * Crossover the two <code>CandidatePrograms</code> provided as arguments 
	 * using uniform swap points. Random crossover points are chosen at random 
	 * in both programs, the genetic material at the points are then exchanged.
	 * The resulting programs are returned as new CandidateProgram objects.
	 * 
	 * @param program1 The first CandidateProgram selected to undergo uniform  
	 * 			       point crossover.
	 * @param program2 The second CandidateProgram selected to undergo uniform  
	 * 				   point crossover.
	 */
	@Override
	public CandidateProgram<TYPE>[] crossover(CandidateProgram<TYPE> program1, CandidateProgram<TYPE> program2) {
		// Select swap points.
		int swapPoint1 = (int) Math.floor(Math.random()*program1.getProgramLength());
		int swapPoint2 = (int) Math.floor(Math.random()*program2.getProgramLength());

		// Get copies of subtrees to swap.
		Node<TYPE> subTree1 = (Node<TYPE>) program1.getNthNode(swapPoint1);//.clone();
		Node<TYPE> subTree2 = (Node<TYPE>) program2.getNthNode(swapPoint2);//.clone();
		
		// Perform swap.
		program1.setNthNode(swapPoint1, subTree2);
		program2.setNthNode(swapPoint2, subTree1);		
		
		return new CandidateProgram[]{program1, program2};
	}
}