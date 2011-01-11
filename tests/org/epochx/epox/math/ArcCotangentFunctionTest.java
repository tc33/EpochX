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
import org.epochx.tools.util.MathUtils;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.math.ArcCotangentFunction}
 */
public class ArcCotangentFunctionTest extends NodeTestCase {

	private ArcCotangentFunction acot;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new ArcCotangentFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		acot = new ArcCotangentFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcCotangentFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(MathUtils.cotan(0.6));
		assertEquals("ACOT should be the inverse of cotan", 0.6, acot.evaluate(), 1);
		
		child.setEvaluate(MathUtils.cotan(-0.6));
		assertEquals("ACOT should be the inverse of cotan", -0.6, acot.evaluate(), 1);
		
		child.setEvaluate(Double.NaN);
		assertEquals("ACOT of NaN should be NaN", Double.NaN, (Object) acot.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcCotangentFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(1);
		assertSame("ACOT of an integer should return double", Double.class, acot.evaluate().getClass());
		
		child.setEvaluate(1);
		assertEquals("ACOT of 0 should be NaN", MathUtils.acot(1.0), (Object) acot.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcCotangentFunction#getReturnType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeAcot() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = acot.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = acot.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = acot.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
