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
		GPConfig config = model.getConfiguration();
				
		// Initialisation		
		Initialiser init = config.getInitialiser();
		List<CandidateProgram> pop = init.getInitialPopulation();
		outputGeneration(0, pop);
		
		for (int i=1; i<=config.getNoGenerations(); i++) {
			pop = config.getCrossover().crossover(pop);
			outputGeneration(i, pop);
		}
	}
	
	/*
	 * TEMPORARY LOGGING - need to setup some proper logging soon.
	 */
	private void outputGeneration(int i, List<CandidateProgram> programs) {
		System.out.println("######################################################");
		System.out.println("Population #"+i+":");
		
		for (CandidateProgram p: programs) {
			outputProgram(p);
		}
	}
	
	private void outputProgram(CandidateProgram program) {
		System.out.println("------------------------------------------------------");
		System.out.println(program);
		System.out.println(model.getFitness(program));
		System.out.println("    depth = " + GPProgramAnalyser.getProgramDepth(program));
		System.out.println("    length = " + GPProgramAnalyser.getProgramLength(program));
	}
}
