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
package org.epochx.epox;

import static org.junit.Assert.*;

import org.epochx.tools.random.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.BooleanERC}
 */
public class BooleanERCTest extends LiteralTest {

	private BooleanERC erc;
	private RandomNumberGenerator rng;

	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() throws Exception {
		rng = new MersenneTwisterFast();
		erc = new BooleanERC(rng);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.BooleanERC#newInstance()} correctly
	 * constructs new instances.
	 */
	@Test
	public void testNewInstanceBooleanERC() {
		final BooleanERC newInstance = erc.newInstance();

		assertSame("rng does not refer to the same instance", rng, newInstance.getRNG());
		assertNotSame("the value of new instance refers to the same object", erc.getValue(), newInstance.getValue());
	}

	/**
	 * Tests that {@link org.epochx.epox.BooleanERC#generateValue()} correctly
	 * generates new values.
	 */
	@Test
	public void testGenerateValue() {
		final MockRandom rng = new MockRandom();
		erc.setRNG(rng);

		rng.setNextBoolean(true);
		boolean generatedValue = erc.generateValue();
		assertSame("generated value unexpected", true, generatedValue);

		rng.setNextBoolean(false);
		generatedValue = erc.generateValue();
		assertSame("generated value unexpected", false, generatedValue);
	}

	/**
	 * Tests that {@link org.epochx.epox.BooleanERC#generateValue()} throws an
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
	public void testCloneBooleanERC() {
		final BooleanERC clone = (BooleanERC) erc.clone();

		assertNotSame("ERC has not been cloned", erc, clone);
		assertSame("rng does not refer to the same instance", rng, clone.getRNG());
		assertSame("value does not refer to the same instance", erc.getValue(), clone.getValue());
	}

}
