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
package org.epochx.epox.math;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.interpret.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.math.Min3Function}
 */
public class Min3FunctionTest extends NodeTestCase {

	private Min3Function min;
	private MockNode child1;
	private MockNode child2;
	private MockNode child3;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new Min3Function();
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
		
		min = new Min3Function(child1, child2, child3);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Min3Function#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(0.0);
		child2.setEvaluate(0.0);
		child3.setEvaluate(0.0);
		Object result = min.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MIN of 0.0, 0.0 and 0.0 should be 0.0", 0.0, result);
		
		child1.setEvaluate(3.1);
		child2.setEvaluate(2.1);
		child3.setEvaluate(3.0);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MIN of 3.1, 2.1 and 3.0 should be 2.1", 2.1, result);
	
		child1.setEvaluate(-3.1);
		child2.setEvaluate(2.1);
		child3.setEvaluate(3.0);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MIN of -3.1, 2.1 and 3.0 should be -3.1", -3.1, result);

		child1.setEvaluate(-1.1);
		child2.setEvaluate(-1.2);
		child3.setEvaluate(-1.3);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MIN of -1.1, -1.2 and -1.3 should be -1.3", -1.3, result);
	}

	/**
	 * Tests that {@link org.epochx.epox.math.Min3Function#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(0);
		child2.setEvaluate(0);
		child3.setEvaluate(0);
		Object result = min.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MIN of 0, 0 and 0 should be 0", 0, result);
		
		child1.setEvaluate(2);
		child2.setEvaluate(3);
		child3.setEvaluate(4);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MIN of 2, 3 and 4 should be 2", 2, result);
	
		child1.setEvaluate(4);
		child2.setEvaluate(3);
		child3.setEvaluate(5);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MIN of 4, 3 and 5 should be 3", 3, result);

		child1.setEvaluate(6);
		child2.setEvaluate(5);
		child3.setEvaluate(4);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MIN of 6, 5 and 4 should be 4", 4, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Min3Function#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child1.setEvaluate(0L);
		child2.setEvaluate(0L);
		child3.setEvaluate(0L);
		Object result = min.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MIN of 0L, 0L and 0L should be 0L", 0L, result);
		
		child1.setEvaluate(2L);
		child2.setEvaluate(3L);
		child3.setEvaluate(4L);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MIN of 2L, 3L and 4L should be 2L", 2L, result);
	
		child1.setEvaluate(3L);
		child2.setEvaluate(5L);
		child3.setEvaluate(4L);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MIN of 5L, 3L and 4L should be 3L", 3L, result);

		child1.setEvaluate(6L);
		child2.setEvaluate(5L);
		child3.setEvaluate(4L);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MIN of 6L, 5L and 4L should be 4L", 4L, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Min3Function#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child1.setEvaluate(0.0f);
		child2.setEvaluate(0.0f);
		child3.setEvaluate(0.0f);
		Object result = min.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MIN of 0.0f, 0.0f and 0.0f should be 0.0f", 0.0f, result);
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(3.1f);
		child3.setEvaluate(3.0f);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MIN of 2.1f, 3.1f and 3.0f should be 2.1f", 2.1f, result);
	
		child1.setEvaluate(3.1f);
		child2.setEvaluate(-3.1f);
		child3.setEvaluate(3.0f);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MIN of 3.0f, -3.1f and 3.0f should be -3.1f", -3.1f, result);

		child1.setEvaluate(-1.1f);
		child2.setEvaluate(-1.2f);
		child3.setEvaluate(-1.3f);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MIN of -1.1f, -1.2f and -1.3f should be -1.3f", -1.3f, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Min3Function#evaluate()} 
	 * correctly evaluates mixed type values.
	 */
	@Test
	public void testEvaluateMixed() {
		child1.setEvaluate(0.0d);
		child2.setEvaluate(0);
		child3.setEvaluate(0.0f);
		Object result = min.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MIN of 0.0, 0 and 0.0f should be 0.0", 0.0, result);
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(3L);
		child3.setEvaluate(4);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MIN of 2.1f, 3L and 4 should be 4L", 2.1f, result);
	
		child1.setEvaluate(-3.1f);
		child2.setEvaluate(2.1d);
		child3.setEvaluate(3);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MIN of -3.1f, 2.1d and 3 should be -3.1d", -3.1, (Double) result, 1);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Min3Function#getReturnType(Class...)}
	 * returns the correct type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypeMin() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = min.getReturnType(type, type, type);
			assertSame("unexpected return type", type, returnType);
		}
		
		returnType = min.getReturnType(Short.class, Double.class, Integer.class);
		assertSame("unexpected return type", Double.class, returnType);
		
		returnType = min.getReturnType(Long.class, Integer.class, Short.class);
		assertSame("unexpected return type", Long.class, returnType);
		
		returnType = min.getReturnType(Byte.class, Float.class, Long.class);
		assertSame("unexpected return type", Float.class, returnType);
		
		returnType = min.getReturnType(Boolean.class, Integer.class, Integer.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = min.getReturnType(Integer.class, Integer.class, Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
		
		returnType = min.getReturnType(Integer.class, Integer.class);
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
			Node n = parser.parse("MIN3(X, X, X)");
			assertSame("Parsing did not return an instance of the correct node", Min3Function.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
