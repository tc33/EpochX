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
package org.epochx.ge.model;

import org.epochx.ge.codon.*;
import org.epochx.ge.mapper.*;
import org.epochx.ge.op.crossover.*;
import org.epochx.ge.op.init.*;
import org.epochx.ge.op.mutation.*;
import org.epochx.ge.stats.GEStatsEngine;
import org.epochx.model.AbstractModel;


/**
 * GEAbstractModel is a partial implementation of GEModel which provides 
 * sensible defaults for many of the necessary control parameters. It also 
 * provides a simple way of setting many values so an extending class isn't 
 * required to override all methods they wish to alter, and can instead use 
 * a simple setter method call. 
 * 
 * <p>Those methods that it isn't possible to provide a <em>sensible</em> 
 * default for, for example getFitness(GECandidateProgram) and 
 * getGrammar(Grammar), are not implemented to force the extending class to 
 * consider their implementation.
 * 
 * @see GEModel
 */
public abstract class GEAbstractModel extends AbstractModel implements GEModel {
	
	private GEInitialiser initialiser;
	private GECrossover crossover;
	private GEMutation mutator;
	private Mapper mapper;
	private CodonGenerator codonGenerator;
	
	private int maxDepth;
	private int maxInitialDepth;
	private int maxCodonSize;
	private int maxChromosomeLength;
	
	private boolean cacheSource;
	
	/**
	 * Construct a GEModel with a set of sensible defaults. See the appropriate
	 * accessor method for information of each default value.
	 */
	public GEAbstractModel() {
		// Set default parameter values.
		maxDepth = 20;
		maxInitialDepth = 8;
		maxCodonSize = Integer.MAX_VALUE;
		maxChromosomeLength = 100;
		
		// Caching.
		cacheSource = true;
		
		// GP Components.
		initialiser = new RampedHalfAndHalfInitialiser(this);
		crossover = new OnePointCrossover(this);
		mutator = new PointMutation(this);
		mapper = new DepthFirstMapper(this);
		codonGenerator = new StandardGenerator(this);
		
		// Stats - overwrite parent default.
		setStatsEngine(new GEStatsEngine());
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to RandomInitialiser in GEAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public GEInitialiser getInitialiser() {
		return initialiser;
	}

	/**
	 * Overwrites the default initialiser.
	 * 
	 * @param initialiser the new GPInitialiser to use when generating the 
	 * 		 			  starting population.
	 */
	public void setInitialiser(GEInitialiser gEInitialiser) {
		this.initialiser = gEInitialiser;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link OnePointCrossover} in GEAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public GECrossover getCrossover() {
		return crossover;
	}
	
	/**
	 * Overwrites the default crossover operator.
	 * 
	 * @param crossover the crossover to set
	 */
	public void setCrossover(GECrossover gECrossover) {
		this.crossover = gECrossover;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to {@link PointMutation} in GEAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public GEMutation getMutation() {
		return mutator;
	}

	/**
	 * Overwrites the default mutator used to perform mutation.
	 * 
	 * @param mutator the mutator to set.
	 */
	public void setMutation(GEMutation mutator) {
		this.mutator = mutator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to DepthFirstMapper in GEAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public Mapper getMapper() {
		return mapper;
	}

	/**
	 * Overwrites the default mapper used to map chromosomes to source strings.
	 * 
	 * @param mapper the mapper to be used during the mapping operation.
	 */
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to StandardGenerator in GEAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public CodonGenerator getCodonGenerator() {
		return codonGenerator;
	}

	/**
	 * Overwrites the default codon generator used to generate new codons 
	 * throughout the run.
	 * 
	 * @param codonGenerator the codon generator to be used any time a new 
	 * 						 codon is required.
	 */
	public void setCodonGenerator(CodonGenerator codonGenerator) {
		this.codonGenerator = codonGenerator;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to Integer.MAX_VALUE in GEAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getMaxCodonSize() {
		return maxCodonSize;
	}

	/**
	 * Overwrites the default maximum size of each codon.
	 * 
	 * @param maxCodonSize the maximum size of a codon to replace the current 
	 * 					   maximum with. Must be a positive integer.
	 */
	public void setMaxCodonSize(int maxCodonSize) {
		this.maxCodonSize = maxCodonSize;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 100 in GEAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int getMaxChromosomeLength() {
		return maxChromosomeLength;
	}

	/**
	 * Overwrites the default maximum number of codons in a program's 
	 * chromosome.
	 * 
	 * @param maxChromosomeLength the maximum number of codons to allow in a 
	 * 							  chromosome.
	 */
	public void setMaxChromosomeLength(int maxChromosomeLength) {
		this.maxChromosomeLength = maxChromosomeLength;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>Defaults to 20 in GEAbstractModel.
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
	 * <p>Defaults to 8 in GEAbstractModel.
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
	 * <p>Defaults to true in GEAbstractModel.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean cacheSource() {
		return cacheSource;
	}
	
	/**
	 * Overwrites the default setting of whether to cache the source code after 
	 * mapping, until the chromosome is changed again.
	 * 
	 * @param cacheSource
	 */
	public void setCacheSource(boolean cacheSource) {
		this.cacheSource = cacheSource;
	}
}
