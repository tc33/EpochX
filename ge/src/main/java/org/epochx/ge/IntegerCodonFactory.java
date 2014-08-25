/* 
 * Copyright 2007-2013
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX
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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.ge;

import static org.epochx.Config.Template.TEMPLATE;
import static org.epochx.RandomSequence.RANDOM_SEQUENCE;
import static org.epochx.ge.Codon.MAXIMUM_VALUE;
import static org.epochx.ge.Codon.MINIMUM_VALUE;

import org.epochx.Config;
import org.epochx.RandomSequence;
import org.epochx.event.ConfigEvent;
import org.epochx.event.EventManager;
import org.epochx.event.Listener;

/**
 * A codon factory responsible for creating integer codons
 * 
 * @since 2.0
 */
public class IntegerCodonFactory implements CodonFactory, Listener<ConfigEvent> {
	
	// Configuration settings
	private RandomSequence random;
	private Long maxCodon;
	private Long minCodon;
	
	private long codonRange;
	
	/**
	 * Constructs an <code>IntegerCodonFactory</code> with control parameters
	 * automatically loaded from the config
	 */
	public IntegerCodonFactory() {
		this(true);
	}
	
	/**
	 * Constructs an <code>IntegerCodonFactory</code> with control parameters
	 * initially loaded from the config. If the <code>autoConfig</code> argument is
	 * set to <code>true</code> then the configuration will be automatically updated
	 * when the config is modified.
	 * 
	 * @param autoConfig whether this operator should automatically update its
	 *        configuration settings from the config
	 */
	public IntegerCodonFactory(boolean autoConfig) {
		// Default config values
		maxCodon = Long.MAX_VALUE;
		minCodon = 0L;
		
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
	 * <li>{@link Codon#MAXIMUM_VALUE}
	 * <li>{@link Codon#MINIMUM_VALUE}
	 * </ul>
	 */
	protected void setup() {
		random = Config.getInstance().get(RANDOM_SEQUENCE);
		maxCodon = Config.getInstance().get(MAXIMUM_VALUE, maxCodon);
		minCodon = Config.getInstance().get(MINIMUM_VALUE, minCodon);
		
		if (minCodon < 0) {
			new IllegalStateException("the minimum codon value must be 0 or greater");			
		} else if (maxCodon <= minCodon) {
			new IllegalStateException("the maximum codon value must be larger than the minimum codon value");
		}
		
		codonRange = maxCodon - minCodon;
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
		if (event.isKindOf(TEMPLATE, RANDOM_SEQUENCE, MAXIMUM_VALUE, MINIMUM_VALUE)) {
			setup();
		}
	}
	
	
	/**
	 * Constructs a new <code>IntegerCodon</code> with a random value between the
	 * minimum and maximum codon values
	 * 
	 * @return a new <code>IntegerCodon</code> with a random value
	 */
	@Override
	public Codon codon() {
		long value = minCodon + random.nextLong(codonRange);
		
		return new IntegerCodon(value);
	}
	
	/**
	 * Constructs a new <code>IntegerCodon</code> with the given value
	 * 
	 * @param value the value to assign to the new codon
	 * @return a new <code>IntegerCodon</code> with the given value
	 */
	@Override
	public Codon codon(long value) {
	    return new IntegerCodon(value);
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
	 * enabled thenAny value set here will be overwritten by the 
	 * {@link RandomSequence#RANDOM_SEQUENCE} configuration setting on the next 
	 * config event.
	 * 
	 * @param random the random number generator to set
	 */
	public void setRandomSequence(RandomSequence random) {
		this.random = random;
	}
	
	/**
	 * Returns the maximum codon value setting
	 * 
	 * @return the maximum codon value
	 */
	public Long getMaximumCodonValue() {
		return maxCodon;
	}

	/**
	 * Sets the maximum codon value to use. If automatic configuration is 
	 * enabled then any value set here will be overwritten by the 
	 * {@link Codon#MAXIMUM_VALUE} configuration setting on the next 
	 * config event.
	 * 
	 * @param random the random number generator to set
	 */
	public void setMaximumCodonValue(Long maxCodon) {
		this.maxCodon = maxCodon;
	}
	
	/**
	 * Returns the minimum codon value setting
	 * 
	 * @return the minimum codon value
	 */
	public Long getMinimumCodonValue() {
		return minCodon;
	}

	/**
	 * Sets the minimum codon value to use. If automatic configuration is 
	 * enabled then any value set here will be overwritten by the 
	 * {@link Codon#MINIMUM_VALUE} configuration setting on the next 
	 * config event.
	 * 
	 * @param random the random number generator to set
	 */
	public void setMinimumCodonValue(Long minCodon) {
		this.minCodon = minCodon;
	}
}
