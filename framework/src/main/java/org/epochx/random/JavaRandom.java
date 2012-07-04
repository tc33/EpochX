/* 
 * Copyright 2007-2011
 * Lawrence Beadle, Tom Castle and Fernando Otero
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
 * The latest version is available from: http://www.epochx.org
 */

package org.epochx.random;

import java.util.Random;

import org.epochx.RandomSequence;

/**
 * Provides a mechanism for using Java's random number generation through
 * EpochX's supported <tt>RandomSequence</tt> interface.
 * 
 * @see Random
 */
public class JavaRandom implements RandomSequence {

	// The underlying Java random number generator.
	private final Random rand;

	/**
	 * Constructs a <tt>JavaRandom</tt> random number generator.
	 */
	public JavaRandom() {
		rand = new Random();
	}

	/**
	 * Constructs a <tt>JavaRandom</tt> random number generator with the
	 * specified seed.
	 * 
	 * @param seed the initial seed.
	 */
	public JavaRandom(long seed) {
		rand = new Random(seed);
	}

	/**
	 * Constructs a <tt>JavaRandom</tt> continuing the sequence from the
	 * given <tt>Random</tt> instance's pseudorandom number generation.
	 * 
	 * @param rand the <tt>Random</tt> instance to use for random number
	 *        generation.
	 */
	public JavaRandom(Random rand) {
		this.rand = rand;
	}

	/**
	 * Returns the next randomly generated <tt>boolean</tt> value, as
	 * returned by the underlying Java pseudorandom number generation.
	 * 
	 * @return <tt>true</tt> or <tt>false</tt>, as randomly selected by the 
	 * Java random number generator
	 * @see Random
	 */
	public boolean nextBoolean() {
		return rand.nextBoolean();
	}

	/**
	 * Returns the next randomly generated <tt>double</tt> value, as
	 * returned by the underlying Java pseudorandom number generation
	 * 
	 * @return a randomly selected double value in the range <tt>0.0</tt>
	 *         (inclusive) to <tt>1.0</tt> (exclusive) as selected by the
	 *         Java
	 *         random number generator
	 * @see Random
	 */
	public double nextDouble() {
		return rand.nextDouble();
	}

	/**
	 * Returns the next randomly generated <tt>int</tt> value between
	 * <tt>0</tt>(inclusive) and <tt>n</tt> (exclusive), as returned by
	 * the underlying Java pseudorandom number generation.
	 * 
	 * @param n the upper limit of the generation
	 * @return a randomly selected <tt>int</tt> value in the range
	 *         <tt>0</tt> (inclusive) to <tt>n</tt> (exclusive) as
	 *         selected by
	 *         the Java random number generator
	 * @see Random
	 */
	public int nextInt(int n) {
		return rand.nextInt(n);
	}

	/**
	 * Returns the next randomly generated <tt>int</tt> value, as returned
	 * by the underlying Java pseudorandom number generation. All 2<sup>32</sup>
	 * possible <tt>int</tt> values may be returned.
	 * 
	 * @return a randomly selected <tt>int</tt> value as selected by the
	 *         Java random number generator
	 * @see Random
	 */
	public int nextInt() {
		return rand.nextInt();
	}

	/**
	 * Returns the next randomly generated <tt>long</tt> value between
	 * <tt>0</tt>(inclusive) and <tt>n</tt> (exclusive), as returned by
	 * the underlying Java pseudorandom number generation
	 * 
	 * @param n the upper limit of the generation
	 * @return a randomly selected <tt>long</tt> value in the range
	 *         <tt>0</tt> (inclusive) to <tt>n</tt> (exclusive) as
	 *         selected by the Java random number generator
	 * @see Random
	 */
	public long nextLong(long n) {
		long bits, val;
		do {
			bits = (nextLong() << 1) >>> 1;
			val = bits % n;
		} while (bits - val + (n - 1) < 0L);
		return val;
	}

	/**
	 * Returns the next randomly generated <tt>long</tt> value, as returned
	 * by the underlying Java pseudorandom number generation. All 2<sup>32</sup>
	 * possible <tt>long</tt> values may be returned.
	 * 
	 * @return a randomly selected <tt>long</tt> value as selected by the
	 *         Java random number generator
	 * @see Random
	 */
	public long nextLong() {
		return rand.nextLong();
	}

	/**
	 * Sets the seed of the underlying Java random number generator
	 * 
	 * @param seed the initial seed
	 */
	public void setSeed(long seed) {
		rand.setSeed(seed);
	}
}
