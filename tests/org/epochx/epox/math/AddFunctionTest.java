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
 * Unit tests for {@link org.epochx.epox.math.AddFunction}
 */
public class AddFunctionTest extends NodeTestCase {

	private AddFunction add;
	private MockNode child1;
	private MockNode child2;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new AddFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child1 = new MockNode();
		child2 = new MockNode();
		
		add = new AddFunction(child1, child2);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.AddFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(0.0);
		child2.setEvaluate(0.0);
		assertEquals("ADD of 0.0 and 0.0 should be 0.0", 0.0, add.evaluate());
		
		child1.setEvaluate(2.1);
		child2.setEvaluate(3.1);
		assertEquals("ADD of 2.1 and 3.1 should be 5.3", 5.2, add.evaluate());
	
		child1.setEvaluate(-2.1);
		child2.setEvaluate(3.1);
		assertEquals("ADD of -2.1 and 3.1 should be 0.9", 1.0, add.evaluate());
		
		child1.setEvaluate(-2.1);
		child2.setEvaluate(-3.1);
		assertEquals("ADD of -2.1 and -3.1 should be -5.2", -5.2, add.evaluate());
	}

	/**
	 * Tests that {@link org.epochx.epox.math.AddFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(0);
		child2.setEvaluate(0);
		assertEquals("ADD of 0 and 0 should be 0", 0, add.evaluate());
		
		child1.setEvaluate(2);
		child2.setEvaluate(3);
		assertEquals("ADD of 2 and 3 should be 5", 5, add.evaluate());
	
		child1.setEvaluate(-2);
		child2.setEvaluate(3);
		assertEquals("ADD of -2 and 3 should be 1", 1, add.evaluate());
		
		child1.setEvaluate(-2);
		child2.setEvaluate(-3);
		assertEquals("ADD of -2 and -3 should be -5", -5, add.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.AddFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child1.setEvaluate(0L);
		child2.setEvaluate(0L);
		assertEquals("ADD of 0L and 0L should be 0L", 0L, add.evaluate());
		
		child1.setEvaluate(Long.MAX_VALUE);
		child2.setEvaluate(-2L);
		assertEquals("ADD of large number should be calculated correctly", Long.MAX_VALUE-2, add.evaluate());
		
		child1.setEvaluate(2L);
		child2.setEvaluate(3L);
		assertEquals("ADD of 2L and 3L should be 5L", 5L, add.evaluate());
	
		child1.setEvaluate(-2L);
		child2.setEvaluate(3L);
		assertEquals("ADD of -2L and 3L should be 1L", 1L, add.evaluate());
		
		child1.setEvaluate(-2L);
		child2.setEvaluate(-3L);
		assertEquals("ADD of -2L and -3L should be -5L", -5L, add.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.AddFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child1.setEvaluate(0.0f);
		child2.setEvaluate(0.0f);
		assertEquals("ADD of 0.0f and 0.0f should be 0.0f", 0.0f, add.evaluate());
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(3.1f);
		assertEquals("ADD of 2.1f and 3.1f should be 5.3f", 5.2f, add.evaluate());
	
		child1.setEvaluate(-2.1f);
		child2.setEvaluate(3.1f);
		assertEquals("ADD of -2.2f and 3.1f should be 0.9f", 1.0f, add.evaluate());
		
		child1.setEvaluate(-2.1f);
		child2.setEvaluate(-3.1f);
		assertEquals("ADD of -2.1f and -3.1f should be -5.2f", -5.2f, add.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.AddFunction#evaluate()} 
	 * correctly evaluates mixed type values.
	 */
	@Test
	public void testEvaluateMixed() {
		child1.setEvaluate(1);
		child2.setEvaluate(2.4);
		assertEquals("ADD of 1 and 2.4 should be 3.4", 3.4, add.evaluate());
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(3.1d);
		Object result = add.evaluate();
		assertSame("types should be widened to double", Double.class, result.getClass());
		assertEquals("ADD of 2.1f and 3.1d should be 5.3d", 5.2d, (Double) result, 1);
	
		child1.setEvaluate(6L);
		child2.setEvaluate(2);
		assertEquals("ADD of 6L and 2 should be 8L", 8L, add.evaluate());
		
		child1.setEvaluate(2.1f);
		child2.setEvaluate(3);
		assertEquals("ADD of 2.1f and 3 should be 5.1f", 5.1f, add.evaluate());
		
		child1.setEvaluate(-2.1f);
		child2.setEvaluate(3L);
		assertEquals("ADD of -2.1f and 3L should be 5.1f", 5.1f, add.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.AddFunction#getReturnType(Class...)}
	 * returns the correct type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypeAdd() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = add.getReturnType(type, type);
			assertSame("unexpected return type", type, returnType);
		}
		
		returnType = add.getReturnType(Short.class, Double.class);
		assertSame("unexpected return type", Double.class, returnType);
		
		returnType = add.getReturnType(Long.class, Integer.class);
		assertSame("unexpected return type", Long.class, returnType);
		
		returnType = add.getReturnType(Byte.class, Float.class);
		assertSame("unexpected return type", Float.class, returnType);
		
		returnType = add.getReturnType(Boolean.class, Integer.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = add.getReturnType(Integer.class, Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
