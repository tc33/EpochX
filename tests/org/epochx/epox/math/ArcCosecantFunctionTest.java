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
 * Unit tests for {@link org.epochx.epox.math.ArcCosecantFunction}
 */
public class ArcCosecantFunctionTest extends NodeTestCase {

	private ArcCosecantFunction acsc;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new ArcCosecantFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		acsc = new ArcCosecantFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcCosecantFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(MathUtils.cosec(0.6));
		assertEquals("ACSC should be the inverse of cosec", 0.6, acsc.evaluate(), 0);
		
		child.setEvaluate(MathUtils.cosec(-0.6));
		assertEquals("ACSC should be the inverse of cosec", -0.6, acsc.evaluate(), 0);
	
		child.setEvaluate(0.0);
		assertEquals("ACSC of 0.0 should be NaN", Double.NaN, acsc.evaluate(), 0);
		
		child.setEvaluate(Double.NaN);
		assertEquals("ACSC of NaN should be NaN", Double.NaN, (Object) acsc.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcCosecantFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(1);
		assertSame("ACSC of an integer should return double", Double.class, acsc.evaluate().getClass());
		
		child.setEvaluate(0);
		assertEquals("ACSC of 0 should be NaN", Double.NaN, (Object) acsc.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.ArcCosecantFunction#getReturnType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeAcsc() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = acsc.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = acsc.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = acsc.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
