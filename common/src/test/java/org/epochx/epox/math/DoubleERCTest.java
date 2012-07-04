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
package org.epochx.epox.math;

import static org.junit.Assert.*;

import org.epochx.RandomSequence;
import org.epochx.epox.*;
import org.epochx.epox.math.DoubleERC;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.tools.random.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.math.DoubleERC}
 */
public class DoubleERCTest extends LiteralTest {

	private DoubleERC erc;
	private RandomSequence rng;

	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	protected Node getNode() {
		return new DoubleERC(new MockRandom(), 0.0, 1.0, 4);
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		rng = new MersenneTwisterFast();
		erc = new DoubleERC(rng, 1.0, 2.0, 3);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.DoubleERC#DoubleERC(RandomNumberGenerator, double, double, int)} 
	 * throws an exception if rng is null.
	 */
	@Test
	public void testNewInstanceDoubleERCNull() {
		try {
			new DoubleERC(null, 0.0, 1.0, 1);
			fail("an exception should be thrown for a null rng");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.DoubleERC#newInstance()} correctly
	 * constructs new instances.
	 */
	@Test
	public void testNewInstanceDoubleERC() {
		final DoubleERC newInstance = erc.newInstance();

		assertSame("rng does not refer to the same instance", rng, newInstance.getRNG());
		// This test is fragile - if it fails it might be because the same number was generated - rerun (v.unlikely to fail twice).
		assertNotSame("the value of new instance refers to the same object", erc.getValue(), newInstance.getValue());
	}

	/**
	 * Tests that {@link org.epochx.epox.math.DoubleERC#generateValue()} correctly
	 * generates new values.
	 */
	@Test
	public void testGenerateValue() {
		final MockRandom rng = new MockRandom();
		erc.setRNG(rng);

		final double lower = 2.0;
		final double upper = 5.0;

		// Set the bounds.
		erc.setLower(lower);
		erc.setUpper(upper);
		erc.setPrecision(4);

		// Tests lower value.
		rng.setNextDouble(0.0);
		double generatedValue = erc.generateValue();
		assertEquals("generated value unexpected", (Object) lower, generatedValue);

		// Tests upper value.
		rng.setNextDouble(1.0);
		generatedValue = erc.generateValue();
		assertEquals("generated value unexpected", (Object) upper, generatedValue);

		// Tests mid-value with precision.
		rng.setNextDouble(0.5155);
		generatedValue = erc.generateValue();
		assertEquals("generated value unexpected", (Object) 3.546, generatedValue);

		// Test reduced precision gets rounded up.
		erc.setPrecision(3);
		generatedValue = erc.generateValue();
		assertEquals("generated value unexpected", (Object) 3.55, generatedValue);
	}

	/**
	 * Tests that {@link org.epochx.epox.math.DoubleERC#generateValue()} throws an
	 * exception if rng is null.
	 */
	@Test
	public void testGenerateValueNull() {
		erc.setRNG(null);
		try {
			erc.generateValue();
			fail("exception not thrown for null RNG");
		} catch (final IllegalStateException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Tests that {@link org.epochx.epox.Literal#clone()} correctly clones
	 * instances.
	 */
	@Test
	public void testCloneDoubleERC() {
		final DoubleERC clone = (DoubleERC) erc.clone();

		assertNotSame("ERC has not been cloned", erc, clone);
		assertSame("rng does not refer to the same instance", rng, clone.getRNG());
		assertSame("value does not refer to the same instance", erc.getValue(), clone.getValue());
	}
}
