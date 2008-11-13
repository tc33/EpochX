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
import com.epochx.core.GPModel;
import com.epochx.core.representation.*;

import core.*;

/**
 * 
 */
public class RampedHalfAndHalfInitialiser<TYPE> implements Initialiser<TYPE> {
	
	private GPModel<TYPE> model;
	
	private GrowInitialiser<TYPE> grow;
	private FullInitialiser<TYPE> full;
	private CandidateProgram<TYPE> candidate;
	private int depth;

	/**
	 * @param syntax A set of nodes containing all functions and terminals
	 * @param functions A set of nodes containing all the functions
	 * @param terminals A set of nodes containing all the terminals
	 * @param semMod The semantic module for this model
	 * @param popSize The population size
	 * @param depth The max depth of the program tree on initialisation
	 * @throws IllegalArgumentException for the max depth being too small to work RHH
	 */
	public RampedHalfAndHalfInitialiser(GPModel<TYPE> model, SemanticModule semMod) {
		this.model = model;
		
		// set up the grow and full parts
		grow = new GrowInitialiser<TYPE>(model, semMod);
		full = new FullInitialiser<TYPE>(model, semMod);
		// modify depth for staged increase as per Koza
		if(model.getMaxDepth()>=6) {
			this.depth = model.getMaxDepth() - 4;
		} else {
			throw new IllegalArgumentException("MAX DEPTH TOO SMALL FOR RH+H");
		}
	}
	
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
            candidate = new CandidateProgram<TYPE>(grow.buildGrowNodeTree(depth), model);
            while(firstGen.contains(candidate)) {
                candidate = new CandidateProgram<TYPE>(grow.buildGrowNodeTree(depth), model);
            }
            firstGen.add(candidate);
            candidate = new CandidateProgram<TYPE>(full.buildFullNodeTree(depth), model);
            while(firstGen.contains(candidate)) {
                candidate = new CandidateProgram<TYPE>(full.buildFullNodeTree(depth), model);
            }
            firstGen.add(candidate);
			
        }
		
		// return starting population
		return firstGen;
	}

}
