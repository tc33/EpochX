/*
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.epox.trig;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.epox.trig.ArcTangentFunction;
import org.epochx.tools.eval.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.trig.ArcTangentFunction}
 */
public class ArcTangentFunctionTest extends NodeTestCase {

	private ArcTangentFunction arctan;
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
		arctan = new ArcTangentFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.ArcTangentFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		assertEquals("ARCTAN of 0.0 should be 0.0", 0.0, (Object) arctan.evaluate());
		
		child.setEvaluate(Math.tan(0.6));
		assertEquals("ARCTAN should be the inverse of tan", 0.6, (Object) arctan.evaluate());
	
		child.setEvaluate(Math.tan(-0.6));
		assertEquals("ARCTAN should be the inverse of tan", -0.6, (Object) arctan.evaluate());
		
		child.setEvaluate(Double.NaN);
		assertEquals("ARCTAN of NaN should be NaN", Double.NaN, (Object) arctan.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.ArcTangentFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertSame("ARCTAN of an integer should return double", Double.class, arctan.evaluate().getClass());
		assertEquals("ARCTAN of 0 should be 0.0", 0.0, (Object) arctan.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.ArcTangentFunction#getReturnType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeAtan() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = arctan.getReturnType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = arctan.getReturnType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = arctan.getReturnType(Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
	
	/**
	 * Tests that this function can be parsed by the EpoxParser.
	 */
	@Test
	public void testEpoxParser() {
		EpoxParser parser = new EpoxParser();
		
		try {
			parser.declareVariable(new Variable("X", Double.class));
			Node n = parser.parse("ARCTAN(X)");
			assertSame("Parsing did not return an instance of the correct node", ArcTangentFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
