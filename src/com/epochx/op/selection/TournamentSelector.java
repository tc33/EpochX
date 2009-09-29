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
package com.epochx.op.selection;

import java.util.*;

import com.epochx.core.GPModel;
import com.epochx.representation.*;

/**
 * Tournament selection provides both program and pool selection. In tournament 
 * selection, x programs are randomly selected from the population to enter a 
 * 'tournament'. The program with the best fitness in the tournament then 
 * becomes the selected program. The tournament size, x, is given as an argument 
 * to the constructor.
 */
public class TournamentSelector<TYPE> implements ProgramSelector<TYPE>, PoolSelector<TYPE> {

	// The current controlling model.
	private GPModel<TYPE> model;
	
	// The size of the tournment from which the best program will be taken.
	private int tournamentSize;
	
	// We use a random selector to construct tournaments.
	private RandomSelector<TYPE> randomSelector;
	
	/**
	 * Construct a tournament selector with the specified tournament size.
	 * 
	 * @param tournamentSize the number of programs in each tournament.
	 */
	public TournamentSelector(GPModel<TYPE> model, int tournamentSize) {
		this.model = model;
		this.tournamentSize = tournamentSize;
		
		randomSelector = new RandomSelector<TYPE>(model);
	}

	/**
	 * Store the population for creating tournaments from.
	 */
	@Override
	public void onGenerationStart(List<CandidateProgram<TYPE>> pop) {
		// We'll be using a random selector to construct a tournament.
		randomSelector.onGenerationStart(pop);
	}
	
	/**
	 * Randomly creates a tournament, then selects the candidate program with 
	 * the best fitness from that tournament. The size of the tournament is 
	 * given at instantiation.
	 * 
	 * @return the best program from a randomly generated tournament.
	 */
	@Override
	public CandidateProgram<TYPE> getProgram() {
		// Use random selector to create tournament.
		CandidateProgram<TYPE>[] tournament = new CandidateProgram[tournamentSize];
		for (int i=0; i<tournamentSize; i++) {
			tournament[i] = randomSelector.getProgram();
		}
		
		// Check the fitness of each program, stashing the best.
		double bestFitness = Double.POSITIVE_INFINITY;
		CandidateProgram<TYPE> bestProgram = null;
		for (CandidateProgram<TYPE> p: tournament) {
			double fitness = p.getFitness();
			if (fitness < bestFitness) {
				bestFitness = fitness;
				bestProgram = p;
			}
		}
				
		return bestProgram;
	}

	/**
	 * Constructs a pool of programs from the population, choosing each one 
	 * with the program selection element of TournamentSelector. The size of 
	 * the pool created will be equal to the poolSize argument. The generated 
	 * pool may contain duplicate programs, and as such the pool size may be 
	 * greater than the population size.
	 * 
	 * @param pop the population of CandidatePrograms from which the programs 
	 * 			  in the pool should be chosen.
	 * @param poolSize the number of programs that should be selected from the 
	 * 			 	   population to form the pool. If poolSize is zero or less  
	 * 				   then no selection takes place and the given population 
	 * 				   is returned unaltered.
	 * @return the pool of candidate programs selected using tournament 
	 * selection.
	 */
	@Override
	public List<CandidateProgram<TYPE>> getPool(List<CandidateProgram<TYPE>> pop, int poolSize) {
		// If pouleSize is 0 or less then we use the whole population.
		if (poolSize <= 0) {
			return pop;
		}
		
		List<CandidateProgram<TYPE>> pool = new ArrayList<CandidateProgram<TYPE>>(poolSize);
		
		ProgramSelector<TYPE> programSelector = new TournamentSelector<TYPE>(model, tournamentSize);
		programSelector.onGenerationStart(pop);
		
		for (int i=0; i<poolSize; i++) {
			pool.add(programSelector.getProgram());
		}
		
		return pool;
	}
}
