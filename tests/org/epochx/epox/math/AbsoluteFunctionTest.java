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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.epox.math;

import static org.junit.Assert.assertEquals;

import org.epochx.epox.*;
import org.epochx.gp.representation.AbstractDoubleNodeTestCase;
import org.junit.Test;

/**
 * 
 */
public class AbsoluteFunctionTest extends AbstractDoubleNodeTestCase {

	@Override
	public Node getNode() {
		return new AbsoluteFunction();
	}

	@Test
	public void testEvaluateZero() {
		final AbsoluteFunction node = (AbsoluteFunction) getNode();
		node.setChild(0, new Literal(0.0));
		final double result = (Double) node.evaluate();

		assertEquals("ABS of 0.0 is not 0.0", 0.0, result);
	}

	@Test
	public void testEvaluateMinusOne() {
		final AbsoluteFunction node = (AbsoluteFunction) getNode();
		node.setChild(0, new Literal(-1.0));
		final double result = (Double) node.evaluate();

		assertEquals("ABS of -1.0 is not 1.0", 1.0, result);
	}

	@Test
	public void testEvaluatePlusOne() {
		final AbsoluteFunction node = (AbsoluteFunction) getNode();
		node.setChild(0, new Literal(1.0));
		final double result = (Double) node.evaluate();

		assertEquals("ABS of 1.0 is not 1.0", 1.0, result);
	}

}
