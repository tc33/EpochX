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
import org.epochx.tools.util.MathUtils;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.trig.HyperbolicTangentFunction}
 */
public class HyperbolicTangentFunctionTest extends NodeTestCase {

	private HyperbolicTangentFunction tanh;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new HyperbolicTangentFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		tanh = new HyperbolicTangentFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.HyperbolicTangentFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(MathUtils.artanh(0.6));
		assertEquals("TANH should be the inverse of arsinh", 0.6, tanh.evaluate(), 1);
		
		child.setEvaluate(MathUtils.artanh(-0.6));
		assertEquals("TANH should be the inverse of arsinh", -0.6, tanh.evaluate(), 1);
		
		child.setEvaluate(MathUtils.artanh(1.0));
		assertEquals("TANH should be the inverse of arsinh", 1.0, tanh.evaluate(), 0);
		
		child.setEvaluate(Double.NaN);
		assertEquals("TANH of NaN should be NaN", Double.NaN, (Object) tanh.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.HyperbolicTangentFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertSame("TANH of an integer should return double", Double.class, tanh.evaluate().getClass());
		
		child.setEvaluate(0);
		assertEquals("TANH of 0 should be 0.0", 0.0, (Object) tanh.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.HyperbolicTangentFunction#getReturnType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeTanh() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = tanh.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = tanh.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = tanh.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
