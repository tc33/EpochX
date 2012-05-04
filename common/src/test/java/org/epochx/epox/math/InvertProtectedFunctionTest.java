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
 * Unit tests for {@link org.epochx.epox.math.InvertProtectedFunction}
 */
public class InvertProtectedFunctionTest extends NodeTestCase {

	private InvertProtectedFunction inv;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new InvertProtectedFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		inv = new InvertProtectedFunction(child);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.InvertProtectedFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		assertEquals("INV of 0.0 should be the protection value", 1.0, inv.evaluate(), 0);
		
		child.setEvaluate(0.0);
		inv.setProtectionValue(3.2);
		assertEquals("INV of 0.0 should be the protection value", 3.2, inv.evaluate(), 0);
	
		child.setEvaluate(2.0);
		assertEquals("INV of 2.0 should be 0.5", 0.5, inv.evaluate(), 0);
	
		child.setEvaluate(1/3.53);
		assertEquals("INV should be self-inversive", 3.53, inv.evaluate(), 0);

		child.setEvaluate(Double.NaN);
		assertEquals("INV of NaN should be NaN", Double.NaN, inv.evaluate(), 0);

		child.setEvaluate(Double.POSITIVE_INFINITY);
		assertEquals("INV of infinity should be 0.0", 0.0, inv.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.InvertProtectedFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertEquals("INV of 0 should be the protection value", 1.0, inv.evaluate(), 0);
		
		child.setEvaluate(0);
		inv.setProtectionValue(3.2);
		assertEquals("INV of 0 should be the protection value", 3.2, inv.evaluate(), 0);
	
		child.setEvaluate(2);
		assertEquals("INV of 2 should be 0.5", 0.5, inv.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.InvertProtectedFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child.setEvaluate(0.0f);
		assertEquals("INV of 0 should be the protection value", 1.0, inv.evaluate(), 0);
		
		child.setEvaluate(0.0f);
		inv.setProtectionValue(3.2);
		assertEquals("INV of 0 should be the protection value", 3.2, inv.evaluate(), 0);
	
		child.setEvaluate(2.0f);
		assertEquals("INV of 2 should be 0.5", 0.5, inv.evaluate(), 0);
		
		child.setEvaluate((float) 1/3.53);
		assertEquals("INV should be self-inversive", 3.53, inv.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.InvertProtectedFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child.setEvaluate(0L);
		assertEquals("INV of 0L should be the protection value", 1.0, inv.evaluate(), 0);
		
		child.setEvaluate(0L);
		inv.setProtectionValue(3.2);
		assertEquals("INV of 0L should be the protection value", 3.2, inv.evaluate(), 0);
	
		child.setEvaluate(2L);
		assertEquals("INV of 2L should be 0.5", 0.5, inv.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.InvertProtectedFunction#dataType(Class...)}
	 * returns the same type for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeInv() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = inv.dataType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = inv.dataType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = inv.dataType(Integer.class, Integer.class);
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
			Node n = parser.parse("INV(X)");
			assertSame("Parsing did not return an instance of the correct node", InvertProtectedFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
