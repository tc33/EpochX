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
 * Unit tests for {@link org.epochx.epox.math.ArcSineFunction}
 */
public class ArcSineFunctionTest extends NodeTestCase {

	private ArcSineFunction asin;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new ArcSineFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		asin = new ArcSineFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcSineFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		assertEquals("ASIN of 0.0 should be 0.0", 0.0, (Object) asin.evaluate());
		
		child.setEvaluate(Math.sin(0.6));
		assertEquals("ASIN should be the inverse of sine", 0.6, (Object) asin.evaluate());
	
		child.setEvaluate(Math.sin(-0.6));
		assertEquals("ASIN should be the inverse of sine", -0.6, (Object) asin.evaluate());
		
		child.setEvaluate(1.1);
		assertEquals("ASIN of 1.1 should be NaN", Double.NaN, (Object) asin.evaluate());
		
		child.setEvaluate(Double.NaN);
		assertEquals("ASIN of NaN should be NaN", Double.NaN, (Object) asin.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcSineFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertSame("ASIN of an integer should return double", Double.class, asin.evaluate().getClass());
		assertEquals("ASIN of 0 should be 0.0", 0.0, (Object) asin.evaluate());
		
		child.setEvaluate(-2);
		assertEquals("ASIN of -2 should be NaN", Double.NaN, (Object) asin.evaluate());
	
		child.setEvaluate(2);
		assertEquals("ASIN of 2 should be NaN", Double.NaN, (Object) asin.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcSineFunction#getReturnType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeAsin() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = asin.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = asin.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = asin.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
