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
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.ge.Codon.*;
import static org.epochx.ge.GEIndividual.MAXIMUM_DEPTH;
import static org.epochx.grammar.Grammar.GRAMMAR;

import org.epochx.*;
import org.epochx.Config.ConfigKey;
import org.epochx.event.*;
import org.epochx.ge.*;
import org.epochx.grammar.Grammar;

/**
 * Initialisation implementation which uses a combination of full and grow
 * initialisers to create an initial population of
 * <code>GECandidatePrograms</code>. For full details of how the initialisation
 * operator works see Chapter 8: Sensible Initialisation, of Grammatical
 * Evolution by O'Neill and Ryan.
 * 
 * <p>
 * Depths are equally split between depths from the minimum possible depth up to
 * the maximum initial depth. Initialisation of programs with parse trees down
 * to each of these depths is then alternated between full and grow initialisers
 * starting with grow. The depth of the programs is controlled by mapping the
 * generated codons in a depth first manner.
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
 * @see FixedLengthInitialisation
 * @see FullInitialisation
 * @see GrowInitialisation
 * 
 * @since 2.0
 */
public class RampedHalfAndHalfInitialisation implements GEInitialisation, Listener<ConfigEvent> {

	/**
	 * The key for setting and retrieving the smallest maximum depth setting
	 * from which the ramping will begin
	 */
	public static final ConfigKey<Integer> RAMPING_START_DEPTH = new ConfigKey<Integer>();
	
	// Configuration settings
	private Grammar grammar;
	private RandomSequence random;
	private Long minCodonValue;
	private Long maxCodonValue;
	private Integer populationSize;
	private Integer endDepth;
	private Integer startDepth;
	private Boolean allowDuplicates;
	
	// The two halves
	private final GrowInitialisation grow;
	private final FullInitialisation full;

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialisation</code> with control
	 * parameters automatically loaded from the config
	 */
	public RampedHalfAndHalfInitialisation() {
		this(true);
	}
	
	/**
	 * Constructs a <code>RampedHalfAndHalfInitialisation</code> with control
	 * parameters initially loaded from the config. If the <code>autoConfig</code>
	 * argument is set to <code>true</code> then the configuration will be
	 * automatically updated when the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public RampedHalfAndHalfInitialisation(boolean autoConfig) {
		// Default config values
		allowDuplicates = true;
		maxCodonValue = Long.MAX_VALUE;
		minCodonValue = 0L;
		
		grow = new GrowInitialisation(false);
		full = new FullInitialisation(false);
		
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
	 * <li>{@link Population#SIZE}
	 * <li>{@link InitialisationMethod#ALLOW_DUPLICATES} (default: <code>true</code>)
	 * <li>{@link Grammar#GRAMMAR}
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}
	 * <li>{@link Codon#MAXIMUM_VALUE} (default: <code>Long.MAXIMUM_VALUE</code>)
	 * <li>{@link Codon#MINIMUM_VALUE} (default: <code>0</code>)
	 * <li>{@link GEIndividual#MAXIMUM_DEPTH}
	 * <li>{@link #RAMPING_START_DEPTH}
	 * </ul>
	 */
	protected void setup() {
		populationSize = Config.getInstance().get(SIZE);
		allowDuplicates = Config.getInstance().get(ALLOW_DUPLICATES, allowDuplicates);
		grammar = Config.getInstance().get(GRAMMAR);
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		maxCodonValue = Config.getInstance().get(MAXIMUM_VALUE, maxCodonValue);
		minCodonValue = Config.getInstance().get(MINIMUM_VALUE, minCodonValue);
		endDepth = Config.getInstance().get(MAXIMUM_DEPTH);
		startDepth = Config.getInstance().get(RAMPING_START_DEPTH);
	}
	
	/**
	 * Receives configuration events and triggers this operator to reconfigure
	 * if the <code>ConfigEvent</code> is for one of its required parameters
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(TEMPLATE, SIZE, ALLOW_DUPLICATES, RANDOM_SEQUENCE, GRAMMAR, MAXIMUM_VALUE, MINIMUM_VALUE, MAXIMUM_DEPTH, RAMPING_START_DEPTH)) {
			setup();
		}
	}

	/**
	 * Will use grow initialisation on half the population and full on the other
	 * half. If the population size is an odd number then the extra individual will be
	 * initialised with grow.
	 */
	@Override
	public Population createPopulation() {
		if (endDepth < startDepth) {
			throw new IllegalStateException("End depth must be greater than the start depth.");
		}

		// Create population list to populate.
		Population firstGen = new Population();

		int currentDepth = startDepth;
		int minDepthPossible = grammar.getMinimumDepth();
		if (currentDepth < minDepthPossible) {
			// Our start depth can only be as small as the grammars minimum depth
			currentDepth = minDepthPossible;
		}

		if (endDepth < startDepth) {
			throw new IllegalStateException("End maximum depth must be greater than the minimum possible depth.");
		}

		// Number of programs each depth SHOULD have. But won't exactly unless the remainder is 0.
		double programsPerDepth = (double) populationSize / (endDepth - startDepth + 1);

		// Whether each program was grown or not (full)
		boolean[] grown = new boolean[populationSize];

		for (int i = 0; i < populationSize; i++) {
			// Calculate depth
			int depth = (int) Math.floor((firstGen.size() / programsPerDepth) + currentDepth);

			// Grow on even numbers, full on odd.
			GEIndividual program;

			do {
				if ((i % 2) == 0) {
					grown[i] = true;
					grow.setMaximumDepth(depth);
					program = grow.createIndividual();
				} else {
					full.setDepth(depth);
					program = full.createIndividual();
				}
			} while (!allowDuplicates && firstGen.contains(program));

			firstGen.add(program);
		}

		return firstGen;
	}
	
	/**
	 * Constructs a new <code>GEIndividual</code> instance using either a full or grow 
	 * initialisation procedure, selected at random.
	 * 
	 * @return a new individual
	 */
	@Override
	public GEIndividual createIndividual() {
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
	 * Returns the grammar that the <code>GEIndividual</code>s will satisfy with
	 * their parse trees
	 * 
	 * @return the currently set grammar
	 */
	public Grammar getGrammar() {
		return grammar;
	}

	/**
	 * Sets the grammar to be satisfied by the full parse trees of the new
	 * <code>GEIndividual</code>s. If automatic configuration is enabled then any
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
	 * overwritten by the {@link GEIndividual#MAXIMUM_DEPTH} setting.
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

	/**
	 * Returns the maximum value that codons chosen by this initialiser are
	 * allowed to have
	 * 
	 * @return the maximum value of codons selected by this initialiser
	 */
	public long getMaximumCodonValue() {
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
		grow.setMaximumCodonValue(maxCodonValue);
		full.setMaximumCodonValue(maxCodonValue);
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
		grow.setMinimumCodonValue(minCodonValue);
		full.setMinimumCodonValue(minCodonValue);
	}
}
