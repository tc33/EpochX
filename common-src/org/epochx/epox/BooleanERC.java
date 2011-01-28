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
package org.epochx.epox;

import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Defines a boolean ephemeral random constant (ERC). An ERC is a literal with
 * a value which is randomly generated upon construction. This implementation
 * will generate a value of either <code>true</code> or <code>false</code>
 * randomly with equal probability. As with all nodes, instances may be
 * constructed in any of 3 ways:
 * <ul>
 * <li>constructor - the new instance will be initialised with a value of
 * <code>null</code>.</li>
 * <li>clone method - will return an instance with a value equal to the cloned
 * value.</li>
 * <li>newInstance method - will return a new instance with a new, randomly
 * generated value.</li>
 * </ul>
 * 
 * @see DoubleERC
 * @see IntegerERC
 */
public class BooleanERC extends Literal {

	private RandomNumberGenerator rng;

	/**
	 * Constructs a new <code>BooleanERC</code> with a value of
	 * <code>null</code>. The given random number generator will be be used to
	 * generate a new value if the <code>newInstance</code> method is used.
	 * 
	 * @param rng the random number generator to use if randomly generating a
	 *        boolean value. An exception will be thrown if it is null.
	 */
	public BooleanERC(final RandomNumberGenerator rng) {
		super(null);
		
		if (rng == null) {
			throw new IllegalArgumentException("random generator must not be null");
		}

		this.rng = rng;
		
		// Set its value.
		setValue(generateValue());
	}

	/**
	 * Constructs a new <code>BooleanERC</code> node with a randomly generated
	 * value, selected using the random number generator.
	 * 
	 * @return a new <code>BooleanERC</code> instance with a randomly generated
	 *         value.
	 */
	@Override
	public BooleanERC newInstance() {
		final BooleanERC erc = (BooleanERC) super.newInstance();

		erc.setValue(generateValue());

		return erc;
	}

	/**
	 * Generates and returns a new boolean value for use in a new
	 * <code>BooleanERC</code> instance.
	 * 
	 * @return a boolean value to be used as the value of a new BooleanERC
	 *         instance.
	 * @throws IllegalStateException if the random number generator is null.
	 */
	protected boolean generateValue() {
		if (rng == null) {
			throw new IllegalStateException("random number generator must not be null");
		}

		return rng.nextBoolean();
	}

	/**
	 * Returns the random number generator that is currently being used to
	 * generate boolean values for new <code>BooleanERC</code> instances.
	 * 
	 * @return the random number generator
	 */
	public RandomNumberGenerator getRNG() {
		return rng;
	}

	/**
	 * Sets the random number generator to be used for generating the boolean
	 * value of new <code>BooleanERC</code> instances.
	 * 
	 * @param rng the random number generator to set.
	 */
	public void setRNG(final RandomNumberGenerator rng) {
		this.rng = rng;
	}
}
