/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.gr.op.init;

import java.util.*;

import org.epochx.gr.model.GRAbstractModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.Grammar;


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
public class RampedHalfAndHalfInitialiser implements GRInitialiser {

	// The current controlling model.
	private GRAbstractModel model;	
	
	private Grammar grammar;
	private int popSize;
	private int maxInitialProgramDepth;
	
	// The grow and full instances for doing their share of the work.
	private GrowInitialiser grow;
	private FullInitialiser full;
	
	// The smallest depth to be used.
	private int minDepth;
	
	/**
	 * Construct a RampedHalfAndHalfInitialiser.
	 * 
	 * @param model The model being assessed
	 */
	public RampedHalfAndHalfInitialiser(GRAbstractModel model) {		
		this(model, -1);
	}
	
	/**
	 * Construct a RampedHalfAndHalfInitialiser.
	 * 
	 * @param model The model being assessed
	 * @param minDepth The smallest depth to generate programs to before 
	 * ramping up. If a value smaller than the smallest allowable by the 
	 * grammar is given then this smallest possible value is used.
	 */
	public RampedHalfAndHalfInitialiser(GRAbstractModel model, int minDepth) {
		this.model = model;
		this.minDepth = minDepth;
		
		// set up the grow and full parts
		grow = new GrowInitialiser(model);
		full = new FullInitialiser(model);
		
		// Configure parameters from the model.
		model.getLifeCycleManager().addConfigListener(new ConfigAdapter() {
			@Override
			public void onConfigure() {
				configure();
			}
		});
	}
	
	/*
	 * Configure component with parameters from the model.
	 */
	private void configure() {
		grammar = model.getGrammar();
		popSize = model.getPopulationSize();
		maxInitialProgramDepth = model.getMaxInitialProgramDepth();
	}
	
	/**
	 * Will use grow initialisation on half the population and full on the other half. If 
	 * the population size is an odd number then the extra individual will be initialised with
	 * grow.
	 */
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to populate.
		List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);
		
		int minDepthPossible = grammar.getMinimumDepth();
		if (minDepth < minDepthPossible) {
			// Our start depth can only be as small as the grammars minimum depth.
			minDepth = minDepthPossible;
		}
		
		if (maxInitialProgramDepth < 2) {
			throw new IllegalArgumentException("Initial maximum depth must be greater than 1 for RH+H.");
		}
		
		// Number of programs each depth SHOULD have. But won't unless remainder is 0.
		double programsPerDepth = (double) popSize / (maxInitialProgramDepth - minDepth + 1);
		
		for (int i=0; i<popSize; i++) {
			// Calculate depth
			int depth = (int) Math.floor((i / programsPerDepth) + minDepth);
			
			// Grow on even numbers, full on odd.
			GRCandidateProgram program;
			
			int tries = 0;
			do {
				if ((i % 2) == 0) {
					program = grow.getInitialProgram(depth);
				} else {
					program = full.getInitialProgram(depth);
				}
				
				tries++;
				if (tries == 10) {
					// If we can't get a unique program after 10 tries, then just go with it.
					break;
				}
				//TODO Need to look out for infinite loop here - where not enough different programs possible, should drop out and readjust depth sizes (with a warning).
			} while(firstGen.contains(program));
			
            firstGen.add(program);
		}
		
		return firstGen;
	}
}
