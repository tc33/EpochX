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
package org.epochx.gp.operator;

import org.epochx.RandomSequence;

/**
 * This class implements a Koza style crossover operation on two
 * <code>Individuals</code>.
 * 
 * <p>
 * Koza crossover performs a one point exchange with the choice of a swap point
 * being either a function or terminal node with assigned probabilities. This
 * class provides a constructor for specifying a probability of selecting a
 * function swap point. The default constructor uses the typical rates of 90%
 * function node swap point and 10% terminal node swap points.
 * 
 * @see SubtreeCrossover
 * @see OnePointCrossover
 */
public class KozaCrossover extends SubtreeCrossover {

	/**
	 * Constructs a <code>KozaCrossover</code>. The probability of a
	 * function node being selected as the swap point will default to 90%.
	 * 
	 * @param random
	 */
	public KozaCrossover(int probability, RandomSequence random) {
		this(probability, random, 0.9);
	}

	/**
	 * Constructs a <code>KozaCrossover</code>.
	 * 
	 * @param random a random number generator.
	 * @param functionSwapProbability The probability of crossover operations
	 *        choosing a function node as the swap point.
	 */
	public KozaCrossover(int probability, RandomSequence random, double functionSwapProbability) {
		super(probability, random, functionSwapProbability);
	}

	/**
	 * Default constructor for Koza standard crossover. The probability of a
	 * function node being selected as the swap point will default to 90%.
	 */
	public KozaCrossover(int probability) {
		this(probability, 0.9);
	}

	/**
	 * Construct an instance of Koza crossover.
	 * 
	 * @param functionSwapProbability The probability of crossover operations
	 *        choosing a function node as the swap point.
	 */
	public KozaCrossover(int probability, double functionSwapProbability) {
		super(functionSwapProbability);
	}
}
