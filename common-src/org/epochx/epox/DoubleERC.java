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

import java.math.*;

import org.epochx.tools.random.RandomNumberGenerator;

/**
 * Defines a double ephemeral random constant (ERC). An ERC is a literal with
 * a value which is randomly generated upon construction. This implementation
 * will generate a double value of the specified precision between the lower
 * and upper bounds provided. All values between the two bounds (inclusive), can
 * appear with equal probability. As with all nodes, instances may be
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
 * @see BooleanERC
 * @see IntegerERC
 */
public class DoubleERC extends Literal {

	private RandomNumberGenerator rng;

	// The inclusive bounds.
	private double lower;
	private double upper;

	// The precision of generated values.
	private int precision;

	/**
	 * Constructs a new <code>DoubleERC</code> with a value of <code>null</code>
	 * . The given random number generator will be be used to
	 * generate a new value if the <code>newInstance</code> method is used.
	 * 
	 * @param rng the random number generator to use if randomly generating a
	 *        double value.
	 * @param lower the inclusive lower bound of values that are generated.
	 * @param upper the inclusive upper bound of values that are generated.
	 * @param precision the non-negative <code>int</code> precision.
	 */
	public DoubleERC(final RandomNumberGenerator rng, final double lower, final double upper, final int precision) {
		super(null);
		
		if (rng == null) {
			throw new IllegalArgumentException("random generator must not be null");
		}
		
		this.rng = rng;
		this.lower = lower;
		this.upper = upper;
		this.precision = precision;
		
		// Set its value.
		setValue(generateValue());
	}

	/**
	 * Constructs a new <code>DoubleERC</code> node with a randomly generated
	 * value, selected using the random number generator. The value will be
	 * randomly selected with an equal probability from the set of values
	 * between the lower and upper bounds and of the specified precision.
	 * 
	 * @return a new <code>DoubleERC</code> instance with a randomly generated
	 *         value.
	 */
	@Override
	public DoubleERC newInstance() {
		final DoubleERC erc = (DoubleERC) super.newInstance();

		erc.setValue(generateValue());

		return erc;
	}

	/**
	 * Generates and returns a new double value for use in a new
	 * <code>DoubleERC</code> instance. This implementation will return a value
	 * selected randomly from the set of values between the lower and upper
	 * bounds, inclusively. The value will be returned with the specified
	 * precision.
	 * 
	 * @return a double value to be used as the value of a new DoubleERC
	 *         instance.
	 * @throws IllegalStateException if the random number generator is null.
	 */
	protected double generateValue() {
		if (rng == null) {
			throw new IllegalStateException("random number generator must not be null");
		}

		// Position random within range.
		final double range = upper - lower;
		final double d = (rng.nextDouble() * range) + lower;

		// Round to the correct precision.
		BigDecimal big = new BigDecimal(d);
		big = big.round(new MathContext(precision));

		return big.doubleValue();
	}

	/**
	 * Returns the random number generator that is currently being used to
	 * generate double values for new <code>DoubleERC</code> instances.
	 * 
	 * @return the random number generator
	 */
	public RandomNumberGenerator getRNG() {
		return rng;
	}

	/**
	 * Sets the random number generator to be used for generating the double
	 * value of new <code>DoubleERC</code> instances.
	 * 
	 * @param rng the random number generator to set
	 */
	public void setRNG(final RandomNumberGenerator rng) {
		this.rng = rng;
	}

	/**
	 * Returns the lower bound of the newly generated values.
	 * 
	 * @return the lower bound of values.
	 */
	public double getLower() {
		return lower;
	}

	/**
	 * Sets the inclusive lower bound for newly generated values.
	 * 
	 * @param lower the lower bound for values.
	 */
	public void setLower(final double lower) {
		this.lower = lower;
	}

	/**
	 * Returns the upper bound of the newly generated values.
	 * 
	 * @return the upper bound of values.
	 */
	public double getUpper() {
		return upper;
	}

	/**
	 * Sets the inclusive upper bound for newly generated values.
	 * 
	 * @param upper the upper bound for values.
	 */
	public void setUpper(final double upper) {
		this.upper = upper;
	}

	/**
	 * Returns the non-negative value precision int.
	 * 
	 * @return the value precision.
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * Sets the non-negative value precision int.
	 * 
	 * @param precision a non-negative precision for generated values
	 */
	public void setPrecision(final int precision) {
		this.precision = precision;
	}
}
