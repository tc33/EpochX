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
package org.epochx.ge.operator;

import static org.epochx.RandomSequence.RANDOM_SEQUENCE;

import java.util.List;

import org.epochx.AbstractOperator;
import org.epochx.Config;
import org.epochx.Individual;
import org.epochx.RandomSequence;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.event.OperatorEvent.EndOperator;
import org.epochx.ge.Codon;
import org.epochx.ge.GEIndividual;

/**
 * This class implements a fixed point crossover on two
 * <code>CandidatePrograms</code>.
 * 
 * <p>
 * The operation is performed on the programs' chromosomes in a similar manner
 * to OnePointCrossover. One random codon position is chosen which is within the
 * length bounds of both parent programs. Then all codons from that point
 * onwards in both programs are exchanged.
 * 
 * <p>
 * Fixed point crossover results in two child programs with chromosomes of equal
 * size to the two parents passed in, thus fixed point crossover prevents
 * chromosome length expanding over a run. However, chromosome length may still
 * change as a result of other operations in the algorithm such as the extension
 * property if used during mapping.
 * 
 * @see OnePointCrossover
 */
public class FixedPointCrossover extends AbstractOperator implements Listener<ConfigEvent> {
	
	// Configuration settings
	private RandomSequence random;
	
	private double probability;
	
	// Data from last crossover
	private int crossoverPoint;
	private List<Codon> codonsExchanged1;
	private List<Codon> codonsExchanged2;

	/**
	 * Constructs a <tt>FixedPointCrossover</tt> with control parameters
	 * automatically loaded from the config
	 */
	public FixedPointCrossover() {
		this(true);
	}

	/**
	 * Constructs a <tt>FixedPointCrossover</tt> with control parameters initially
	 * loaded from the config. If the <tt>autoConfig</tt> argument is set to
	 * <tt>true</tt> then the configuration will be automatically updated when
	 * the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public FixedPointCrossover(boolean autoConfig) {
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
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
	}

	/**
	 * Receives configuration events and triggers this operator to configure its
	 * parameters if the <tt>ConfigEvent</tt> is for one of its required
	 * parameters.
	 * 
	 * @param event {@inheritDoc}
	 */
	@Override
	public void onEvent(ConfigEvent event) {
		if (event.isKindOf(RANDOM_SEQUENCE)) {
			setup();
		}
	}
	
	/**
	 * Performs a fixed point crossover operation on the specified parent
	 * programs.
	 * 
	 * <p>
	 * The operation is performed on the programs' chromosomes in a similar
	 * manner to OnePointCrossover. One random codon position is chosen which is
	 * within the length bounds of both parent programs. Then all codons from
	 * that point onwards in both programs are exchanged.
	 * 
	 * @param p1 {@inheritDoc}
	 * @param p2 {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public GEIndividual[] perform(EndOperator event, Individual ... parents) {
		final GEIndividual parent1 = (GEIndividual) parents[0];
		final GEIndividual parent2 = (GEIndividual) parents[1];

		// Pick a point in the shortest parent chromosome.
		crossoverPoint = 0;
		final int parent1Codons = parent1.getChromosome().length();
		final int parent2Codons = parent2.getChromosome().length();
		if (parent1Codons < parent2Codons) {
			crossoverPoint = random.nextInt(parent1Codons);
		} else {
			crossoverPoint = random.nextInt(parent2Codons);
		}

		// Make copies of the parents.
		final GEIndividual child1 = (GEIndividual) parent1.clone();
		final GEIndividual child2 = (GEIndividual) parent2.clone();

		final List<Integer> part1 = child1.removeCodons(crossoverPoint, child1.getNoCodons());
		final List<Integer> part2 = child2.removeCodons(crossoverPoint, child2.getNoCodons());

		// Swap over the endings at the crossover points.
		child2.appendCodons(part1);
		child1.appendCodons(part2);

		return new GEIndividual[]{child1, child2};
	}
	
	/**
	 * Returns a <tt>OnePointCrossoverEndEvent</tt> with the operator and 
	 * parents set
	 */
	@Override
	protected FixedPointCrossoverEndEvent getEndEvent(Individual ... parents) {
		FixedPointCrossoverEndEvent event = new FixedPointCrossoverEndEvent(this, parents);
		event.setCrossoverPoint(point);
		return event;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Fixed-point crossover operates on 2 individuals.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int inputSize() {
		return 2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double probability() {
		return probability;
	}

	/**
	 * Sets the probability of this operator being selected
	 * 
	 * @param probability the new probability to set
	 */
	public void setProbability(double probability) {
		this.probability = probability;
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
	public void setRandomSequence(final RandomSequence random) {
		this.random = random;
	}
}