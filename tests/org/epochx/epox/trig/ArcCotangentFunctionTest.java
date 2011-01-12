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
import org.epochx.epox.trig.ArcCotangentFunction;
import org.epochx.tools.util.MathUtils;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.trig.ArcCotangentFunction}
 */
public class ArcCotangentFunctionTest extends NodeTestCase {

	private ArcCotangentFunction arccot;
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
		arccot = new ArcCotangentFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.ArcCotangentFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(MathUtils.cot(0.6));
		assertEquals("ARCCOT should be the inverse of cot", 0.6, arccot.evaluate(), 1);
		
		child.setEvaluate(MathUtils.cot(-0.6));
		assertEquals("ARCCOT should be the inverse of cot", -0.6, arccot.evaluate(), 1);
		
		child.setEvaluate(Double.NaN);
		assertEquals("ARCCOT of NaN should be NaN", Double.NaN, (Object) arccot.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.ArcCotangentFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(1);
		assertSame("ARCCOT of an integer should return double", Double.class, arccot.evaluate().getClass());
		
		child.setEvaluate(1);
		assertEquals("ARCCOT of 0 should be NaN", MathUtils.arccot(1.0), (Object) arccot.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.ArcCotangentFunction#getReturnType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeArccot() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = arccot.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = arccot.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = arccot.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
}
