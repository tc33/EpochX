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
 * Unit tests for {@link org.epochx.epox.math.CosecantFunction}
 */
public class CosecantFunctionTest extends NodeTestCase {

	private CosecantFunction cosec;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new CosecantFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child = new MockNode();
		cosec = new CosecantFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.CosecantFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		assertEquals("COSEC of 0.0 should be infinity", Double.POSITIVE_INFINITY, (Object) cosec.evaluate());
		
		child.setEvaluate(0.6);
		assertEquals("COSEC is not calculated correctly", Math.sin(0.6), (Object) (1/cosec.evaluate()));
	
		child.setEvaluate(-0.6);
		assertEquals("COSEC is not calculated correctly", Math.sin(-0.6), (Object) (1/cosec.evaluate()));
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.CosecantFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertEquals("COSEC of 0 should be infinity", Double.POSITIVE_INFINITY, (Object) cosec.evaluate());
		
		child.setEvaluate(1);
		assertSame("COSEC of an integer should return double", Double.class, cosec.evaluate().getClass());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.CosecantFunction#getReturnType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeCosec() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = cosec.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = cosec.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = cosec.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
