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
import com.epochx.core.*;
import com.epochx.core.representation.*;
import com.epochx.semantics.*;

/**
 * Ramped Half and Half initialisation technique
 */
public class RampedHalfAndHalfInitialiser<TYPE> implements Initialiser<TYPE> {
	
	private GPModel<TYPE> model;	
	private GrowInitialiser<TYPE> grow;
	private FullInitialiser<TYPE> full;
	private CandidateProgram<TYPE> candidate;
	private int depth;

	/**
	 * @param model The model being assessed
	 * @param semMod The semantic module for this model
	 */
	public RampedHalfAndHalfInitialiser(GPModel<TYPE> model) {
		this.model = model;
		
		// set up the grow and full parts
		grow = new GrowInitialiser<TYPE>(model);
		full = new FullInitialiser<TYPE>(model);
		// modify depth for staged increase as per Koza
		if(model.getInitialMaxDepth()>=6) {
			this.depth = model.getInitialMaxDepth() - 4;
		} else {
			throw new IllegalArgumentException("MAX DEPTH TOO SMALL FOR RH+H");
		}
	}
	
	/**
	 * Will use grow initialisation on half the population and full on the other half. If 
	 * the population size is an odd number then the extra individual will be initialised with
	 * grow.
	 */
	public List<CandidateProgram<TYPE>> getInitialPopulation() {
		
		// initialise population of candidate programs
		int popSize = model.getPopulationSize();
		List<CandidateProgram<TYPE>> firstGen = new ArrayList<CandidateProgram<TYPE>>(popSize);
		
		// build population
		int split = popSize / 5;
		int marker = popSize / 5;
		for(int i=0; i<popSize; i++) {
			if(i>split) {
				depth++;
				split = split + marker;
			}
			// Grow on even numbers, full on odd.
			if (((i % 2) == 0)) {
	            candidate = new CandidateProgram<TYPE>(grow.buildGrowNodeTree(depth), model);
	            while(firstGen.contains(candidate)) {
	                candidate = new CandidateProgram<TYPE>(grow.buildGrowNodeTree(depth), model);
	            }
			} else {
	            candidate = new CandidateProgram<TYPE>(full.buildFullNodeTree(depth), model);
	            while(firstGen.contains(candidate)) {
	                candidate = new CandidateProgram<TYPE>(full.buildFullNodeTree(depth), model);
	            }
			}
            firstGen.add(candidate);
			
        }
		
		// Return starting population.
		return firstGen;
	}
}
