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
package org.epochx.epox.math;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.math.CosineFunction}
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
	 * Tests that {@link org.epochx.epox.math.CosineFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		assertEquals("COS of 0.0 should be 1.0", 1.0, cos.evaluate(), 0);
		
		child.setEvaluate(Math.acos(0.6));
		assertEquals("COS is not calculated correctly", 0.6, cos.evaluate(), 0);
	
		child.setEvaluate(Math.acos(-0.6));
		assertEquals("COS is not calculated correctly", -0.6, cos.evaluate(), 1);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.CosineFunction#evaluate()} 
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
	 * Tests that {@link org.epochx.epox.math.CosineFunction#getReturnType(Class...)}
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
}
