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

package org.epochx.gp.stat;

import java.util.Arrays;

import org.epochx.*;
import org.epochx.event.GenerationEvent.EndGeneration;
import org.epochx.event.stat.AbstractStat;
import org.epochx.gp.STGPIndividual;

/**
 * 
 */
public class GenerationAverageNodesPerDepth extends AbstractStat<EndGeneration> {

	private double[] averages;

	public GenerationAverageNodesPerDepth() {
		super(GenerationAverageDepth.class);
	}

	@Override
	public void onEvent(EndGeneration event) {
		int maxDepth = AbstractStat.get(GenerationMaximumDepth.class).getMaximum();
		Population population = event.getPopulation();
		
		averages = new double[maxDepth];

		// For each depth calculate an average
		for (int d = 0; d < maxDepth; d++) {
			// Get number of nodes for each program.
			int noNodes = 0;
			for (Individual individual: population) {
				if (individual instanceof STGPIndividual) {
					noNodes += ((STGPIndividual) individual).getRoot().getNodesAtDepth(d).size();
				}
			}
			averages[d] = noNodes / (double) population.size();
		}
	}
	
	public double[] getAverageNodesPerDepth() {
		return averages;
	}

	@Override
	public String toString() {
		return Arrays.toString(averages);
	}
}