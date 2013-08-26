/*
 * Copyright 2007-2013
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
package org.epochx.epox.math;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.interpret.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.math.GreaterThanFunction}
 */
public class GreaterThanFunctionTest extends NodeTestCase {

	private GreaterThanFunction gt;
	private MockNode child1;
	private MockNode child2;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new GreaterThanFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child1 = new MockNode();
		child2 = new MockNode();
		
		gt = new GreaterThanFunction(child1, child2);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.GreaterThanFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(0.0);
		child2.setEvaluate(0.0);
		assertFalse("GT of 0.0 and 0.0 should be FALSE", gt.evaluate());
		
		child1.setEvaluate(0.1);
		child2.setEvaluate(0.0);
		assertTrue("GT of 0.1 and 0.0 should be TRUE", gt.evaluate());
	
		child1.setEvaluate(0.0);
		child2.setEvaluate(0.1);
		assertFalse("GT of 0.0 and 0.1 should be FALSE", gt.evaluate());
		
		child1.setEvaluate(-0.1);
		child2.setEvaluate(0.0);
		assertFalse("GT of -0.1 and 0.0 should be FALSE", gt.evaluate());
		
		child1.setEvaluate(0.0);
		child2.setEvaluate(-0.1);
		assertTrue("GT of 0.0 and -0.1 should be TRUE", gt.evaluate());
		
		child1.setEvaluate(Double.POSITIVE_INFINITY);
		child2.setEvaluate(Double.NEGATIVE_INFINITY);
		assertTrue("GT of +Infinity and -Infinity should be TRUE", gt.evaluate());
	}

	/**
	 * Tests that {@link org.epochx.epox.math.GreaterThanFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(0);
		child2.setEvaluate(0);
		assertFalse("GT of 0 and 0 should be FALSE", gt.evaluate());
		
		child1.setEvaluate(1);
		child2.setEvaluate(0);
		assertTrue("GT of 1 and 0 should be TRUE", gt.evaluate());
	
		child1.setEvaluate(0);
		child2.setEvaluate(1);
		assertFalse("GT of 0 and 1 should be FALSE", gt.evaluate());
		
		child1.setEvaluate(-1);
		child2.setEvaluate(0);
		assertFalse("GT of -1 and 0 should be FALSE", gt.evaluate());
		
		child1.setEvaluate(0);
		child2.setEvaluate(-1);
		assertTrue("GT of 0 and -1 should be TRUE", gt.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.GreaterThanFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child1.setEvaluate(0L);
		child2.setEvaluate(0L);
		assertFalse("GT of 0L and 0L should be FALSE", gt.evaluate());
		
		child1.setEvaluate(1L);
		child2.setEvaluate(0L);
		assertTrue("GT of 1 and 0 should be TRUE", gt.evaluate());
	
		child1.setEvaluate(0L);
		child2.setEvaluate(1L);
		assertFalse("GT of 0 and 1 should be FALSE", gt.evaluate());
		
		child1.setEvaluate(-1L);
		child2.setEvaluate(0L);
		assertFalse("GT of -1 and 0 should be FALSE", gt.evaluate());
		
		child1.setEvaluate(0L);
		child2.setEvaluate(-1L);
		assertTrue("GT of 0 and -1 should be TRUE", gt.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.GreaterThanFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child1.setEvaluate(0.0f);
		child2.setEvaluate(0.0f);
		assertFalse("GT of 0.0f and 0.0f should be FALSE", gt.evaluate());
		
		child1.setEvaluate(0.1f);
		child2.setEvaluate(0.0f);
		assertTrue("GT of 0.1f and 0.0f should be TRUE", gt.evaluate());
	
		child1.setEvaluate(0.0f);
		child2.setEvaluate(0.1f);
		assertFalse("GT of 0.0f and 0.1f should be FALSE", gt.evaluate());
		
		child1.setEvaluate(-0.1f);
		child2.setEvaluate(0.0f);
		assertFalse("GT of -0.1f and 0.0f should be FALSE", gt.evaluate());
		
		child1.setEvaluate(0.0f);
		child2.setEvaluate(-0.1f);
		assertTrue("GT of 0.0f and -0.1f should be TRUE", gt.evaluate());
		
		child1.setEvaluate(Double.POSITIVE_INFINITY);
		child2.setEvaluate(Double.NEGATIVE_INFINITY);
		assertTrue("GT of +Infinity and -Infinity should be TRUE", gt.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.GreaterThanFunction#evaluate()} 
	 * correctly evaluates mixed type values.
	 */
	@Test
	public void testEvaluateMixed() {
		child1.setEvaluate(1.1);
		child2.setEvaluate(1);
		assertTrue("GT of 1.1 and 1 should be TRUE", gt.evaluate());
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(2.2d);
		assertFalse("GT of 2.1f and 2.2d should be FALSE", gt.evaluate());
	
		child1.setEvaluate(6L);
		child2.setEvaluate(5);
		assertTrue("GT of 6L and 5 should be TRUE", gt.evaluate());
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(3);
		assertFalse("GT of -2.1f and 3 should be FALSE", gt.evaluate());
		
		child1.setEvaluate(3L);
		child2.setEvaluate(2.1f);
		assertTrue("GT of 3L and 2.1f should be TRUE", gt.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.GreaterThanFunction#dataType(Class...)}
	 * returns the Boolean type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypeGt() {
		Class<?>[] inputTypes = {Byte.class, Short.class, Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type1: inputTypes) {
			for (Class<?> type2: inputTypes) {
				returnType = gt.dataType(type1, type2);
				assertSame("unexpected return type", Boolean.class, returnType);
			}
		}
		
		returnType = gt.dataType(Boolean.class, Integer.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = gt.dataType(Integer.class, Integer.class, Integer.class);
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
			Node n = parser.parse("GT(X, X)");
			assertSame("Parsing did not return an instance of the correct node", GreaterThanFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
