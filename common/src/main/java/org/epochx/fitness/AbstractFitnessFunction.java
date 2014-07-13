/* 
 * Copyright 2007-2013
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
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

import org.epochx.AbstractIndividual;
import org.epochx.Fitness;
import org.epochx.FitnessFunction;
import org.epochx.Individual;
import org.epochx.Population;

/**
 * 
 */
public abstract class AbstractFitnessFunction implements FitnessFunction {

	/** 
	 * 
	 */
	@Override
	public void evaluate(Population population) {
		for (Individual individual: population) {
			Fitness fitness = evaluate(individual);
			assignFitness(fitness, individual);
		}
	}
	
	public abstract Fitness evaluate(Individual individual);
	
	/**
	 * 
	 * @param fitness
	 * @param individual
	 */
	protected void assignFitness(Fitness fitness, Individual individual) {
		if (individual instanceof AbstractIndividual) {
			AbstractIndividual abstractIndividual = (AbstractIndividual) individual;
			abstractIndividual.setFitness(fitness);
		}
	}
	
}