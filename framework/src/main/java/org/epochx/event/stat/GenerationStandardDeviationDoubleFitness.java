/*
 * Copyright 2007-2011
 * Lawrence Beadle, Tom Castle and Fernando Otero
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
 * The latest version is available from: http://www.epochx.org
 */

package org.epochx.event.stat;

import org.epochx.*;
import org.epochx.event.GenerationEvent.EndGeneration;

public class GenerationStandardDeviationDoubleFitness extends AbstractStat<EndGeneration> {

	private double stdev;

	@SuppressWarnings("unchecked")
	public GenerationStandardDeviationDoubleFitness() {
		super(GenerationFitnesses.class, GenerationAverageDoubleFitness.class);
	}

	@Override
	public void refresh(EndGeneration event) {
		Fitness[] fitnesses = AbstractStat.get(GenerationFitnesses.class).getFitnesses();
		double average = AbstractStat.get(GenerationAverageDoubleFitness.class).getAverage();
		
		// Sum the squared differences.
		double sqDiff = 0;
		for (int i = 0; i < fitnesses.length; i++) {
			sqDiff += Math.pow(((DoubleFitness) fitnesses[i]).getValue() - average, 2);
		}

		// Take the square root of the average.
		stdev = Math.sqrt(sqDiff / fitnesses.length);
	}

	public double getStandardDeviation() {
		return stdev;
	}

	@Override
	public String toString() {
		return Double.toString(stdev);
	}
}
