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
import com.epochx.semantics.*;
import com.epochx.stats.*;
import com.epochx.stats.CrossoverStats.*;
import com.epochx.stats.GenerationStats.*;
import com.epochx.stats.MutationStats.*;
import com.epochx.stats.RunStats.*;


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
	private Mutator<TYPE> mutator;
	
	private PouleSelector<TYPE> pouleSelector;
	private ParentSelector<TYPE> parentSelector;
	
	private SemanticModule semanticModule;

	private int noRuns;
	private int noGenerations;
	private int populationSize;
	private int pouleSize;
	private int noElites;
	private int maxInitialDepth;
	private int maxDepth;
	
	private double crossoverProbability;
	private double reproductionProbability;
	private double mutationProbability;
	
	private boolean doStateCheckedCrossover;
	private boolean doStateCheckedMutation;
	
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
		doStateCheckedCrossover = false;
		doStateCheckedMutation = false;
		
		parentSelector = new RandomSelector<TYPE>();
		pouleSelector = new TournamentSelector<TYPE>(3, this);
		
		initialiser = new FullInitialiser<TYPE>(this);
		crossover = new UniformPointCrossover<TYPE>();
		mutator = new SubtreeMutation<TYPE>(this);
		
		semanticModule = null;
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
	public List<Node<?>> getSyntax() {
		List<Node<?>> syntax = new ArrayList<Node<?>>(getTerminals());
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
	public Mutator<TYPE> getMutator() {
		return mutator;
	}

	/**
	 * Overwrites the default mutator used to perform mutation.
	 * 
	 * @param mutator the mutator to set
	 */
	public void setMutator(Mutator<TYPE> mutator) {
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
	public GenStatField[] getGenStatFields() {
		return new GenStatField[0];
	}
	
	/**
	 * Default implementation. Does nothing. This is implemented here rather 
	 * than being abstract to remove the need for the user to extend it if 
	 * they're not interested in generational stats.
	 */
	public void generationStats(int generation, Object[] stats) {}
	
	/**
	 * Default implementation. No fields are requested, the overriding class 
	 * is expected to override this method IF it wants to know about runs.
	 * This is implemented here rather than being abstract to remove the need 
	 * for the user to extend it if they're not interested in run stats.
	 */
	public RunStatField[] getRunStatFields() {
		return new RunStatField[0];
	}
	
	/**
	 * Default implementation. Does nothing. This is implemented here rather 
	 * than being abstract to remove the need for the user to extend it if 
	 * they're not interested in run stats.
	 */
	public void runStats(int runNo, Object[] stats) {}
	
	/**
	 * Default implementation. No fields are requested, the overriding class 
	 * is expected to override this method IF it wants to know about crossovers.
	 * This is implemented here rather than being abstract to remove the need 
	 * for the user to extend it if they're not interested in crossover stats.
	 */
	public CrossoverStatField[] getCrossoverStatFields() {
		return new CrossoverStatField[0];
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
		return new MutationStatField[0];
	}
	
	/**
	 * Default implementation. Does nothing. This is implemented here rather 
	 * than being abstract to remove the need for the user to extend it if 
	 * they're not interested in mutation stats.
	 */
	public void mutationStats(Object[] stats) {}
	
	/**
	 * Returns whether to run the crossover state checker
	 * @return TRUE if the crossover state checker should be run
	 */
	public boolean getStateCheckedCrossover() {
		return doStateCheckedCrossover;
	}
	
	/**
	 * Sets whether to run the crossover state checker
	 * @param runStateCheck TRUE if the crossover state checker should be run
	 */
	public void setStateCheckedCrossover(boolean runStateCheck) {
		this.doStateCheckedCrossover = runStateCheck;
	}
	
	public boolean getStateCheckedMutation() {
		return doStateCheckedMutation;
	}
	
	public void setStateCheckedMutation(boolean doStateCheckedMutation) {
		this.doStateCheckedMutation = doStateCheckedMutation;
	}
	
	/**
	 * Returns the semantic module associated with this problem
	 * @return The associate Semantic module
	 */
	public SemanticModule getSemanticModule() {
		return this.semanticModule;
	}
	
	/**
	 * Sets the semantic module for this run
	 * @param semMod The desired semantic module to use
	 */
	public void setSemanticModule(SemanticModule semMod) {
		this.semanticModule = semMod;
	}
}
