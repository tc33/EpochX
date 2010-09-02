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
package org.epochx.ge.op.init;

import java.util.*;

import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.life.ConfigAdapter;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.Grammar;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Initialisation implementation which uses a combination of full and grow
 * initialisers to create an initial population of
 * <code>GECandidatePrograms</code>. For full details of how the initialisation
 * operator works see Chapter 8: Sensible Initialisation, of Grammatical
 * Evolution by O'Neill and Ryan.
 * 
 * <p>
 * Depths are equally split between depths from the minimum possible depth
 * up to the maximum initial depth. Initialisation of programs with
 * parse trees down to each of these depths is then alternated between full and
 * grow initialisers starting with grow. The depth of the programs is controlled
 * by mapping the generated codons in a depth first manner.
 * 
 * <p>
 * There will not always be an equal number of programs created to each depth,
 * this will depend on if the population size is exactly divisible by the range
 * of depths (<code>initial maximum depth - initial minimum depth</code>). If
 * the range of depths is greater than the population size then some depths will
 * not occur at all in order to ensure as wide a spread of depths up to the
 * maximum as possible.
 * 
 * <p>
 * If a model is provided then the following parameters are loaded upon every
 * configure event:
 * 
 * <ul>
 * <li>population size</li>
 * <li>maximum initial program initialDepth</li>
 * <li>syntax</li>
 * <li>random number generator</li>
 * </ul>
 * 
 * @see FixedLengthInitialiser
 * @see FullInitialiser
 * @see GrowInitialiser
 */
public class RampedHalfAndHalfInitialiser implements GEInitialiser {

	// The controlling model.
	private GEModel model;

	// The grow and full instances for doing their share of the work.
	private final GrowInitialiser grow;
	private final FullInitialiser full;

	// The grammar each program's parse tree must satisfy.
	private Grammar grammar;

	// The size of the populations to construct.
	private int popSize;

	// The depth limits of each program tree to generate.
	private int endMaxDepth;
	
	// Whether programs must be unique in generated populations.
	private boolean acceptDuplicates;

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with all the
	 * necessary parameters given.
	 */
	public RampedHalfAndHalfInitialiser(final RandomNumberGenerator rng,
			final Grammar grammar, final int popSize,
			final int endMaxDepth, final int maxCodonValue,
			final boolean acceptDuplicates) {
		this.endMaxDepth = endMaxDepth;
		this.grammar = grammar;
		this.popSize = popSize;
		this.acceptDuplicates = acceptDuplicates;

		// Set up the grow and full parts.
		grow = new GrowInitialiser(rng, grammar, popSize, endMaxDepth,
				maxCodonValue, acceptDuplicates);
		full = new FullInitialiser(rng, grammar, popSize, endMaxDepth,
				maxCodonValue, acceptDuplicates);
	}

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events. Duplicate programs are allowed in the populations that
	 * are constructed.
	 * 
	 * @param model the <code>GEModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 */
	public RampedHalfAndHalfInitialiser(final GEModel model) {
		this(model, true);
	}

	/**
	 * Construct a RampedHalfAndHalfInitialiser.
	 * 
	 * @param model The model being assessed
	 */
	public RampedHalfAndHalfInitialiser(final GEModel model,
			final boolean acceptDuplicates) {
		this.model = model;
		this.acceptDuplicates = acceptDuplicates;

		// set up the grow and full parts
		grow = new GrowInitialiser(model);
		full = new FullInitialiser(model);

		// Configure parameters from the model.
		model.getLifeCycleManager().addConfigListener(new ConfigAdapter() {

			@Override
			public void onConfigure() {
				configure();
			}
		});
	}

	/*
	 * Configure component with parameters from the model.
	 */
	private void configure() {
		grammar = model.getGrammar();
		popSize = model.getPopulationSize();
		endMaxDepth = model.getMaxInitialDepth();
	}

	/**
	 * Will use grow initialisation on half the population and full on the other
	 * half. If
	 * the population size is an odd number then the extra individual will be
	 * initialised with
	 * grow.
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		if (popSize < 1) {
			throw new IllegalStateException(
					"Population size must be 1 or greater");
		} else if (grammar == null) {
			throw new IllegalStateException("No grammar has been set");
		}
		
		// Create population list to populate.
		final List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(
				popSize);

		// Our start depth can only be as small as the grammars minimum depth.
		final int startDepth = grammar.getMinimumDepth();

		if (endMaxDepth < startDepth) {
			throw new IllegalStateException(
					"End maximum depth must be greater than the minimum possible depth.");
		}

		// Number of programs each depth SHOULD have. But won't unless remainder
		// is 0.
		final double programsPerDepth = (double) popSize
				/ (endMaxDepth - startDepth + 1);

		for (int i = 0; i < popSize; i++) {
			// Calculate depth
			final int depth = (int) Math
					.floor((firstGen.size() / programsPerDepth) + startDepth);

			// Grow on even numbers, full on odd.
			GECandidateProgram program;

			do {
				if ((i % 2) == 0) {
					grow.setMaxDepth(depth);
					program = grow.getInitialProgram();
				} else {
					full.setDepth(depth);
					program = full.getInitialProgram();
				}
			} while (!acceptDuplicates && firstGen.contains(program));

			firstGen.add(program);
		}

		return firstGen;
	}

	/**
	 * Returns whether or not duplicates are currently accepted or rejected from
	 * generated populations.
	 * 
	 * @return <code>true</code> if duplicates are currently accepted in any
	 *         populations generated by the <code>getInitialPopulation</code>
	 *         method and <code>false</code> otherwise
	 */
	public boolean isDuplicatesEnabled() {
		return acceptDuplicates;
	}

	/**
	 * Sets whether duplicates should be allowed in the populations that are
	 * generated, or if they should be discarded.
	 * 
	 * @param acceptDuplicates whether duplicates should be accepted in the
	 *        populations that are constructed.
	 */
	public void setDuplicatesEnabled(final boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
	}

	/**
	 * Returns the model that is providing the configuration for this
	 * initialiser, or <code>null</code> if none is set.
	 * 
	 * @return the model that is supplying the configuration parameters or null
	 *         if the parameters are individually set.
	 */
	public GEModel getModel() {
		return model;
	}


	/**
	 * Sets a model that will provide the configuration for this initialiser.
	 * The necessary parameters will be obtained from the model the next time,
	 * and each time a configure event is triggered. Note that until a configure
	 * event is fired this initialiser may be in an unusable state. Any
	 * previously set parameters will stay active until they are overwritten at
	 * the next configure event.
	 * 
	 * <p>
	 * If a model is already set, it may be cleared by calling this method with
	 * <code>null</code>.
	 * 
	 * @param model the model to set or null to clear any current model.
	 */
	public void setModel(final GEModel model) {
		this.model = model;

		grow.setModel(model);
		full.setModel(model);
	}

	/**
	 * Sets the random number generator to use. If a model has been set then
	 * this parameter will be overwritten with the random number generator from
	 * that model on the next configure event.
	 * 
	 * @param rng the random number generator to set.
	 */
	public void setRNG(final RandomNumberGenerator rng) {
		grow.setRNG(rng);
		full.setRNG(rng);
	}

	/**
	 * Returns the grammar that this initialiser is generating programs to
	 * satisfy. It is using this grammar that the initialiser will map the
	 * program's codons to determine the parse tree depth.
	 * 
	 * @return the grammar that generated programs are being constructed for.
	 */
	public Grammar getGrammar() {
		return grammar;
	}

	/**
	 * Sets the grammar that should be used to determine which codons to use to
	 * create a program parse tree within the set depth limitations.
	 * 
	 * @param grammar the <code>Grammar</code> that generated programs should be
	 *        constructed for.
	 */
	public void setGrammar(final Grammar grammar) {
		this.grammar = grammar;
		
		grow.setGrammar(grammar);
		full.setGrammar(grammar);
	}

	/**
	 * Returns the size of the populations that this initialiser constructs or
	 * <code>-1</code> if none has been set.
	 * 
	 * @return the size of the populations that this initialiser will generate.
	 */
	public int getPopSize() {
		return popSize;
	}

	/**
	 * Sets the size of the populations that this initialiser should construct
	 * on calls to the <code>getInitialPopulation</code> method.
	 * 
	 * @param popSize the size of the populations that should be created by this
	 *        initialiser.
	 */
	public void setPopSize(final int popSize) {
		this.popSize = popSize;
	}

	/**
	 * Returns the final maximum depth that will be used for programs this
	 * initialiser generates. Program depths will have been gradually ramped up
	 * to this value from the start max depth.
	 * 
	 * @return the maximum depth the program trees constructed should be.
	 */
	public int getEndMaxDepth() {
		return endMaxDepth;
	}

	/**
	 * Sets the maximum depth used for the parse trees of the programs this 
	 * initialiser constructs. Depths will be ramped up to this maximum depth.
	 * 
	 * @param endMaxDepth the maximum depth of the ramping process.
	 */
	public void setEndMaxDepth(final int maxDepth) {
		this.endMaxDepth = maxDepth;
	}

	/**
	 * Sets the maximum value that codons chosen by this initialiser should
	 * take.
	 * 
	 * @param maxCodonValue the maximum integer value that codons selected by
	 *        this initialiser should take.
	 */
	public void setMaxCodonValue(int maxCodonValue) {
		grow.setMaxCodonValue(maxCodonValue);
		full.setMaxCodonValue(maxCodonValue);
	}
}
