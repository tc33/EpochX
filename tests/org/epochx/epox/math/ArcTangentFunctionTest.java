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
 * Unit tests for {@link org.epochx.epox.math.ArcTangentFunction}
 */
public class ArcTangentFunctionTest extends NodeTestCase {

	private ArcTangentFunction atan;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new ArcTangentFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		atan = new ArcTangentFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcTangentFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		assertEquals("ATAN of 0.0 should be 0.0", 0.0, (Object) atan.evaluate());
		
		child.setEvaluate(Math.tan(0.6));
		assertEquals("ATAN should be the inverse of tan", 0.6, (Object) atan.evaluate());
	
		child.setEvaluate(Math.tan(-0.6));
		assertEquals("ATAN should be the inverse of tan", -0.6, (Object) atan.evaluate());
		
		child.setEvaluate(Double.NaN);
		assertEquals("ATAN of NaN should be NaN", Double.NaN, (Object) atan.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcTangentFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertSame("ATAN of an integer should return double", Double.class, atan.evaluate().getClass());
		assertEquals("ATAN of 0 should be 0.0", 0.0, (Object) atan.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcTangentFunction#getReturnType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeAtan() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = atan.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = atan.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = atan.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
