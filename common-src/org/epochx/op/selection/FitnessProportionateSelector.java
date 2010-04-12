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

import java.util.List;

import org.epochx.core.Model;
import org.epochx.life.ConfigAdapter;
import org.epochx.op.ProgramSelector;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * 
 */
public class FitnessProportionateSelector implements ProgramSelector {

	// The controlling model.
	private final Model model;
	
	private List<CandidateProgram> pool;
	
	// Normalised fitnesses.
	private double[] fitnesses;
	
	// Random number generator.
	private RandomNumberGenerator rng;
	
	public FitnessProportionateSelector(final Model model) {
		this.model = model;
		
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
	 * Sets the population from which programs will be selected.
	 * 
	 * @param pool the population of candidate programs from which programs 
	 * 			  should be selected.
	 */
	@Override
	public void setSelectionPool(List<CandidateProgram> pool) {
		this.pool = pool;
		
		fitnesses = new double[pool.size()];
		
		double maxFitness = Double.NEGATIVE_INFINITY;
		
		// Find the max fitness.
		for (int i=0; i<fitnesses.length; i++) {
			fitnesses[i] = pool.get(i).getFitness();
			if (fitnesses[i] > maxFitness) {
				maxFitness = fitnesses[i];
			}
		}
		
		// Invert and sum all the fitnesses.
		double totalFitness = 0;
		for (int i=0; i<pool.size(); i++) {
			fitnesses[i] = maxFitness - fitnesses[i];
			totalFitness += fitnesses[i];
		}
		
		// Calculate normalised fitnesses.
		for (int i=0; i<fitnesses.length; i++) {
			fitnesses[i] = fitnesses[i] / totalFitness;
		}
	}
	
	/**
	 * 
	 */
	@Override
	public CandidateProgram getProgram() {
		double ran = rng.nextDouble();
		
		assert (ran >= 0.0 && ran <= 1.0);
		
		double sum = 0.0;
		for (int i=0; i<fitnesses.length; i++) {
			sum += fitnesses[i];
			if (ran <= sum) {
				return pool.get(i);
			}
		}
		
		// This shouldn't ever happen assuming the fitnesses add up to 1.
		//TODO This will happen occasionally because of rounding errors.
		assert false;
		
		return null;
	}

}
