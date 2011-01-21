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
import org.epochx.tools.eval.*;
import org.junit.Test;


/**
 * Unit tests for {@link org.epochx.epox.math.CoefficientPowerFunction}
 */
public class CoefficientPowerFunctionTest extends NodeTestCase {

	private CoefficientPowerFunction cvp;
	private MockNode child1;
	private MockNode child2;
	private MockNode child3;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new CoefficientPowerFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		child1 = new MockNode();
		child2 = new MockNode();
		child3 = new MockNode();
		
		cvp = new CoefficientPowerFunction(child1, child2, child3);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.math.CoefficientPowerFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child1.setEvaluate(4.0);
		child2.setEvaluate(3.0);
		child3.setEvaluate(0.0);
		assertEquals("CVP with 4.0 x (3.0 ^ 0.0) should be 4.0", 4.0, cvp.evaluate(), 0);
		
		child1.setEvaluate(-4.0);
		child2.setEvaluate(3.0);
		child3.setEvaluate(0.0);
		assertEquals("CVP with -4.0 x (3.0 ^ 0.0) should be -4.0", -4.0, cvp.evaluate(), 0);
	
		child1.setEvaluate(2.0);
		child2.setEvaluate(3.0);
		child3.setEvaluate(2.0);
		assertEquals("CVP with 2.0 x (3.0 ^ 2.0) should be 18.0", 18.0, cvp.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.CoefficientPowerFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child1.setEvaluate(4);
		child2.setEvaluate(3);
		child3.setEvaluate(0);
		assertEquals("CVP with 4 x (3 ^ 0) should be 4.0", 4.0, cvp.evaluate(), 0);
		
		child1.setEvaluate(-4);
		child2.setEvaluate(3);
		child3.setEvaluate(0);
		assertEquals("CVP with -4 x (3 ^ 0) should be -4.0", -4.0, cvp.evaluate(), 0);
	
		child1.setEvaluate(2);
		child2.setEvaluate(3);
		child3.setEvaluate(2);
		assertEquals("CVP with 2 x (3 ^ 2) should be 18.0", 18.0, cvp.evaluate(), 0);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.math.CoefficientPowerFunction#getReturnType(Class...)}
	 * returns the correct type for numeric input types and <code>null</code> 
	 * otherwise.
	 */
	@Test
	public void testGetReturnTypeCvp() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = cvp.getReturnType(type, type, type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = cvp.getReturnType(Short.class, Double.class, Integer.class);
		assertSame("mixed numeric input types should be Double return type", Double.class, returnType);
		
		returnType = cvp.getReturnType(Double.class, Double.class, Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = cvp.getReturnType(Integer.class, Integer.class, Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
		
		returnType = cvp.getReturnType(Integer.class, Integer.class);
		assertNull("too few inputs should be invalid", returnType);
	}
	
	/**
	 * Tests that this function can be parsed by the EpoxParser.
	 */
	@Test
	public void testEpoxParser() {
		EpoxParser parser = new EpoxParser();
		
		try {
			parser.declareVariable(new Variable("X", Double.class));
			Node n = parser.parse("CVP(X, X, X)");
			assertSame("Parsing did not return an instance of the correct node", CoefficientPowerFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
