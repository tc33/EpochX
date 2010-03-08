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
package org.epochx.gp.model;

import java.util.*;

import org.epochx.gp.op.crossover.*;
import org.epochx.gp.op.init.*;
import org.epochx.gp.op.mutation.*;
import org.epochx.gp.representation.*;
import org.epochx.gp.stats.GPStatsEngine;
import org.epochx.model.AbstractModel;


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
public abstract class GPAbstractModel extends AbstractModel implements GPModel {

	private int maxInitialDepth;
	private int maxProgramDepth;
	
	// Run components.
	private GPInitialiser initialiser;
	private GPCrossover crossover;
	private GPMutation mutator;
	
	/**
	 * Construct a GPModel with a set of sensible defaults. See the appropriate
	 * accessor method for information of each default value.
	 */
	public GPAbstractModel() {
		// Initialise default parameter values.
		maxInitialDepth = 6;
		maxProgramDepth = 17;
		
		// Initialise components.
		initialiser = new FullInitialiser(this);
		crossover = new UniformPointCrossover(this);
		mutator = new SubtreeMutation(this);
		
		// Stats - overwrite parent default.
		setStatsEngine(new GPStatsEngine());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to FullInitialiser in GPAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public GPInitialiser getInitialiser() {
		return initialiser;
	}

	/**
	 * Overwrites the default initialiser.
	 * 
	 * @param initialiser the new GPInitialiser to use when generating the 
	 * 		 			  starting population.
	 */
	public void setInitialiser(GPInitialiser initialiser) {
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
	public GPCrossover getCrossover() {
		return crossover;
	}

	/**
	 * Overwrites the default crossover operator.
	 * 
	 * @param crossover the crossover to set
	 */
	public void setCrossover(GPCrossover crossover) {
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
	public GPMutation getMutation() {
		return mutator;
	}

	/**
	 * Overwrites the default mutator used to perform mutation.
	 * 
	 * @param mutator the mutator to set.
	 */
	public void setMutator(GPMutation mutator) {
		this.mutator = mutator;
	}

	/**
	 * Returns the union of calls to getTerminals() and getFunctions.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public List<Node> getSyntax() {
		List<Node> syntax = new ArrayList<Node>(getTerminals());
		syntax.addAll(getFunctions());
		
		return syntax;
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
		//TODO The name of this needs to be made consistent with those from XGR and XGE.
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
	 * @return True if the crossover operation should proceed, false if it is 
	 * 		   rejected and should be retried with new parents.
	 */
	public boolean acceptCrossover(GPCandidateProgram[] parents, 
								   GPCandidateProgram[] children) {
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
	public boolean acceptMutation(GPCandidateProgram parent, 
								  GPCandidateProgram child) {
		return true;
	}

}
