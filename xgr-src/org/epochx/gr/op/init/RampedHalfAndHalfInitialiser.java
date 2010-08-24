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
package org.epochx.gr.op.init;

import java.util.*;

import org.epochx.gr.model.GRModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.life.ConfigAdapter;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.Grammar;

/**
 * Initialisation implementation which uses a combination of full and grow
 * initialisers to create an initial population of
 * <code>GRCandidatePrograms</code>.
 * 
 * <p>
 * Depths are equally split between depths from the minimum initial depth
 * attribute up to the maximum initial depth. Initialisation of individuals at
 * each of these depths is then alternated between full and grow initialisers
 * starting with grow.
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
 * <li>grammar</li>
 * <li>random number generator</li>
 * </ul>
 * 
 * @see FullInitialiser
 * @see GrowInitialiser
 */
public class RampedHalfAndHalfInitialiser implements GRInitialiser {

	// The controlling model.
	private final GRModel model;

	// The grammar all new programs must be valid against.
	private Grammar grammar;

	// The size of the populations to construct.
	private int popSize;

	// The depth of every program parse tree to generate.
	private int maxInitialDepth;

	// The smallest depth to be used.
	private int minDepth;

	// Whether programs must be unique in generated populations.
	private boolean acceptDuplicates;

	// The grow and full instances for doing their share of the work.
	private final GrowInitialiser grow;
	private final FullInitialiser full;

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events. Duplicate programs are allowed in the populations that
	 * are constructed.
	 * 
	 * @param model the <code>GRModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 */
	public RampedHalfAndHalfInitialiser(final GRModel model) {
		this(model, -1);
	}

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events. Duplicate programs are allowed in the populations that
	 * are constructed.
	 * 
	 * @param model the <code>Model</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param minDepth the minimum depth from which programs should be generated
	 *        to.
	 */
	public RampedHalfAndHalfInitialiser(final GRModel model, final int minDepth) {
		this(model, minDepth, true);
	}

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events.
	 * 
	 * @param model the <code>Model</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param minDepth the minimum depth from which programs should be generated
	 *        to.
	 * @param acceptDuplicates whether duplicates should be allowed in the
	 *        populations that are generated.
	 */
	public RampedHalfAndHalfInitialiser(final GRModel model,
			final int minDepth, final boolean acceptDuplicates) {
		this.model = model;
		this.minDepth = minDepth;
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
		maxInitialDepth = model.getMaxInitialDepth();
	}

	/**
	 * Generates a population of new <code>GRCandidatePrograms</code> constructed
	 * from the <code>Grammar</code> attribute. The size of the population will
	 * be equal to the population size attribute. All programs in the population
	 * are only guarenteed to be unique (as defined by the <code>equals</code>
	 * method on <code>GRCandidateProgram</code>) if the
	 * <code>isDuplicatesEnabled</code> method returns <code>true</code>.
	 * Each program will alternately be generated with the
	 * {@link FullInitialiser} and {@link GrowInitialiser}. If the population
	 * size is odd then the extra individual will be initialised using grow.
	 * 
	 * @return A <code>List</code> of newly generated
	 *         <code>GRCandidatePrograms</code>.
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to populate.
		final List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(
				popSize);

		final int minDepthPossible = grammar.getMinimumDepth();
		if (minDepth < minDepthPossible) {
			// Our start depth can only be as small as the grammars minimum
			// depth.
			minDepth = minDepthPossible;
		}

		if (maxInitialDepth < 2) {
			throw new IllegalArgumentException(
					"Initial maximum depth must be greater than 1 for RH+H.");
		}

		// Number of programs each depth SHOULD have. But won't unless remainder
		// is 0.
		final double programsPerDepth = (double) popSize
				/ (maxInitialDepth - minDepth + 1);

		for (int i = 0; i < popSize; i++) {
			// Calculate depth
			final int depth = (int) Math.floor((i / programsPerDepth)
					+ minDepth);

			// Grow on even numbers, full on odd.
			GRCandidateProgram program;

			do {
				if ((i % 2) == 0) {
					program = grow.getInitialProgram(depth);
				} else {
					program = full.getInitialProgram(depth);
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
	public void setDuplicatesEnabled(boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
	}
}
