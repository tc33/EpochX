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

import static org.epochx.Config.Template.TEMPLATE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.ge.CodonFactory.CODON_FACTORY;

import org.epochx.AbstractOperator;
import org.epochx.Config;
import org.epochx.Individual;
import org.epochx.RandomSequence;
import org.epochx.Config.ConfigKey;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.event.OperatorEvent.EndOperator;
import org.epochx.ge.Chromosome;
import org.epochx.ge.CodonFactory;
import org.epochx.ge.GEIndividual;

/**
 * This class performs a single point mutation on a <code>GEIndividual</code>.
 * 
 * <p>
 * Whereas a standard <tt>PointMutation</tt> will consider each codon in the selected
 * program for mutation, and could potentially mutate any number of codons from
 * 0 to the number of codons, <tt>SinglePointMutation</tt> will always mutate exactly one
 * codon of an individual that it is asked to mutate. If the codon does undergo
 * mutation then a replacement codon is generated using the <tt>CodonFactory</tt>.
 * 
 * @since 2.0
 */
public class SinglePointMutation extends AbstractOperator implements Listener<ConfigEvent> {
	
	/**
	 * The key for setting and retrieving the probability of this operator being applied
	 */
	public static final ConfigKey<Double> PROBABILITY = new ConfigKey<Double>();
	
	// Configuration settings
	private RandomSequence random;
	private CodonFactory codonFactory;
	private Double probability;

	/**
	 * Constructs a <tt>SinglePointMutation</tt> with control parameters
	 * automatically loaded from the config
	 */
	public SinglePointMutation() {
		this(true);
	}

	/**
	 * Constructs a <tt>SinglePointMutation</tt> with control parameters initially
	 * loaded from the config. If the <tt>autoConfig</tt> argument is set to
	 * <tt>true</tt> then the configuration will be automatically updated when
	 * the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public SinglePointMutation(boolean autoConfig) {
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
	 * <li>{@link #PROBABILITY}
	 * <li>{@link CodonFactory#CODON_FACTORY}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		probability = Config.getInstance().get(PROBABILITY);
		codonFactory = Config.getInstance().get(CODON_FACTORY);
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
		if (event.isKindOf(TEMPLATE, RANDOM_SEQUENCE, PROBABILITY, CODON_FACTORY)) {
			setup();
		}
	}

	/**
	 * Performs single point mutation on the given <tt>GEIndividual</tt>. A codon position
	 * will be randomly chosen from the length of codons in the program and that codon will 
	 * be replaced with a new codon generated using the codon factory.
	 * 
	 * @param event the <tt>EndOperator</tt> event to be filled with information
	 *        about this operation
	 * @param parent an array of one individual to undergo point mutation. The individual
	 * 		  must be an instance of <tt>GEIndividual</tt>.
	 * @return an array containing one <tt>GEIndividual</tt> that is the
	 *         result of the mutation
	 */
	@Override
	public GEIndividual[] perform(EndOperator event, Individual ... parent) {
		GEIndividual parent1 = (GEIndividual) parent[0];
		
		Chromosome codons = parent1.getChromosome().clone();
		int noCodons = codons.length();

		int mutationPoint = random.nextInt(noCodons);
		codons.setCodon(mutationPoint, codonFactory.codon(mutationPoint));

		((SinglePointMutationEndEvent) event).setMutationPoint(mutationPoint);

		return new GEIndividual[]{new GEIndividual(codons)};
	}
	
	/**
	 * Returns a <tt>SinglePointMutationEndEvent</tt> with the operator and 
	 * parent set
	 * 
	 * @param parents the parents that were operated on
	 * @return operator end event
	 */
	@Override
	protected SinglePointMutationEndEvent getEndEvent(Individual ... parent) {
		return new SinglePointMutationEndEvent(this, parent);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Single point mutation operates on one individual.
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public int inputSize() {
		return 1;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double probability() {
		return probability;
	}

	/**
	 * Sets the probability of this operator being selected. If automatic configuration is
	 * enabled then any value set here will be overwritten by the {@link #PROBABILITY} 
	 * configuration setting on the next config event.
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
	public void setRandomSequence(RandomSequence random) {
		this.random = random;
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
