/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.tools.random;


/**
 * A mock random number generator for the purpose of testing.
 */
public class MockRandom implements RandomNumberGenerator {

	// The values that will be returned as the 'random' values.
	private int nextInt;
	private double nextDouble;
	private boolean nextBoolean;
	
	/**
	 * Sets the <code>int</code> that will be returned by the 
	 * <code>nextInt</code> method.
	 * 
	 * @param nextInt the int to be the next 'random' int.
	 */
	public void setNextInt(int nextInt) {
		this.nextInt = nextInt;
	}
	
	/**
	 * Sets the <code>double</code> that will be returned by the 
	 * <code>nextDouble</code> method.
	 * 
	 * @param nextDouble the double to be the next 'random' double.
	 */
	public void setNextDouble(double nextDouble) {
		this.nextDouble = nextDouble;
	}
	
	/**
	 * Sets the <code>boolean</code> that will be returned by the 
	 * <code>nextBoolean</code> method.
	 * 
	 * @param nextBoolean the boolean to be the next 'random' boolean.
	 */
	public void setNextBoolean(boolean nextBoolean) {
		this.nextBoolean = nextBoolean;
	}
	
	/**
	 * Returns the mock next 'random' int.
	 */
	@Override
	public int nextInt(int n) {
		return nextInt;
	}

	/**
	 * Returns the mock next 'random' int.
	 */
	@Override
	public int nextInt() {
		return nextInt;
	}

	/**
	 * Returns the mock next 'random' double.
	 */
	@Override
	public double nextDouble() {
		return nextDouble;
	}

	/**
	 * Returns the mock next 'random' boolean.
	 */
	@Override
	public boolean nextBoolean() {
		return nextBoolean;
	}

	/**
	 * Not implemented.
	 */
	@Override
	public void setSeed(long seed) {}

}
