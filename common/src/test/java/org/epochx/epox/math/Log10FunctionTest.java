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
 * Unit tests for {@link org.epochx.epox.math.Log10Function}
 */
public class Log10FunctionTest extends NodeTestCase {

	private Log10Function log;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new Log10Function();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		log = new Log10Function(child);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.Log10Function#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(Math.pow(10, 3.2));
		assertEquals("LOG-10 should be the inverse of 10^x", 3.2, log.evaluate(), 0);
		
		child.setEvaluate(0.0);
		assertEquals("LOG-10 of 0.0 should be -Infinity", Double.NEGATIVE_INFINITY, log.evaluate(), 0);
	
		child.setEvaluate(Double.POSITIVE_INFINITY);
		assertEquals("LOG-10 of +Infinity should be +Infinity", Double.POSITIVE_INFINITY, log.evaluate(), 0);
	
		child.setEvaluate(Double.NaN);
		assertEquals("LOG-10 of NaN should be NaN", Double.NaN, log.evaluate(), 0);

		child.setEvaluate(-1.0);
		assertEquals("LOG-10 of -1.0 should be NaN", Double.NaN, log.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Log10Function#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertEquals("LOG-10 of 0 should be -Infinity", Double.NEGATIVE_INFINITY, log.evaluate(), 0);
		
		child.setEvaluate(-1);
		assertEquals("LOG-10 of -1 should be NaN", Double.NaN, log.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Log10Function#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child.setEvaluate(0.0f);
		assertEquals("LOG-10 of 0.0f should be -Infinity", Double.NEGATIVE_INFINITY, log.evaluate(), 0);
	
		child.setEvaluate(Float.NaN);
		assertEquals("LOG-10 of NaN should be NaN", Double.NaN, log.evaluate(), 0);

		child.setEvaluate(-1.0f);
		assertEquals("LOG-10 of -1.0f should be NaN", Double.NaN, log.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Log10Function#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child.setEvaluate(0L);
		assertEquals("LOG-10 of 0L should be -Infinity", Double.NEGATIVE_INFINITY, log.evaluate(), 0);
		
		child.setEvaluate(-1L);
		assertEquals("LOG-10 of -1L should be NaN", Double.NaN, log.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.Log10Function#getReturnType(Class...)}
	 * returns the same type for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeLog10() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = log.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = log.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = log.getReturnType(Integer.class, Integer.class);
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
			Node n = parser.parse("LOG-10(X)");
			assertSame("Parsing did not return an instance of the correct node", Log10Function.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
