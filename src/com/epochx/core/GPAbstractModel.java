/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.core;

import java.util.*;

import com.epochx.op.crossover.*;
import com.epochx.op.initialisation.*;
import com.epochx.op.mutation.*;
import com.epochx.op.selection.*;
import com.epochx.random.*;
import com.epochx.representation.*;
import com.epochx.stats.*;

/**
 * GPAbstractModel is a partial implementation of GPModel which provides 
 * sensible defaults for many of the necessary control parameters. It also 
 * provides a simple way of setting many values so an extending class isn't 
 * required to override all methods they wish to alter, and can instead use 
 * a simple setter method call. 
 * 
 * <p>Those methods that it isn't possible to provide a <em>sensible</em> 
 * default for, for example getFitness(CandidateProgram), getTerminals() and
 * getFunctions(), are not implemented to force the extending class to 
 * consider their implementation.
 * 
 * @see GPModel
 */
public abstract class GPAbstractModel<TYPE> implements GPModel<TYPE>, 
													   GenerationStatListener, 
													   RunStatListener, 
													   CrossoverStatListener, 
													   MutationStatListener,
													   LifeCycleListener<TYPE> {

	private Initialiser<TYPE> initialiser;
	private Crossover<TYPE> crossover;
	private Mutation<TYPE> mutator;
	private PoolSelector<TYPE> poolSelector;
	private ProgramSelector<TYPE> programSelector;
	private RandomNumberGenerator randomNumberGenerator;

	private int noRuns;
	private int noGenerations;
	private int populationSize;
	private int poolSize;
	private int noElites;
	private int maxInitialDepth;
	private int maxDepth;
	private double terminationFitness;
	
	private double crossoverProbability;
	private double mutationProbability;
	
	private MutationStatField[] mutationStatFields;
	private CrossoverStatField[] crossoverStatFields;
	private GenerationStatField[] generationStatFields;
	private RunStatField[] runStatFields;
	
	private RunStatListener runStatListener;
	private GenerationStatListener generationStatListener;
	private CrossoverStatListener crossoverStatListener;
	private MutationStatListener mutationStatListener;
	
	private LifeCycleListener<TYPE> lifeCycleListener;
	
	/**
	 * Construct a GPModel with a set of sensible defaults. See the appropriate
	 * accessor method for information of each default value.
	 */
	public GPAbstractModel() {
		// Set default parameter values.
		noRuns = 1;
		noGenerations = 50;
		populationSize = 500;
		maxInitialDepth = 6;
		maxDepth = 17;
		poolSize = 50;
		noElites = 10;
		terminationFitness = 0.0;
		crossoverProbability = 0.9;
		mutationProbability = 0;
		
		// Statistics fields.
		mutationStatFields = null;
		crossoverStatFields = null;
		generationStatFields = null;
		runStatFields = null;
		
		// Statistics listeners.
		runStatListener = this;
		generationStatListener = this;
		crossoverStatListener = this;
		mutationStatListener = this;
		
		// Life cycle listener.
		lifeCycleListener = this;
		
		// GP components.
		programSelector = new RandomSelector<TYPE>(this);
		poolSelector = new TournamentSelector<TYPE>(this, 3);
		initialiser = new FullInitialiser<TYPE>(this);
		crossover = new UniformPointCrossover<TYPE>(this);
		mutator = new SubtreeMutation<TYPE>(this);
		randomNumberGenerator = new MersenneTwisterFast();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to FullInitialiser in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public Initialiser<TYPE> getInitialiser() {
		return initialiser;
	}

	/**
	 * Overwrites the default initialiser.
	 * 
	 * @param initialiser the new Initialiser to use when generating the 
	 * 		 			  starting population.
	 */
	public void setInitialiser(Initialiser<TYPE> initialiser) {
		this.initialiser = initialiser;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link UniformPointCrossover} in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public Crossover<TYPE> getCrossover() {
		return crossover;
	}

	/**
	 * Overwrites the default crossover operator.
	 * 
	 * @param crossover the crossover to set
	 */
	public void setCrossover(Crossover<TYPE> crossover) {
		this.crossover = crossover;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link SubtreeMutation} in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public Mutation<TYPE> getMutator() {
		return mutator;
	}

	/**
	 * Overwrites the default mutator used to perform mutation.
	 * 
	 * @param mutator the mutator to set.
	 */
	public void setMutator(Mutation<TYPE> mutator) {
		this.mutator = mutator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link RandomSelector} in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public ProgramSelector<TYPE> getProgramSelector() {
		return programSelector;
	}

	/**
	 * Overwrites the default parent selector used to select parents to undergo
	 * a genetic operator from either a pool or the previous population.
	 * 
	 * @param programSelector the new ProgramSelector to be used when selecting 
	 * 						 parents for a genetic operator.
	 */
	public void setProgramSelector(ProgramSelector<TYPE> programSelector) {
		this.programSelector = programSelector;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link TournamentSelector} with a tournament size of 3 
	 * in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public PoolSelector<TYPE> getPoolSelector() {
		return poolSelector;
	}

	/**
	 * Overwrites the default pool selector used to generate a mating pool.
	 * 
	 * @param poolSelector the new PoolSelector to be used when building a 
	 * 						breeding pool.
	 */
	public void setPoolSelector(PoolSelector<TYPE> poolSelector) {
		this.poolSelector = poolSelector;
	}

	/**
	 * Returns the union of calls to getTerminals() and getFunctions.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public List<Node<TYPE>> getSyntax() {
		List<Node<TYPE>> syntax = new ArrayList<Node<TYPE>>(getTerminals());
		syntax.addAll(getFunctions());
		
		return syntax;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to JavaRandom in GEAbstractModel.
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
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 1 in GPAbstractModel.
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
	 * <p>Defaults to 50 in GPAbstractModel.
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
	 * <p>Defaults to 500 in GPAbstractModel.
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
	 * <p>Defaults to 50 in GPAbstractModel.
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
	 * <p>Defaults to 10 in GPAbstractModel.
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
	 * <p>Defaults to 6 in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getInitialMaxDepth() {
		return maxInitialDepth;
	}

	/**
	 * Overwrites the default max program tree depth allowed after 
	 * initialisation is performed.
	 * 
	 * @param maxInitialDepth the new max program tree depth to use.
	 */
	public void setInitialMaxDepth(int maxInitialDepth) {
		this.maxInitialDepth = maxInitialDepth;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 17 in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * Overwrites the default max program tree depth allowed after genetic 
	 * operators are performed.
	 * 
	 * @param maxDepth the new max program tree depth to use.
	 */
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 0.9 in GPAbstractModel to represent a 90% chance.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public double getCrossoverProbability() {
		return crossoverProbability;
	}

	/**
	 * Overwrites the default crossover probability.
	 * 
	 * @param crossoverProbability the new crossover probability to use.
	 */
	public void setCrossoverProbability(double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 0.0 in GPAbstractModel to represent a 0% chance.
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
	 * crossover and mutation probabilities as all three together must add up 
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
	 * <p>Defaults to 0.0 in GPAbstractModel.
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
	 * Default implementation which accepts all crossovers.
	 * 
	 * @param parents The programs that were crossed over to create the given 
	 * 				  children.
	 * @param children The children that resulted from the parents being 
	 * 				   crossed over.
	 * @return True if the crossover operation should proceed, false if it is 
	 * 		   rejected and should be retried with new parents.
	 */
	public boolean acceptCrossover(CandidateProgram<TYPE>[] parents, 
								   CandidateProgram<TYPE>[] children) {
		return true;
	}

	/**
	 * Default implementation which accepts all mutations.
	 * 
	 * @param parent The program before the mutation operation.
	 * @param child  The program after the mutation operation has been carried 
	 * 				 out.
	 * @return True if the mutation operation should proceed, false if it is 
	 * rejected and should be retried with a new parent.
	 */
	public boolean acceptMutation(CandidateProgram<TYPE> parent, 
								  CandidateProgram<TYPE> child) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to this model object.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public RunStatListener getRunStatListener() {
		return runStatListener;
	}
	
	/**
	 * Overwrites the default listener for run statistics.
	 * 
	 * @param runStatListener the run stat listener to set.
	 */
	public void setRunStatListener(RunStatListener runStatListener) {
		this.runStatListener = runStatListener;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to this model object.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public GenerationStatListener getGenerationStatListener() {
		return this.generationStatListener;
	}
	
	/**
	 * Overwrites the default listener for generation statistics.
	 * 
	 * @param generationStatListener the generation stat listener to set.
	 */
	public void setGenerationStatListener(GenerationStatListener generationStatListener) {
		this.generationStatListener = generationStatListener;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to this model object.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public CrossoverStatListener getCrossoverStatListener() {
		return this.crossoverStatListener;
	}
	
	/**
	 * Overwrites the default listener for crossover statistics.
	 * 
	 * @param crossoverStatListener the crossover stat listener to set.
	 */
	public void setCrossoverStatListener(CrossoverStatListener crossoverStatListener) {
		this.crossoverStatListener = crossoverStatListener;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to this model object.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public MutationStatListener getMutationStatListener() {
		return this.mutationStatListener;
	}

	/**
	 * Overwrites the default listener for mutation statistics.
	 * 
	 * @param mutationStatListener the mutation stat listener to set.
	 */
	public void setMutationStatListener(MutationStatListener mutationStatListener) {
		this.mutationStatListener = mutationStatListener;
	}
	
	/**
	 * Default implementation. No fields are requested, the overriding class 
	 * is expected to override this method or call the setter method IF it 
	 * wants to receive information about the runs. 
	 * 
	 * <p>Typically it is the model that receives the statistics but this can
	 * be overridden by returning a different RunStatListener in the 
	 * getRunStatListener() method. The runStats method of this object will 
	 * then be called at the end of each run with these requested statistics.
	 */
	public RunStatField[] getRunStatFields() {
		return runStatFields;
	}
	
	/**
	 * Set the run statistics that the given RunStatListener will receive at 
	 * the end of each run.
	 * 
	 * @param runStatFields an array of RunStatFields that indicate the 
	 * 						statistics fields that the RunStatListener will 
	 * 						receive. The fields will be delivered in the same 
	 * 						order as given here.
	 */
	public void setRunStatFields(RunStatField[] runStatFields) {
		this.runStatFields = runStatFields;
	}
	
	/**
	 * Default implementation. If any run stats fields have been requested then 
	 * they are printed to the console separated by tabs. In addition to the 
	 * statistics, the first column will contain the run number.
	 * 
	 * @param runNo the run number of the current run. Runs are indexed from
	 * 				zero (0).
	 * @param stats an array of the statistics that were requested, given in 
	 * 				the order that they were requested. The data type of each 
	 * 				of the stats fields varies according to the field and is 
	 * 				specified in the JavaDoc of the RunStatField enum.
	 */
	public void runStats(int runNo, Object[] stats) {
    	if (stats.length > 0) {
			System.out.print(runNo + "\t");
	    	for (Object o: stats) {
	    		System.out.print(o + "\t");
	    	}
	    	System.out.println();
    	}
	}
	
	/**
	 * Default implementation. No fields are requested, the overriding class 
	 * is expected to override this method or call the setter method IF it 
	 * wants to receive information about generations. 
	 * 
	 * <p>Typically it is the model that receives the statistics but this can
	 * be overridden by returning a different GenerationStatListener in the 
	 * getGenerationStatListener() method. The genenerationStats method of this 
	 * object will then be called at the end of each generation with these 
	 * requested statistics. 
	 */
	@Override
	public GenerationStatField[] getGenStatFields() {
		return generationStatFields;
	}
	
	/**
	 * Set the generation statistics that the given GenerationStatListener will
	 * receive at the end of each generation.
	 * 
	 * @param generationStatFields an array of GenerationStatFields that 
	 * 							   indicate the statistics fields that the 
	 * 							   GenerationStatListener will receive. The 
	 * 							   fields will be delivered in the same order 
	 * 							   as given here.
	 */
	public void setGenStatFields(GenerationStatField[] generationStatFields) {
		this.generationStatFields = generationStatFields;
	}
	
	/**
	 * Default implementation. If any generation stats fields have been 
	 * requested then they are printed to the console separated by tabs. In 
	 * addition to the statistics, the first column will contain the 
	 * generation number.
	 * 
	 * @param generation the generation number of the current run. The first 
	 * 					 generation in a run will be zero (0), which references 
	 * 					 the population immediately after initialisation.
	 * @param stats an array of the statistics that were requested, given in 
	 * 				the order that they were requested. The data type of each 
	 * 				of the stats fields varies according to the field and is 
	 * 				specified in the JavaDoc of the GenerationStatField enum.
	 */
	@Override
	public void generationStats(int generation, Object[] stats) {
    	if (stats.length > 0) {
			System.out.print(generation + "\t");
	    	for (Object o: stats) {
	    		System.out.print(o + "\t");
	    	}
	    	System.out.println();
    	}
	}

	/**
	 * Default implementation. No fields are requested, the overriding class 
	 * is expected to override this method or call the setter method IF it 
	 * wants to receive information about each crossover operation. 
	 * 
	 * <p>Typically it is the model that receives the statistics but this can
	 * be overridden by returning a different CrossoverStatListener in the 
	 * getCrossoverStatListener() method. The crossoverStats method of this 
	 * object will then be called at the end of each run with these requested 
	 * statistics. 
	 */
	public CrossoverStatField[] getCrossoverStatFields() {
		return crossoverStatFields;
	}
	
	/**
	 * Set the crossover statistics that the given CrossoverStatListener will
	 * receive after each crossover operation.
	 * 
	 * @param crossoverStatFields an array of CrossoverStatFields that indicate 
	 * 							  the statistics fields that the 
	 * 							  CrossoverStatListener will receive. The fields 
	 * 							  will be delivered in the same order as given 
	 * 							  here.
	 */
	public void setCrossoverStatFields(CrossoverStatField[] crossoverStatFields) {
		this.crossoverStatFields = crossoverStatFields;
	}
	
	/**
	 * Default implementation. Does nothing. This is implemented here rather 
	 * than being abstract to remove the need for the user to extend it if 
	 * they are not interested in crossover stats.
	 * 
	 * @param stats an array of the statistics that were requested, given in 
	 * 				the order that they were requested. The data type of each 
	 * 				of the stats fields varies according to the field and is 
	 * 				specified in the JavaDoc of the CrossoverStatField enum.
	 */
	public void crossoverStats(Object[] stats) {}
	
	/**
	 * Default implementation. No fields are requested, the overriding class 
	 * is expected to override this method or call the setter method IF it 
	 * wants to receive information about each mutation operation. 
	 * 
	 * <p>Typically it is the model that receives the statistics but this can
	 * be overridden by returning a different MutationStatListener in the 
	 * getMutationStatListener() method. The mutationStats method of this 
	 * object will then be called at the end of each run with these requested 
	 * statistics.
	 */
	public MutationStatField[] getMutationStatFields() {
		return mutationStatFields;
	}
	
	/**
	 * Set the mutation statistics that the given MutationStatListener will
	 * receive after each mutation operation.
	 * 
	 * @param mutationStatFields an array of MutationStatFields that indicate 
	 * 							  the statistics fields that the 
	 * 							  MutationStatListener will receive. The fields 
	 * 							  will be delivered in the same order as given 
	 * 							  here.
	 */
	public void setMutationStatFields(MutationStatField[] mutationStatFields) {
		this.mutationStatFields = mutationStatFields;
	}
	
	/**
	 * Default implementation. Does nothing. This is implemented here rather 
	 * than being abstract to remove the need for the user to extend it if 
	 * they're not interested in mutation stats.
	 * 
	 * @param stats an array of the statistics that were requested, given in 
	 * 				the order that they were requested. The data type of each 
	 * 				of the stats fields varies according to the field and is 
	 * 				specified in the JavaDoc of the MutationStatField enum.
	 */
	public void mutationStats(Object[] stats) {}

	/**
	 * Default implementation returns this model as the life cycle listener. By
	 * default all the listener methods will confirm the events though. 
	 * Override the individual life cycle methods to add extra functionality in 
	 * the model, or override this method to return a different life cycle 
	 * listener.
	 * 
	 * @return the LifeCycleListener to inform of all events during the GP life 
	 * cycle.
	 */
	@Override
	public LifeCycleListener<TYPE> getLifeCycleListener() {
		return lifeCycleListener;
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
	public List<CandidateProgram<TYPE>> onInitialisation(List<CandidateProgram<TYPE>> pop) {
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
	public List<CandidateProgram<TYPE>> onElitism(List<CandidateProgram<TYPE>> elites) {
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
	public List<CandidateProgram<TYPE>> onPoolSelection(List<CandidateProgram<TYPE>> pool) {
		return pool;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Default implementation confirms the crossover operation by returning
	 * the given array of children.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public CandidateProgram<TYPE>[] onCrossover(CandidateProgram<TYPE>[] parents,
			CandidateProgram<TYPE>[] children) {
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
	public CandidateProgram<TYPE> onMutation(CandidateProgram<TYPE> parent,
			CandidateProgram<TYPE> child) {
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
	public CandidateProgram<TYPE> onReproduction(CandidateProgram<TYPE> child) {
		return child;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Default implementation confirms the generation by returning the given 
	 * population.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public List<CandidateProgram<TYPE>> onGeneration(List<CandidateProgram<TYPE>> pop) {
		return pop;
	}
	
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
