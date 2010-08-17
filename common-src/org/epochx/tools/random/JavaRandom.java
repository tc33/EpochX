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

import java.util.Random;

/**
 *
 */
public class JavaRandom implements RandomNumberGenerator {

	private Random rand;
	
	public JavaRandom() {
		this.rand = new Random();
	}
	
	public JavaRandom(long seed) {
		this.rand = new Random(seed);
	}
	
	public JavaRandom(Random rand) {
		this.rand = rand;
	}

	@Override
	public boolean nextBoolean() {
		return rand.nextBoolean();
	}

	@Override
	public double nextDouble() {
		return rand.nextDouble();
	}

	@Override
	public int nextInt(int n) {
		return rand.nextInt(n);
	}

	@Override
	public int nextInt() {
		return rand.nextInt();
	}

	@Override
	public void setSeed(long l) {
		rand.setSeed(l);
	}
}
