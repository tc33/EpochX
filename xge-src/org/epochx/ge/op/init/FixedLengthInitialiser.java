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

/**
 * Initialisation implementation that randomly generates a chromosome up to a
 * specified length.
 */
public class FixedLengthInitialiser implements GEInitialiser {

	// The controlling model.
	private final GEModel model;

	private int popSize;

	private int chromosomeLength;
	
	private boolean acceptDuplicates;

	public FixedLengthInitialiser(final GEModel model, final int chromosomeLength) {
		this(model, chromosomeLength, true);
	}
	
	/**
	 * Constructs a RandomInitialiser.
	 * 
	 * @param model The GE model that will provide any required control
	 *        parameters such as the desired population size.
	 * @param chromosomeLength The initial length that chromosomes should be
	 *        generated to.
	 */
	public FixedLengthInitialiser(final GEModel model,
			final int chromosomeLength, final boolean acceptDuplicates) {
		this.model = model;
		this.chromosomeLength = chromosomeLength;
		this.acceptDuplicates = acceptDuplicates;

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
	}

	/**
	 * Generate a population of new CandidatePrograms constructed by randomly
	 * generating their chromosomes. The size of the population will be equal
	 * to the result of calling getPopulationSize() on the controlling model.
	 * All programs in the population will be unique. Each candidate program
	 * will have a chromosome length equal to the chromosomeLength provided to
	 * the
	 * constructor.
	 * 
	 * @return A List of newly generated CandidatePrograms which will form the
	 *         initial population for a GE run.
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		// Initialise population of candidate programs.
		final List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(
				popSize);

		// Build population.
		for (int i=0; i<popSize; i++) {
			GECandidateProgram candidate;

			do {
				candidate = new GECandidateProgram(model);
				// Initialise the program.
				candidate.appendNewCodons(chromosomeLength);
	
				firstGen.add(candidate);
			} while (!acceptDuplicates && firstGen.contains(candidate));
		}

		// Return starting population.
		return firstGen;
	}

	/**
	 * Returns the length of chromosome that all new
	 * <code>CandidateProgram</code> instances will be generated with.
	 * 
	 * @return the fixed length that this initialiser is generating chromosomes
	 *         to.
	 */
	public int getChromosomeLength() {
		return chromosomeLength;
	}

	/**
	 * Sets the length of chromosome for all future
	 * <code>CandidateProgram</code> generations.
	 * 
	 * @param chromosomeLength the fixed length to use for the chromosomes of
	 *        generated programs.
	 */
	public void setChromosomeLength(int chromosomeLength) {
		this.chromosomeLength = chromosomeLength;
	}

	public boolean isDuplicatesEnabled() {
		return acceptDuplicates;
	}

	public void setDuplicatesEnabled(boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
	}
}
