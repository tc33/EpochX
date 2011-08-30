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

import java.util.Comparator;

import org.epochx.Individual;


/**
 * 
 */
public class FitnessComparator<T extends Individual> implements Comparator<T> {

	private FitnessEvaluator<T> evaluator;
	
	private boolean reverse;
	
	public FitnessComparator(FitnessEvaluator<T> evaluator) {
		this(evaluator, false);
	}
	
	public FitnessComparator(FitnessEvaluator<T> evaluator, boolean reverse) {
		this.evaluator = evaluator;
		this.reverse = reverse;
	}

	@Override
	public int compare(T o1, T o2) {
		double fitness1 = evaluator.getFitness(o1);
		double fitness2 = evaluator.getFitness(o2);
		
		if (fitness1 > fitness2) {
			return reverse ? 1 : -1;
		} else if (fitness1 == fitness2) {
			return 0;
		} else {
			return reverse ? -1 : 1;
		}
	}
}
