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
 * Unit tests for {@link org.epochx.epox.trig.AreaHyperbolicCosineFunction}
 */
public class AreaHyperbolicCosineFunctionTest extends NodeTestCase {

	private AreaHyperbolicCosineFunction arcosh;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new AreaHyperbolicCosineFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		arcosh = new AreaHyperbolicCosineFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.AreaHyperbolicCosineFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(Math.cosh(1.6));
		assertEquals("ARCOSH should be the inverse of cosh", 1.6, arcosh.evaluate(), 1);
		
		child.setEvaluate(Math.cosh(1.0));
		assertEquals("ARCOSH should be the inverse of cosh", 1.0, arcosh.evaluate(), 0);
		
		child.setEvaluate(1.0);
		assertEquals("ARCOSH of 1.0 should be 0.0", 0.0, arcosh.evaluate(), 0);
		
		child.setEvaluate(Double.POSITIVE_INFINITY);
		assertEquals("ARCOSH of positive infinity should be positive infinity", Double.POSITIVE_INFINITY, arcosh.evaluate(), 0);
		
		child.setEvaluate(0.9);
		assertEquals("ARCOSH of 0.9 should be NaN", Double.NaN, arcosh.evaluate(), 0);
		
		child.setEvaluate(-1.0);
		assertEquals("ARCOSH of -1.0 should be NaN", Double.NaN, arcosh.evaluate(), 0);
		
		child.setEvaluate(Double.NaN);
		assertEquals("ARCOSH of NaN should be NaN", Double.NaN, arcosh.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.AreaHyperbolicCosineFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(2);
		assertSame("ARCOSH of an integer should return double", Double.class, arcosh.evaluate().getClass());
		
		child.setEvaluate(1);
		assertEquals("ARCOSH of 1 should be 0.0", 0.0, arcosh.evaluate(), 0);
		
		child.setEvaluate(-1);
		assertEquals("ARCOSH should be NaN", Double.NaN, arcosh.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.AreaHyperbolicCosineFunction#getReturnType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeArcosh() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = arcosh.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = arcosh.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = arcosh.getReturnType(Integer.class, Integer.class);
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
			Node n = parser.parse("ARCOSH(X)");
			assertSame("Parsing did not return an instance of the correct node", AreaHyperbolicCosineFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
