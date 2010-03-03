package org.epochx.model;

import org.epochx.life.LifeCycleListener;
import org.epochx.op.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.CommonStatsEngine;
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
	
	public LifeCycleListener getLifeCycleListener();
	
	public CommonStatsEngine getStatsEngine();
	
	public boolean cacheFitness();
}
