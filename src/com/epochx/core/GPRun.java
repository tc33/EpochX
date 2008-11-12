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

import com.epochx.core.initialisation.*;
import com.epochx.core.representation.*;

/**
 * 
 */
public class GPRun {

	private GPModel model;
	
	public void run(GPModel model) {
		this.model = model;
		
		// Set things up.
		GPConfig config = model.getConfiguration();
		GPCrossover crossover = new GPCrossover(config);
		
		// Initialisation
		Initialiser init = config.getInitialiser();
		List<CandidateProgram> pop = init.getInitialPopulation();
		//outputGeneration(0, pop);
		
		// TEMPORARY
		double bestFitness = Double.POSITIVE_INFINITY;
		CandidateProgram bestProgram = null;
		
		for (int i=1; i<=config.getNoGenerations(); i++) {
			List<CandidateProgram> nextPop = new ArrayList<CandidateProgram>();
			
			// Perform elitism.
			/*Collections.sort(pop);
			if (config.getNoElites() > 0)
				nextPop.addAll(pop.subList(pop.size()-config.getNoElites(),pop.size()-1));*/
			
			// Construct a poule.
			List<CandidateProgram> poule = config.getPouleSelector().getPoule(pop, config.getPouleSize());
			
			while(nextPop.size() < config.getPopulationSize()) {
				// Pick a genetic operator using Pr, Pe and Pm.
				double random = Math.random();
				double pr = config.getReproductionProbability();
				double pe = config.getCrossoverProbability();
				
				if (random < pr) {
					// Do reproduction. - Should this use clone?
					nextPop.add(poule.get((int) Math.floor(Math.random()*poule.size())));
				} else if (random < pr+pe) {
					// Do crossover.
					CandidateProgram[] children = crossover.crossover(poule);
					for (CandidateProgram c: children) {
						if (nextPop.size() < config.getPopulationSize())
							nextPop.add(c);
					}
				} else {
					// Do mutation.
					//TODO Implement mutation.
				}
			}
			
			for (CandidateProgram p: pop) {
				double fitness = model.getFitness(p);
				if (fitness < bestFitness) {
					bestFitness = fitness;
					bestProgram = p;
				}
			}
			outputGeneration(i, pop);
			
			pop = nextPop;
		}
		
		System.out.println("BEST PROGRAM: " + bestProgram);
		System.out.println("BEST FITNESS: " + bestFitness);
		System.out.println();
	}
	
	
		
	/*
	 * TEMPORARY LOGGING - need to setup some proper logging soon.
	 */
	private void outputGeneration(int i, List<CandidateProgram> programs) {
		System.out.println("######################################################");
		System.out.println("Population #"+i+":");
		System.out.println("Size: " + programs.size());
		
		/*for (CandidateProgram p: programs) {
			outputProgram(p);
		}*/
	}
	
	private void outputProgram(CandidateProgram program) {
		System.out.println("------------------------------------------------------");
		System.out.println(program);
		System.out.println("    fitness = " + model.getFitness(program));
		System.out.println("    depth = " + GPProgramAnalyser.getProgramDepth(program));
		System.out.println("    length = " + GPProgramAnalyser.getProgramLength(program));
	}
}
