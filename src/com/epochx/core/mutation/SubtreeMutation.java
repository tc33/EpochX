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
package com.epochx.core.mutation;

import com.epochx.core.*;
import com.epochx.core.initialisation.*;
import com.epochx.core.representation.*;

/**
 * Subtree mutation method
 */
public class SubtreeMutation<TYPE> implements Mutation<TYPE> {

	private GPModel<TYPE> model;
	private int subtreeDepth;
	
	/**
	 * Simple constructor for subtree mutation using depth 4 for new subtrees
	 * @param model The GP model in use
	 */
	public SubtreeMutation(GPModel<TYPE> model) {
		// 4 is a slightly arbitrary choice but we had to choose something.
		this(model, 4);
	}
	
	/**
	 * Subtree mutation constructor with new subtree depth control
	 * @param model The GP model in use
	 * @param subtreeDepth The maximum depth of the inserted subtree
	 */
	public SubtreeMutation(GPModel<TYPE> model, int subtreeDepth) {
		this.model = model;
		this.subtreeDepth = subtreeDepth;
	}
	
	@Override
	public CandidateProgram<TYPE> mutate(CandidateProgram<TYPE> program) {
		int length = GPProgramAnalyser.getProgramLength(program);
		int mutationPoint = (int) Math.floor(Math.random() * length);
		
		// Randomly generate the new subtree.
		GrowInitialiser<TYPE> init = new GrowInitialiser<TYPE>(model);
		Node<TYPE> subtree = init.buildGrowNodeTree(subtreeDepth);
		
		// Set the new subtree.
		program.setNthNode(subtree, mutationPoint);
		
		return program;
	}

}
