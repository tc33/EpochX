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
package org.epochx.ge.init;

import static org.epochx.Config.Template.TEMPLATE;
import static org.epochx.Population.SIZE;
import static org.epochx.ge.Codon.*;
import static org.epochx.ge.CodonFactory.CODON_FACTORY;
import static org.epochx.ge.GEIndividual.MAXIMUM_DEPTH;
import static org.epochx.grammar.Grammar.GRAMMAR;

import java.util.*;

import org.epochx.*;
import org.epochx.event.*;
import org.epochx.ge.*;
import org.epochx.grammar.*;

/**
 * Initialisation method which produces <tt>GEIndividual</tt>s with chromosomes
 * that map to parse trees within a specified maximum depth. Since the
 * initialisation is tied to the parse tree, an internal mapping is used which
 * is equivalent to a depth-first mapping.
 * 
 * <p>
 * See the {@link #setup()} method documentation for a list of configuration
 * parameters used to control this operator.
 * 
 * @see FixedLengthInitialisation
 * @see FullInitialisation
 * @see RampedHalfAndHalfInitialisation
 * 
 * @since 2.0
 */
public class GrowInitialisation implements GEInitialisation, Listener<ConfigEvent> {

	// Configuration settings
	private RandomSequence random;
	private Grammar grammar;
	private long maxCodonValue;
	private long minCodonValue;
	private int populationSize;
	private int maxDepth;
	private boolean allowDuplicates;
	private CodonFactory codonFactory;

	/**
	 * Constructs a <tt>GrowInitialisation</tt> with control parameters
	 * automatically loaded from the config
	 */
	public GrowInitialisation() {
		this(true);
	}

	/**
	 * Constructs a <tt>GrowInitialisation</tt> with control parameters
	 * initially loaded from the config. If the <tt>autoConfig</tt> argument is
	 * set to <tt>true</tt> then the configuration will be automatically updated
	 * when the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public GrowInitialisation(boolean autoConfig) {
		// Default config values
		maxCodonValue = Long.MAX_VALUE;
		minCodonValue = 0L;
		
		setup();

		if (autoConfig) {
			EventManager.getInstance().add(ConfigEvent.class, this);
		}
	}

	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <tt>ConfigEvent</tt> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li>{@link Population#SIZE}
	 * <li>{@link InitialisationMethod#ALLOW_DUPLICATES} (default: <tt>true</tt>)
	 * <li>{@link Grammar#GRAMMAR}
	 * <li>{@link Codon#MAXIMUM_VALUE} (default: <tt>Long.MAX_VALUE</tt>)
	 * <li>{@link Codon#MINIMUM_VALUE} (default: <tt>0L</tt>)
	 * <li>{@link GEIndividual#MAXIMUM_DEPTH}
	 * <li>{@link CodonFactory#CODON_FACTORY}
	 * </ul>
	 */
	protected void setup() {
		populationSize = Config.getInstance().get(SIZE);
		allowDuplicates = Config.getInstance().get(ALLOW_DUPLICATES, true);
		grammar = Config.getInstance().get(GRAMMAR);
		maxCodonValue = Config.getInstance().get(MAXIMUM_VALUE, maxCodonValue);
		minCodonValue = Config.getInstance().get(MINIMUM_VALUE, minCodonValue);
		maxDepth = Config.getInstance().get(MAXIMUM_DEPTH);
		codonFactory = Config.getInstance().get(CODON_FACTORY);
	}

	/**
	 * Receives configuration events and triggers this operator to reconfigure
	 * if the <tt>ConfigEvent</tt> is for one of its required parameters
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(TEMPLATE, SIZE, ALLOW_DUPLICATES, GRAMMAR, MAXIMUM_VALUE, MINIMUM_VALUE, MAXIMUM_DEPTH, CODON_FACTORY)) {
			setup();
		}
	}

	/**
	 * Creates a population of new <tt>GEIndividuals</tt>. Each individual is
	 * created by a call to the<tt>createIndividual</tt> method. The size of the
	 * population will be equal to the {@link Population#SIZE} config parameter.
	 * If the {@link InitialisationMethod#ALLOW_DUPLICATES} config parameter is
	 * set to <tt>false</tt> then the individuals in the population will be
	 * unique according to their <tt>equals</tt> methods. By default, duplicates
	 * are allowed.
	 * 
	 * @return a population of <tt>GEIndividual</tt> objects
	 */
	@Override
	public Population createPopulation() {
		EventManager.getInstance().fire(new InitialisationEvent.StartInitialisation());

		Population population = new Population();

		for (int i = 0; i < populationSize; i++) {
			GEIndividual individual;
			do {
				individual = createIndividual();
			} while (!allowDuplicates && population.contains(individual));

			population.add(individual);
		}

		EventManager.getInstance().fire(new InitialisationEvent.EndInitialisation(population));

		return population;
	}

	/**
	 * Constructs a new <tt>GEIndividual</tt> with a sequence of codons that map
	 * to a derivation tree using the currently set grammar which has a depth at
	 * most equal to the max depth property
	 * 
	 * @return a new <tt>GEIndividual</tt> instance
	 */
	@Override
	public GEIndividual createIndividual() {
		if (random == null) {
			throw new IllegalStateException("no random number generator has been set");
		} else if (grammar == null) {
			throw new IllegalStateException("no grammar has been set");
		}

		GrammarRule start = grammar.getStartRule();

		// Determine the minimum depth possible for a valid program.
		int minDepth = start.getMinDepth();
		if (minDepth > maxDepth) {
			throw new IllegalStateException("no possible programs within given max depth parameter for this grammar.");
		}

		Chromosome codons = new Chromosome();

		// Fill in the list of codons with reference to the grammar.
		fillCodons(codons, start, 0, maxDepth);

		return new GEIndividual(codons);
	}

	/*
	 * Constructs a full parse tree by making appropriate production choices
	 * and then filling in a randomly selected codon that matches the production
	 * choice.
	 */
	private void fillCodons(Chromosome codons, GrammarNode rule, int depth, int maxDepth) {
		if (rule instanceof GrammarRule) {
			GrammarRule nt = (GrammarRule) rule;

			// Check if theres more than one production.
			int productionIndex = 0;
			int noProductions = nt.getNoProductions();
			if (noProductions > 1) {
				List<Integer> validProductions = validProductions(nt.getProductions(), maxDepth - depth - 1);

				// Choose a production randomly.
				int chosenProduction = random.nextInt(validProductions.size());
				productionIndex = validProductions.get(chosenProduction);

				// Scale the production index up to get our new codon.
				long codonValue = scaleUp(productionIndex, noProductions);

				codons.appendCodon(codonFactory.codon(codonValue));
			}

			// Drop down the tree at this production.
			GrammarProduction p = nt.getProduction(productionIndex);

			List<GrammarNode> symbols = p.getGrammarNodes();
			for (GrammarNode s: symbols) {
				fillCodons(codons, s, depth + 1, maxDepth);
			}
		}
	}

	/*
	 * Helper method for fillCodons which determines which of a set of
	 * productions are valid choices to meet the depth constraints.
	 */
	private List<Integer> validProductions(List<GrammarProduction> grammarProductions, int maxDepth) {
		List<Integer> valid = new ArrayList<Integer>();

		for (int i = 0; i < grammarProductions.size(); i++) {
			GrammarProduction p = grammarProductions.get(i);

			if (p.getMinDepth() <= maxDepth) {
				valid.add(i);
			}
		}

		return valid;
	}

	/*
	 * Scales up a production choice up to a random number inside the codon size
	 * limits, while maintaining the modulo of the number
	 */
	private long scaleUp(int productionIndex, int noProductions) {
		// TODO This is the same as the one used in FullInitialisation - should
		// be moved to utilities class
		long range = maxCodonValue - minCodonValue;
		long value = random.nextLong(range - noProductions);
		value += minCodonValue;

		// Comparing separate index count saves us %ing large values
		long index = value % noProductions;
		while ((index % noProductions) != productionIndex) {
			value++;
			index++;
		}

		return value;
	}

	/**
	 * Returns whether or not duplicates are currently allowed in generated
	 * populations
	 * 
	 * @return <tt>true</tt> if duplicates are currently allowed in populations
	 *         generated by the <tt>createPopulation</tt> method and
	 *         <tt>false</tt> otherwise
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
	 * Returns the grammar that the <tt>GEIndividual</tt>s will satisfy with
	 * full parse trees
	 * 
	 * @return the currently set grammar
	 */
	public Grammar getGrammar() {
		return grammar;
	}

	/**
	 * Sets the grammar to be satisfied by the full parse trees of the new
	 * <tt>GEIndividual</tt>s. If automatic configuration is enabled then any
	 * value set here will be overwritten by the {@link Grammar#GRAMMAR}
	 * configuration setting on the next config event.
	 * 
	 * @param grammar the grammar to set
	 */
	public void setGrammar(Grammar grammar) {
		this.grammar = grammar;
	}

	/**
	 * Returns the number of individuals to be generated in a population created
	 * by the <tt>createPopulation</tt> method
	 * 
	 * @return the size of the populations generated
	 */
	public int getPopulationSize() {
		return populationSize;
	}

	/**
	 * Sets the number of individuals to be generated in a population created
	 * by the <tt>createPopulation</tt> method. If automatic configuration is
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
	 * <tt>createIndividual</tt> method. If automatic configuration is enabled
	 * then any value set here will be overwritten by the
	 * {@link GEIndividual#MAXIMUM_DEPTH} configuration setting on
	 * the next config event.
	 * 
	 * @param maxDepth the maximum depth of all parse trees generated
	 */
	public void setMaximumDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * Returns the maximum value that codons chosen by this initialiser are
	 * allowed to have
	 * 
	 * @return the maximum value of codons selected by this initialiser
	 */
	public long getMaximimCodonValue() {
		return maxCodonValue;
	}

	/**
	 * Sets the maximum value that codons chosen by this initialiser are
	 * allowed to have. If automatic configuration is enabled
	 * then any value set here will be overwritten by the
	 * {@link Codon#MAXIMUM_VALUE} configuration setting on the next config
	 * event.
	 * 
	 * @param maxCodonValue the maximum value for codons selected by this
	 *        initialiser
	 */
	public void setMaximumCodonValue(long maxCodonValue) {
		this.maxCodonValue = maxCodonValue;
	}

	/**
	 * Returns the maximum value that codons chosen by this initialiser are
	 * allowed to have
	 * 
	 * @return the maximum value of codons selected by this initialiser
	 */
	public long getMinimumCodonValue() {
		return minCodonValue;
	}

	/**
	 * Sets the maximum value that codons chosen by this initialiser are
	 * allowed to have. If automatic configuration is enabled
	 * then any value set here will be overwritten by the
	 * {@link Codon#MAXIMUM_VALUE} configuration setting on the next config
	 * event.
	 * 
	 * @param minCodonValue the maximum value for codons selected by this
	 *        initialiser
	 */
	public void setMinimumCodonValue(long minCodonValue) {
		this.minCodonValue = minCodonValue;
	}
	
	/**
	 * Returns the <tt>CodonFactory</tt> currently in use
	 * 
	 * @return the codon factory being used to generate new codons
	 */
	public CodonFactory getCodonFactory() {
		return codonFactory;
	}

	/**
	 * Sets the <tt>CodonFactory</tt> this initialiser should use to generate
	 * new codon instances. If automatic configuration is enabled
	 * then any value set here will be overwritten by the
	 * {@link CodonFactory#CODON_FACTORY} configuration setting on the next config
	 * event.
	 * 
	 * @param codonFactory the codon factory to use to generate new codons
	 */
	public void setCodonFactory(CodonFactory codonFactory) {
		this.codonFactory = codonFactory;
	}
}
