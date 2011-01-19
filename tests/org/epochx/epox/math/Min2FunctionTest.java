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

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.math.Min2Function}
 */
public class Min2FunctionTest extends NodeTestCase {

	private Min2Function min;
	private MockNode child1;
	private MockNode child2;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new Min2Function();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child1 = new MockNode();
		child2 = new MockNode();
		
		min = new Min2Function(child1, child2);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Min2Function#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(0.0);
		child2.setEvaluate(0.0);
		Object result = min.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MIN of 0.0 and 0.0 should be 0.0", 0.0, result);
		
		child1.setEvaluate(2.1);
		child2.setEvaluate(3.1);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MIN of 2.1 and 3.1 should be 2.1", 2.1, result);
	
		child1.setEvaluate(-3.1);
		child2.setEvaluate(2.1);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MIN of -3.1 and 2.1 should be -3.1", -3.1, result);
	}

	/**
	 * Tests that {@link org.epochx.epox.math.Min2Function#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(0);
		child2.setEvaluate(0);
		Object result = min.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MIN of 0 and 0 should be 0", 0, result);
		
		child1.setEvaluate(2);
		child2.setEvaluate(3);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MIN of 2 and 3 should be 2", 2, result);
	
		child1.setEvaluate(-3);
		child2.setEvaluate(2);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MIN of -3 and 2 should be -3", -3, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Min2Function#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child1.setEvaluate(0L);
		child2.setEvaluate(0L);
		Object result = min.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MIN of 0L and 0L should be 0L", 0L, result);
		
		child1.setEvaluate(2L);
		child2.setEvaluate(3L);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MIN of 2L and 3L should be 2L", 2L, result);
	
		child1.setEvaluate(-3L);
		child2.setEvaluate(2L);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MIN of -3L and 2L should be -3L", -3L, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Min2Function#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child1.setEvaluate(0.0f);
		child2.setEvaluate(0.0f);
		Object result = min.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MIN of 0.0f and 0.0f should be 0.0f", 0.0f, result);
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(3.1f);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MIN of 2.1f and 3.1f should be 2.1f", 2.1f, result);
	
		child1.setEvaluate(-3.1f);
		child2.setEvaluate(2.1f);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MIN of -3.1f and 2.1f should be -3.1f", -3.1f, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Min2Function#evaluate()} 
	 * correctly evaluates mixed type values.
	 */
	@Test
	public void testEvaluateMixed() {
		child1.setEvaluate(1);
		child2.setEvaluate(2.4);
		Object result = min.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MIN of 1 and 2.4 should be 1.0", 1.0, min.evaluate());
		
		child1.setEvaluate(3.1d);
		child2.setEvaluate(2.1f);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MIN of 3.1d and 2.1f should be 3.1d", 2.1d, (Double) result, 1);
	
		child1.setEvaluate(6L);
		child2.setEvaluate(2);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MIN of 6L and 2 should be 2L", 2L, result);
		
		child1.setEvaluate(2);
		child2.setEvaluate(3.1f);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MIN of 2 and 3.1f should be 2.0f", 2.0f, result);
		
		child1.setEvaluate(-2.1f);
		child2.setEvaluate(2L);
		result = min.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MIN of -2.1f and 2L should be -2.1f", -2.1f, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Min2Function#getReturnType(Class...)}
	 * returns the correct type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypeMin() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = min.getReturnType(type, type);
			assertSame("unexpected return type", type, returnType);
		}
		
		returnType = min.getReturnType(Short.class, Double.class);
		assertSame("unexpected return type", Double.class, returnType);
		
		returnType = min.getReturnType(Long.class, Integer.class);
		assertSame("unexpected return type", Long.class, returnType);
		
		returnType = min.getReturnType(Byte.class, Float.class);
		assertSame("unexpected return type", Float.class, returnType);
		
		returnType = min.getReturnType(Boolean.class, Integer.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = min.getReturnType(Integer.class, Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
