/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
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
package org.epochx.gp.init;

import static org.epochx.Population.SIZE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.gp.GPIndividual.*;
import static org.epochx.gp.init.RampedHalfAndHalfInitialiser.Method.*;

import java.math.BigInteger;
import java.util.*;

import org.epochx.*;
import org.epochx.epox.Node;
import org.epochx.event.*;
import org.epochx.gp.GPIndividual;

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
 * @see FullInitialiser
 * @see GrowInitialiser
 */
public class RampedHalfAndHalfInitialiser extends GPInitialiser implements Listener<ConfigEvent> {

	public enum Method {
		GROW,
		FULL;
	}
	
	// The grow and full instances for doing their share of the work - do not
	// allow access.
	private final GrowInitialiser grow;
	private final FullInitialiser full;

	// Nodes the programs will be constructed from.
	private Node[] syntax;

	// Each generated program's return type.
	private Class<?> returnType;

	// The size of the populations to construct.
	private int popSize;

	// The depth limits of each program tree to generate.
	private int endMaxDepth;
	private int startMaxDepth;

	// Whether programs must be unique in generated populations.
	private boolean acceptDuplicates;
	
	private RandomSequence random;

	/**
	 * Constructs a <code>FullInitialiser</code> with all the necessary
	 * parameters given.
	 */
	public RampedHalfAndHalfInitialiser(RandomSequence random, Node[] syntax,
			Class<?> returnType, int popSize, int startMaxDepth, int endMaxDepth,
			boolean acceptDuplicates) {
		this.random = random;
		this.acceptDuplicates = acceptDuplicates;
		this.endMaxDepth = endMaxDepth;
		this.startMaxDepth = startMaxDepth;
		this.popSize = popSize;
		this.syntax = syntax;
		this.returnType = returnType;

		// Set up the grow and full parts.
		grow = new GrowInitialiser();
		full = new FullInitialiser();
		
		grow.setRandomSequence(random);
		grow.setSyntax(syntax);
		grow.setReturnType(returnType);
		full.setRandomSequence(random);
		full.setSyntax(syntax);
		full.setReturnType(returnType);
	}

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events. Duplicate programs are allowed in the populations that
	 * are constructed.
	 * 
	 * @param model the <code>GPModel</code> instance from which the necessary
	 *        parameters should be loaded.
	 */
	public RampedHalfAndHalfInitialiser() {
		this(1);
	}

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events. Duplicate programs are allowed in the populations that
	 * are constructed.
	 * 
	 * @param model the <code>Model</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param startMaxDepth the minimum depth from which programs should be
	 *        generated
	 *        to.
	 */
	public RampedHalfAndHalfInitialiser(int startMaxDepth) {
		this(startMaxDepth, true);
	}

	/**
	 * Constructs a <code>RampedHalfAndHalfInitialiser</code> with the necessary
	 * parameters loaded from the given model. The parameters are reloaded on
	 * configure events.
	 * 
	 * @param model the <code>Model</code> instance from which the necessary
	 *        parameters should be loaded.
	 * @param startMaxDepth the minimum depth from which programs should be
	 *        generated
	 *        to.
	 * @param acceptDuplicates whether duplicates should be allowed in the
	 *        populations that are generated.
	 */
	public RampedHalfAndHalfInitialiser(int startMaxDepth, boolean acceptDuplicates) {
		this.startMaxDepth = startMaxDepth;
		this.acceptDuplicates = acceptDuplicates;

		// Set up the grow and full parts.
		grow = new GrowInitialiser();
		full = new FullInitialiser();
		
		setup();
		EventManager.getInstance().add(ConfigEvent.class, this);
	}
	
	/**
	 * Sets up this operator with the appropriate configuration settings.
	 * This method is called whenever a <code>ConfigEvent</code> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li><code>RandomSequence.RANDOM_SEQUENCE</code>
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		popSize = Config.getInstance().get(SIZE);
		syntax = Config.getInstance().get(SYNTAX);
		returnType = Config.getInstance().get(RETURN_TYPE);
		
		endMaxDepth = Config.getInstance().get(MAXIMUM_DEPTH);
		int maxInitialDepth = Config.getInstance().get(MAXIMUM_INITIAL_DEPTH);
		
		if (maxInitialDepth < endMaxDepth || endMaxDepth == -1) {
			endMaxDepth = maxInitialDepth;
		}
	}

	/**
	 * Generates a population of new <code>CandidatePrograms</code> constructed
	 * from the <code>Nodes</code> in the syntax attribute. The size of the
	 * population will be equal to the population size attribute. All programs
	 * in the population are only guarenteed to be unique (as defined by the
	 * <code>equals</code> method on <code>GPIndividual</code>) if the
	 * <code>isDuplicatesEnabled</code> method returns <code>true</code>.
	 * Each program will alternately be generated with the
	 * {@link FullInitialiser} and {@link GrowInitialiser}. If the population
	 * size is odd then the extra individual will be initialised using grow.
	 * 
	 * @return A <code>List</code> of newly generated
	 *         <code>CandidatePrograms</code>.
	 */
	@Override
	public Population process(Population population) {
		EventManager.getInstance().fire(new InitialisationEvent.StartInitialisation(population));
		
		if (popSize < 1) {
			throw new IllegalStateException("Population size must be 1 or greater");
		} else if (endMaxDepth < startMaxDepth) {
			throw new IllegalStateException("End maximum depth must be greater than the start maximum depth.");
		}

		// Number of programs to create at each depth.
		int[] programsPerDepth = getProgramsPerDepth();

		// Whether each program was grown or not (full).
		Method[] method = new Method[popSize];

		int popIndex = 0;
		boolean growNext = true;
		for (int depth = startMaxDepth; depth <= endMaxDepth; depth++) {
			final int noPrograms = programsPerDepth[depth - startMaxDepth];

			for (int i = 0; i < noPrograms; i++) {
				GPIndividual program;

				do {
					method[popIndex] = growNext ? GROW : FULL;
					if (growNext) {
						grow.setMaxDepth(depth);
						program = grow.create();
					} else {
						full.setDepth(depth);
						program = full.create();
					}
					// The effect is that if is a duplicate then will use other
					// approach next - this is deliberate because full may have
					// less possible programs for a given depth.
					growNext = !growNext;
				} while (!acceptDuplicates && population.contains(program));

				population.add(program);
				popIndex++;
			}
		}

		EventManager.getInstance().fire(new RampedHalfAndHalfEvent(population, method));

		return population;
	}
	
	private int[] getProgramsPerDepth() {
		final int noDepths = endMaxDepth - startMaxDepth + 1;
		final int[] noPrograms = new int[noDepths];

		final int programsPerDepth = popSize / noDepths;
		Arrays.fill(noPrograms, programsPerDepth);

		// Remaining programs missed out by rounding.
		final int remainder = popSize % noDepths;

		// Add remainders to largest depth. TODO Change to spread?
		noPrograms[noPrograms.length - 1] += remainder;

		if (!acceptDuplicates) {
			// Can sufficient be created at each depth?
			int cumulative = 0;
			for (int i = startMaxDepth; i <= endMaxDepth; i++) {
				final int target = noPrograms[i - startMaxDepth];
				final BigInteger targetBI = BigInteger.valueOf(target);
				if (!grow.isSufficientVarieties(i, returnType, targetBI)) {
					final BigInteger noPossibleBI = grow.noVarieties(i, returnType);

					// Must fit into an int because target was an int.
					int noPossible = noPossibleBI.intValue();

					// Exclude those for lower depths because will already be in
					// pop.
					noPossible -= cumulative;

					// Update cumulative.
					cumulative += noPossible;
					final int shortfall = target - noPossible;

					// Move the shortfall to the next depth if there is one.
					if (i + 1 <= endMaxDepth) {
						noPrograms[i + 1 - startMaxDepth] += shortfall;
						noPrograms[i - startMaxDepth] -= shortfall;
					} else {
						throw new IllegalStateException(
								"Impossible to create sufficient programs inside depth parameters");
					}
				} else {
					// Assume that if we can create enough programs at a depth, then we can at greater depths too.
					break;
				}
			}
		}

		return noPrograms;
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
	public void setDuplicatesEnabled(final boolean acceptDuplicates) {
		this.acceptDuplicates = acceptDuplicates;
	}

	/**
	 * Sets the random number generator to use. If a model has been set then
	 * this parameter will be overwritten with the random number generator from
	 * that model on the next configure event.
	 * 
	 * @param random the random number generator to set.
	 */
	public void setRandomSequence(RandomSequence random) {
		grow.setRandomSequence(random);
		full.setRandomSequence(random);
	}

	/**
	 * Returns a <code>List</code> of the <code>Nodes</code> that form the
	 * syntax of new program generated with this initialiser, or
	 * an empty list if none have been set.
	 * 
	 * @return the types of <code>Node</code> that should be used in
	 *         constructing new programs.
	 */
	public Node[] getSyntax() {
		return syntax;
	}

	/**
	 * Sets the <code>Nodes</code> that should be used to construct new
	 * programs. If a model has been set then this parameter will be overwritten
	 * with the syntax from that model on the next configure event.
	 * 
	 * @param syntax a <code>List</code> of the types of <code>Node</code> that
	 *        should be used in constructing new programs.
	 */
	public void setSyntax(Node[] syntax) {
		this.syntax = syntax;

		grow.setSyntax(syntax);
		full.setSyntax(syntax);
	}

	/**
	 * Returns a <code>List</code> of the <code>Nodes</code> that form the
	 * syntax of new program generated with this initialiser, or
	 * an empty list if none have been set.
	 * 
	 * @return the types of <code>Node</code> that should be used in
	 *         constructing new programs.
	 */
	public Class<?> getReturnType() {
		return returnType;
	}

	/**
	 * Sets the <code>Nodes</code> that should be used to construct new
	 * programs. If a model has been set then this parameter will be overwritten
	 * with the syntax from that model on the next configure event.
	 * 
	 * @param syntax a <code>List</code> of the types of <code>Node</code> that
	 *        should be used in constructing new programs.
	 */
	public void setReturnType(final Class<?> returnType) {
		this.returnType = returnType;

		grow.setSyntax(syntax);
		full.setSyntax(syntax);
	}

	/**
	 * Returns the size of the populations that this initialiser constructs or
	 * <code>-1</code> if none has been set.
	 * 
	 * @return the size of the populations that this initialiser will generate.
	 */
	public int getPopSize() {
		return popSize;
	}

	/**
	 * Sets the size of the populations that this initialiser should construct
	 * on calls to the <code>getInitialPopulation</code> method.
	 * 
	 * @param popSize the size of the populations that should be created by this
	 *        initialiser.
	 */
	public void setPopSize(final int popSize) {
		this.popSize = popSize;
	}

	/**
	 * Returns the final maximum depth that will be used for programs this
	 * initialiser generates. Program depths will have been gradually ramped up
	 * to this value from the start max depth.
	 * 
	 * @return the maximum depth the program trees constructed should be.
	 */
	public int getEndMaxDepth() {
		return endMaxDepth;
	}

	/**
	 * Sets the maximum depth used for program trees this initialiser
	 * constructs. Depths will be ramped up to this maximum depth.
	 * 
	 * @param endMaxDepth the maximum depth of the ramping process.
	 */
	public void setEndMaxDepth(final int endMaxDepth) {
		this.endMaxDepth = endMaxDepth;
	}

	/**
	 * Returns the first maximum depth that will be used for programs this
	 * initialiser generates. Program depths will then be gradually ramped up to
	 * the end max depth.
	 * 
	 * @return the maximum depth used for the first program trees before the
	 *         depth is ramped up to the end max depth.
	 */
	public int getStartMaxDepth() {
		return startMaxDepth;
	}

	/**
	 * Sets the first maximum depth used for program trees this initialiser
	 * constructs. Depths will be ramped up from here to the end maximum depth.
	 * 
	 * @param startMaxDepth the maximum depth to be used for the smallest set of
	 *        program trees.
	 */
	public void setStartMaxDepth(final int startMaxDepth) {
		this.startMaxDepth = startMaxDepth;
	}

	public GPIndividual create() {
		if (random.nextBoolean()) {
			return grow.create();
		} else {
			return full.create();
		}
	}

	/**
	 * Receives configuration events and triggers this operator to configure its
	 * parameters if the <code>ConfigEvent</code> is for one of its required
	 * parameters.
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(RANDOM_SEQUENCE, SIZE, SYNTAX, RETURN_TYPE, MAXIMUM_INITIAL_DEPTH, MAXIMUM_DEPTH)) {
			setup();
		}
	}
}
