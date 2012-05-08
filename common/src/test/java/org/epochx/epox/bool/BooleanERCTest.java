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
package org.epochx.epox.bool;

import static org.junit.Assert.*;

import org.epochx.RandomSequence;
import org.epochx.epox.*;
import org.epochx.epox.bool.BooleanERC;
import org.epochx.random.MersenneTwisterFast;
import org.epochx.tools.random.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.bool.BooleanERC}
 */
public class BooleanERCTest extends LiteralTest {

	private BooleanERC erc;
	private RandomSequence rng;

	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	protected Node getNode() {
		return new BooleanERC(new MockRandom());
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		rng = new MersenneTwisterFast();
		erc = new BooleanERC(rng);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.bool.BooleanERC#BooleanERC(RandomNumberGenerator)} 
	 * throws an exception if rng is null.
	 */
	@Test
	public void testNewInstanceBooleanERCNull() {
		try {
			new BooleanERC(null);
			fail("an exception should be thrown for a null rng");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.bool.BooleanERC#generateValue()} correctly
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
	 * Tests that {@link org.epochx.epox.bool.BooleanERC#generateValue()} throws an
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
		assertSame("rng does not refer to the same instance", rng, clone.getRandomSequence());
		assertSame("value does not refer to the same instance", erc.getValue(), clone.getValue());
	}

}
