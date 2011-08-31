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

package org.epochx.event.stat;

import java.util.*;

import org.epochx.*;
import org.epochx.event.GenerationEvent.EndGeneration;

/**
 * 
 */
public class GenerationBestIndividuals extends AbstractStat<EndGeneration> {

	private List<Individual> best;

	public GenerationBestIndividuals() {
		super(NO_DEPENDENCIES);
	}

	@Override
	public void onEvent(EndGeneration event) {
		Population pop = event.getPopulation();
		
		best = new ArrayList<Individual>();
		
		for (Individual individual: pop) {
			int comparison = individual.compareTo(best.get(0));
			
			if (comparison > 0) {
				best.clear();
			}
			if (best.isEmpty() || comparison >= 0) {
				best.add(individual);
			}
		}
	}

	public Individual[] getAllBest() {
		return best.toArray(new Individual[best.size()]);
	}
	
	/**
	 * Returns an arbitrary best individual.
	 */
	public Individual getBest() {
		return (best == null || best.isEmpty()) ? null : best.get(0);
	}

	/**
	 * Returns the string representation of an arbitrary best individual.
	 */
	@Override
	public String toString() {
		return getBest().toString();
	}

}