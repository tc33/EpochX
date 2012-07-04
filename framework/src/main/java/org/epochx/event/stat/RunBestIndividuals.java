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
import org.epochx.event.RunEvent.StartRun;

/**
 * 
 */
public class RunBestIndividuals extends AbstractStat<StartRun> {

	private List<Individual> best;

	public RunBestIndividuals() {
		super(RunBestIndividualsGeneration.class);
	}

	@Override
	public void refresh(StartRun event) {
		best = new ArrayList<Individual>();
	}
	
	private void addGeneration(Individual[] generationBest) {
		int comparison = generationBest[0].compareTo(best.get(0));
		
		if (comparison > 0) {
			best.clear();
		}		
		if (best.isEmpty() || comparison >= 0) {
			best.addAll(Arrays.asList(generationBest));
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

	private class RunBestIndividualsGeneration extends AbstractStat<EndGeneration> {

		@SuppressWarnings("unused")
		public RunBestIndividualsGeneration() {
			super(GenerationBestIndividuals.class);
		}
		
		@Override
		public void refresh(EndGeneration event) {
			addGeneration(AbstractStat.get(GenerationBestIndividuals.class).getAllBest());
		}
	}
}