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
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.epox.math;


import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.junit.Test;


/**
 * Unit tests for {@link org.epochx.epox.math.SquareFunction}
 */
public class SquareFunctionTest extends NodeTestCase {

	private SquareFunction sq;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new SquareFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		sq = new SquareFunction(child);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.SquareFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		Object result = sq.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SQUARE of 0.0 should be 0.0", 0.0, result);
		
		child.setEvaluate(1.0);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SQUARE of 1.0 should be 1.0", 1.0, result);
	
		child.setEvaluate(1.5);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SQUARE of 1.5 should be 2.25", 2.25, result);
		
		child.setEvaluate(-1.0);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SQUARE of -1.0 should be 1.0", 1.0, result);
		
		child.setEvaluate(-2.0);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SQUARE of -2.0 should be 4.0", 4.0, result);
		
		child.setEvaluate(Double.NEGATIVE_INFINITY);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SQUARE of -Infinity should be +Infinity", Double.POSITIVE_INFINITY, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SquareFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child.setEvaluate(0.0f);
		Object result = sq.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SQUARE of 0.0f should be 0.0f", 0.0f, result);
		
		child.setEvaluate(1.0f);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SQUARE of 1.0f should be 1.0f", 1.0f, result);
	
		child.setEvaluate(1.5f);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SQUARE of 1.5f should be 2.25f", 2.25f, result);
		
		child.setEvaluate(-1.0f);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SQUARE of -1.0f should be 1.0f", 1.0f, result);
		
		child.setEvaluate(-2.0f);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SQUARE of -2.0f should be 4.0f", 4.0f, result);
		
		child.setEvaluate(Float.NEGATIVE_INFINITY);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SQUARE of -Infinity should be +Infinity", Float.POSITIVE_INFINITY, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SquareFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		Object result = sq.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("SQUARE of 0 should be 0", 0, result);
		
		child.setEvaluate(1);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("SQUARE of 1 should be 1", 1, result);
	
		child.setEvaluate(2);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("SQUARE of 2 should be 4", 4, result);
		
		child.setEvaluate(-1);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("SQUARE of -1 should be 1", 1, result);
		
		child.setEvaluate(-2);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("SQUARE of -2 should be 4", 4, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SquareFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child.setEvaluate(0L);
		Object result = sq.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("SQUARE of 0L should be 0L", 0L, result);
		
		child.setEvaluate(1L);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("SQUARE of 1L should be 1L", 1L, result);
	
		child.setEvaluate(2L);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("SQUARE of 2L should be 4L", 4L, result);
		
		child.setEvaluate(-1L);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("SQUARE of -1L should be 1L", 1L, result);
		
		child.setEvaluate(-2L);
		result = sq.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("SQUARE of -2L should be 4L", 4L, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SquareFunction#getReturnType(Class...)}
	 * returns the same type for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeCube() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = sq.getReturnType(type);
			assertSame("unexpected return type", type, returnType);
		}
		
		returnType = sq.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = sq.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
