/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.model;

import org.epochx.op.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.StatsEngine;
import org.epochx.tools.random.RandomNumberGenerator;


public interface Model {

	public Initialiser getInitialiser();
	
	public Crossover getCrossover();
	
	public Mutation getMutation();
	
	public PoolSelector getPoolSelector();
	
	public ProgramSelector getProgramSelector();
	
	public double getFitness(CandidateProgram program);

	public double getTerminationFitness();
	
	public RandomNumberGenerator getRNG();
	
	public double getMutationProbability();
	
	public double getCrossoverProbability();
	
	public double getReproductionProbability();
	
	public int getNoRuns();
	
	public int getNoGenerations();
	
	public int getPopulationSize();
	
	public int getNoElites();
	
	public int getPoolSize();
	
	public StatsEngine getStatsEngine();
	
	public boolean cacheFitness();
}
