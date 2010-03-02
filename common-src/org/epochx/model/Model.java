package org.epochx.model;

import org.epochx.life.*;
import org.epochx.op.*;
import org.epochx.representation.*;
import org.epochx.stats.StatsEngine;
import org.epochx.tools.random.*;


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
	
	public LifeCycleListener getLifeCycleListener();
	
	public StatsEngine getStatsEngine();
	
	public boolean cacheFitness();
}
