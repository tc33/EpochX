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
 * Unit tests for {@link org.epochx.epox.math.ModuloProtectedFunction}
 */
public class ModuloProtectedFunctionTest extends NodeTestCase {

	private ModuloProtectedFunction pmod;
	private MockNode child1;
	private MockNode child2;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new ModuloProtectedFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child1 = new MockNode();
		child2 = new MockNode();
		pmod = new ModuloProtectedFunction(child1, child2);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.ModuloProtectedFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(2.0);
		child2.setEvaluate(1.0);
		Object result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("PMOD of 2.0 and 1.0 should be 2.0", 0.0, result);
		
		child1.setEvaluate(2.2);
		child2.setEvaluate(0.5);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("PMOD of 2.2 and 0.5 should be 0.2", 0.2, (Double) result, 1);
		
		child1.setEvaluate(5.0);
		child2.setEvaluate(0.0);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("PMOD of 5.0 and 0.0 should be 5.0", 5.0, result);

		child1.setEvaluate(-5.0);
		child2.setEvaluate(-2.0);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("PMOD of -5.0 and -2.0 should be -1.0", -1.0, result);
		
		child1.setEvaluate(-5.0);
		child2.setEvaluate(2.0);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("PMOD of -5.0 and 2.0 should be -1.0", -1.0, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ModuloProtectedFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child1.setEvaluate(2.0f);
		child2.setEvaluate(1.0f);
		Object result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("PMOD of 2.0f and 1.0f should be 2.0f", 0.0f, result);
		
		child1.setEvaluate(2.2f);
		child2.setEvaluate(0.5f);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("PMOD of 2.2f and 0.5f should be 0.2f", 0.2f, (Float) result, 1);
		
		child1.setEvaluate(5.0f);
		child2.setEvaluate(0.0f);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("PMOD of 5.0f and 0.0f should be 5.0f", 5.0f, result);

		child1.setEvaluate(-5.0f);
		child2.setEvaluate(-2.0f);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("PMOD of -5.0f and -2.0f should be -1.0f", -1.0f, result);
		
		child1.setEvaluate(-5.0f);
		child2.setEvaluate(2.0f);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("PMOD of -5.0f and 2.0f should be -1.0f", -1.0f, result);
	}

	/**
	 * Tests that {@link org.epochx.epox.math.ModuloProtectedFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(2);
		child2.setEvaluate(1);
		Object result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("PMOD of 2 and 1 should be 0", 0, result);
		
		child1.setEvaluate(3);
		child2.setEvaluate(2);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("PMOD of 3 and 2 should be 1", 1, result);
		
		child1.setEvaluate(5);
		child2.setEvaluate(0);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("PMOD of 5 and 0 should be 5", 5, result);

		child1.setEvaluate(-5);
		child2.setEvaluate(-2);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("PMOD of -5 and -2 should be -1", -1, result);
		
		child1.setEvaluate(-5);
		child2.setEvaluate(2);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("PMOD of -5 and 2 should be -1", -1, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ModuloProtectedFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child1.setEvaluate(2L);
		child2.setEvaluate(1L);
		Object result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("PMOD of 2L and 1L should be 0L", 0L, result);
		
		child1.setEvaluate(3L);
		child2.setEvaluate(2L);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("PMOD of 3L and 2L should be 1L", 1L, result);
		
		child1.setEvaluate(5L);
		child2.setEvaluate(0L);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("PMOD of 5L and 0L should be 5L", 5L, result);

		child1.setEvaluate(-5L);
		child2.setEvaluate(-2L);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("PMOD of -5L and -2L should be -1L", -1L, result);
		
		child1.setEvaluate(-5L);
		child2.setEvaluate(2L);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("PMOD of -5L and 2L should be -1L", -1L, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ModuloProtectedFunction#evaluate()} 
	 * correctly evaluates mixed type values.
	 */
	@Test
	public void testEvaluateMixed() {
		child1.setEvaluate(5);
		child2.setEvaluate(2.4f);
		Object result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("PMOD of 5 and 2.4f should be 0.2f", 0.2f, (Float) result, 1);
		
		child1.setEvaluate(5.0f);
		child2.setEvaluate(2.4d);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("PMOD of 5.0f and 2.4d should be 0.2d", 0.2, (Double) result, 1);
	
		child1.setEvaluate(5L);
		child2.setEvaluate(0);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("PMOD of 5L and 0 should be 5L", 5L, result);
		
		child1.setEvaluate(5L);
		child2.setEvaluate(2);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("PMOD of 5L and 2 should be 1L", 1L, result);
		
		child1.setEvaluate(-5.0f);
		child2.setEvaluate(-2);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("PMOD of -5.0f and -2 should be -1.0f", -1.0f, result);
		
		child1.setEvaluate(8.0f);
		child2.setEvaluate(2L);
		result = pmod.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("PMOD of 8.0f and 2L should be 0.0f", 0.0f, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ModuloProtectedFunction#getReturnType(Class...)}
	 * returns the correct type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypePDiv() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = pmod.getReturnType(type, type);
			assertSame("unexpected return type", type, returnType);
		}
		
		returnType = pmod.getReturnType(Short.class, Double.class);
		assertSame("unexpected return type", Double.class, returnType);
		
		returnType = pmod.getReturnType(Long.class, Integer.class);
		assertSame("unexpected return type", Long.class, returnType);
		
		returnType = pmod.getReturnType(Byte.class, Float.class);
		assertSame("unexpected return type", Float.class, returnType);
		
		returnType = pmod.getReturnType(Boolean.class, Integer.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = pmod.getReturnType(Integer.class, Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
