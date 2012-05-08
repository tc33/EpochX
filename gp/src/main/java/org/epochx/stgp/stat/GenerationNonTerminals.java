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
 * The latest version is available from: http:/www.epochx.org
 */

package org.epochx.stgp.stat;

import java.util.Arrays;

import org.epochx.Individual;
import org.epochx.Population;
import org.epochx.event.GenerationEvent.EndGeneration;
import org.epochx.event.stat.AbstractStat;
import org.epochx.stgp.STGPIndividual;

/**
 * A stat that returns the number of non-terminals in all program trees in the 
 * population from the previous generation. All individuals in the population 
 * must be instances of <tt>STGPIndividual</tt>.
 */
public class GenerationNonTerminals extends AbstractStat<EndGeneration> {

	private int[] nonTerminals;

	/**
	 * Constructs a <tt>GenerationNonTerminals</tt> stat and registers
	 * its dependencies
	 */
	public GenerationNonTerminals() {
		super(NO_DEPENDENCIES);
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
	public void onEvent(EndGeneration event) {
		Population population = event.getPopulation();
		nonTerminals = new int[population.size()];
		int index = 0;

		for (Individual individual: population) {
			if (individual instanceof STGPIndividual) {
				nonTerminals[index++] = ((STGPIndividual) individual).getRoot().getNoFunctions();
			}
		}
	}
	
	/**
	 * Returns an array containing the number of non-terminal nodes in each 
	 * program tree in the population
	 * 
	 * @return the number of non-terminal nodes in each program tree in the 
	 * previous generation
	 */
	public int[] getNonTerminals() {
		return nonTerminals;
	}

	/**
	 * Returns a string representation of the value of this stat
	 * 
	 * @return a <tt>String</tt> that represents the value of this stat
	 */
	@Override
	public String toString() {
		return Arrays.toString(nonTerminals);
	}
}