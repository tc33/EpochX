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
 * Unit tests for {@link org.epochx.epox.math.LessThanFunction}
 */
public class LessThanFunctionTest extends NodeTestCase {

	private LessThanFunction lt;
	private MockNode child1;
	private MockNode child2;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new LessThanFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child1 = new MockNode();
		child2 = new MockNode();
		
		lt = new LessThanFunction(child1, child2);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.LessThanFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(0.0);
		child2.setEvaluate(0.0);
		assertFalse("LT of 0.0 and 0.0 should be FALSE", lt.evaluate());
		
		child1.setEvaluate(0.1);
		child2.setEvaluate(0.0);
		assertFalse("LT of 0.1 and 0.0 should be FALSE", lt.evaluate());
	
		child1.setEvaluate(0.0);
		child2.setEvaluate(0.1);
		assertTrue("LT of 0.0 and 0.1 should be TRUE", lt.evaluate());
		
		child1.setEvaluate(-0.1);
		child2.setEvaluate(0.0);
		assertTrue("LT of -0.1 and 0.0 should be TRUE", lt.evaluate());
		
		child1.setEvaluate(0.0);
		child2.setEvaluate(-0.1);
		assertFalse("LT of 0.0 and -0.1 should be FALSE", lt.evaluate());
		
		child1.setEvaluate(Double.POSITIVE_INFINITY);
		child2.setEvaluate(Double.NEGATIVE_INFINITY);
		assertFalse("LT of +Infinity and -Infinity should be FALSE", lt.evaluate());
	}

	/**
	 * Tests that {@link org.epochx.epox.math.LessThanFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(0);
		child2.setEvaluate(0);
		assertFalse("LT of 0 and 0 should be FALSE", lt.evaluate());
		
		child1.setEvaluate(1);
		child2.setEvaluate(0);
		assertFalse("LT of 1 and 0 should be FALSE", lt.evaluate());
	
		child1.setEvaluate(0);
		child2.setEvaluate(1);
		assertTrue("LT of 0 and 1 should be TRUE", lt.evaluate());
		
		child1.setEvaluate(-1);
		child2.setEvaluate(0);
		assertTrue("LT of -1 and 0 should be TRUE", lt.evaluate());
		
		child1.setEvaluate(0);
		child2.setEvaluate(-1);
		assertFalse("LT of 0 and -1 should be FALSE", lt.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.LessThanFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child1.setEvaluate(0L);
		child2.setEvaluate(0L);
		assertFalse("LT of 0L and 0L should be FALSE", lt.evaluate());
		
		child1.setEvaluate(1L);
		child2.setEvaluate(0L);
		assertFalse("LT of 1 and 0 should be FALSE", lt.evaluate());
	
		child1.setEvaluate(0L);
		child2.setEvaluate(1L);
		assertTrue("LT of 0 and 1 should be TRUE", lt.evaluate());
		
		child1.setEvaluate(-1L);
		child2.setEvaluate(0L);
		assertTrue("LT of -1 and 0 should be TRUE", lt.evaluate());
		
		child1.setEvaluate(0L);
		child2.setEvaluate(-1L);
		assertFalse("LT of 0 and -1 should be FALSE", lt.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.LessThanFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child1.setEvaluate(0.0f);
		child2.setEvaluate(0.0f);
		assertFalse("LT of 0.0f and 0.0f should be FALSE", lt.evaluate());
		
		child1.setEvaluate(0.1f);
		child2.setEvaluate(0.0f);
		assertFalse("LT of 0.1f and 0.0f should be FALSE", lt.evaluate());
	
		child1.setEvaluate(0.0f);
		child2.setEvaluate(0.1f);
		assertTrue("LT of 0.0f and 0.1f should be TRUE", lt.evaluate());
		
		child1.setEvaluate(-0.1f);
		child2.setEvaluate(0.0f);
		assertTrue("LT of -0.1f and 0.0f should be TRUE", lt.evaluate());
		
		child1.setEvaluate(0.0f);
		child2.setEvaluate(-0.1f);
		assertFalse("LT of 0.0f and -0.1f should be FALSE", lt.evaluate());
		
		child1.setEvaluate(Double.POSITIVE_INFINITY);
		child2.setEvaluate(Double.NEGATIVE_INFINITY);
		assertFalse("LT of +Infinity and -Infinity should be FALSE", lt.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.LessThanFunction#evaluate()} 
	 * correctly evaluates mixed type values.
	 */
	@Test
	public void testEvaluateMixed() {
		child1.setEvaluate(1.1);
		child2.setEvaluate(1);
		assertFalse("LT of 1.1 and 1 should be FALSE", lt.evaluate());
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(2.2d);
		assertTrue("LT of 2.1f and 2.2d should be TRUE", lt.evaluate());
	
		child1.setEvaluate(6L);
		child2.setEvaluate(5);
		assertFalse("LT of 6L and 5 should be FALSE", lt.evaluate());
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(3);
		assertTrue("LT of -2.1f and 3 should be TRUE", lt.evaluate());
		
		child1.setEvaluate(3L);
		child2.setEvaluate(2.1f);
		assertFalse("LT of 3L and 2.1f should be FALSE", lt.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.LessThanFunction#dataType(Class...)}
	 * returns the Boolean type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypeLt() {
		Class<?>[] inputTypes = {Byte.class, Short.class, Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type1: inputTypes) {
			for (Class<?> type2: inputTypes) {
				returnType = lt.dataType(type1, type2);
				assertSame("unexpected return type", Boolean.class, returnType);
			}
		}
		
		returnType = lt.dataType(Boolean.class, Integer.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = lt.dataType(Integer.class, Integer.class, Integer.class);
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
			Node n = parser.parse("LT(X, X)");
			assertSame("Parsing did not return an instance of the correct node", LessThanFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
