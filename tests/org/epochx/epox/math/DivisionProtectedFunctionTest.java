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
import org.epochx.tools.eval.*;
import org.junit.Test;


/**
 * Unit tests for {@link org.epochx.epox.math.DivisionProtectedFunction}
 */
public class DivisionProtectedFunctionTest extends NodeTestCase {

	private DivisionProtectedFunction pdiv;
	private MockNode child1;
	private MockNode child2;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new DivisionProtectedFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child1 = new MockNode();
		child2 = new MockNode();
		pdiv = new DivisionProtectedFunction(child1, child2);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.DivisionProtectedFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(2.0);
		child2.setEvaluate(1.0);
		assertEquals("PDIV of 2.0 and 1.0 should be 2.0", 2.0, pdiv.evaluate());
		
		child1.setEvaluate(2.0);
		child2.setEvaluate(0.5);
		assertEquals("PDIV of 2.0 and 0.5 should be 4.0", 4.0, pdiv.evaluate());
	
		child1.setEvaluate(2.0);
		child2.setEvaluate(4.0);
		assertEquals("PDIV of 2.0 and 4.0 should be 0.5", 0.5, pdiv.evaluate());
		
		child1.setEvaluate(5.0);
		child2.setEvaluate(0.0);
		assertEquals("PDIV of 5.0 and 0.0 should be the protection value", 0.0, pdiv.evaluate());
		
		child1.setEvaluate(5.0);
		child2.setEvaluate(0.0);
		pdiv.setProtectionValue(3.2);
		assertEquals("PDIV of 5.0 and 0.0 should be the protection value", 3.2, pdiv.evaluate());
		
		child1.setEvaluate(-5.0);
		child2.setEvaluate(-2.0);
		assertEquals("PDIV of -5.0 and -2.0 should be 2.5", 2.5, pdiv.evaluate());
		
		child1.setEvaluate(-5.0);
		child2.setEvaluate(2.0);
		assertEquals("PDIV of -5.0 and 2.0 should be -2.5", -2.5, pdiv.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.DivisionProtectedFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child1.setEvaluate(2.0f);
		child2.setEvaluate(1.0f);
		assertEquals("PDIV of 2.0f and 1.0f should be 2.0f", 2.0f, pdiv.evaluate());
		
		child1.setEvaluate(2.0f);
		child2.setEvaluate(0.5f);
		assertEquals("PDIV of 2.0f and 0.5f should be 4.0f", 4.0f, pdiv.evaluate());
	
		child1.setEvaluate(2.0f);
		child2.setEvaluate(4.0f);
		assertEquals("PDIV of 2.0f and 4.0f should be 0.5f", 0.5f, pdiv.evaluate());
		
		child1.setEvaluate(5.0f);
		child2.setEvaluate(0.0f);
		assertEquals("PDIV of 5.0f and 0.0f should be the protection value", 0.0f, pdiv.evaluate());
		
		child1.setEvaluate(-5.0f);
		child2.setEvaluate(-2.0f);
		assertEquals("PDIV of -5.0f and -2.0f should be 2.5f", 2.5f, pdiv.evaluate());
		
		child1.setEvaluate(-5.0f);
		child2.setEvaluate(2.0f);
		assertEquals("PDIV of -5.0 and 2.0f should be -2.5f", -2.5f, pdiv.evaluate());
	}

	/**
	 * Tests that {@link org.epochx.epox.math.DivisionProtectedFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(2);
		child2.setEvaluate(1);
		assertEquals("PDIV of 2 and 1 should be 2", 2, pdiv.evaluate());
		
		child1.setEvaluate(5);
		child2.setEvaluate(2);
		assertEquals("PDIV of 5 and 2 should be 2", 2, pdiv.evaluate());
	
		child1.setEvaluate(5);
		child2.setEvaluate(0);
		assertEquals("PDIV of 5 and 0 should be the protection value", 0, pdiv.evaluate());
		
		child1.setEvaluate(-5);
		child2.setEvaluate(-2);
		assertEquals("PDIV of -5 and -2 should be 2", 2, pdiv.evaluate());
		
		child1.setEvaluate(-5);
		child2.setEvaluate(2);
		assertEquals("PDIV of -5 and 2 should be -2", -2, pdiv.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.DivisionProtectedFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child1.setEvaluate(2L);
		child2.setEvaluate(1L);
		assertEquals("PDIV of 2L and 1L should be 2L", 2L, pdiv.evaluate());
		
		child1.setEvaluate(5L);
		child2.setEvaluate(2L);
		assertEquals("PDIV of 5L and 2L should be 2L", 2L, pdiv.evaluate());
	
		child1.setEvaluate(Long.MAX_VALUE);
		child2.setEvaluate(2L);
		assertEquals("PDIV of large value incorrect", (Long.MAX_VALUE/2), pdiv.evaluate());
		
		child1.setEvaluate(5L);
		child2.setEvaluate(0L);
		assertEquals("PDIV of 5 and 0 should be the protection value", 0L, pdiv.evaluate());
		
		child1.setEvaluate(-5L);
		child2.setEvaluate(-2L);
		assertEquals("PDIV of -5L and -2L should be 2L", 2L, pdiv.evaluate());
		
		child1.setEvaluate(-5L);
		child2.setEvaluate(2L);
		assertEquals("PDIV of -5L and 2L should be -2L", -2L, pdiv.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.DivisionProtectedFunction#evaluate()} 
	 * correctly evaluates mixed type values.
	 */
	@Test
	public void testEvaluateMixed() {
		child1.setEvaluate(5);
		child2.setEvaluate(2.5);
		assertEquals("PDIV of 5 and 2.5 should be 2", 2.0, pdiv.evaluate());
		
		child1.setEvaluate(5.0f);
		child2.setEvaluate(2.0d);
		assertEquals("PDIV of 5.0f and 2.0 should be 2.5", 2.5, pdiv.evaluate());
	
		child1.setEvaluate(5L);
		child2.setEvaluate(0);
		assertEquals("PDIV of 5L and 0 should be the protection value", 0L, pdiv.evaluate());
		
		child1.setEvaluate(5L);
		child2.setEvaluate(2);
		assertEquals("PDIV of 5L and 2 should be 2L", 2L, pdiv.evaluate());
		
		child1.setEvaluate(-5.0f);
		child2.setEvaluate(-2);
		assertEquals("PDIV of -5.0f and -2 should be 2.5f", 2.5f, pdiv.evaluate());
		
		child1.setEvaluate(8.0f);
		child2.setEvaluate(2L);
		assertEquals("PDIV of 8.0f and 2L should be 4.0f", 4.0f, pdiv.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.DivisionProtectedFunction#getReturnType(Class...)}
	 * returns the correct type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypePDiv() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = pdiv.getReturnType(type, type);
			assertSame("unexpected return type", type, returnType);
		}
		
		returnType = pdiv.getReturnType(Short.class, Double.class);
		assertSame("unexpected return type", Double.class, returnType);
		
		returnType = pdiv.getReturnType(Long.class, Integer.class);
		assertSame("unexpected return type", Long.class, returnType);
		
		returnType = pdiv.getReturnType(Byte.class, Float.class);
		assertSame("unexpected return type", Float.class, returnType);
		
		returnType = pdiv.getReturnType(Boolean.class, Integer.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = pdiv.getReturnType(Integer.class, Integer.class, Integer.class);
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
			Node n = parser.parse("PDIV(X, X)");
			assertSame("Parsing did not return an instance of the correct node", DivisionProtectedFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
