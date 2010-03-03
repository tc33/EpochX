package org.epochx.model;

import java.util.List;

import org.epochx.life.LifeCycleListener;
import org.epochx.op.*;
import org.epochx.op.selection.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.tools.random.*;


public abstract class AbstractModel implements Model, LifeCycleListener {

	// Run parameters.
	private int noRuns;
	private int noGenerations;
	private int populationSize;
	private int poolSize;
	private int noElites;

	private double terminationFitness;
	private double crossoverProbability;
	private double mutationProbability;
	
	private PoolSelector poolSelector;
	private ProgramSelector programSelector;
	private RandomNumberGenerator randomNumberGenerator;
	
	// Life cycle listeners.
	private LifeCycleListener lifeCycleListener;
	
	// Caching.
	private boolean cacheFitness;
	
	// Stats.
	private StatsEngine statsEngine;
	
	public AbstractModel() {
		noRuns = 1;
		noGenerations = 50;
		populationSize = 500;
		poolSize = 50;
		noElites = 10;
		terminationFitness = 0.0;
		crossoverProbability = 0.9;
		mutationProbability = 0.1;
		
		programSelector = new RandomSelector(this);
		poolSelector = new TournamentSelector(this, 3);
		randomNumberGenerator = new MersenneTwisterFast();
		
		// Life cycle listener.
		lifeCycleListener = this;
		
		// Caching.
		cacheFitness = true;
		
		// Stats.
		statsEngine = new CommonStatsEngine();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to true in AbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean cacheFitness() {
		return cacheFitness;
	}
	
	/**
	 * Overwrites the default setting of whether to cache the fitness values.
	 * 
	 * @param cacheFitness whether fitnesses should be cached or not.
	 */
	public void setCacheFitness(boolean cacheFitness) {
		this.cacheFitness = cacheFitness;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 1 in AbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getNoRuns() {
		return noRuns;
	}

	/**
	 * Overwrites the default number of runs.
	 * 
	 * @param noRuns the new number of runs to execute with this model.
	 */
	public void setNoRuns(int noRuns) {
		this.noRuns = noRuns;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 50 in AbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getNoGenerations() {
		return noGenerations;
	}

	/**
	 * Overwrites the default number of generations.
	 * 
	 * @param noGenerations the new number of generations to use within a run.
	 */
	public void setNoGenerations(int noGenerations) {
		this.noGenerations = noGenerations;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 500 in AbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getPopulationSize() {
		return populationSize;
	}

	/**
	 * Overwrites the default population size of CandidatePrograms.
	 * 
	 * @param populationSize the new number of CandidatePrograms each generation 
	 * 						 should contain.
	 */
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 50 in AbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getPoolSize() {
		return poolSize;
	}

	/**
	 * Overwrites the default pool size value.
	 * 
	 * @param poolSize the new size of the mating pool to use.
	 */
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 10 in AbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getNoElites() {
		return noElites;
	}

	/**
	 * Overwrites the default number of elites to copy from one generation to
	 * the next.
	 * 
	 * @param noElites the new number of elites to copy across from one 
	 * 				   population to the next.
	 */
	public void setNoElites(int noElites) {
		this.noElites = noElites;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 0.9 in AbstractModel to represent a 90% chance.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getCrossoverProbability() {
		return crossoverProbability;
	}

	/**
	 * Overwrites the default Crossover probability.
	 * 
	 * @param crossoverProbability the new Crossover probability to use.
	 */
	public void setCrossoverProbability(double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 0.0 in AbstractModel to represent a 0% chance.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getMutationProbability() {
		return mutationProbability;
	}

	/**
	 * Overwrites the default mutation probability.
	 * 
	 * @param mutationProbability the new mutation probability to use.
	 */
	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Automatically calculates the reproduction probability based upon the 
	 * Crossover and mutation probabilities as all three together must add up 
	 * to 100%.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getReproductionProbability() {
		return 1.0 - (getCrossoverProbability() + getMutationProbability());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 0.0 in AbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getTerminationFitness() {
		return terminationFitness;
	}
	
	/**
	 * Overwrites the default fitness for run termination.
	 * 
	 * @param terminationFitness the new fitness below which a run will be 
	 * 							 terminated.
	 */
	public void setTerminationFitness(double terminationFitness) {
		this.terminationFitness = terminationFitness;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link RandomSelector} in AbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public ProgramSelector getProgramSelector() {
		return programSelector;
	}

	/**
	 * Overwrites the default parent selector used to select parents to undergo
	 * a genetic operator from either a pool or the previous population.
	 * 
	 * @param ProgramSelector the new ProgramSelector to be used when selecting 
	 * 						 parents for a genetic operator.
	 */
	public void setProgramSelector(ProgramSelector programSelector) {
		this.programSelector = programSelector;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link TournamentSelector} with a tournament size of 3 
	 * in AbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public PoolSelector getPoolSelector() {
		return poolSelector;
	}

	/**
	 * Overwrites the default pool selector used to generate a mating pool.
	 * 
	 * @param PoolSelector the new PoolSelector to be used when building a 
	 * 						breeding pool.
	 */
	public void setPoolSelector(PoolSelector poolSelector) {
		this.poolSelector = poolSelector;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to MersenneTwisterFast in AbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public RandomNumberGenerator getRNG() {
		return randomNumberGenerator;
	}
	
	/**
	 * Overwrites the default random number generator used to generate random 
	 * numbers to control behaviour throughout a run.
	 * 
	 * @param rng the random number generator to be used any time random 
	 * 				behaviour is required.
	 */
	public void setRNG(RandomNumberGenerator rng) {
		this.randomNumberGenerator = rng;
	}
	
	@Override
	public StatsEngine getStatsEngine() {
		return statsEngine;
	}
	
	public void setStatsEngine(StatsEngine statsEngine) {
		this.statsEngine = statsEngine;
	}
	
	/**
	 * Default implementation returns this model as the life cycle listener. By
	 * default all the listener methods will confirm the events though. 
	 * Override the individual life cycle methods to add extra functionality in 
	 * the model, or override this method to return a different life cycle 
	 * listener.
	 * 
	 * @return the LifeCycleListener to inform of all events during the  life 
	 * cycle.
	 */
	@Override
	public LifeCycleListener getLifeCycleListener() {
		return lifeCycleListener;
	}
	
	/**
	 * Overwrites the default life cycle listener.
	 * 
	 * @param lifeCycleListener the object that should be informed about life 
	 * cycle events as they happen.
	 */
	public void setLifeCycleListener(LifeCycleListener lifeCycleListener) {
		this.lifeCycleListener = lifeCycleListener;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Default implementation confirms the initialised population by 
	 * returning the given population argument.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public List<CandidateProgram> onInitialisation(List<CandidateProgram> pop) {
		return pop;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Default implementation confirms the selected elites by returning the
	 * given list of elites.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public List<CandidateProgram> onElitism(List<CandidateProgram> elites) {
		return elites;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Default implementation confirms the selected breeding pool by 
	 * returning the given list of programs.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public List<CandidateProgram> onPoolSelection(List<CandidateProgram> pool) {
		return pool;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Default implementation confirms the Crossover operation by returning
	 * the given array of children.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public CandidateProgram[] onCrossover(CandidateProgram[] parents,
			CandidateProgram[] children) {
		return children;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Default implementation confirms the mutation operation by returning 
	 * the given child program.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public CandidateProgram onMutation(CandidateProgram parent,
			CandidateProgram child) {
		return child;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Default implementation confirms the reproduction operation by 
	 * returning the given selected program.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public CandidateProgram onReproduction(CandidateProgram child) {
		return child;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Default implementation does nothing.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public void onGenerationStart() {}
	
	@Override
	public void onGenerationEnd() {}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Default implementation does nothing.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public void onFitnessTermination() {}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Default implementation does nothing.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public void onGenerationTermination() {}
}
