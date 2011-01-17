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
 * Unit tests for {@link org.epochx.epox.math.CubeRootFunction}
 */
public class CubeRootFunctionTest extends NodeTestCase {

	private CubeRootFunction cbrt;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new CubeRootFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		cbrt = new CubeRootFunction(child);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.CubeRootFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		assertEquals("CBRT of 0.0 should be 0.0", 0.0, cbrt.evaluate(), 0);
		
		child.setEvaluate(1.0);
		assertEquals("CBRT of 1.0 should be 1.0", 1.0, cbrt.evaluate(), 0);
	
		child.setEvaluate(Math.pow(1.5, 3));
		assertEquals("CBRT should be the inverse of cube", 1.5, cbrt.evaluate(), 0);
		
		child.setEvaluate(-1.0);
		assertEquals("CBRT of -1.0 should be -1.0", -1.0, cbrt.evaluate(), 0);
		
		child.setEvaluate(Math.pow(-2.0, 3));
		assertEquals("CBRT should be the inverse of cube", -2.0, cbrt.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.CubeRootFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertEquals("CBRT of 0 should be 0.0", 0.0, cbrt.evaluate(), 0);
		
		child.setEvaluate(1);
		assertEquals("CBRT of 1 should be 1.0", 1.0, cbrt.evaluate(), 0);
	
		child.setEvaluate(2);
		assertEquals("CBRT incorrect", Math.cbrt(2), cbrt.evaluate(), 0);
		
		child.setEvaluate(-1);
		assertEquals("CBRT of -1 should be -1", -1, cbrt.evaluate(), 0);
	
		child.setEvaluate(-8);
		assertEquals("CBRT incorrect", Math.cbrt(-8), cbrt.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.CubeRootFunction#getReturnType(Class...)}
	 * returns the same type for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeCbrt() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = cbrt.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = cbrt.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = cbrt.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
