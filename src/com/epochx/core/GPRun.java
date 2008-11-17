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
public class GPRun<TYPE> {

	private GPModel<TYPE> model;
	
	public void run(GPModel<TYPE> model) {
		this.model = model;
		
		// Set things up.
		GPCrossover<TYPE> crossover = new GPCrossover<TYPE>(model);
		
		// Initialisation
		Initialiser<TYPE> init = model.getInitialiser();
		List<CandidateProgram<TYPE>> pop = init.getInitialPopulation();
		//outputGeneration(0, pop);
		
		// TEMPORARY
		double bestFitness = Double.POSITIVE_INFINITY;
		CandidateProgram<?> bestProgram = null;
		
		for (int i=1; i<=model.getNoGenerations(); i++) {
			List<CandidateProgram<TYPE>> nextPop = new ArrayList<CandidateProgram<TYPE>>();
			
			// Perform elitism.
			List<CandidateProgram<TYPE>> elites = GPElitism.getElites(pop, model.getNoElites());
			for (CandidateProgram<TYPE> e: elites) {
				if (nextPop.size() < model.getPopulationSize())
					nextPop.add(e);
			}
			
			// Construct a poule.
			List<CandidateProgram<TYPE>> poule = model.getPouleSelector().getPoule(pop, model.getPouleSize());
			
			while(nextPop.size() < model.getPopulationSize()) {
				// Pick a genetic operator using Pr, Pe and Pm.
				double random = Math.random();
				double pr = model.getReproductionProbability();
				double pe = model.getCrossoverProbability();
				
				if (random < pr) {
					// Do reproduction. - Should this use clone?
					nextPop.add(poule.get((int) Math.floor(Math.random()*poule.size())));
				} else if (random < pr+pe) {
					// Do crossover.
					CandidateProgram<TYPE>[] children = crossover.crossover(poule);
					for (CandidateProgram<TYPE> c: children) {
						if (nextPop.size() < model.getPopulationSize())
							nextPop.add(c);
					}
				} else {
					// Do mutation.
					//TODO Implement mutation.
				}
			}
			
			for (CandidateProgram<TYPE> p: pop) {
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
	private void outputGeneration(int i, List<CandidateProgram<TYPE>> programs) {
		System.out.println("######################################################");
		System.out.println("Population #"+i+":");
		System.out.println("Size: " + programs.size());
		
		/*for (CandidateProgram p: programs) {
			outputProgram(p);
		}*/
	}
	
	private void outputProgram(CandidateProgram<TYPE> program) {
		System.out.println("------------------------------------------------------");
		System.out.println(program);
		System.out.println("    fitness = " + model.getFitness(program));
		System.out.println("    depth = " + GPProgramAnalyser.getProgramDepth(program));
		System.out.println("    length = " + GPProgramAnalyser.getProgramLength(program));
	}
}
