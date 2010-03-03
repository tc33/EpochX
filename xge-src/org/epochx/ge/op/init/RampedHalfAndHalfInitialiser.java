/*  
 *  Copyright 2009 Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of XGE: grammatical evolution for research
 *
 *  XGE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XGE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with XGE.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.ge.op.init;

import java.util.*;

import org.epochx.ge.model.*;
import org.epochx.ge.representation.*;
import org.epochx.representation.CandidateProgram;


/**
 * Note: Ramped half & half initialisation currently only works for depth first
 * mapping.
 * 
 * Ramped half-and-half initialisation in grammatical evolution works similarly
 * to ramped half-and-half in genetic programming but due to the differences of
 * representation it has differences. See Chapter 8: Sensible Initialisation, 
 * in Grammatical Evolution by O'Neill and Ryan for details.
 * 
 */
public class RampedHalfAndHalfInitialiser implements GEInitialiser {

	// The current controlling model.
	private GEModel model;	
	
	// The grow and full instances for doing their share of the work.
	private GrowInitialiser grow;
	private FullInitialiser full;
	
	/**
	 * Construct a RampedHalfAndHalfInitialiser.
	 * 
	 * @param model The model being assessed
	 */
	public RampedHalfAndHalfInitialiser(GEModel model) {
		this.model = model;
		
		// set up the grow and full parts
		grow = new GrowInitialiser(model);
		full = new FullInitialiser(model);
	}
	
	/**
	 * Will use grow initialisation on half the population and full on the other half. If 
	 * the population size is an odd number then the extra individual will be initialised with
	 * grow.
	 */
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to populate.
		int popSize = model.getPopulationSize();
		List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);
		
		// Our start depth can only be as small as the grammars minimum depth.
		int startDepth = model.getGrammar().getMinimumDepth();
		int endDepth = model.getMaxInitialProgramDepth();
		
		if (endDepth < 2) {
			throw new IllegalArgumentException("Initial maximum depth must be greater than 1 for RH+H.");
		}
		
		// Number of programs each depth SHOULD have. But won't unless remainder is 0.
		double programsPerDepth = (double) popSize / (endDepth - startDepth + 1);
		
		for (int i=0; i<popSize; i++) {
			// Calculate depth
			int depth = (int) Math.floor((firstGen.size() / programsPerDepth) + startDepth);
			
			// Grow on even numbers, full on odd.
			GECandidateProgram program;
			
			do {
				if ((i % 2) == 0) {
					program = grow.getInitialProgram(depth);
				} else {
					program = full.getInitialProgram(depth);
				}
			} while(firstGen.contains(program));
			
            firstGen.add(program);
		}
		
		return firstGen;
	}
}
