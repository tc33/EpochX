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
package org.epochx.epox.trig;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.interpret.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.trig.AreaHyperbolicSineFunction}
 */
public class AreaHyperbolicSineFunctionTest extends NodeTestCase {

	private AreaHyperbolicSineFunction arsinh;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new AreaHyperbolicSineFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		arsinh = new AreaHyperbolicSineFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.AreaHyperbolicSineFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(Math.sinh(1.6));
		assertEquals("ARSINH should be the inverse of sinh", 1.6, arsinh.evaluate(), 1);
		
		child.setEvaluate(Math.sinh(1.0));
		assertEquals("ARSINH should be the inverse of sinh", 1.0, arsinh.evaluate(), 0);
		
		child.setEvaluate(0.0);
		assertEquals("ARSINH of 0.0 should be 0.0", 0.0, arsinh.evaluate(), 0);
		
		child.setEvaluate(Double.POSITIVE_INFINITY);
		assertEquals("ARSINH of positive infinity should be positive infinity", Double.POSITIVE_INFINITY, arsinh.evaluate(), 0);
		
		child.setEvaluate(Double.NaN);
		assertEquals("ARSINH of NaN should be NaN", Double.NaN, arsinh.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.AreaHyperbolicSineFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(1);
		assertSame("ARSINH of an integer should return double", Double.class, arsinh.evaluate().getClass());
		
		child.setEvaluate(0);
		assertEquals("ARSINH of 0 should be 0.0", 0.0, arsinh.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.AreaHyperbolicSineFunction#dataType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeArsinh() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = arsinh.dataType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = arsinh.dataType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = arsinh.dataType(Integer.class, Integer.class);
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
			Node n = parser.parse("ARSINH(X)");
			assertSame("Parsing did not return an instance of the correct node", AreaHyperbolicSineFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
