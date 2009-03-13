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

import java.util.*;
import org.apache.log4j.*;
import com.epochx.core.crossover.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;
import com.epochx.semantics.*;

/**
 * This class performs the very simple task of linking together parent 
 * selection and crossover. The actual tasks of crossover and selection are 
 * performed by Crossover and ParentSelector implementations respectively.
 * 
 * @see Crossover
 * @see UniformPointCrossover
 * @see KozaCrossover
 * @see ParentSelector
 * @see TournamentSelector
 */
public class GPCrossover<TYPE> {

	static Logger logger = Logger.getLogger(GPCrossover.class);
	
	private GPModel<TYPE> model;
	private boolean doStateCheckedCrossover;
	private ParentSelector<TYPE> parentSelector;
	private Crossover<TYPE> crossover;
	
	/**
	 * Constructs an instance of GPCrossover which will be capable of 
	 * performing crossover using the crossover operator and parent selector 
	 * provided by the given GPModel.
	 * 
	 * @param model the GPModel which defines the Crossover operator and 
	 * 				ParentSelector to use to perform one act of crossover on 
	 * 				a population.
	 */
	public GPCrossover(GPModel<TYPE> model) {
		this.model = model;
		this.doStateCheckedCrossover = model.getStateCheckedCrossover();
		this.parentSelector = model.getParentSelector();
		this.crossover = model.getCrossover();
	}

	/**
	 * Selects two parents and submits them to the Crossover operator which 
	 * is obtained by calling getCrossover() on the instance of GPModel given 
	 * at construction. The method of parent selection is also provided by this 
	 * model with a call to getParentSelector().
	 * 
	 * @param pop the population of CandidatePrograms from which 2 parents 
	 * 			  should be selected for crossover.
	 * @return an array of CandidatePrograms generated through crossover. This 
	 * 		   is most likely 2 child programs, but could in theory be any 
	 * 		   number as returned by the Crossover operator in use.
	 */
	public CandidateProgram<TYPE>[] crossover() {
		
		// define variables required
		CandidateProgram<TYPE> parent1;
		CandidateProgram<TYPE> parent2;

		CandidateProgram<TYPE> clone1;
		CandidateProgram<TYPE> clone2;

		CandidateProgram<TYPE>[] parents = null;
		CandidateProgram<TYPE>[] children = null;
		
		if(!doStateCheckedCrossover) {
			// do normal crossover
			parent1 = parentSelector.getParent();
			parent2 = parentSelector.getParent();
			clone1 = (CandidateProgram<TYPE>) parent1.clone();
			clone2 = (CandidateProgram<TYPE>) parent2.clone();
			parents = new CandidateProgram[]{parent1, parent2};
			children = crossover.crossover(clone1, clone2);
		} else {
			boolean equal = true;
			while(equal) {
				equal = false;
				// pull out semantic module and check its not null
				SemanticModule semMod = model.getSemanticModule();
				if(semMod==null) {
					throw new IllegalArgumentException("SEMANTIC MODULE UNDEFINED FOR SEMANTICALLY DRIVEN CROSSOVER");
				}
				//start semantic module
				semMod.start();
				
				// do crossover
				parent1 = parentSelector.getParent();
				parent2 = parentSelector.getParent();
				clone1 = (CandidateProgram<TYPE>) parent1.clone();
				clone2 = (CandidateProgram<TYPE>) parent2.clone();
				parents = new CandidateProgram[]{parent1, parent2};
				children = crossover.crossover(clone1, clone2);
				
				// check behaviours
				Representation p1Rep = semMod.codeToBehaviour(parents[0]);
				Representation p2Rep = semMod.codeToBehaviour(parents[1]);
				Representation c1Rep = semMod.codeToBehaviour(children[0]);
				Representation c2Rep = semMod.codeToBehaviour(children[1]);
				if(c1Rep.equals(p1Rep) || c1Rep.equals(p2Rep)) {
					equal = true;
				}
				if(c2Rep.equals(p1Rep) || c2Rep.equals(p2Rep)) {
					equal = true;
				}
				
				// stop semantic module
				semMod.stop();
			}
		}
		
		//TODO Need to be more careful here, potential for array out of bounds if crossover 
		//returns an array with more than 2 elements.
		for (int i=0; i<children.length; i++) {
			if (GPProgramAnalyser.getProgramDepth(children[i]) > model.getMaxDepth()) {
				children[i] = (CandidateProgram<TYPE>) parents[i].clone();
			}
		}
		
		return children;
	}
}
