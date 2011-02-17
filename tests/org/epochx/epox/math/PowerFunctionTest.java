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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.epox.math;


import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.interpret.*;
import org.junit.Test;


/**
 * Unit tests for {@link org.epochx.epox.math.PowerFunction}
 */
public class PowerFunctionTest extends NodeTestCase {

	private PowerFunction pow;
	private MockNode child1;
	private MockNode child2;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new PowerFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child1 = new MockNode();
		child2 = new MockNode();
		
		pow = new PowerFunction(child1, child2);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.PowerFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(3.0);
		child2.setEvaluate(0.0);
		assertEquals("POW with (3.0 ^ 0.0) should be 1.0", 1.0, pow.evaluate(), 0);
		
		child1.setEvaluate(3.2);
		child2.setEvaluate(1.0);
		assertEquals("POW with (3.2 ^ 1.0) should be 3.2", 3.2, pow.evaluate(), 0);
	
		child1.setEvaluate(3.2);
		child2.setEvaluate(Double.NaN);
		assertEquals("POW with (3.2 ^ NaN) should be NaN", Double.NaN, pow.evaluate(), 0);
		
		child1.setEvaluate(Double.NaN);
		child2.setEvaluate(1.2);
		assertEquals("POW with (NaN ^ 1.2) should be NaN", Double.NaN, pow.evaluate(), 0);
		
		child1.setEvaluate(Double.NaN);
		child2.setEvaluate(0.0);
		assertEquals("POW with (NaN ^ 0.0) should be 1.0", 1.0, pow.evaluate(), 0);
		
		child1.setEvaluate(3.0);
		child2.setEvaluate(2.0);
		assertEquals("POW with (3.0 ^ 2.0) should be 9.0", 9.0, pow.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.PowerFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child1.setEvaluate(3.0f);
		child2.setEvaluate(0.0f);
		assertEquals("POW with (3.0f ^ 0.0f) should be 1.0", 1.0, pow.evaluate(), 0);
		
		child1.setEvaluate(3.2f);
		child2.setEvaluate(1.0f);
		assertEquals("POW with (3.2f ^ 1.0f) should be 3.2", 3.2, pow.evaluate(), 1);
	
		child1.setEvaluate(3.2f);
		child2.setEvaluate(Float.NaN);
		assertEquals("POW with (3.2f ^ NaN) should be NaN", Double.NaN, pow.evaluate(), 0);
		
		child1.setEvaluate(Float.NaN);
		child2.setEvaluate(1.2f);
		assertEquals("POW with (NaN ^ 1.2f) should be NaN", Double.NaN, pow.evaluate(), 0);
		
		child1.setEvaluate(Float.NaN);
		child2.setEvaluate(0.0f);
		assertEquals("POW with (NaN ^ 0.0f) should be 1.0", 1.0, pow.evaluate(), 0);
		
		child1.setEvaluate(3.0f);
		child2.setEvaluate(2.0f);
		assertEquals("POW with (3.0f ^ 2.0f) should be 9.0", 9.0, pow.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.PowerFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(3);
		child2.setEvaluate(0);
		assertEquals("POW with (3 ^ 0) should be 1.0", 1.0, pow.evaluate(), 0);
		
		child1.setEvaluate(3);
		child2.setEvaluate(1);
		assertEquals("POW with (3 ^ 1) should be 3.0", 3.0, pow.evaluate(), 0);
		
		child1.setEvaluate(3);
		child2.setEvaluate(2);
		assertEquals("POW with (3 ^ 2) should be 9.0", 9.0, pow.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.PowerFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child1.setEvaluate(3L);
		child2.setEvaluate(0L);
		assertEquals("POW with (3L ^ 0L) should be 1.0", 1.0, pow.evaluate(), 0);
		
		child1.setEvaluate(3L);
		child2.setEvaluate(1L);
		assertEquals("POW with (3L ^ 1L) should be 3.0", 3.0, pow.evaluate(), 0);
		
		child1.setEvaluate(3L);
		child2.setEvaluate(2L);
		assertEquals("POW with (3L ^ 2L) should be 9.0", 9.0, pow.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.PowerFunction#evaluate()} 
	 * correctly evaluates mixed type values.
	 */
	@Test
	public void testEvaluateMixed() {
		child1.setEvaluate(3.0f);
		child2.setEvaluate(0L);
		assertEquals("POW with (3.0f ^ 0L) should be 1.0", 1.0, pow.evaluate(), 0);
		
		child1.setEvaluate(3.2f);
		child2.setEvaluate(1);
		assertEquals("POW with (3.2f ^ 1) should be 3.2", 3.2, pow.evaluate(), 1);
		
		child1.setEvaluate(3L);
		child2.setEvaluate(2);
		assertEquals("POW with (3L ^ 2) should be 9.0", 9.0, pow.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.PowerFunction#getReturnType(Class...)}
	 * returns the correct type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypePow() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = pow.getReturnType(type, type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = pow.getReturnType(Short.class, Double.class);
		assertSame("mixed numeric input types should be Double return type", Double.class, returnType);
		
		returnType = pow.getReturnType(Double.class, Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = pow.getReturnType(Integer.class, Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
		
		returnType = pow.getReturnType(Integer.class);
		assertNull("too few inputs should be invalid", returnType);
	}
	
	/**
	 * Tests that this function can be parsed by the EpoxParser.
	 */
	@Test
	public void testEpoxParser() {
		EpoxParser parser = new EpoxParser();
		
		try {
			parser.declareVariable(new Variable("X", Double.class));
			Node n = parser.parse("POW(X, X)");
			assertSame("Parsing did not return an instance of the correct node", PowerFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
