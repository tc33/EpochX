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
package org.epochx.gr.model;

import org.epochx.core.Model;
import org.epochx.gr.op.crossover.*;
import org.epochx.gr.op.init.*;
import org.epochx.gr.op.mutation.*;
import org.epochx.gr.stats.GRStatsEngine;
import org.epochx.tools.grammar.Grammar;

public abstract class GRModel extends Model {
	
	private GRInitialiser initialiser;
	private GRCrossover crossover;
	private GRMutation mutator;
	
	private int maxDepth;
	private int maxInitialDepth;
	
	/**
	 * Construct a GRModel with a set of sensible defaults. See the appropriate
	 * accessor method for information of each default value.
	 */
	public GRModel() {
		// Set default parameter values.
		maxDepth = 12;
		maxInitialDepth = 8;
		
		// GP Components.
		initialiser = new RampedHalfAndHalfInitialiser(this);
		crossover = new WhighamCrossover(this);
		mutator = new WhighamMutation(this);
		
		// Stats - overwrite parent default.
		setStatsEngine(new GRStatsEngine(this));
	}
	
	/**
	 * Returns the grammar instance that determines the structure of the 
	 * programs to be evolved. As well as defining the syntax of solutions, the 
	 * grammar also essentially determines the function and terminal sets which 
	 * are features of tree GP. 
	 * 
	 * @return the language grammar that defines the syntax of solutions.
	 */
	public abstract Grammar getGrammar();
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to RandomInitialiser in GRModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public GRInitialiser getInitialiser() {
		return initialiser;
	}

	/**
	 * Overwrites the default initialiser.
	 * 
	 * @param initialiser the new GPInitialiser to use when generating the 
	 * 		 			  starting population.
	 */
	public void setInitialiser(GRInitialiser gEInitialiser) {
		this.initialiser = gEInitialiser;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link OnePointCrossover} in GRModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public GRCrossover getCrossover() {
		return crossover;
	}
	
	/**
	 * Overwrites the default crossover operator.
	 * 
	 * @param crossover the crossover to set
	 */
	public void setCrossover(GRCrossover crossover) {
		this.crossover = crossover;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link PointMutation} in GRModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public GRMutation getMutation() {
		return mutator;
	}

	/**
	 * Overwrites the default mutator used to perform mutation.
	 * 
	 * @param mutator the mutator to set.
	 */
	public void setMutation(GRMutation mutator) {
		this.mutator = mutator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 20 in GRModel.
	 * 
	 * @return {@inheritDoc}
	 */
	public int getMaxProgramDepth() {
		return maxDepth;
	}

	/**
	 * Overwrites the default maximum allowable depth of a program's derivation 
	 * tree.
	 * 
	 * @param maxDepth the maximum depth to allow a program's derivation tree.
	 */
	public void setMaxProgramDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 8 in GRModel.
	 * 
	 * @return {@inheritDoc}
	 */
	public int getMaxInitialProgramDepth() {
		return maxInitialDepth;
	}

	/**
	 * Overwrites the default maximum allowable depth of a program's derivation 
	 * tree after initialisation.
	 * 
	 * @param maxDepth the maximum depth to allow a program's derivation tree
	 * 				   after initialisation.
	 */
	public void setMaxInitialProgramDepth(int maxInitialDepth) {
		this.maxInitialDepth = maxInitialDepth;
	}
}
