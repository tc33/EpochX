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
import org.epochx.epox.trig.CosecantFunction;
import org.epochx.interpret.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.trig.CosecantFunction}
 */
public class CosecantFunctionTest extends NodeTestCase {

	private CosecantFunction csc;
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
		csc = new CosecantFunction(child);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.CosecantFunction#evaluate()} 
	 * correctly evaluates double values.
	 */
	@Test
	public void testEvaluateDouble() {
		child.setEvaluate(0.0);
		assertEquals("CSC of 0.0 should be infinity", Double.POSITIVE_INFINITY, (Object) csc.evaluate());
		
		child.setEvaluate(0.6);
		assertEquals("CSC is not calculated correctly", Math.sin(0.6), (Object) (1/csc.evaluate()));
	
		child.setEvaluate(-0.6);
		assertEquals("CSC is not calculated correctly", Math.sin(-0.6), (Object) (1/csc.evaluate()));
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.CosecantFunction#evaluate()} 
	 * correctly evaluates integer values.
	 */
	@Test
	public void testEvaluateInteger() {
		child.setEvaluate(0);
		assertEquals("CSC of 0 should be infinity", Double.POSITIVE_INFINITY, (Object) csc.evaluate());
		
		child.setEvaluate(1);
		assertSame("CSC of an integer should return double", Double.class, csc.evaluate().getClass());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.trig.CosecantFunction#dataType(Class...)}
	 * returns <code>Double</code> for a numeric class and <code>null</code> otherwise.
	 */
	@Test
	public void testGetReturnTypeCsc() {
		Class<?>[] inputTypes = {Double.class, Integer.class, Float.class, Long.class, Short.class, Byte.class};
		
		Class<?> returnType;
		for (Class<?> type: inputTypes) {
			returnType = csc.dataType(type);
			assertSame("unexpected return type", Double.class, returnType);
		}
		
		returnType = csc.dataType(Boolean.class);
		assertNull("non-numeric type for child should be invalid", returnType);
		
		returnType = csc.dataType(Integer.class, Integer.class);
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
			Node n = parser.parse("CSC(X)");
			assertSame("Parsing did not return an instance of the correct node", CosecantFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
