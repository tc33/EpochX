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
package org.epochx.epox.bool;

import org.epochx.RandomSequence;
import org.epochx.epox.*;
import org.epochx.epox.math.*;

/**
 * Defines a boolean ephemeral random constant (ERC). An ERC is a literal with
 * a value which is randomly generated on construction. This implementation
 * will generate a value of either <tt>true</tt> or <tt>false</tt> randomly with
 * equal probability. As with all nodes, instances may be
 * constructed in any of 3 ways:
 * <ul>
 * <li>constructor - the new instance will be initialised with a value of
 * <tt>null</tt>.</li>
 * <li><tt>clone</tt> method - will return an instance with a value equal to the
 * cloned value.</li>
 * <li><tt>newInstance</tt> method - will return a new instance with a new,
 * randomly generated value.</li>
 * </ul>
 * 
 * @see DoubleERC
 * @see IntegerERC
 */
public class BooleanERC extends Literal {

	private RandomSequence random;

	/**
	 * Constructs a new <tt>BooleanERC</tt> with a value of <tt>null</tt>. The
	 * given random number generator will be used to generate a new value if the
	 * <tt>newInstance</tt> method is used.
	 * 
	 * @param random the random number generator to use if randomly generating a
	 *        boolean value. An exception will be thrown if it is <tt>null</tt>.
	 */
	public BooleanERC(RandomSequence random) {
		super(null);

		if (random == null) {
			throw new IllegalArgumentException("random generator must not be null");
		}

		this.random = random;

		setValue(generateValue());
	}

	/**
	 * Constructs a new <tt>BooleanERC</tt> node with a randomly generated
	 * value, selected using the random number generator.
	 * 
	 * @return a new <tt>BooleanERC</tt> instance with a randomly generated
	 *         value
	 */
	@Override
	public BooleanERC newInstance() {
		BooleanERC erc = (BooleanERC) super.newInstance();

		erc.setValue(generateValue());

		return erc;
	}

	/**
	 * Generates and returns a new boolean value for use in a new
	 * <tt>BooleanERC</tt> instance
	 * 
	 * @return a boolean value to be used as the value of a new
	 *         <tt>BooleanERC</tt> instance
	 * @throws IllegalStateException if the random number generator is
	 *         <tt>null</tt>
	 */
	protected boolean generateValue() {
		if (random == null) {
			throw new IllegalStateException("random number generator must not be null");
		}

		return random.nextBoolean();
	}

	/**
	 * Returns the random number generator that is currently being used to
	 * generate boolean values for new <tt>BooleanERC</tt> instances
	 * 
	 * @return the random number generator
	 */
	public RandomSequence getRandomSequence() {
		return random;
	}

	/**
	 * Sets the random number generator to be used for generating the boolean
	 * value of new <tt>BooleanERC</tt> instances
	 * 
	 * @param random the random number generator to set
	 */
	public void setRNG(RandomSequence random) {
		this.random = random;
	}
}
