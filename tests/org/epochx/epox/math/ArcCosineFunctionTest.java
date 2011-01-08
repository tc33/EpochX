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
public class ArcCosineFunctionTest extends AbstractDoubleNodeTestCase {

	@Override
	public Node getNode() {
		return new ArcCosineFunction();
	}

	@Test
	public void testEvaluatePositive() {
		final ArcCosineFunction node = (ArcCosineFunction) getNode();
		node.setChild(0, new Literal(Math.cos(0.6)));
		final double result = node.evaluate();

		assertEquals("ACOS of 0.6 is not equal to the inverse of cos", 0.6, result);
	}

	@Test
	public void testEvaluateNegative() {
		final ArcCosineFunction node = (ArcCosineFunction) getNode();
		node.setChild(0, new Literal(Math.cos(-0.6)));
		final double result = node.evaluate();

		assertEquals("ACOS of -0.6 is not equal to the inverse of cos", 0.6, result);
	}

	@Test
	public void testEvaluateOne() {
		final ArcCosineFunction node = (ArcCosineFunction) getNode();
		node.setChild(0, new Literal(1.0));
		final double result = node.evaluate();

		assertEquals("ACOS of 0.0 and not equal to the inverse of cos", 0.0, result);
	}

	@Test
	public void testEvaluatePositiveNaN() {
		final ArcCosineFunction node = (ArcCosineFunction) getNode();
		node.setChild(0, new Literal(2.0));
		final double result = node.evaluate();

		assertEquals("ACOS of 2.0 not returning NaN", Double.NaN, result);
	}

	@Test
	public void testEvaluateNegativeNaN() {
		final ArcCosineFunction node = (ArcCosineFunction) getNode();
		node.setChild(0, new Literal(-2.0));
		final double result = node.evaluate();

		assertEquals("ACOS of 0.0 not returning NaN", Double.NaN, result);
	}
}
