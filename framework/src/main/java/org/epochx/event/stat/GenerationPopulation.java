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

import org.epochx.Population;
import org.epochx.event.GenerationEvent.EndGeneration;

/**
 * 
 */
public class GenerationPopulation extends AbstractStat<EndGeneration> {

	private Population population;

	public GenerationPopulation() {
		super(NO_DEPENDENCIES);
	}

	@Override
	public void refresh(EndGeneration event) {
		population = event.getPopulation();
	}

	public Population getPopulation() {
		return population;
	}

	@Override
	public String toString() {
		return population.toString();
	}

	public class Sorted extends GenerationFitnesses {

		@Override
		public void refresh(EndGeneration event) {
			super.refresh(event);

			// This will sort the population itself so may influence further
			// execution.
			population.sort();
		}
	}

}