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
package org.epochx.cfg.init;

import static org.epochx.Config.Template.TEMPLATE;
import static org.epochx.Population.SIZE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.cfg.CFGIndividual.MAXIMUM_DEPTH;
import static org.epochx.grammar.Grammar.GRAMMAR;

import java.util.*;

import org.epochx.Config;
import org.epochx.InitialisationMethod;
import org.epochx.Population;
import org.epochx.RandomSequence;
import org.epochx.cfg.CFGIndividual;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.InitialisationEvent;
import org.epochx.event.Listener;
import org.epochx.grammar.*;

/**
 * Initialisation method which produces <code>CFGIndividual</code>s with full parse 
 * trees at the specified depth. Parse trees are constructed randomly with 
 * reference to the current grammar. 
 * 
 * <p>
 * Full parse trees are achieved by only selecting from the recursive production rules 
 * when possible, in order to keep extend the tree down to the specified depth. If there
 * are no recursive production rules in the grammar then the parse trees constructed are
 * not guaranteed to be full depth.
 * 
 * <p>
 * See the {@link #setup()} method documentation for a list of configuration
 * parameters used to control this operator.
 * 
 * @see GrowInitialisation
 * @see RampedHalfAndHalfInitialisation
 * 
 * @since 2.0
 */
public class FullInitialisation implements CFGInitialisation, Listener<ConfigEvent> {

	// Configuration settings
	private RandomSequence random;
	private Grammar grammar;
	private Integer populationSize;
	private Integer depth;
	private Boolean allowDuplicates;

	/**
	 * Constructs a <code>FullInitialisation</code> with control parameters
	 * automatically loaded from the config
	 */
	public FullInitialisation() {
		this(true);
	}

	/**
	 * Constructs a <code>FullInitialisation</code> with control parameters
	 * initially loaded from the config. If the <code>autoConfig</code> argument is
	 * set to <code>true</code> then the configuration will be automatically updated
	 * when the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public FullInitialisation(boolean autoConfig) {
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
	 * <li>{@link CFGIndividual#MAXIMUM_DEPTH}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		populationSize = Config.getInstance().get(SIZE);
		allowDuplicates = Config.getInstance().get(ALLOW_DUPLICATES, allowDuplicates);
		grammar = Config.getInstance().get(GRAMMAR);
		depth = Config.getInstance().get(MAXIMUM_DEPTH);
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
	 * Creates a population of new <code>CFGIndividuals</code> with full parse trees. Each 
	 * individual is created by a call to the<code>createIndividual</code> method. The size 
	 * of the population will be equal to the {@link Population#SIZE} config parameter.
	 * If the {@link InitialisationMethod#ALLOW_DUPLICATES} config parameter is
	 * set to <code>false</code> then the individuals in the population will be
	 * unique according to their <code>equals</code> methods. By default, duplicates
	 * are allowed.
	 * 
	 * @return a population of <code>CFGIndividual</code> objects
	 */
	@Override
	public Population createPopulation() {
		EventManager.getInstance().fire(new InitialisationEvent.StartInitialisation());

		Population population = new Population();

		for (int i = 0; i < populationSize; i++) {
			CFGIndividual individual;
			do {
				individual = createIndividual();
			} while (!allowDuplicates && population.contains(individual));

			population.add(individual);
		}

		EventManager.getInstance().fire(new InitialisationEvent.EndInitialisation(population));

		return population;
	}

	/**
	 * Constructs a new <code>CFGIndividual</code> with a full derivation tree
	 * 
	 * @return a new <code>CFGIndividual</code> instance
	 */
	@Override
	public CFGIndividual createIndividual() {
		if (random == null) {
			throw new IllegalStateException("no random number generator has been set");
		} else if (grammar == null) {
			throw new IllegalStateException("no grammar has been set");
		}

		GrammarRule startRule = grammar.getStartRule();

		// Determine the minimum depth possible for a valid program
		int minDepth = startRule.getMinDepth();
		if (minDepth > depth) {
			throw new IllegalStateException("no possible programs within given depth parameter for this grammar");
		}

		NonTerminalSymbol parseTree = fullParseTree(depth, startRule);
		
		return new CFGIndividual(parseTree);
	}
	
	/**
	 * Builds and returns a new full parse tree with a depth as specified by the depth parameter
	 * 
	 * @param depth The depth of the parse tree, where depth is the number of nodes from the root
	 * @return The root node of a randomly generated parse tree
	 */
	public NonTerminalSymbol fullParseTree(int depth, GrammarRule startRule) {
		if (random == null) {
			throw new IllegalStateException("no random number generator has been set");
		} else if (depth < 1) {
			throw new IllegalStateException("maximum depth must be 1 or greater");
		}

		NonTerminalSymbol parseTree = new NonTerminalSymbol(startRule);

		buildDerivationTree(parseTree, startRule, 0, depth);

		return parseTree;
	}

	/*
	 * Builds a full parse tree from the given non-terminal symbol using the grammar rule
	 */
	private void buildDerivationTree(NonTerminalSymbol parseTree, GrammarRule rule, int currentDepth, int maxDepth) {
		// Check if there's more than one production.
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
	 * is one which can be used to reach the specified depth.
	 */
	private List<Integer> validProductionIndexes(List<GrammarProduction> grammarProductions, int maxDepth) {
		List<Integer> validRecursive = new ArrayList<Integer>();
		List<Integer> validAll = new ArrayList<Integer>();

		for (int i = 0; i < grammarProductions.size(); i++) {
			GrammarProduction p = grammarProductions.get(i);

			if (p.getMinDepth() <= maxDepth) {
				validAll.add(i);

				if (p.isRecursive()) {
					validRecursive.add(i);
				}
			}
		}

		// If there were any valid recursive productions, return them, otherwise use the others.
		return validRecursive.isEmpty() ? validAll : validRecursive;
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
	 * Returns the grammar that the <code>CFGIndividual</code>s will satisfy with
	 * their parse trees
	 * 
	 * @return the currently set grammar
	 */
	public Grammar getGrammar() {
		return grammar;
	}

	/**
	 * Sets the grammar to be satisfied by the parse trees of the new <code>CFGIndividual</code>s. 
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
	 * Returns the depth of the parse trees generated with this initialisation method
	 * 
	 * @return the depth of the parse trees constructed
	 */
	public int getMaximumDepth() {
		return depth;
	}

	/**
	 * Sets the depth of the parse trees created by the <code>createIndividual</code> method. 
	 * If automatic configuration is enabled then any value set here will be overwritten by the
	 * {@link CFGIndividual#MAXIMUM_DEPTH} configuration setting on the next config event.
	 * 
	 * @param depth the depth of all parse trees generated
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}
}
