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
package org.epochx.gr.op.init;

import static org.junit.Assert.*;

import org.epochx.tools.grammar.Grammar;
import org.epochx.tools.random.MersenneTwisterFast;
import org.junit.*;

/**
 * 
 */
public class RampedHalfAndHalfInitialiserTest {

	private RampedHalfAndHalfInitialiser initialiser;

	@Before
	public void setUp() {
		initialiser = new RampedHalfAndHalfInitialiser(null, null, -1, -1, -1, false);

		final String grammarStr = "<a> ::= x | y\n";

		// Ensure setup is valid.
		initialiser.setStartMaxDepth(1);
		initialiser.setEndMaxDepth(1);
		initialiser.setRNG(new MersenneTwisterFast());
		initialiser.setGrammar(new Grammar(grammarStr));
		initialiser.setPopSize(1);
	}

	/**
	 * Tests that an illegal state exception is not thrown with valid
	 * parameters.
	 */
	@Test
	public void testGetPopValid() {
		try {
			initialiser.getInitialPopulation();
		} catch (final IllegalStateException e) {
			fail("illegal state exception thrown for valid parameters");
		}
	}

	/**
	 * Tests that an illegal state exception is thrown if population size
	 * parameter is zero.
	 */
	@Test
	public void testGetPopZeroPopSize() {
		// Setup initialiser to be valid except for pop size.
		initialiser.setPopSize(0);

		try {
			initialiser.getInitialPopulation();
			fail("illegal state exception not thrown for pop size being 0");
		} catch (final IllegalStateException e) {
		}
	}

	/**
	 * Tests that an illegal state exception is thrown if the minimum possible
	 * depth is greater than the maximum depth setting.
	 */
	@Test
	public void testGetPopMaxDepth() {
		initialiser.setEndMaxDepth(1);
		final String grammarStr = "<a> ::= <b> | y <b>\n" + "<b> ::= c | x\n";
		initialiser.setGrammar(new Grammar(grammarStr));

		final int minDepth = initialiser.getGrammar().getStartRule().getMinDepth();
		assertEquals("Minimum depth not calculated correctly", 2, minDepth);

		try {
			initialiser.getInitialPopulation();
			fail("illegal state exception not thrown for a maximum depth which is less than the minimum possible depth");
		} catch (final IllegalStateException e) {
		}
	}

	/**
	 * Tests that an illegal state exception is thrown if no grammar is set.
	 */
	@Test
	public void testGetPopGrammarNull() {
		initialiser.setGrammar(null);

		try {
			initialiser.getInitialPopulation();
			fail("illegal state exception not thrown for a null grammar");
		} catch (final IllegalStateException e) {
		}
	}

	/**
	 * Tests that an illegal state exception is thrown if the start maximum
	 * depth is larger than the end maximum depth.
	 */
	@Test
	public void testGetPopDepthsSequence() {
		initialiser.setStartMaxDepth(4);
		initialiser.setEndMaxDepth(3);

		try {
			initialiser.getInitialPopulation();
			fail("illegal state exception not thrown when start depth is greater than end depth");
		} catch (final IllegalStateException e) {
		}
	}
}
