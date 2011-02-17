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
 * Unit tests for {@link org.epochx.epox.math.LogFunction}
 */
public class LogFunctionTest extends NodeTestCase {

	private LogFunction log;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new LogFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		log = new LogFunction(child);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.LogFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(Math.exp(3.2));
		assertEquals("LOG should be the inverse of EXP", 3.2, log.evaluate(), 0);
		
		child.setEvaluate(0.0);
		assertEquals("LOG of 0.0 should be -Infinity", Double.NEGATIVE_INFINITY, log.evaluate(), 0);
	
		child.setEvaluate(Double.POSITIVE_INFINITY);
		assertEquals("LOG of +Infinity should be +Infinity", Double.POSITIVE_INFINITY, log.evaluate(), 0);
	
		child.setEvaluate(Double.NaN);
		assertEquals("LOG of NaN should be NaN", Double.NaN, log.evaluate(), 0);

		child.setEvaluate(-1.0);
		assertEquals("LOG of -1.0 should be NaN", Double.NaN, log.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.LogFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertEquals("LOG of 0 should be -Infinity", Double.NEGATIVE_INFINITY, log.evaluate(), 0);
		
		child.setEvaluate(-1);
		assertEquals("LOG of -1 should be NaN", Double.NaN, log.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.LogFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child.setEvaluate(0.0f);
		assertEquals("LOG of 0.0f should be -Infinity", Double.NEGATIVE_INFINITY, log.evaluate(), 0);
	
		child.setEvaluate(Float.NaN);
		assertEquals("LOG of NaN should be NaN", Double.NaN, log.evaluate(), 0);

		child.setEvaluate(-1.0f);
		assertEquals("LOG of -1.0f should be NaN", Double.NaN, log.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.LogFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child.setEvaluate(0L);
		assertEquals("LOG of 0L should be -Infinity", Double.NEGATIVE_INFINITY, log.evaluate(), 0);
		
		child.setEvaluate(-1L);
		assertEquals("LOG of -1L should be NaN", Double.NaN, log.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.LogFunction#getReturnType(Class...)}
	 * returns the same type for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeLog() {
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
			Node n = parser.parse("LN(X)");
			assertSame("Parsing did not return an instance of the correct node", LogFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
