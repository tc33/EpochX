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
package org.epochx.epox.trig;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.interpret.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.trig.AreaHyperbolicTangentFunction}
 */
public class AreaHyperbolicTangentFunctionTest extends NodeTestCase {

	private AreaHyperbolicTangentFunction artanh;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new AreaHyperbolicTangentFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		artanh = new AreaHyperbolicTangentFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.AreaHyperbolicTangentFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(Math.tanh(1.6));
		assertEquals("ARTANH should be the inverse of tanh", 1.6, artanh.evaluate(), 1);
		
		child.setEvaluate(1.0);
		assertEquals("ARTANH of 1.0 should be positive infinity", Double.POSITIVE_INFINITY, artanh.evaluate(), 0);
		
		child.setEvaluate(1.1);
		assertEquals("ARTANH of 1.0 should be NaN", Double.NaN, artanh.evaluate(), 0);
		
		child.setEvaluate(Double.POSITIVE_INFINITY);
		assertEquals("ARTANH of positive infinity should be NaN", Double.NaN, artanh.evaluate(), 0);
		
		child.setEvaluate(0.0);
		assertEquals("ARTANH of 0.0 should be 0.0", 0.0, artanh.evaluate(), 0);
		
		child.setEvaluate(-1.0);
		assertEquals("ARTANH of -1.0 should be negative infinity", Double.NEGATIVE_INFINITY, artanh.evaluate(), 0);
		
		child.setEvaluate(-1.1);
		assertEquals("ARTANH of -1.1 should be NaN", Double.NaN, artanh.evaluate(), 0);
		
		child.setEvaluate(Double.NEGATIVE_INFINITY);
		assertEquals("ARTANH of negative infinity should be NaN", Double.NaN, artanh.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.AreaHyperbolicTangentFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertSame("ARTANH of an integer should return double", Double.class, artanh.evaluate().getClass());
		
		child.setEvaluate(0);
		assertEquals("ARTANH of 0 should be 0.0", 0.0, artanh.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.AreaHyperbolicTangentFunction#getReturnType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeArtanh() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = artanh.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = artanh.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = artanh.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
	
	/**
	 * Tests that this function can be parsed by the EpoxParser.
	 */
	@Test
	public void testEpoxParser() {
		EpoxParser parser = new EpoxParser();
		
		try {
			parser.declareVariable(new Variable("X", Double.class));
			Node n = parser.parse("ARTANH(X)");
			assertSame("Parsing did not return an instance of the correct node", AreaHyperbolicTangentFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
