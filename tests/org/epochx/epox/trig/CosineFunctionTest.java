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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.epox.trig;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.epox.trig.CosineFunction;
import org.epochx.tools.eval.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.trig.CosineFunction}
 */
public class CosineFunctionTest extends NodeTestCase {

	private CosineFunction cos;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new CosineFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		cos = new CosineFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.CosineFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		assertEquals("COS of 0.0 should be 1.0", 1.0, cos.evaluate(), 0);
		
		child.setEvaluate(Math.acos(0.6));
		assertEquals("COS should be the inverse of acos", 0.6, cos.evaluate(), 0);
	
		child.setEvaluate(Math.acos(-0.6));
		assertEquals("COS should be the inverse of acos", -0.6, cos.evaluate(), 1);
		
		child.setEvaluate(Double.POSITIVE_INFINITY);
		assertEquals("COS of infinity should be NaN", Double.NaN, cos.evaluate(), 0);
		
		child.setEvaluate(Double.NaN);
		assertEquals("COS of NaN should be NaN", Double.NaN, cos.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.CosineFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertEquals("COS of 0 should be 1.0", 1.0, cos.evaluate(), 0);
		
		child.setEvaluate(1);
		assertSame("COS of an integer should return double", Double.class, cos.evaluate().getClass());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.CosineFunction#getReturnType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeCos() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = cos.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = cos.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = cos.getReturnType(Integer.class, Integer.class);
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
			Node n = parser.parse("COS(X)");
			assertSame("Parsing did not return an instance of the correct node", CosineFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
