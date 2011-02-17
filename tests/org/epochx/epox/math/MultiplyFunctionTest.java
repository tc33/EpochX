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
 * Unit tests for {@link org.epochx.epox.math.MultiplyFunction}
 */
public class MultiplyFunctionTest extends NodeTestCase {

	private MultiplyFunction mul;
	private MockNode child1;
	private MockNode child2;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new MultiplyFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child1 = new MockNode();
		child2 = new MockNode();
		
		mul = new MultiplyFunction(child1, child2);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.MultiplyFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(0.0);
		child2.setEvaluate(0.0);
		Object result = mul.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MUL of 0.0 and 0.0 should be 0.0", 0.0, result);
		
		child1.setEvaluate(2.0);
		child2.setEvaluate(3.1);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MUL of 2.0 and 3.1 should be 6.2", 6.2, result);
	
		child1.setEvaluate(-2.0);
		child2.setEvaluate(3.1);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MUL of -2.0 and 3.1 should be -6.2", -6.2, result);
		
		child1.setEvaluate(-2.0);
		child2.setEvaluate(-3.1);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MUL of -2.0 and -3.1 should be 6.2", 6.2, result);
	}

	/**
	 * Tests that {@link org.epochx.epox.math.MultiplyFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(0);
		child2.setEvaluate(0);
		Object result = mul.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MUL of 0 and 0 should be 0", 0, result);
		
		child1.setEvaluate(2);
		child2.setEvaluate(3);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MUL of 2 and 3 should be 6", 6, result);
	
		child1.setEvaluate(-2);
		child2.setEvaluate(3);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MUL of -2 and 3 should be -6", -6, result);
		
		child1.setEvaluate(-2);
		child2.setEvaluate(-3);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
		assertEquals("MUL of -2 and -3 should be 6", 6, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.MultiplyFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child1.setEvaluate(0L);
		child2.setEvaluate(0L);
		Object result = mul.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MUL of 0L and 0L should be 0L", 0L, result);
		
		child1.setEvaluate(2L);
		child2.setEvaluate(3L);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MUL of 2L and 3L should be 6L", 6L, result);
	
		child1.setEvaluate(-2L);
		child2.setEvaluate(3L);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MUL of -2L and 3L should be -6L", -6L, result);
		
		child1.setEvaluate(-2L);
		child2.setEvaluate(-3L);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MUL of -2L and -3L should be 6L", 6L, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.MultiplyFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child1.setEvaluate(0.0f);
		child2.setEvaluate(0.0f);
		Object result = mul.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MUL of 0.0f and 0.0f should be 0.0f", 0.0f, result);
		
		child1.setEvaluate(2.0f);
		child2.setEvaluate(3.1f);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MUL of 2.0f and 3.1f should be 6.2f", 6.2f, result);
	
		child1.setEvaluate(-2.0f);
		child2.setEvaluate(3.1f);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MUL of -2.0f and 3.1f should be -6.2f", -6.2f, result);
		
		child1.setEvaluate(-2.0f);
		child2.setEvaluate(-3.1f);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MUL of -2.0f and -3.1f should be 6.2f", 6.2f, result);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.MultiplyFunction#evaluate()} 
	 * correctly evaluates mixed type values.
	 */
	@Test
	public void testEvaluateMixed() {
		child1.setEvaluate(0);
		child2.setEvaluate(0.0f);
		Object result = mul.evaluate();
		assertSame("Return type should be widest of input types", Float.class, result.getClass());
		assertEquals("MUL of 0 and 0.0f should be 0.0f", 0.0f, result);
		
		child1.setEvaluate(2.0f);
		child2.setEvaluate(3.1d);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MUL of 2.0f and 3.1d should be 6.2d", 6.2d, result);
	
		child1.setEvaluate(-2L);
		child2.setEvaluate(3.0d);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Double.class, result.getClass());
		assertEquals("MUL of -2L and 3.0d should be -6.0d", -6.0d, result);
		
		child1.setEvaluate(-2);
		child2.setEvaluate(-3L);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Long.class, result.getClass());
		assertEquals("MUL of -2 and -3L should be 6L", 6L, result);
		
		child1.setEvaluate(Short.MAX_VALUE);
		child2.setEvaluate(2);
		result = mul.evaluate();
		assertSame("Return type should be widest of input types", Integer.class, result.getClass());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.MultiplyFunction#getReturnType(Class...)}
	 * returns the correct type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypeMul() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = mul.getReturnType(type, type);
			assertSame("unexpected return type", type, returnType);
		}
		
		returnType = mul.getReturnType(Short.class, Double.class);
		assertSame("unexpected return type", Double.class, returnType);
		
		returnType = mul.getReturnType(Long.class, Integer.class);
		assertSame("unexpected return type", Long.class, returnType);
		
		returnType = mul.getReturnType(Byte.class, Float.class);
		assertSame("unexpected return type", Float.class, returnType);
		
		returnType = mul.getReturnType(Boolean.class, Integer.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = mul.getReturnType(Integer.class, Integer.class, Integer.class);
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
			Node n = parser.parse("MUL(X, X)");
			assertSame("Parsing did not return an instance of the correct node", MultiplyFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
