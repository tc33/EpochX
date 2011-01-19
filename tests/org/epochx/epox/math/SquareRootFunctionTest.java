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
import org.junit.Test;


/**
 * Unit tests for {@link org.epochx.epox.math.SquareRootFunction}
 */
public class SquareRootFunctionTest extends NodeTestCase {

	private SquareRootFunction sqrt;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new SquareRootFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		sqrt = new SquareRootFunction(child);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.SquareRootFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		assertEquals("SQRT of 0.0 should be 0.0", 0.0, sqrt.evaluate(), 0);
		
		child.setEvaluate(1.0);
		assertEquals("SQRT of 1.0 should be 1.0", 1.0, sqrt.evaluate(), 0);
	
		child.setEvaluate(Math.pow(1.5, 2));
		assertEquals("SQRT should be the inverse of square", 1.5, sqrt.evaluate(), 0);
		
		child.setEvaluate(-1.0);
		assertEquals("SQRT of -1.0 should be NaN", Double.NaN, sqrt.evaluate(), 0);
		
		child.setEvaluate(Double.NaN);
		assertEquals("SQRT of -1.0 should be NaN", Double.NaN, sqrt.evaluate(), 0);
		
		child.setEvaluate(Math.pow(-2.0, 2));
		assertEquals("SQRT should be the absolute value of inverse of square", 2.0, sqrt.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SquareRootFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child.setEvaluate(0.0f);
		assertEquals("SQRT of 0.0f should be 0.0f", 0.0, sqrt.evaluate(), 0);
		
		child.setEvaluate(1.0f);
		assertEquals("SQRT of 1.0f should be 1.0f", 1.0, sqrt.evaluate(), 0);
	
		child.setEvaluate(Math.pow(1.5, 2));
		assertEquals("SQRT should be the inverse of square", 1.5, sqrt.evaluate(), 0);
		
		child.setEvaluate(-1.0f);
		assertEquals("SQRT of -1.0f should be NaN", Double.NaN, sqrt.evaluate(), 0);
		
		child.setEvaluate(Float.NaN);
		assertEquals("SQRT of -1.0f should be NaN", Double.NaN, sqrt.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SquareRootFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertEquals("SQRT of 0 should be 0.0", 0.0, sqrt.evaluate(), 0);
		
		child.setEvaluate(1);
		assertEquals("SQRT of 1 should be 1.0", 1.0, sqrt.evaluate(), 0);
	
		child.setEvaluate(-1);
		assertEquals("SQRT of -1 should be NaN", Double.NaN, sqrt.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SquareRootFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child.setEvaluate(0L);
		assertEquals("SQRT of 0L should be 0.0", 0.0, sqrt.evaluate(), 0);
		
		child.setEvaluate(1L);
		assertEquals("SQRT of 1L should be 1.0", 1.0, sqrt.evaluate(), 0);
	
		child.setEvaluate(-1L);
		assertEquals("SQRT of -1L should be NaN", Double.NaN, sqrt.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.SquareRootFunction#getReturnType(Class...)}
	 * returns the same type for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeSqrt() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = sqrt.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = sqrt.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = sqrt.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
