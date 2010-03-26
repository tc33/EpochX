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
package org.epochx.op.selection;

import java.util.*;

import org.epochx.core.Model;
import org.epochx.op.*;
import org.epochx.representation.CandidateProgram;


/**
 * Tournament selection provides both program and pool selection. In tournament 
 * selection, x programs are randomly selected from the population to enter a 
 * 'tournament'. The program with the best fitness in the tournament then 
 * becomes the selected program. The tournament size, x, is given as an argument 
 * to the constructor.
 */
public class TournamentSelector implements ProgramSelector, PoolSelector {

	// The controlling model.
	private Model model;
	
	// The size of the tournment from which the best program will be taken.
	private int tournamentSize;

	private ProgramTournamentSelector poolSelection;
	
	private ProgramTournamentSelector programSelection;
	
	/**
	 * Construct a tournament selector with the specified tournament size.
	 * 
	 * @param tournamentSize the number of programs in each tournament.
	 */
	public TournamentSelector(Model model, int tournamentSize) {
		this.model = model;
		this.tournamentSize = tournamentSize;
		
		poolSelection = new ProgramTournamentSelector();
		programSelection = new ProgramTournamentSelector();
	}
	
	/**
	 * Store the population for creating tournaments from.
	 */
	@Override
	public void setSelectionPool(List<CandidateProgram> pop) {
		// We'll be using a random selector to construct a tournament.
		programSelection.setSelectionPool(pop);
	}
	
	/**
	 * Randomly creates a tournament, then selects the candidate program with 
	 * the best fitness from that tournament. The size of the tournament is 
	 * given at instantiation.
	 * 
	 * @return the best program from a randomly generated tournament.
	 */
	@Override
	public CandidateProgram getProgram() {
		return programSelection.getProgram();
	}
	
	/**
	 * Returns the number of programs that compete in each tournament selection.
	 * 
	 * @return the number of programs that a tournament consists of.
	 */
	public int getTournamentSize() {
		return tournamentSize;
	}
	
	/**
	 * Sets the number of programs that should compete in each tournament.
	 * 
	 * @param tournamentSize the number of programs to use in each tournament.
	 */
	public void setTournamentSize(int tournamentSize) {
		this.tournamentSize = tournamentSize;
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
	public List<CandidateProgram> getPool(List<CandidateProgram> pop, int poolSize) {
		// If poolSize is 0 or less then we use the whole population.
		if (poolSize <= 0) {
			return pop;
		}
		
		List<CandidateProgram> pool = new ArrayList<CandidateProgram>(poolSize);
		
		poolSelection.setSelectionPool(pop);
		
		for (int i=0; i<poolSize; i++) {
			pool.add(poolSelection.getProgram());
		}
		
		return pool;
	}
	
	/*
	 * This is a little strange, but we use an inner class here so we can 
	 * create 2 separate instances of it internally for the 2 tasks of pool
	 * selection and program selection which is necessary because they both 
	 * select from different pools. The original implementation of getPool 
	 * created an internal instance of TournamentSelector but it is not 
	 * advisable to create components between model configurations.
	 */
	private class ProgramTournamentSelector implements ProgramSelector {

		// We use a random selector to construct tournaments.
		private RandomSelector randomSelector;
		
		public ProgramTournamentSelector() {
			randomSelector = new RandomSelector(model);
		}
		
		@Override
		public void setSelectionPool(List<CandidateProgram> pop) {
			// We'll be using a random selector to construct a tournament.
			randomSelector.setSelectionPool(pop);
		}

		@Override
		public CandidateProgram getProgram() {
			// Use random selector to create tournament.
			CandidateProgram[] tournament = new CandidateProgram[tournamentSize];
			for (int i=0; i<tournamentSize; i++) {
				tournament[i] = randomSelector.getProgram();
			}
			
			// Check the fitness of each program, stashing the best.
			double bestFitness = Double.POSITIVE_INFINITY;
			CandidateProgram bestProgram = null;
			for (CandidateProgram p: tournament) {
				double fitness = p.getFitness();
				if (fitness < bestFitness) {
					bestFitness = fitness;
					bestProgram = p;
				}
			}
					
			return bestProgram;
		}
	}
}
