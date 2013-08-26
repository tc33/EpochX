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
 * Unit tests for {@link org.epochx.epox.math.Max3Function}
 */
public class Max3FunctionTest extends NodeTestCase {

	private Max3Function max;
	private MockNode child1;
	private MockNode child2;
	private MockNode child3;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new Max3Function();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child1 = new MockNode();
		child2 = new MockNode();
		child3 = new MockNode();
		
		max = new Max3Function(child1, child2, child3);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Max3Function#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(0.0);
		child2.setEvaluate(0.0);
		child3.setEvaluate(0.0);
		Object result = max.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MAX of 0.0, 0.0 and 0.0 should be 0.0", 0.0, result);
		
		child1.setEvaluate(2.1);
		child2.setEvaluate(3.1);
		child3.setEvaluate(3.0);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MAX of 2.1, 3.1 and 3.0 should be 3.1", 3.1, result);
	
		child1.setEvaluate(-3.1);
		child2.setEvaluate(2.1);
		child3.setEvaluate(3.0);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MAX of -3.1, 2.1 and 3.0 should be 3.0", 3.0, result);

		child1.setEvaluate(-1.1);
		child2.setEvaluate(-1.2);
		child3.setEvaluate(-1.3);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MAX of -1.1, -1.2 and -1.3 should be -1.1", -1.1, result);
	}

	/**
	 * Tests that {@link org.epochx.epox.math.Max3Function#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(0);
		child2.setEvaluate(0);
		child3.setEvaluate(0);
		Object result = max.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MAX of 0, 0 and 0 should be 0", 0, result);
		
		child1.setEvaluate(2);
		child2.setEvaluate(3);
		child3.setEvaluate(4);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MAX of 2, 3 and 4 should be 4", 4, result);
	
		child1.setEvaluate(3);
		child2.setEvaluate(5);
		child3.setEvaluate(4);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MAX of 3, 5 and 4 should be 5", 5, result);

		child1.setEvaluate(6);
		child2.setEvaluate(4);
		child3.setEvaluate(5);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MAX of 6, 4 and 5 should be 6", 6, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Max3Function#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child1.setEvaluate(0L);
		child2.setEvaluate(0L);
		child3.setEvaluate(0L);
		Object result = max.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MAX of 0L, 0L and 0L should be 0L", 0L, result);
		
		child1.setEvaluate(2L);
		child2.setEvaluate(3L);
		child3.setEvaluate(4L);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MAX of 2L, 3L and 4L should be 4L", 4L, result);
	
		child1.setEvaluate(3L);
		child2.setEvaluate(5L);
		child3.setEvaluate(4L);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MAX of 3L, 5L and 4L should be 5L", 5L, result);

		child1.setEvaluate(6L);
		child2.setEvaluate(4L);
		child3.setEvaluate(5L);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MAX of 6L, 4L and 5L should be 6L", 6L, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Max3Function#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child1.setEvaluate(0.0f);
		child2.setEvaluate(0.0f);
		child3.setEvaluate(0.0f);
		Object result = max.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MAX of 0.0f, 0.0f and 0.0f should be 0.0f", 0.0f, result);
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(3.1f);
		child3.setEvaluate(3.0f);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MAX of 2.1f, 3.1f and 3.0f should be 3.1f", 3.1f, result);
	
		child1.setEvaluate(-3.1f);
		child2.setEvaluate(2.1f);
		child3.setEvaluate(3.0f);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MAX of -3.1f, 2.1f and 3.0f should be 3.0f", 3.0f, result);

		child1.setEvaluate(-1.1f);
		child2.setEvaluate(-1.2f);
		child3.setEvaluate(-1.3f);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MAX of -1.1f, -1.2f and -1.3f should be -1.1f", -1.1f, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Max3Function#evaluate()} 
	 * correctly evaluates mixed type values.
	 */
	@Test
	public void testEvaluateMixed() {
		child1.setEvaluate(0.0d);
		child2.setEvaluate(0);
		child3.setEvaluate(0.0f);
		Object result = max.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MAX of 0.0, 0 and 0.0f should be 0.0", 0.0, result);
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(3L);
		child3.setEvaluate(4);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MAX of 2.1f, 3L and 4 should be 4L", 4.0f, result);
	
		child1.setEvaluate(-3.1f);
		child2.setEvaluate(2.1d);
		child3.setEvaluate(3);
		result = max.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MAX of -3.1f, 2.1d and 3 should be 3.0", 3.0, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Max3Function#dataType(Class...)}
	 * returns the correct type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypeMax() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = max.dataType(type, type, type);
			assertSame("unexpected return type", type, returnType);
		}
		
		returnType = max.dataType(Short.class, Double.class, Integer.class);
		assertSame("unexpected return type", Double.class, returnType);
		
		returnType = max.dataType(Long.class, Integer.class, Short.class);
		assertSame("unexpected return type", Long.class, returnType);
		
		returnType = max.dataType(Byte.class, Float.class, Long.class);
		assertSame("unexpected return type", Float.class, returnType);
		
		returnType = max.dataType(Boolean.class, Integer.class, Integer.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = max.dataType(Integer.class, Integer.class, Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
		
		returnType = max.dataType(Integer.class, Integer.class);
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
			Node n = parser.parse("MAX3(X, X, X)");
			assertSame("Parsing did not return an instance of the correct node", Max3Function.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
