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

import java.util.List;

import org.epochx.AbstractOperator;
import org.epochx.Config;
import org.epochx.Individual;
import org.epochx.RandomSequence;
import org.epochx.Config.ConfigKey;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;
import org.epochx.event.OperatorEvent;
import org.epochx.event.OperatorEvent.EndOperator;
import org.epochx.ge.Chromosome;
import org.epochx.ge.Codon;
import org.epochx.ge.GEIndividual;

/**
 * This class implements a fixed point crossover on two <code>GEIndividual</code>s.
 * 
 * <p>
 * The operation is performed on the programs' chromosomes in a similar manner
 * to <code>OnePointCrossover</code>. One random codon position is chosen which is within the
 * length bounds of both parent programs. Then all codons from that point
 * onwards in both programs are exchanged.
 * 
 * <p>
 * Fixed point crossover results in two child individuals with chromosomes of equal
 * size to the two parents passed in, thus fixed point crossover prevents
 * chromosome length expanding over a run. However, chromosome length may still
 * change as a result of other operations in the algorithm such as the extension
 * property if used during mapping.
 * 
 * @see OnePointCrossover
 * 
 * @since 2.0
 */
public class FixedPointCrossover extends AbstractOperator implements Listener<ConfigEvent> {
	
	/**
	 * The key for setting and retrieving the probability of this operator being applied
	 */
	public static final ConfigKey<Double> PROBABILITY = new ConfigKey<Double>();
	
	// Configuration settings
	private RandomSequence random;
	private Double probability;

	/**
	 * Constructs a <code>FixedPointCrossover</code> with control parameters
	 * automatically loaded from the config
	 */
	public FixedPointCrossover() {
		this(true);
	}

	/**
	 * Constructs a <code>FixedPointCrossover</code> with control parameters initially
	 * loaded from the config. If the <code>autoConfig</code> argument is set to
	 * <code>true</code> then the configuration will be automatically updated when
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
	 * This method is called whenever a <code>ConfigEvent</code> occurs for a
	 * change in any of the following configuration parameters:
	 * <ul>
	 * <li>{@link RandomSequence#RANDOM_SEQUENCE}
	 * <li>{@link #PROBABILITY}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		probability = Config.getInstance().get(PROBABILITY);
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
		if (event.isKindOf(TEMPLATE, RANDOM_SEQUENCE, PROBABILITY)) {
			setup();
		}
	}
	
	/**
	 * Performs a fixed-point crossover operation on the specified parent individuals.
	 * 
	 * <p>
	 * The operation is performed on the individuals' chromosomes in a similar
	 * manner to <code>OnePointCrossover</code>. One random codon position is chosen which 
	 * is within the length bounds of both parent programs. Then all codons from
	 * that point onwards in both programs are exchanged.
	 * 
	 * @param event the <code>EndOperator</code> event to be filled with information
	 *        about this operation
	 * @param parents an array of two individuals to undergo fixed-point
	 *        crossover. Both individuals must be instances of <code>GEIndividual</code>.
	 * @return an array containing two <code>GEIndividual</code>s that are the
	 *         result of the crossover
	 */
	@Override
	public GEIndividual[] perform(EndOperator event, Individual ... parents) {
		GEIndividual parent1 = (GEIndividual) parents[0];
		GEIndividual parent2 = (GEIndividual) parents[1];
		
		Chromosome parent1Codons = parent1.getChromosome();
		Chromosome parent2Codons = parent2.getChromosome();
		
		// Pick a point in the shortest parent chromosome.
		int crossoverPoint = 0;
		int parent1Length = parent1Codons.length();
		int parent2Length = parent2Codons.length();
		if (parent1Length < parent2Length) {
			crossoverPoint = random.nextInt(parent1Length);
		} else {
			crossoverPoint = random.nextInt(parent2Length);
		}
		
		((EndEvent) event).setCrossoverPoint(crossoverPoint);

		// Make copies of the parents' chromosomes.
		Chromosome child1Codons = parent1Codons.clone();
		Chromosome child2Codons = parent2Codons.clone();
		
		List<Codon> codonsExchanged1 = child1Codons.removeCodons(crossoverPoint, parent1Length);
		List<Codon> codonsExchanged2 = child2Codons.removeCodons(crossoverPoint, parent2Length);
		
		((EndEvent) event).setExchangedCodons1(codonsExchanged1);
		((EndEvent) event).setExchangedCodons2(codonsExchanged2);
		
		// Swap over the endings at the crossover points.
		child1Codons.appendCodons(codonsExchanged2);
		child2Codons.appendCodons(codonsExchanged1);

		return new GEIndividual[]{new GEIndividual(child1Codons), new GEIndividual(child2Codons)};
	}
	
	/**
	 * Returns a <code>FixedPointCrossoverEndEvent</code> with the operator and parents set
	 * 
	 * @param parents the parents that were operated on
	 * @return operator end event
	 */
	@Override
	protected EndEvent getEndEvent(Individual ... parents) {
		return new EndEvent(this, parents);
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
	 * An event fired at the end of a fixed-point crossover
	 * 
	 * @see FixedPointCrossover
	 * 
	 * @since 2.0
	 */
	public class EndEvent extends OperatorEvent.EndOperator {

		private List<Codon> exchangedCodons1;
		private List<Codon> exchangedCodons2;
		private int point;

		/**
		 * Constructs a <code>FixedPointCrossoverEndEvent</code> with the details of the
		 * event
		 * 
		 * @param operator the operator that performed the crossover
		 * @param parents an array of two individuals that the operator was
		 *        performed on
		 */
		public EndEvent(FixedPointCrossover operator, Individual[] parents) {
			super(operator, parents);
		}

		/**
		 * Returns an integer which is the position within the codons that the crossover
		 * was performed
		 * 
		 * @return an integer which is the index of the crossover point
		 */
		public int getCrossoverPoint() {
			return point;
		}
		
		/**
		 * Sets the crossover point
		 * 
		 * @param point index used as the crossover point in both individuals
		 */
		public void setCrossoverPoint(int point) {
			this.point = point;
		}

		/**
		 * Returns a list of the codons from the first parent that were exchanged
		 * with codons from the second parent (as returned from getExchangedCodons2).
		 * The codons returned will be from the tail of the individual's chromosome,
		 * from the crossover point to the end of the chromosome.
		 * 
		 * @return a list of the codons exchanged from parent 1
		 */
		public List<Codon> getExchangedCodons1() {
			return exchangedCodons1;
		}

		/**
		 * Sets a list containing the codons from the first parent that were exchanged
		 * with codons from the second parent.
		 * 
		 * @param exchangedCodons1 the codons that were exchanged from parent 1
		 */
		public void setExchangedCodons1(List<Codon> exchangedCodons1) {
			this.exchangedCodons1 = exchangedCodons1;
		}
		
		/**
		 * Returns a list of the codons from the second parent that were exchanged
		 * with codons from the first parent (as returned from getExchangedCodons1).
		 * The codons returned will be from the tail of the individual's chromosome,
		 * from the crossover point to the end of the chromosome.
		 * 
		 * @return a list of the codons exchanged from parent 2
		 */
		public List<Codon> getExchangedCodons2() {
			return exchangedCodons2;
		}

		/**
		 * Sets a list containing the codons from the second parent that were exchanged
		 * with codons from the first parent.
		 * 
		 * @param exchangedCodons2 the codons that were exchanged from parent 2
		 */
		public void setExchangedCodons2(List<Codon> exchangedCodons2) {
			this.exchangedCodons2 = exchangedCodons2;
		}
	}
}