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
package org.epochx.gp.op.init;

import java.util.*;

import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.*;
import org.epochx.gr.model.*;
import org.epochx.life.*;
import org.epochx.representation.CandidateProgram;


/**
 * Initialisation implementation which uses a combination of full and grow 
 * initialisers to create an initial population of <code>CandidatePrograms</code>.
 * 
 * <p>Depths are equally split between depths of 2 up to the maximum initial 
 * depth as specified by the model. Initialisation of individuals at each of 
 * these depths is then alternated between full and grow initialisers.
 * 
 * <p>There will not always be an equal number of programs created to each 
 * depth, this will depend on if the population size is exactly divisible by 
 * the range of depths (initial maximum depth - 2). If the range of depths is 
 * greater than the population size then some depths will not occur at all in 
 * order to ensure as wide a spread of depths up to the maximum as possible.
 */
public class RampedHalfAndHalfInitialiser implements GPInitialiser {
	
	// The current controlling model.
	private GPModel model;
	
	private int popSize;
	private int initialMaxDepth;
	
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
	public RampedHalfAndHalfInitialiser(GPModel model) {		
		this(model, -1);
	}
	
	/**
	 * Construct a RampedHalfAndHalfInitialiser.
	 * 
	 * @param model The model being assessed
	 */
	public RampedHalfAndHalfInitialiser(GPModel model, int minDepth) {
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
		popSize = model.getPopulationSize();
		initialMaxDepth = model.getMaxInitialDepth();
	}
	
	/**
	 * Will use grow initialisation on half the population and full on the other half. If 
	 * the population size is an odd number then the extra individual will be initialised with
	 * grow.
	 */
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to populate.
		List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(popSize);
		
		int startDepth = 2;
		
		if (initialMaxDepth < 2) {
			throw new IllegalArgumentException("Initial maximum depth must be greater than 1 for RH+H.");
		}
		
		// Number of programs each depth SHOULD have. But won't unless remainder is 0.
		double programsPerDepth = (double) popSize / (initialMaxDepth - startDepth + 1);
		
		for (int i=0; i<popSize; i++) {			
			// Calculate depth
			int depth = (int) Math.floor((firstGen.size() / programsPerDepth) + startDepth);
			
			// Grow on even numbers, full on odd.
			GPCandidateProgram program;
			
			int attempts=0;
			do {
				Node rootNode;
				if ((i % 2) == 0) {
					rootNode = grow.buildGrowNodeTree(depth);
				} else {
					rootNode = full.buildFullNodeTree(depth);
				}
				program = new GPCandidateProgram(rootNode, model);
				attempts++;
			} while(firstGen.contains(program) && attempts<5);

            firstGen.add(program);
		}
		
		return firstGen;
	}
}
