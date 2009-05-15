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
package com.epochx.core;

import com.epochx.core.mutation.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;
import com.epochx.semantics.*;
import com.epochx.stats.*;

/**
 *
 */
public class GPMutation<TYPE> {

	private GPModel<TYPE> model;
	
	private ParentSelector<TYPE> parentSelector;
	private Mutator<TYPE> mutator;
	private MutationStats<TYPE> mutationStats;
	private int reversions;
	
	public GPMutation(GPModel<TYPE> model) {
		this.model = model;
		
		this.parentSelector = model.getParentSelector();
		this.mutator = model.getMutator();
		
		mutationStats = new MutationStats<TYPE>();
		
		// This provides a shortcut for the common convention of making a model the listener.
		//TODO Actually might be better to allow models a way of giving their own listener for more flexibility.
		if (model instanceof GenerationStatListener) {
			mutationStats.addMutationStatListener((MutationStatListener) model);
		}
	}
	
	public CandidateProgram<TYPE> mutate() {
		long crossoverStartTime = System.nanoTime();
		
		CandidateProgram<TYPE> parent = null;
		CandidateProgram<TYPE> child = null;
		
		reversions = 0;
		
		if (model.getStateCheckedMutation()) {
			boolean equal = true;
			while(equal) {
				// pull out semantic module and check its not null
				SemanticModule semMod = model.getSemanticModule();
				if(semMod==null) {
					throw new IllegalArgumentException("SEMANTIC MODULE UNDEFINED FOR SEMANTICALLY DRIVEN MUTATION");
				}
				
				//start semantic module
				semMod.start();
	
				parent = parentSelector.getParent();
				CandidateProgram<TYPE> clone = (CandidateProgram<TYPE>) parent.clone();
				
				child = mutator.mutate(clone);
				
				// If the new program is too deep, replace it with the original.
				if (GPProgramAnalyser.getProgramDepth(child) > model.getMaxDepth()) {
					child = (CandidateProgram<TYPE>) parent.clone();
				}
				
				// check behaviours
				Representation p1Rep = semMod.codeToBehaviour(parent);
				Representation c1Rep = semMod.codeToBehaviour(child);
				
				if(!c1Rep.equals(p1Rep)) {
					equal = false;
				}
				
				if (equal) {
					// We're going to have to revert.
					reversions++;
				}
				
				// stop semantic module
				semMod.stop();
			}
		} else {
			parent = parentSelector.getParent();
			CandidateProgram<TYPE> clone = (CandidateProgram<TYPE>) parent.clone();
			
			child = mutator.mutate(clone);
			
			// If the new program is too deep, replace it with the original.
			if (GPProgramAnalyser.getProgramDepth(child) > model.getMaxDepth()) {
				child = (CandidateProgram<TYPE>) parent.clone();
			}
		}
		
		long runtime = System.nanoTime() - crossoverStartTime;
		mutationStats.addMutation(parent, child, runtime);
		
		return child;
	}
	
	public int getRevertedCount() {
		return reversions;
	}
	
}
