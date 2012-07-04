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
 * Unit tests for {@link org.epochx.epox.math.AbsoluteFunction}
 */
public class AbsoluteFunctionTest extends NodeTestCase {

	private AbsoluteFunction abs;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new AbsoluteFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		abs = new AbsoluteFunction(child);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.AbsoluteFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		assertEquals("ABS of 0.0 should be 0.0", 0.0, abs.evaluate());
		
		child.setEvaluate(-2.42);
		assertEquals("ABS of -2.42 should be 2.42", 2.42, abs.evaluate());
	
		child.setEvaluate(2.42);
		assertEquals("ABS of 2.42 should be 2.42", 2.42, abs.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.AbsoluteFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertSame("ABS of 0 should be 0", 0, abs.evaluate());
		
		child.setEvaluate(-2);
		assertSame("ABS of -2 should be 2", 2, abs.evaluate());
	
		child.setEvaluate(3);
		assertSame("ABS of 3 should be 3", 3, abs.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.AbsoluteFunction#dataType(Class...)}
	 * returns the same type for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeAbs() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = abs.dataType(type);
			assertSame("unexpected return type", type, returnType);
		}
		
		returnType = abs.dataType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = abs.dataType(Integer.class, Integer.class);
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
			Node n = parser.parse("ABS(X)");
			assertSame("Parsing did not return an instance of the correct node", AbsoluteFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
