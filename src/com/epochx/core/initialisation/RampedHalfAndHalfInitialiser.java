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
package com.epochx.core.initialisation;

import java.util.*;

import com.epochx.core.GPModel;
import com.epochx.core.representation.*;

/**
 * Initialisation implementation which uses a combination of full and grow 
 * initialisers to create an initial population of <code>CandidatePrograms</code>.
 * 
 * <p>A variety of depths are used from 2 up to the maximum depth, with 50% of 
 * the individuals created with full and 50% with grow initialisers.
 * 
 * TODO I'm not sure this works correctly. Koza's RH+H description is a little 
 * vague about some details but I think this implementation has misunderstood 
 * the intention.
 */
public class RampedHalfAndHalfInitialiser<TYPE> implements Initialiser<TYPE> {
	
	// The current controlling model.
	private GPModel<TYPE> model;	
	
	// The grow and full instances for doing their share of the work.
	private GrowInitialiser<TYPE> grow;
	private FullInitialiser<TYPE> full;

	/**
	 * @param model The model being assessed
	 * @param semMod The semantic module for this model
	 */
	public RampedHalfAndHalfInitialiser(GPModel<TYPE> model) {
		this.model = model;
		
		// set up the grow and full parts
		grow = new GrowInitialiser<TYPE>(model);
		full = new FullInitialiser<TYPE>(model);
	}
	
	/**
	 * Will use grow initialisation on half the population and full on the other half. If 
	 * the population size is an odd number then the extra individual will be initialised with
	 * grow.
	 */
	public List<CandidateProgram<TYPE>> getInitialPopulation() {
		// modify depth for staged increase as per Koza
		int depth = -1;
		if(model.getInitialMaxDepth()>=6) {
			depth = model.getInitialMaxDepth() - 4;
		} else {
			throw new IllegalArgumentException("Max depth too small for RH+H initialisation.");
		}	
		
		// initialise population of candidate programs
		int popSize = model.getPopulationSize();
		List<CandidateProgram<TYPE>> firstGen = new ArrayList<CandidateProgram<TYPE>>(popSize);
		
		// Build population.
		int split = popSize / 5;
		int marker = popSize / 5;
		for(int i=0; i<popSize; i++) {
			if(i>split) {
				depth++;
				split = split + marker;
			}
			// Grow on even numbers, full on odd.
			CandidateProgram<TYPE> program;
			
			do {
				Node<TYPE> rootNode;
				if ((i % 2) == 0) {
					rootNode = grow.buildGrowNodeTree(depth);
				} else {
					rootNode = full.buildFullNodeTree(depth);
				}
				program = new CandidateProgram<TYPE>(rootNode, model);
			} while(firstGen.contains(program));
			
            firstGen.add(program);
			
        }
		
		return firstGen;
	}
}
