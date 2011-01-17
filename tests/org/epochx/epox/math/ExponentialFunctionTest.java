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
 * Unit tests for {@link org.epochx.epox.math.ExponentialFunction}
 */
public class ExponentialFunctionTest extends NodeTestCase {

	private ExponentialFunction exp;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new ExponentialFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		exp = new ExponentialFunction(child);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.ExponentialFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(Math.log(2.5));
		assertEquals("EXP should be the inverse of natural log", 2.5, exp.evaluate(), 0);
		
		child.setEvaluate(0.0);
		assertEquals("EXP of 0.0 should be 1.0", 1.0, exp.evaluate(), 0);
		
		child.setEvaluate(1.0);
		assertEquals("EXP of 1.0 should be e", Math.E, exp.evaluate(), 1);
		
		child.setEvaluate(Double.NaN);
		assertEquals("EXP of NaN should be NaN", Double.NaN, exp.evaluate(), 0);
		
		child.setEvaluate(Double.POSITIVE_INFINITY);
		assertEquals("EXP of +infinity should be +infinity", Double.POSITIVE_INFINITY, exp.evaluate(), 0);
		
		child.setEvaluate(Double.NEGATIVE_INFINITY);
		assertEquals("EXP of -infinity should be 0.0", 0.0, exp.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ExponentialFunction#evaluate()} 
	 * correctly evaluates float values.
	 */
	@Test
	public void testEvaluateFloat() {
		child.setEvaluate(0.0f);
		assertEquals("EXP of 0.0f should be 1.0d", 1.0d, exp.evaluate(), 0);
	}

	/**
	 * Tests that {@link org.epochx.epox.math.ExponentialFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertEquals("EXP of 0 should be 1.0d", 1.0d, exp.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ExponentialFunction#evaluate()} 
	 * correctly evaluates long values.
	 */
	@Test
	public void testEvaluateLong() {
		child.setEvaluate(0L);
		assertEquals("EXP of 0L should be 1.0d", 1.0d, exp.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ExponentialFunction#getReturnType(Class...)}
	 * returns Double type for numeric input type and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypeExp() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = exp.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = exp.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = exp.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
