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
 * Unit tests for {@link org.epochx.epox.math.SubtractFunction}
 */
public class SubtractFunctionTest extends NodeTestCase {

	private SubtractFunction sub;
	private MockNode child1;
	private MockNode child2;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new SubtractFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child1 = new MockNode();
		child2 = new MockNode();
		
		sub = new SubtractFunction(child1, child2);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SubtractFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(0.0);
		child2.setEvaluate(0.0);
		Object result = sub.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SUB of 0.0 and 0.0 should be 0.0", 0.0, result);
		
		child1.setEvaluate(3.1);
		child2.setEvaluate(2.0);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SUB of 3.1 and 2.0 should be 1.1", 1.1, result);
		
		child1.setEvaluate(2.0);
		child2.setEvaluate(3.1);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SUB of 2.0 and 3.1 should be -1.1", -1.1, result);
	
		child1.setEvaluate(-2.0);
		child2.setEvaluate(3.1);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SUB of -2.0 and 3.1 should be -5.1", -5.1, result);
		
		child1.setEvaluate(-2.0);
		child2.setEvaluate(-3.1);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SUB of -2.0 and -3.1 should be 1.1", 1.1, result);
	}

	/**
	 * Tests that {@link org.epochx.epox.math.SubtractFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(0);
		child2.setEvaluate(0);
		Object result = sub.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("SUB of 0 and 0 should be 0", 0, result);
		
		child1.setEvaluate(3);
		child2.setEvaluate(2);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("SUB of 3 and 2 should be 1", 1, result);
		
		child1.setEvaluate(2);
		child2.setEvaluate(3);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("SUB of 2 and 3 should be -1", -1, result);
	
		child1.setEvaluate(-2);
		child2.setEvaluate(3);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("SUB of -2 and 3 should be -5", -5, result);
		
		child1.setEvaluate(-2);
		child2.setEvaluate(-3);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("SUB of -2 and -3 should be 1", 1, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SubtractFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child1.setEvaluate(0L);
		child2.setEvaluate(0L);
		Object result = sub.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("SUB of 0L and 0L should be 0L", 0L, result);
		
		child1.setEvaluate(3L);
		child2.setEvaluate(2L);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("SUB of 3L and 2L should be 1L", 1L, result);
		
		child1.setEvaluate(2L);
		child2.setEvaluate(3L);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("SUB of 2L and 3L should be -1L", -1L, result);
	
		child1.setEvaluate(-2L);
		child2.setEvaluate(3L);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("SUB of -2L and 3L should be -5L", -5L, result);
		
		child1.setEvaluate(-2L);
		child2.setEvaluate(-3L);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("SUB of -2L and -3L should be 1L", 1L, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SubtractFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child1.setEvaluate(0.0f);
		child2.setEvaluate(0.0f);
		Object result = sub.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SUB of 0.0f and 0.0f should be 0.0f", 0.0f, result);
		
		child1.setEvaluate(3.1f);
		child2.setEvaluate(2.0f);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SUB of 3.1f and 2.0f should be 1.1f", 1.1f, (Float) result, 1);
		
		child1.setEvaluate(2.0f);
		child2.setEvaluate(3.1f);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SUB of 2.0f and 3.1f should be -1.1f", -1.1f, (Float) result, 1);
	
		child1.setEvaluate(-2.0f);
		child2.setEvaluate(3.1f);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SUB of -2.0f and 3.1f should be -5.1f", -5.1f, result);
		
		child1.setEvaluate(-2.0f);
		child2.setEvaluate(-3.1f);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SUB of -2.0f and -3.1f should be 1.1f", 1.1f, (Float) result, 1);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SubtractFunction#evaluate()} 
	 * correctly evaluates mixed type values.
	 */
	@Test
	public void testEvaluateMixed() {
		child1.setEvaluate(0);
		child2.setEvaluate(0.0f);
		Object result = sub.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("SUB of 0 and 0.0f should be 0.0f", 0.0f, result);
		
		child1.setEvaluate(2.0f);
		child2.setEvaluate(3.1d);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SUB of 2.0f and 3.1d should be -1.1d", -1.1d, result);
	
		child1.setEvaluate(-2L);
		child2.setEvaluate(3.0d);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("SUB of -2L and 3.0d should be -5.0d", -5.0d, result);
		
		child1.setEvaluate(-2);
		child2.setEvaluate(-3L);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("SUB of -2 and -3L should be 1L", 1L, result);
		
		child1.setEvaluate(Short.MAX_VALUE);
		child2.setEvaluate(2);
		result = sub.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SubtractFunction#dataType(Class...)}
	 * returns the correct type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypeMul() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = sub.dataType(type, type);
			assertSame("unexpected return type", type, returnType);
		}
		
		returnType = sub.dataType(Short.class, Double.class);
		assertSame("unexpected return type", Double.class, returnType);
		
		returnType = sub.dataType(Long.class, Integer.class);
		assertSame("unexpected return type", Long.class, returnType);
		
		returnType = sub.dataType(Byte.class, Float.class);
		assertSame("unexpected return type", Float.class, returnType);
		
		returnType = sub.dataType(Boolean.class, Integer.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = sub.dataType(Integer.class, Integer.class, Integer.class);
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
			Node n = parser.parse("SUB(X, X)");
			assertSame("Parsing did not return an instance of the correct node", SubtractFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
