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
package org.epochx.epox.lang;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.epox.lang.IfFunction;
import org.epochx.interpret.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.lang.IfFunction}
 */
public class IfFunctionTest extends NodeTestCase {

	private IfFunction ifFunction;
	private MockNode child1;
	private MockNode child2;
	private MockNode child3;

	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new IfFunction();
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
		child1.setGetIdentifier("child1");
		child2.setGetIdentifier("child2");
		child3.setGetIdentifier("child3");
		
		ifFunction = new IfFunction(child1, child2, child3);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.lang.IfFunction#evaluate()} 
	 * correctly evaluates its second child if the first child evaluates
	 * to <code>true</code>.
	 */
	@Test
	public void testEvaluateTrue() {
		assertSame("unit test broken", 0, child2.getEvaluateCount());
		assertSame("unit test broken", 0, child3.getEvaluateCount());
		
		// Test where the first child evaluates to true.
		child1.setEvaluate(Boolean.TRUE);
		ifFunction.evaluate();
		assertSame("unit test broken", 1, child2.getEvaluateCount());
		assertSame("unit test broken", 0, child3.getEvaluateCount());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.lang.IfFunction#evaluate()} 
	 * correctly evaluates its third child if the first child evaluates
	 * to <code>false</code>.
	 */
	@Test
	public void testEvaluateFalse() {
		assertSame("unit test broken", 0, child2.getEvaluateCount());
		assertSame("unit test broken", 0, child3.getEvaluateCount());
		
		// Test where the first child evaluates to false.
		child1.setEvaluate(Boolean.FALSE);
		ifFunction.evaluate();
		assertSame("unit test broken", 0, child2.getEvaluateCount());
		assertSame("unit test broken", 1, child3.getEvaluateCount());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.lang.IfFunction#getReturnType(Class...)}
	 * returns a non-null type for the correct inputs, and <code>null</code>
	 * for incorrectly typed or incorrect number of inputs.
	 */
	@Test
	public void testGetReturnTypeIf() {
		Class<?> returnType = ifFunction.getReturnType(Boolean.class, Number.class, Integer.class);
		assertSame("unexpected return type", Number.class, returnType);
		
		returnType = ifFunction.getReturnType(Number.class, Number.class, Integer.class);
		assertNull("non-Boolean type for first child should be invalid", returnType);
		
		returnType = ifFunction.getReturnType(Boolean.class, Number.class, Integer.class, Integer.class);
		assertNull("too many inputs should be invalid", returnType);
	}
	
	/**
	 * Tests that this function can be parsed by the EpoxParser.
	 */
	@Test
	public void testEpoxParser() {
		EpoxParser parser = new EpoxParser();
		
		try {
			parser.declareVariable(new Variable("X", Boolean.class));
			Node n = parser.parse("IF(X, X, X)");
			assertSame("Parsing did not return an instance of the correct node", IfFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
