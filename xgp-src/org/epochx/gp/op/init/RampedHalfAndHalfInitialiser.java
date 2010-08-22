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
package org.epochx.gp.op.init;

import java.util.*;

import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.*;
import org.epochx.life.ConfigAdapter;
import org.epochx.representation.CandidateProgram;

/**
 * Initialisation implementation which uses a combination of full and grow
 * initialisers to create an initial population of
 * <code>GPCandidatePrograms</code>.
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
 * <li>syntax</li>
 * <li>random number generator</li>
 * </ul>
 * 
 * @see FullInitialiser
 * @see GrowInitialiser
 */
public class RampedHalfAndHalfInitialiser implements GPInitialiser {

	// The current controlling model.
	private final GPModel model;

	// The grow and full instances for doing their share of the work.
	private final GrowInitialiser grow;
	private final FullInitialiser full;

	// The size of the populations to construct.
	private int popSize;

	// The depth limits of each program tree to generate.
	private int initialMaxDepth;
	private int initialMinDepth;

	// Whether programs must be unique in generated populations.
	private boolean acceptDuplicates;

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events. Duplicate programs are allowed in the populations that
	 * are constructed.
	 * 
	 * @param model the <code>Model</code> instance from which the necessary
	 *        parameters should be loaded.
	 */
	public RampedHalfAndHalfInitialiser(final GPModel model) {
		this(model, 2);
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
	public RampedHalfAndHalfInitialiser(final GPModel model, final int minDepth) {
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
	public RampedHalfAndHalfInitialiser(final GPModel model,
			final int minDepth, final boolean acceptDuplicates) {
		this.model = model;
		this.initialMinDepth = minDepth;
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
		popSize = model.getPopulationSize();
		initialMaxDepth = model.getMaxInitialDepth();
	}

	/**
	 * Generates a population of new <code>CandidatePrograms</code> constructed
	 * from the <code>Nodes</code> in the syntax attribute. The size of the
	 * population will be equal to the population size attribute. All programs
	 * in the population are only guarenteed to be unique (as defined by the
	 * <code>equals</code> method on <code>GPCandidateProgram</code>) if the
	 * <code>isDuplicatesEnabled</code> method returns <code>true</code>.
	 * Each program will alternately be generated with the
	 * {@link FullInitialiser} and {@link GrowInitialiser}. If the population
	 * size is odd then the extra individual will be initialised using grow.
	 * 
	 * @return A <code>List</code> of newly generated
	 *         <code>CandidatePrograms</code>.
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to populate.
		final List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(
				popSize);

		final int startDepth = initialMinDepth;

		if (initialMaxDepth < initialMinDepth) {
			throw new IllegalArgumentException(
					"Initial maximum depth must be greater than the start depth.");
		}

		// Number of programs each depth SHOULD have. But won't unless remainder
		// is 0.
		final double programsPerDepth = (double) popSize
				/ (initialMaxDepth - startDepth + 1);

		for (int i = 0; i < popSize; i++) {
			// Calculate depth
			final int depth = (int) Math
					.floor((firstGen.size() / programsPerDepth) + startDepth);

			// Grow on even numbers, full on odd.
			GPCandidateProgram program;

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
	 * Returns the minimum initial depth from which depths are being ramped.
	 * 
	 * @return the minimum depth that programs are being generated down to.
	 */
	public int getMinDepth() {
		return initialMinDepth;
	}

	/**
	 * Sets the minimum initial depth from which depths are being ramped.
	 * 
	 * @param minDepth the minimum depth that programs should be generated down
	 *        to.
	 */
	public void setMinDepth(final int minDepth) {
		this.initialMinDepth = minDepth;
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
