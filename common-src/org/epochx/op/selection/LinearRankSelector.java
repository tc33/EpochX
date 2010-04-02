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
import org.epochx.life.*;
import org.epochx.op.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Linear rank selection chooses programs by fitness rank. All the programs in 
 * the population are ranked according to their fitness from lowest to highest.
 * Each program is then assigned a probability according to their rank in a 
 * linear fashion with a gradient as given at construction. Programs are 
 * selected according to this probability.
 */
public class LinearRankSelector implements ProgramSelector, PoolSelector {
	
	// The controlling model.
	private final Model model;
	
	// Random number generator.
	private RandomNumberGenerator rng;
	
	// An array of size pool.size() giving probabilities for each program.
	private double probabilities[];
	private double nPlus;
	private double nMinus;
	
	// Internal program selectors used by the 2 different tasks.
	private ProgramLinearRankSelector programSelection;
	private ProgramLinearRankSelector poolSelection;
	
	/**
	 * Constructs an instance of <code>LinearRankSelector</code>.
	 * 
	 * @param model
	 * @param gradient
	 */
	public LinearRankSelector(final Model model, final double gradient) {
		this.model = model;
		setGradient(gradient);
		
		// Configure parameters from the model.
		model.getLifeCycleManager().addConfigListener(new ConfigAdapter() {
			@Override
			public void onConfigure() {
				configure();
			}
		});
	}
	
	/*
	 * Configures component with parameters from the model.
	 */
	private void configure() {
		rng = model.getRNG();
	}
	
	/**
	 * Sets the gradient of the linear probabilities.
	 * 
	 * @param gradient the gradient to use when assigning probabilities.
	 */
	public void setGradient(double gradient) {
		nMinus = 2/(gradient+1);
		nPlus = (2*gradient)/(gradient+1);
	}
	
	/**
	 * Sets the population from which programs will be selected. The 
	 * probabilities are calculated once at this point based upon the linear 
	 * fitness rank.
	 * 
	 * @param pool the population of candidate programs from which programs 
	 * 			  should be selected.
	 */
	@Override
	public void setSelectionPool(List<CandidateProgram> pool) {
		programSelection.setSelectionPool(pool);
	}

	/**
	 * Selects a candidate program from the population using the probabilities 
	 * which were assigned based on fitness rank.
	 * 
	 * @return a program selected from the current population based on fitness 
	 * rank.
	 */
	@Override
	public CandidateProgram getProgram() {
		return programSelection.getProgram();
	}
	
	/**
	 * Constructs a pool of programs from the population, choosing each one 
	 * with the program selection element of LinearRankSelector. The size of 
	 * the pool created will be equal to the poolSize argument. The generated 
	 * pool may contain duplicate programs, and as such the pool size may be 
	 * greater than the population size.
	 * 
	 * @param pool the population of CandidatePrograms from which the programs 
	 * 			  in the pool should be chosen.
	 * @param poolSize the number of programs that should be selected from the 
	 * 			 	   population to form the pool. The poolSize must be 1 or 
	 * 				   greater.
	 * @return the pool of candidate programs selected according to fitness 
	 * rank.
	 */
	@Override
	public List<CandidateProgram> getPool(
			List<CandidateProgram> pop, int poolSize) {
		if (poolSize < 1) {
			throw new IllegalArgumentException("poolSize must be greater than 0");
		}
		
		poolSelection.setSelectionPool(pop);
		List<CandidateProgram> pool = new ArrayList<CandidateProgram>();
		
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
	private class ProgramLinearRankSelector implements ProgramSelector {
		
		// The current population from which programs should be chosen.
		private List<CandidateProgram> pool;
		
		@Override
		public void setSelectionPool(List<CandidateProgram> pool) {
			if (pool == null || pool.size() < 1) {
				throw new IllegalArgumentException("selection pool cannot be " +
						"null and must contain 1 or more CandidatePrograms");
			}
			
			// Sort the pool of programs.
			this.pool = pool;
			Collections.sort(pool);
			
			// Create array of probabilities.
			int popSize = pool.size();
			probabilities = new double[popSize];
			double total = 0;
			
			for (int i=1; i<=popSize; i++) {
				double p = (1/popSize) * (nMinus + ((nPlus - nMinus) * ((i-1)/(popSize-1))));
				
				total += p;
				probabilities[i-1] = total;
			}
			
			// This probably won't be true how things are because of rounding.
			//TODO We can overcome this problem (IF it is a problem) by assigning the last element the probability of 1.0.
			assert (total == 1.0);
		}

		/**
		 * Selects a candidate program from the population using the probabilities 
		 * which were assigned based on fitness rank.
		 * 
		 * @return a program selected from the current population based on fitness 
		 * rank.
		 */
		@Override
		public CandidateProgram getProgram() {
			double ran = rng.nextDouble();
			
			assert (ran >= 0.0 && ran <= 1.0);
			
			for (int i=0; i<probabilities.length; i++) {
				if (ran < probabilities[i]) {
					return pool.get(i);
				}
			}
			
			// This shouldn't ever happen assuming the probabilities add up to 1.
			assert false;
			
			return null;
		}
	}
}
