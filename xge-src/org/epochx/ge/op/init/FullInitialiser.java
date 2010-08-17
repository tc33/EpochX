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
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Note: full initialisation currently only works for depth first mapping.
 * 
 */
public class FullInitialiser implements GEInitialiser {

	/*
	 * TODO This constructs the chromosome using depth first mapping - what
	 * about others?
	 */

	// The controlling model.
	private final GEModel model;

	private RandomNumberGenerator rng;
	private Grammar grammar;
	private int popSize;
	private int maxInitialProgramDepth;
	private int maxCodonSize;

	/**
	 * Constructs a full initialiser.
	 * 
	 * @param model
	 */
	public FullInitialiser(final GEModel model) {
		this.model = model;

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
		rng = model.getRNG();
		grammar = model.getGrammar();
		popSize = model.getPopulationSize();
		maxInitialProgramDepth = model.getMaxInitialDepth();
		maxCodonSize = model.getMaxCodonSize();
	}

	@Override
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to be populated.
		final List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(
				popSize);

		// Create and add new programs to the population.
		for (int i = 0; i < popSize; i++) {
			GECandidateProgram candidate;
			do {
				// Create a new program at the models initial max depth.
				candidate = getInitialProgram(maxInitialProgramDepth);
			} while (firstGen.contains(candidate));

			// Add to the new population.
			firstGen.add(candidate);
		}

		return firstGen;
	}

	public GECandidateProgram getInitialProgram(final int depth) {
		final GrammarNode start = grammar.getStartRule();

		// TODO Check it is possible to create a program inside the max depth.
		final List<Integer> codons = new ArrayList<Integer>();

		buildDerivationTree(codons, start, 0, depth);

		return new GECandidateProgram(codons, model);
	}

	private void buildDerivationTree(final List<Integer> codons,
			final GrammarNode rule, final int depth, final int maxDepth) {

		if (rule instanceof GrammarRule) {
			final GrammarRule nt = (GrammarRule) rule;

			// Check if theres more than one production.
			int productionIndex = 0;
			final int noProductions = nt.getNoProductions();
			if (noProductions > 1) {
				final List<Integer> validProductions = getValidProductionIndexes(
						nt.getProductions(), maxDepth - depth - 1);

				// Choose a production randomly.
				final int chosenProduction = rng.nextInt(validProductions
						.size());
				productionIndex = validProductions.get(chosenProduction);

				// Scale the production index up to get our new codon.
				final int codon = convertToCodon(productionIndex, noProductions);

				codons.add(codon);
			}

			// Drop down the tree at this production.
			final GrammarProduction p = nt.getProduction(productionIndex);

			final List<GrammarNode> symbols = p.getGrammarNodes();
			for (final GrammarNode s: symbols) {
				buildDerivationTree(codons, s, depth + 1, maxDepth);
			}
		} else {
			// Do nothing.
		}

	}

	private List<Integer> getValidProductionIndexes(
			final List<GrammarProduction> grammarProductions, final int maxDepth) {
		final List<Integer> validRecursive = new ArrayList<Integer>();
		final List<Integer> validAll = new ArrayList<Integer>();

		for (int i = 0; i < grammarProductions.size(); i++) {
			final GrammarProduction p = grammarProductions.get(i);

			if (p.getMinDepth() <= maxDepth) {
				validAll.add(i);

				if (p.isRecursive()) {
					validRecursive.add(i);
				}
			}
		}

		// If there were any valid recursive productions, return them, otherwise
		// use the others.
		return validRecursive.isEmpty() ? validAll : validRecursive;
	}

	/*
	 * Converts a production choice from a number of productions to a codon by
	 * scaling the production index up to a random number inside the model's
	 * max codon size limit, while maintaining the modulo of the number.
	 */
	private int convertToCodon(final int productionIndex,
			final int noProductions) {
		int codon = rng.nextInt(maxCodonSize - noProductions);

		// Increment codon until it is valid index.
		int currentIndex = codon % noProductions;
		// Comparing separate index count saves us %ing large ints.
		while ((currentIndex % noProductions) != productionIndex) {
			codon++;
			currentIndex++;
		}

		return codon;
	}

}
