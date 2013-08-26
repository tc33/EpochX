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
 * The latest version is available from: http://www.epochx.org
 */

package org.epochx.stgp.stat;

import org.epochx.*;
import org.epochx.event.GenerationEvent.EndGeneration;
import org.epochx.event.stat.AbstractStat;

/**
 * A stat that returns the standard error of the mean number of non-terminals, 
 * as calculated by the {@link GenerationAverageNonTerminals} stat. All 
 * individuals in the population must be instances of <tt>STGPIndividual</tt>.
 * 
 * @see GenerationAverageNonTerminals
 * @see GenerationStandardDeviationNonTerminals
 */
public class GenerationAverageNonTerminalsError extends AbstractStat<EndGeneration> {

	private double error;

	/**
	 * Constructs a <tt>GenerationAverageNonTerminalsError</tt> stat and 
	 * registers its dependencies
	 */
	public GenerationAverageNonTerminalsError() {
		super(GenerationStandardDeviationNonTerminals.class);
	}

	/**
	 * Triggers the generation of an updated value for this stat. Once this stat
	 * has been registered, this method will be called on each
	 * <tt>EndGeneration</tt> event.
	 * 
	 * @param event an object that encapsulates information about the event that
	 *        occurred
	 */
	@Override
	public void refresh(EndGeneration event) {
		double stdev = AbstractStat.get(GenerationStandardDeviationNonTerminals.class).getStandardDeviation();
		Population population = event.getPopulation();
		
		error = stdev / Math.sqrt(population.size());
	}

	/**
	 * Returns the standard error of the mean number of non-terminal nodes in 
	 * the program trees in the previous generation
	 * 
	 * @return the error of the mean number of non-terminals in the program 
	 * trees
	 */
	public double getError() {
		return error;
	}

	/**
	 * Returns a string representation of the value of this stat
	 * 
	 * @return a <tt>String</tt> that represents the value of this stat
	 */
	@Override
	public String toString() {
		return Double.toString(error);
	}
}
