/*
 * Copyright 2007-2013
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.gr.init;

import static org.epochx.Config.Template.TEMPLATE;
import static org.epochx.Population.SIZE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.gr.GRIndividual.MAXIMUM_DEPTH;
import static org.epochx.grammar.Grammar.GRAMMAR;

import java.util.*;

import org.epochx.Config;
import org.epochx.InitialisationMethod;
import org.epochx.Population;
import org.epochx.RandomSequence;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.InitialisationEvent;
import org.epochx.event.Listener;
import org.epochx.gr.GRIndividual;
import org.epochx.grammar.*;

/**
 * Initialisation method which produces <code>GRIndividual</code>s with parse 
 * trees within a specified maximum depth. Parse trees are constructed randomly with 
 * reference to the current grammar.
 * 
 * <p>
 * See the {@link #setup()} method documentation for a list of configuration
 * parameters used to control this operator.
 * 
 * @see FullInitialisation
 * @see RampedHalfAndHalfInitialisation
 * 
 * @since 2.0
 */
public class GrowInitialisation implements GRInitialisation, Listener<ConfigEvent> {

	// Configuration settings
	private RandomSequence random;
	private Grammar grammar;
	private Integer populationSize;
	private Integer maxDepth;
	private Boolean allowDuplicates;

	/**
	 * Constructs a <code>GrowInitialisation</code> with control parameters
	 * automatically loaded from the config
	 */
	public GrowInitialisation() {
		this(true);
	}

	/**
	 * Constructs a <code>GrowInitialisation</code> with control parameters
	 * initially loaded from the config. If the <code>autoConfig</code> argument is
	 * set to <code>true</code> then the configuration will be automatically updated
	 * when the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public GrowInitialisation(boolean autoConfig) {
		// Default config values
		allowDuplicates = true;
		
		setup();

		if (autoConfig) {
			EventManager.getInstance().add(ConfigEvent.class, this);
		}
	}

	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <code>ConfigEvent</code> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}
	 * <li>{@link Population#SIZE}
	 * <li>{@link InitialisationMethod#ALLOW_DUPLICATES} (default: <code>true</code>)
	 * <li>{@link Grammar#GRAMMAR}
	 * <li>{@link GRIndividual#MAXIMUM_DEPTH}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		populationSize = Config.getInstance().get(SIZE);
		allowDuplicates = Config.getInstance().get(ALLOW_DUPLICATES, allowDuplicates);
		grammar = Config.getInstance().get(GRAMMAR);
		maxDepth = Config.getInstance().get(MAXIMUM_DEPTH);
	}
	
	/**
	 * Receives configuration events and triggers this operator to reconfigure
	 * if the <code>ConfigEvent</code> is for one of its required parameters
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(TEMPLATE, RANDOM_SEQUENCE, SIZE, ALLOW_DUPLICATES, GRAMMAR, MAXIMUM_DEPTH)) {
			setup();
		}
	}
	
	/**
	 * Creates a population of new <code>GRIndividuals</code>. Each individual is
	 * created by a call to the<code>createIndividual</code> method. The size of the
	 * population will be equal to the {@link Population#SIZE} config parameter.
	 * If the {@link InitialisationMethod#ALLOW_DUPLICATES} config parameter is
	 * set to <code>false</code> then the individuals in the population will be
	 * unique according to their <code>equals</code> methods. By default, duplicates
	 * are allowed.
	 * 
	 * @return a population of <code>GRIndividual</code> objects
	 */
	@Override
	public Population createPopulation() {
		EventManager.getInstance().fire(new InitialisationEvent.StartInitialisation());

		Population population = new Population();

		for (int i = 0; i < populationSize; i++) {
			GRIndividual individual;
			do {
				individual = createIndividual();
			} while (!allowDuplicates && population.contains(individual));

			population.add(individual);
		}

		EventManager.getInstance().fire(new InitialisationEvent.EndInitialisation(population));

		return population;
	}
	
	/**
	 * Constructs a new <code>GRIndividual</code> with a derivation tree which has a depth at
	 * most equal to the max depth property
	 * 
	 * @return a new <code>GRIndividual</code> instance
	 */
	@Override
	public GRIndividual createIndividual() {
		if (random == null) {
			throw new IllegalStateException("no random number generator has been set");
		} else if (grammar == null) {
			throw new IllegalStateException("no grammar has been set");
		}

		GrammarRule startRule = grammar.getStartRule();

		// Determine the minimum depth possible for a valid program
		int minDepth = startRule.getMinDepth();
		if (minDepth > maxDepth) {
			throw new IllegalStateException("no possible programs within given max depth parameter for this grammar");
		}

		NonTerminalSymbol parseTree = growParseTree(maxDepth, startRule);
		
		return new GRIndividual(parseTree);
	}

	/**
	 * Grows and returns a new parse tree with a maximum depth as specified by the maxDepth parameter
	 * 
	 * @param maxDepth The maximum depth of the parse tree, where depth is the number of nodes from the root
	 * @return The root node of a randomly generated parse tree
	 */
	public NonTerminalSymbol growParseTree(int maxDepth, GrammarRule startRule) {
		if (random == null) {
			throw new IllegalStateException("no random number generator has been set");
		} else if (maxDepth < 1) {
			throw new IllegalStateException("maximum depth must be 1 or greater");
		}

		NonTerminalSymbol parseTree = new NonTerminalSymbol(startRule);

		buildDerivationTree(parseTree, startRule, 0, maxDepth);

		return parseTree;
	}

	/*
	 * Recursive helper for the growParseTree method
	 */
	private void buildDerivationTree(NonTerminalSymbol parseTree, GrammarRule rule, int currentDepth, int maxDepth) {
		// Check if there's more than one production
		int productionIndex = 0;
		int noProductions = rule.getNoProductions();
		if (noProductions > 1) {
			int nextDepth = maxDepth - currentDepth - 1;
			List<Integer> validProductions = validProductionIndexes(rule.getProductions(), nextDepth);

			// Choose a production randomly
			int chosenProduction = random.nextInt(validProductions.size());
			productionIndex = validProductions.get(chosenProduction);
		}

		// Drop down the tree at this production
		GrammarProduction p = rule.getProduction(productionIndex);

		List<GrammarNode> grammarNodes = p.getGrammarNodes();
		for (GrammarNode node: grammarNodes) {
			if (node instanceof GrammarRule) {
				// Create the parse tree symbol
				GrammarRule r = (GrammarRule) node;
				NonTerminalSymbol nt = new NonTerminalSymbol(r);

				// Build this symbol's subtree
				buildDerivationTree(nt, r, currentDepth + 1, maxDepth);

				parseTree.addChild(nt);
			} else {
				// Must be a grammar literal
				parseTree.addChild(new TerminalSymbol((GrammarLiteral) node));
			}
		}
	}

	/*
	 * Returns a list of indexes for the valid productions in the list given. A valid production
	 * is one which can be used within the specified maximum depth constraint.
	 */
	private List<Integer> validProductionIndexes(List<GrammarProduction> grammarProductions, int maxDepth) {
		List<Integer> valid = new ArrayList<Integer>();

		for (int i = 0; i < grammarProductions.size(); i++) {
			GrammarProduction p = grammarProductions.get(i);

			if (p.getMinDepth() <= maxDepth) {
				valid.add(i);
			}
		}

		return valid;
	}

	/**
	 * Returns the random number sequence in use
	 * 
	 * @return the currently set random sequence
	 */
	public RandomSequence getRandomSequence() {
		return random;
	}

	/**
	 * Sets the random number sequence to use. If automatic configuration is
	 * enabled then any value set here will be overwritten by the
	 * {@link RandomSequence#RANDOM_SEQUENCE} configuration setting on the next
	 * config event.
	 * 
	 * @param random the random number generator to set
	 */
	public void setRandomSequence(RandomSequence random) {
		this.random = random;
	}

	/**
	 * Returns whether or not duplicates are currently allowed in generated
	 * populations
	 * 
	 * @return <code>true</code> if duplicates are currently allowed in populations
	 *         generated by the <code>createPopulation</code> method and
	 *         <code>false</code> otherwise
	 */
	public boolean isDuplicatesEnabled() {
		return allowDuplicates;
	}

	/**
	 * Sets whether duplicates should be allowed in populations that are
	 * generated. If automatic configuration is enabled then any value set here
	 * will be overwritten by the {@link InitialisationMethod#ALLOW_DUPLICATES}
	 * configuration setting on the next config event.
	 * 
	 * @param allowDuplicates whether duplicates should be allowed in
	 *        populations that are generated
	 */
	public void setDuplicatesEnabled(boolean allowDuplicates) {
		this.allowDuplicates = allowDuplicates;
	}
	
	/**
	 * Returns the grammar that the <code>GRIndividual</code>s will satisfy with
	 * their parse trees
	 * 
	 * @return the currently set grammar
	 */
	public Grammar getGrammar() {
		return grammar;
	}

	/**
	 * Sets the grammar to be satisfied by the parse trees of the new <code>GRIndividual</code>s. 
	 * If automatic configuration is enabled then any value set here will be overwritten by the 
	 * {@link Grammar#GRAMMAR} configuration setting on the next config event.
	 * 
	 * @param grammar the grammar to set
	 */
	public void setGrammar(Grammar grammar) {
		this.grammar = grammar;
	}

	/**
	 * Returns the number of individuals to be generated in a population created
	 * by the <code>createPopulation</code> method
	 * 
	 * @return the size of the populations generated
	 */
	public int getPopulationSize() {
		return populationSize;
	}

	/**
	 * Sets the number of individuals to be generated in a population created
	 * by the <code>createPopulation</code> method. If automatic configuration is
	 * enabled thenAny value set here will be overwritten by the
	 * {@link Population#SIZE} configuration setting on the next config event.
	 * 
	 * @param size the size of the populations generated
	 */
	public void setPopulationSize(int size) {
		this.populationSize = size;
	}

	/**
	 * Returns the maximum depth of the parse trees generated with this
	 * initialisation method
	 * 
	 * @return the maximum depth of the parse trees constructed
	 */
	public int getMaximumDepth() {
		return maxDepth;
	}

	/**
	 * Sets the maximum depth of the parse trees created by the
	 * <code>createIndividual</code> method. If automatic configuration is enabled
	 * then any value set here will be overwritten by the
	 * {@link GRIndividual#MAXIMUM_DEPTH} configuration setting on
	 * the next config event.
	 * 
	 * @param maxDepth the maximum depth of all parse trees generated
	 */
	public void setMaximumDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}
}
