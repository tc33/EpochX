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
import org.epochx.tools.grammar.*;
import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Initialisation implementation which randomly grows program parse trees down
 * to a maximum depth.
 * 
 * <p>
 * If a model is provided then the following parameters are loaded upon every
 * configure event:
 * 
 * <ul>
 * <li>population size</li>
 * <li>maximum initial program depth</li>
 * <li>grammar</li>
 * <li>random number generator</li>
 * </ul>
 * 
 * @see FullInitialiser
 * @see RampedHalfAndHalfInitialiser
 */
public class GrowInitialiser implements GRInitialiser {

	// The controlling model.
	private final GRModel model;

	private RandomNumberGenerator rng;

	// The grammar all new programs must be valid against.
	private Grammar grammar;

	// The size of the populations to construct.
	private int popSize;

	// The maximum depth of each program tree to generate.
	private int initialMaxDepth;

	// Whether programs must be unique in generated populations.
	private boolean acceptDuplicates;

	/**
	 * Constructs a <code>GrowInitialiser</code> with the necessary parameters
	 * loaded from the given model. The parameters are reloaded on configure
	 * events. Duplicate programs are allowed in the populations that are
	 * constructed.
	 * 
	 * @param model the <code>GRModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 */
	public GrowInitialiser(final GRModel model) {
		this(model, true);
	}

	/**
	 * Constructs a <code>GrowInitialiser</code> with the necessary parameters
	 * loaded from the given model. The parameters are reloaded on configure
	 * events.
	 * 
	 * @param model the <code>GRModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param acceptDuplicates whether duplicates should be allowed in the
	 *        populations that are generated.
	 */
	public GrowInitialiser(final GRModel model, final boolean acceptDuplicates) {
		this.model = model;
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
		rng = model.getRNG();
		grammar = model.getGrammar();
		popSize = model.getPopulationSize();
		initialMaxDepth = model.getMaxInitialDepth();
	}

	/**
	 * Generates a population of new <code>GRCandidatePrograms</code>
	 * constructed
	 * from the <code>Grammar</code> attribute. The size of the
	 * population will be equal to the population size attribute. All programs
	 * in the population are only guarenteed to be unique (as defined by the
	 * <code>equals</code> method on <code>GRCandidateProgram</code>) if the
	 * <code>isDuplicatesEnabled</code> method returns <code>true</code>.
	 * 
	 * @return A <code>List</code> of newly generated
	 *         <code>GRCandidatePrograms</code>.
	 */
	@Override
	public List<CandidateProgram> getInitialPopulation() {
		// Create population list to be populated.
		final List<CandidateProgram> firstGen = new ArrayList<CandidateProgram>(
				popSize);

		// Create and add new programs to the population.
		for (int i = 0; i < popSize; i++) {
			GRCandidateProgram candidate;

			do {
				// Create a new program down to the models initial max depth.
				candidate = getInitialProgram(initialMaxDepth);
			} while (!acceptDuplicates && firstGen.contains(candidate));

			// Add to the new population.
			firstGen.add(candidate);
		}

		return firstGen;
	}

	/**
	 * Constructs and returns a new <code>GRCandidateProgram</code> with a grown
	 * parse tree with the given maximum depth.
	 * 
	 * @param maxDepth The maximum depth of the parse tree, where the
	 *        depth is the number of nodes from the root.
	 * @return A new <code>GRCandidateProgram</code> with a grown parse tree.
	 */
	public GRCandidateProgram getInitialProgram(final int maxDepth) {
		final GrammarRule startRule = grammar.getStartRule();

		return new GRCandidateProgram(growParseTree(maxDepth, startRule), model);
	}

	/**
	 * Grows and returns a new parse tree with a maximum depth of the specified
	 * maxDepth parameter.
	 * 
	 * @param maxDepth The maximum depth of the parse tree, where the
	 *        depth is the number of nodes from the root.
	 * @return The root node of a randomly generated full parse tree of the
	 *         requested depth.
	 */
	public NonTerminalSymbol growParseTree(final int maxDepth,
			final GrammarRule startRule) {
		final NonTerminalSymbol parseTree = new NonTerminalSymbol(startRule);

		buildDerivationTree(parseTree, startRule, 0, maxDepth);

		return parseTree;
	}

	/*
	 * Recursive helper for the growParseTree method.
	 */
	private void buildDerivationTree(final NonTerminalSymbol parseTree,
			final GrammarRule rule, final int depth, final int maxDepth) {
		// Check if theres more than one production.
		int productionIndex = 0;
		final int noProductions = rule.getNoProductions();
		if (noProductions > 1) {
			final List<Integer> validProductions = getValidProductionIndexes(
					rule.getProductions(), maxDepth - depth - 1);

			// Choose a production randomly.
			final int chosenProduction = rng.nextInt(validProductions.size());
			productionIndex = validProductions.get(chosenProduction);
		}

		// Drop down the tree at this production.
		final GrammarProduction p = rule.getProduction(productionIndex);

		final List<GrammarNode> grammarNodes = p.getGrammarNodes();
		for (final GrammarNode node: grammarNodes) {
			if (node instanceof GrammarRule) {
				final GrammarRule r = (GrammarRule) node;

				final NonTerminalSymbol nt = new NonTerminalSymbol(
						(GrammarRule) node);

				buildDerivationTree(nt, r, depth + 1, maxDepth);

				parseTree.addChild(nt);
			} else {
				// Must be a grammar literal.
				parseTree.addChild(new TerminalSymbol((GrammarLiteral) node));
			}
		}
	}

	/*
	 * Gets a List of indexes to those productions from the List of productions
	 * given that can be used with the specified maximum depth constraint.
	 */
	private List<Integer> getValidProductionIndexes(
			final List<GrammarProduction> grammarProductions, final int maxDepth) {
		final List<Integer> valid = new ArrayList<Integer>();

		for (int i = 0; i < grammarProductions.size(); i++) {
			final GrammarProduction p = grammarProductions.get(i);

			if (p.getMinDepth() <= maxDepth) {
				valid.add(i);
			}
		}

		// If there were any valid recursive productions, return them, otherwise
		// use the others.
		return valid;
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
