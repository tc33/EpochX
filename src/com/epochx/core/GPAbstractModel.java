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

import com.epochx.core.crossover.*;
import com.epochx.core.initialisation.*;
import com.epochx.core.mutation.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;
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
													   MutationStatListener {

	private Initialiser<TYPE> initialiser;
	private Crossover<TYPE> crossover;
	private Mutation<TYPE> mutator;
	
	private PouleSelector<TYPE> pouleSelector;
	private ParentSelector<TYPE> parentSelector;

	private int noRuns;
	private int noGenerations;
	private int populationSize;
	private int pouleSize;
	private int noElites;
	private int maxInitialDepth;
	private int maxDepth;
	private double terminationFitness;
	
	private double crossoverProbability;
	private double reproductionProbability;
	private double mutationProbability;
	
	private MutationStatField[] mutationStatFields;
	private CrossoverStatField[] crossoverStatFields;
	private GenerationStatField[] generationStatFields;
	private RunStatField[] runStatFields;
	
	/**
	 * Construct a GPModel with a set of sensible defaults. See the appropriate
	 * accessor method for information of each default value.
	 */
	public GPAbstractModel() {
		// Set default values.
		noRuns = 1;
		noGenerations = 50;
		populationSize = 500;
		maxInitialDepth = 6;
		maxDepth = 17;
		crossoverProbability = 0.9;
		reproductionProbability = 0.1;
		mutationProbability = 0;
		pouleSize = 50;
		noElites = 0;
		terminationFitness = 0.0;
		
		parentSelector = new RandomSelector<TYPE>();
		pouleSelector = new TournamentSelector<TYPE>(3, this);
		
		initialiser = new FullInitialiser<TYPE>(this);
		crossover = new UniformPointCrossover<TYPE>();
		mutator = new SubtreeMutation<TYPE>(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 50 in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	public int getPouleSize() {
		return pouleSize;
	}

	/**
	 * Overwrites the default poule size value.
	 * 
	 * @param pouleSize the new size of the mating pool to use.
	 */
	public void setPouleSize(int pouleSize) {
		this.pouleSize = pouleSize;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to FullInitialiser in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	public Initialiser<TYPE> getInitialiser() {
		return initialiser;
	}

	/**
	 * Overwrites the default initialiser.
	 * 
	 * @param initialiser the new Initialiser to use when generating the 
	 * 		 			  initial population.
	 */
	public void setInitialiser(Initialiser<TYPE> initialiser) {
		this.initialiser = initialiser;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 1 in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
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
	 * Returns the union of calls to getTerminals() and getFunctions.
	 * 
	 * @return {@inheritDoc}
	 */
	public List<Node<TYPE>> getSyntax() {
		List<Node<TYPE>> syntax = new ArrayList<Node<TYPE>>(getTerminals());
		syntax.addAll(getFunctions());
		
		return syntax;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 17 in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
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
	 * <p>Defaults to 6 in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
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
	 * <p>Defaults to {@link UniformPointCrossover} in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
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
	public Mutation<TYPE> getMutator() {
		return mutator;
	}

	/**
	 * Overwrites the default mutator used to perform mutation.
	 * 
	 * @param mutator the mutator to set
	 */
	public void setMutator(Mutation<TYPE> mutator) {
		this.mutator = mutator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 0.9 in GPAbstractModel to represent a 90% chance.
	 * 
	 * @return {@inheritDoc}
	 */
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
	 * <p>Defaults to 0.1 in GPAbstractModel to represent a 10% chance.
	 * 
	 * @return {@inheritDoc}
	 */
	public double getReproductionProbability() {
		return reproductionProbability;
	}
	
	/**
	 * Overwrites the default reproduction probability.
	 * 
	 * @param reproductionProbability the new reproduction probability to use.
	 */
	public void setReproductionProbability(double reproductionProbability) {
		this.reproductionProbability = reproductionProbability;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 0.0 in GPAbstractModel to represent a 0% chance.
	 * 
	 * @return {@inheritDoc}
	 */
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
	 * <p>Defaults to {@link TournamentSelector} with a tournament size of 3 
	 * in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	public PouleSelector<TYPE> getPouleSelector() {
		return pouleSelector;
	}

	/**
	 * Overwrites the default poule selector used to generate a mating pool.
	 * 
	 * @param pouleSelector the new PouleSelector to be used when building a 
	 * 						breeding pool.
	 */
	public void setPouleSelector(PouleSelector<TYPE> pouleSelector) {
		this.pouleSelector = pouleSelector;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link RandomSelector} in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	public ParentSelector<TYPE> getParentSelector() {
		return parentSelector;
	}

	/**
	 * Overwrites the default parent selector used to select parents to undergo
	 * a genetic operator from either a poule or the previous population.
	 * 
	 * @param parentSelector the new ParentSelector to be used when selecting 
	 * 						 parents for a genetic operator.
	 */
	public void setParentSelector(ParentSelector<TYPE> parentSelector) {
		this.parentSelector = parentSelector;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 0 in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
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
	
	@Override
	public double getTerminationFitness() {
		return terminationFitness;
	}
	
	public void setTerminationFitness(double terminationFitness) {
		this.terminationFitness = terminationFitness;
	}
	
	public RunStatListener getRunStatListener() {
		return this;
	}
	
	public GenerationStatListener getGenerationStatListener() {
		return this;
	}
	
	public CrossoverStatListener getCrossoverStatListener() {
		return this;
	}
	
	public MutationStatListener getMutationStatListener() {
		return this;
	}
	
	/**
	 * Default implementation. No fields are requested, the overriding class 
	 * is expected to override this method IF it wants to know about generations.
	 * This is implemented here rather than being abstract to remove the need 
	 * for the user to extend it if they're not interested in generational stats.
	 */
	public GenerationStatField[] getGenStatFields() {
		return generationStatFields;
	}
	
	public void setGenStatFields(GenerationStatField[] generationStatFields) {
		this.generationStatFields = generationStatFields;
	}
	
	/**
	 * Default implementation. Does nothing. This is implemented here rather 
	 * than being abstract to remove the need for the user to extend it if 
	 * they're not interested in generational stats.
	 */
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
	 * is expected to override this method IF it wants to know about runs.
	 * This is implemented here rather than being abstract to remove the need 
	 * for the user to extend it if they're not interested in run stats.
	 */
	public RunStatField[] getRunStatFields() {
		return runStatFields;
	}
	
	public void setRunStatFields(RunStatField[] runStatFields) {
		this.runStatFields = runStatFields;
	}
	
	/**
	 * Default implementation. Does nothing. This is implemented here rather 
	 * than being abstract to remove the need for the user to extend it if 
	 * they're not interested in run stats.
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
	 * is expected to override this method IF it wants to know about crossovers.
	 * This is implemented here rather than being abstract to remove the need 
	 * for the user to extend it if they're not interested in crossover stats.
	 */
	public CrossoverStatField[] getCrossoverStatFields() {
		return crossoverStatFields;
	}
	
	public void setCrossoverStatFields(CrossoverStatField[] crossoverStatFields) {
		this.crossoverStatFields = crossoverStatFields;
	}
	
	/**
	 * Default implementation. Does nothing. This is implemented here rather 
	 * than being abstract to remove the need for the user to extend it if 
	 * they're not interested in crossover stats.
	 */
	public void crossoverStats(Object[] stats) {}
	
	/**
	 * Default implementation. No fields are requested, the overriding class 
	 * is expected to override this method IF it wants to know about mutations.
	 * This is implemented here rather than being abstract to remove the need 
	 * for the user to extend it if they're not interested in mutation stats.
	 */
	public MutationStatField[] getMutationStatFields() {
		return mutationStatFields;
	}
	
	public void setMutationStatFields(MutationStatField[] mutationStatFields) {
		this.mutationStatFields = mutationStatFields;
	}
	
	/**
	 * Default implementation. Does nothing. This is implemented here rather 
	 * than being abstract to remove the need for the user to extend it if 
	 * they're not interested in mutation stats.
	 */
	public void mutationStats(Object[] stats) {}
	
	/**
	 * Default implementation which accepts all crossovers.
	 */
	public boolean acceptCrossover(CandidateProgram<TYPE>[] parents, 
								   CandidateProgram<TYPE>[] children) {
		return true;
	}
	
	/**
	 * Default implementation which accepts all mutations.
	 */
	public boolean acceptMutation(CandidateProgram<TYPE> parent, 
								  CandidateProgram<TYPE> child) {
		return true;
	}
}
