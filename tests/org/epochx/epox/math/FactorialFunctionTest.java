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
 * Unit tests for {@link org.epochx.epox.math.FactorialFunction}
 */
public class FactorialFunctionTest extends NodeTestCase {

	private FactorialFunction factorial;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new FactorialFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		factorial = new FactorialFunction(child);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.FactorialFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(5);
		assertEquals("FACTORIAL of 5 should be 120", 120, factorial.evaluate());
		
		child.setEvaluate(0);
		assertEquals("FACTORIAL of 1 should be 1", 1, factorial.evaluate());
		
		child.setEvaluate(1);
		assertEquals("FACTORIAL of 0 should be 1", 1, factorial.evaluate());
		
		child.setEvaluate(-5);
		assertEquals("FACTORIAL of -1 should be 120", 120, factorial.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.FactorialFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child.setEvaluate(5L);
		assertEquals("FACTORIAL of 5L should be 120L", 120L, factorial.evaluate());
		
		child.setEvaluate(0L);
		assertEquals("FACTORIAL of 1L should be 1L", 1L, factorial.evaluate());
		
		child.setEvaluate(1L);
		assertEquals("FACTORIAL of 0L should be 1L", 1L, factorial.evaluate());
		
		child.setEvaluate(-5L);
		assertEquals("FACTORIAL of -1L should be 120L", 120L, factorial.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.FactorialFunction#getReturnType(Class...)}
	 * returns Double type for numeric input type and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypeFactorial() {
		Class<?>[] inputTypes = {Byte.class, Integer.class, Short.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = factorial.getReturnType(type);
			assertSame("unexpected return type", Integer.class, returnType);
		}
		
		returnType = factorial.getReturnType(Long.class);
		assertSame("unexpected return type", Long.class, returnType);
		
		returnType = factorial.getReturnType(Float.class);
		assertNull("float type for child should be invalid", returnType);
		
		returnType = factorial.getReturnType(Double.class);
		assertNull("float type for child should be invalid", returnType);
		
		returnType = factorial.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = factorial.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
	
	/**
	 * Tests that this function can be parsed by the EpoxParser.
	 */
	@Test
	public void testEpoxParser() {
		EpoxParser parser = new EpoxParser();
		
		try {
			parser.declareVariable(new Variable("X", Integer.class));
			Node n = parser.parse("FACTORIAL(X)");
			assertSame("Parsing did not return an instance of the correct node", FactorialFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
