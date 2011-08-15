/* 
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.fitness;

import org.epochx.Individual;


/**
 * Calculates and returns the adjusted fitness of this program. A program's
 * adjusted fitness lies between 0 and 1, with a larger value for better
 * individuals. The fitness value returned from the <code>getFitness</code>
 * method is used as the standardised fitness in the calculation, so it is
 * assumed that the lowest possible value returned from that method is 0.0.
 * 
 * <p>
 * The adjusted fitness is calculated using the formula:
 * 
 * <code><blockquote>
 * adjusted-fitness = 1 / (1 + standardised-fitness)
 * </blockquote></code>
 */
public class AdjustedFitnessEvaluator<T extends Individual> implements FitnessEvaluator<T> {

	private FitnessEvaluator<T> delegate;
	
	public AdjustedFitnessEvaluator(FitnessEvaluator<T> delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public double[] getFitness(T[] pop) {
		final double[] standardised = delegate.getFitness(pop);
		final double[] adjusted = new double[pop.length];
		
		for (int i=0; i<pop.length; i++) {
			adjusted[i] = getAdjustedFitness(standardised[i]);
		}
		
		return adjusted;
	}
	
	@Override
	public double getFitness(T program) {
		final double standardised = delegate.getFitness(program);

		return getAdjustedFitness(standardised);
	}
	
	public double getAdjustedFitness(double standardised) {
		final double adjusted = 1.0 / (1.0 + standardised);

		return adjusted;
	}
}
