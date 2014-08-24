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
import static org.epochx.cfg.init.RampedHalfAndHalfInitialisation.Method.FULL;
import static org.epochx.cfg.init.RampedHalfAndHalfInitialisation.Method.GROW;
import static org.epochx.grammar.Grammar.GRAMMAR;

import org.epochx.Config;
import org.epochx.Config.ConfigKey;
import org.epochx.InitialisationMethod;
import org.epochx.Population;
import org.epochx.RandomSequence;
import org.epochx.cfg.CFGIndividual;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.InitialisationEvent;
import org.epochx.event.Listener;
import org.epochx.grammar.Grammar;

/**
 * Initialisation implementation which uses a combination of full and grow
 * initialisers to create an initial population of <code>CFGIndividual</code>s.
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
 * See the {@link #setup()} method documentation for a list of configuration
 * parameters used to control this operator.
 * 
 * @see FullInitialisation
 * @see GrowInitialisation
 * 
 * @since 2.0
 */
public class RampedHalfAndHalfInitialisation implements CFGInitialisation, Listener<ConfigEvent> {

	/**
	 * The key for setting and retrieving the smallest maximum depth setting
	 * from which the ramping will begin
	 */
	public static final ConfigKey<Integer> RAMPING_START_DEPTH = new ConfigKey<Integer>();
	
	// The two initialisation methods to ramp
	private final GrowInitialisation grow;
	private final FullInitialisation full;
	
	// Configuration settings
	private RandomSequence random;
	private Grammar grammar;
	private Integer populationSize;
	private Boolean allowDuplicates;
	private Integer endDepth;
	private Integer startDepth;

	/**
	 * Initialisation method labels
	 */
	public enum Method {
		GROW, FULL;
	}

	
	/**
	 * Constructs a <code>RampedHalfAndHalfInitialisation</code> with control parameters
	 * automatically loaded from the config
	 */
	public RampedHalfAndHalfInitialisation() {
		this(true);
	}

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialisation</code> with control parameters
	 * initially loaded from the config. If the <code>autoConfig</code> argument is
	 * set to <code>true</code> then the configuration will be automatically updated
	 * when the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public RampedHalfAndHalfInitialisation(boolean autoConfig) {
		grow = new GrowInitialisation(false);
		full = new FullInitialisation(false);
		
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
	 * <li>{@link #RAMPING_START_DEPTH}
	 * <li>{@link CFGIndividual#MAXIMUM_DEPTH}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		populationSize = Config.getInstance().get(SIZE);
		allowDuplicates = Config.getInstance().get(ALLOW_DUPLICATES, allowDuplicates);
		grammar = Config.getInstance().get(GRAMMAR);
		startDepth = Config.getInstance().get(RAMPING_START_DEPTH);
		endDepth = Config.getInstance().get(MAXIMUM_DEPTH);
	}
	
	/**
	 * Receives configuration events and triggers this operator to reconfigure
	 * if the <code>ConfigEvent</code> is for one of its required parameters
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(TEMPLATE, RANDOM_SEQUENCE, SIZE, ALLOW_DUPLICATES, GRAMMAR, RAMPING_START_DEPTH, MAXIMUM_DEPTH)) {
			setup();
		}
	}
	
	/**
	 * Creates a new population of <code>CFGIndividual</code>s. Will use grow initialisation to construct half 
	 * the population and full to create the other half. If the population size is an odd number then the extra 
	 * individual will be initialised with grow.
	 * 
	 * @return a new population of individuals
	 */
	@Override
	public Population createPopulation() {
		EventManager.getInstance().fire(new InitialisationEvent.StartInitialisation());
		
		if (endDepth < startDepth) {
			throw new IllegalStateException("end depth must be greater than the start depth");
		}

		// Create population list to populate
		Population population = new Population();

		int currentDepth = startDepth;
		int minDepthPossible = grammar.getMinimumDepth();
		if (currentDepth < minDepthPossible) {
			// Our start depth can only be as small as the grammars minimum depth
			currentDepth = minDepthPossible;
		}

		if (endDepth < startDepth) {
			throw new IllegalStateException("end maximum depth must be greater than the minimum possible depth");
		}

		// Number of individuals each depth SHOULD have. But won't exactly unless the remainder is 0.
		double individualsPerDepth = (double) populationSize / (endDepth - startDepth + 1);

		// Whether each program was grown or not (full)
		Method[] method = new Method[populationSize];

		for (int i = 0; i < populationSize; i++) {
			// Calculate depth
			int depth = (int) Math.floor((population.size() / individualsPerDepth) + currentDepth);

			// Grow on even numbers, full on odd
			CFGIndividual individual;

			do {
				if ((i % 2) == 0) {
					method[i] = GROW;
					grow.setMaximumDepth(depth);
					individual = grow.createIndividual();
				} else {
					method[i] = FULL;
					full.setDepth(depth);
					individual = full.createIndividual();
				}
			} while (!allowDuplicates && population.contains(individual));

			population.add(individual);
		}
		
		EventManager.getInstance().fire(new RampedHalfAndHalfEndEvent(population, method));

		return population;
	}
	
	/**
	 * Constructs a new <code>CFGIndividual</code> instance using either a full or grow 
	 * initialisation procedure, selected at random
	 * 
	 * @return a new individual
	 */
	@Override
	public CFGIndividual createIndividual() {
		if (random.nextBoolean()) {
			return grow.createIndividual();
		} else {
			return full.createIndividual();
		}
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

		//grow.setRandomSequence(random);
		full.setRandomSequence(random);
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
	 * Sets the grammar to be satisfied by the full parse trees of the new
	 * <code>CFGIndividual</code>s. If automatic configuration is enabled then any
	 * value set here will be overwritten by the {@link Grammar#GRAMMAR}
	 * configuration setting on the next config event.
	 * 
	 * @param grammar the grammar to set
	 */
	public void setGrammar(Grammar grammar) {
		this.grammar = grammar;

		grow.setGrammar(grammar);
		full.setGrammar(grammar);
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
	 * Returns the depth that the maximum depth is ramped up to
	 * 
	 * @return the maximum setting the depth will be ramped to
	 */
	public int getEndDepth() {
		return endDepth;
	}

	/**
	 * Sets the depth that the maximum depth will be ramped up to when a
	 * population is created with the <code>createPopulation</code> method. If
	 * automatic configuration is enabled, then any value set here will be 
	 * overwritten by the {@link CFGIndividual#MAXIMUM_DEPTH} setting.
	 * 
	 * @param endDepth the maximum setting to ramp the depth to
	 */
	public void setEndDepth(int endDepth) {
		this.endDepth = endDepth;
	}

	/**
	 * Returns the depth that the maximum depth is ramped up from
	 * 
	 * @return the initial maximum depth setting before being ramped
	 */
	public int getStartDepth() {
		return startDepth;
	}

	/**
	 * Sets the depth that the maximum depth will be ramped up from when a
	 * population is created with the <code>createPopulation</code> method. If
	 * automatic configuration is enabled, then any
	 * value set here will be overwritten by the
	 * {@link RampedHalfAndHalfInitialisation#RAMPING_START_DEPTH} configuration
	 * setting on the next config event.
	 * 
	 * @param startDepth the initial maximum depth setting before being ramped
	 */
	public void setStartDepth(int startDepth) {
		this.startDepth = startDepth;
	}
}