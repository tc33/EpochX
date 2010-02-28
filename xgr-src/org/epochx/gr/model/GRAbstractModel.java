package org.epochx.gr.model;

import org.epochx.gr.op.crossover.*;
import org.epochx.gr.op.init.*;
import org.epochx.gr.op.mutation.*;
import org.epochx.model.AbstractModel;

public abstract class GRAbstractModel extends AbstractModel implements GRModel {
	
	private GRInitialiser initialiser;
	private GRCrossover crossover;
	private GRMutation mutator;
	
	private int maxDepth;
	private int maxInitialDepth;
	
	private boolean cacheFitness;
	
	/**
	 * Construct a GRModel with a set of sensible defaults. See the appropriate
	 * accessor method for information of each default value.
	 */
	public GRAbstractModel() {
		// Set default parameter values.
		maxDepth = 20;
		maxInitialDepth = 8;
		
		// Caching.
		cacheFitness = true;
		
		// GP Components.
		initialiser = new RampedHalfAndHalfInitialiser(this);
		crossover = new WhighamCrossover(this);
		mutator = new WhighamMutation(this);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to RandomInitialiser in GRAbstractModel.
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
	 * <p>Defaults to {@link OnePointCrossover} in GRAbstractModel.
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
	 * <p>Defaults to {@link PointMutation} in GRAbstractModel.
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
	 * <p>Defaults to 20 in GRAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
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
	 * <p>Defaults to 8 in GRAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
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
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to true in GRAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean cacheFitness() {
		return cacheFitness;
	}
	
	/**
	 * Overwrites the default setting of whether to cache the fitness of a 
	 * program after evaluation, until the chromosome is changed again.
	 * 
	 * @param cacheFitness
	 */
	public void setCacheFitness(boolean cacheFitness) {
		this.cacheFitness = cacheFitness;
	}
}
