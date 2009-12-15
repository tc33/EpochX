/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.core;

import java.util.*;

import org.epochx.op.crossover.*;
import org.epochx.op.initialisation.*;
import org.epochx.op.mutation.*;
import org.epochx.op.selection.*;
import org.epochx.representation.*;
import org.epochx.tools.random.*;


/**
 * GPAbstractModel is a partial implementation of GPModel which provides 
 * sensible defaults for many of the necessary control parameters. It also 
 * provides a simple way of setting many values so an extending class isn't 
 * required to override all methods they wish to alter, and can instead use 
 * a simple setter method call. 
 * 
 * <p>Those methods that it isn't possible to provide a <em>sensible</em> 
 * default for, for example getFitness(GPCandidateProgram), getTerminals() and
 * getFunctions(), are not implemented to force the extending class to 
 * consider their implementation.
 * 
 * @see GPModel
 */
public abstract class GPAbstractModel<TYPE> extends AbstractModel
											implements GPModel<TYPE> {

	private int maxInitialDepth;
	private int maxProgramDepth;
	
	// Run components.
	private GPInitialiser<TYPE> gPInitialiser;
	private GPCrossover<TYPE> gPCrossover;
	private GPMutation<TYPE> mutator;
	private GPPoolSelector<TYPE> gPPoolSelector;
	private GPProgramSelector<TYPE> gPProgramSelector;
	private RandomNumberGenerator randomNumberGenerator;
	
	/**
	 * Construct a GPModel with a set of sensible defaults. See the appropriate
	 * accessor method for information of each default value.
	 */
	public GPAbstractModel() {
		// Initialise default parameter values.
		maxInitialDepth = 6;
		maxProgramDepth = 17;
		
		// Initialise components.
		gPProgramSelector = new RandomSelector<TYPE>(this);
		gPPoolSelector = new TournamentSelector<TYPE>(this, 3);
		gPInitialiser = new FullInitialiser<TYPE>(this);
		gPCrossover = new UniformPointCrossover<TYPE>(this);
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
	public GPInitialiser<TYPE> getInitialiser() {
		return gPInitialiser;
	}

	/**
	 * Overwrites the default gPInitialiser.
	 * 
	 * @param gPInitialiser the new GPInitialiser to use when generating the 
	 * 		 			  starting population.
	 */
	public void setInitialiser(GPInitialiser<TYPE> initialiser) {
		this.gPInitialiser = initialiser;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link UniformPointCrossover} in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public GPCrossover<TYPE> getCrossover() {
		return gPCrossover;
	}

	/**
	 * Overwrites the default gPCrossover operator.
	 * 
	 * @param gPCrossover the gPCrossover to set
	 */
	public void setCrossover(GPCrossover<TYPE> crossover) {
		this.gPCrossover = crossover;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link SubtreeMutation} in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public GPMutation<TYPE> getMutation() {
		return mutator;
	}

	/**
	 * Overwrites the default mutator used to perform mutation.
	 * 
	 * @param mutator the mutator to set.
	 */
	public void setMutator(GPMutation<TYPE> mutator) {
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
	public GPProgramSelector<TYPE> getProgramSelector() {
		return gPProgramSelector;
	}

	/**
	 * Overwrites the default parent selector used to select parents to undergo
	 * a genetic operator from either a pool or the previous population.
	 * 
	 * @param gPProgramSelector the new GPProgramSelector to be used when selecting 
	 * 						 parents for a genetic operator.
	 */
	public void setProgramSelector(GPProgramSelector<TYPE> programSelector) {
		this.gPProgramSelector = programSelector;
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
	public GPPoolSelector<TYPE> getPoolSelector() {
		return gPPoolSelector;
	}

	/**
	 * Overwrites the default pool selector used to generate a mating pool.
	 * 
	 * @param gPPoolSelector the new GPPoolSelector to be used when building a 
	 * 						breeding pool.
	 */
	public void setPoolSelector(GPPoolSelector<TYPE> poolSelector) {
		this.gPPoolSelector = poolSelector;
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
	 * <p>Defaults to MersenneTwisterFast in GPAbstractModel.
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
	public int getMaxProgramDepth() {
		return maxProgramDepth;
	}

	/**
	 * Overwrites the default max program tree depth allowed after genetic 
	 * operators are performed.
	 * 
	 * @param maxDepth the new max program tree depth to use.
	 */
	public void setMaxProgramDepth(int maxDepth) {
		this.maxProgramDepth = maxDepth;
	}
	
	/**
	 * Default implementation which accepts all crossovers.
	 * 
	 * @param parents The programs that were crossed over to create the given 
	 * 				  children.
	 * @param children The children that resulted from the parents being 
	 * 				   crossed over.
	 * @return True if the gPCrossover operation should proceed, false if it is 
	 * 		   rejected and should be retried with new parents.
	 */
	public boolean acceptCrossover(GPCandidateProgram<TYPE>[] parents, 
								   GPCandidateProgram<TYPE>[] children) {
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
	public boolean acceptMutation(GPCandidateProgram<TYPE> parent, 
								  GPCandidateProgram<TYPE> child) {
		return true;
	}

}
