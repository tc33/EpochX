/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.op.selection;

import java.util.*;

import org.epochx.core.GPModel;
import org.epochx.representation.*;


/**
 * Linear rank selection chooses programs by fitness rank. All the programs in 
 * the population are ranked according to their fitness from lowest to highest.
 * Each program is then assigned a probability according to their rank in a 
 * linear fashion with a gradient as given at construction.
 */
public class LinearRankSelector<TYPE> implements GPProgramSelector<TYPE>, GPPoolSelector<TYPE> {
	
	// The current controlling model.
	private GPModel<TYPE> model;
	
	// The current population from which programs should be chosen.
	private List<GPCandidateProgram<TYPE>> pop;
	
	// An array of size pop.size() giving probabilities for each program.
	private double probabilities[];
	private double nPlus;
	private double nMinus;
	
	// The gradient of the linear probabilities.
	private double gradient;
	
	public LinearRankSelector(GPModel<TYPE> model, double gradient) {
		this.model = model;
		this.gradient = gradient;
		
		nMinus = 2/(gradient+1);
		nPlus = (2*gradient)/(gradient+1);
	}
	
	/**
	 * Sets the population from which programs will be selected. The 
	 * probabilities are calculated once at this point based upon the linear 
	 * fitness rank.
	 * 
	 * @param pop the population of candidate programs from which programs 
	 * 			  should be selected.
	 */
	@Override
	public void setSelectionPool(List<GPCandidateProgram<TYPE>> pop) {
		Collections.sort(pop);
		this.pop = pop;
		
		int popSize = pop.size();
		probabilities = new double[popSize];
		double total = 0;
		for (int i=1; i<=popSize; i++) {
			double N = (double) popSize;
			
			double p = (1/N) * (nMinus + ((nPlus - nMinus) * ((i-1)/(N-1))));
			total += p;
			probabilities[i-1] = total;
		}
	}

	/**
	 * Selects a candidate program from the population using the probabilities 
	 * which were assigned based on fitness rank.
	 * 
	 * @return a program selected from the current population based on fitness 
	 * rank.
	 */
	@Override
	public GPCandidateProgram<TYPE> getProgram() {
		double ran = model.getRNG().nextDouble();
		
		for (int i=0; i<probabilities.length; i++) {
			if (ran < probabilities[i]) {
				return pop.get(i);
			}
		}
		
		// This shouldn't ever happen assuming the probabilities add up to 1 and Math.ran is never >1.
		return null;
	}
	
	/**
	 * Constructs a pool of programs from the population, choosing each one 
	 * with the program selection element of LinearRankSelector. The size of 
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
	 * @return the pool of candidate programs selected according to fitness 
	 * rank.
	 */
	@Override
	public List<GPCandidateProgram<TYPE>> getPool(
			List<GPCandidateProgram<TYPE>> pop, int poolSize) {
		// If poolSize is 0 or less then we use the whole population.
		if (poolSize <= 0) {
			return pop;
		}
		
		GPProgramSelector<TYPE> programSelector = new LinearRankSelector<TYPE>(model, gradient);
		programSelector.setSelectionPool(pop);
		List<GPCandidateProgram<TYPE>> pool = new ArrayList<GPCandidateProgram<TYPE>>();
		
		for (int i=0; i<poolSize; i++) {
			pool.add(programSelector.getProgram());
		}
		
		return pool;
	}
}
