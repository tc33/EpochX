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
package org.epochx.epox.bool;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.interpret.*;
import org.junit.Test;

/**
 * Unit tests for {@link org.epochx.epox.bool.NotFunction}
 */
public class NotFunctionTest extends NodeTestCase {

	private NotFunction node;
	private MockNode child;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new NotFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		child = new MockNode();
		child.setGetIdentifier("child");
		
		node = new NotFunction(child);

		super.setUp();
	}

	/**
	 * Tests that {@link org.epochx.epox.bool.NotFunction#evaluate()} correctly
	 * evaluates inputs.
	 */
	@Test
	public void testEvaluate() {
		child.setEvaluate(Boolean.TRUE);
		assertFalse("NOT of true should be false", node.evaluate());
		
		child.setEvaluate(Boolean.FALSE);
		assertTrue("NOT of false should be true", node.evaluate());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.bool.NotFunction#dataType(Class...)}
	 * returns a Boolean data-type for one Boolean input, and <code>null</code>
	 * for non-Boolean or incorrect number.
	 */
	@Test
	public void testGetReturnTypeNor() {
		Class<?> returnType = node.dataType(Boolean.class);
		assertEquals("type should be boolean for 1 boolean input", Boolean.class, returnType);
		
		returnType = node.dataType(Integer.class);
		assertNull("non-boolean inputs should be invalid", returnType);
		
		returnType = node.dataType(Boolean.class, Boolean.class);
		assertNull("too many boolean inputs should be invalid", returnType);
	}
	
	/**
	 * Tests that this function can be parsed by the EpoxParser.
	 */
	@Test
	public void testEpoxParser() {
		EpoxParser parser = new EpoxParser();
		
		try {
			parser.declareVariable(new Variable("X", Boolean.class));
			Node n = parser.parse("NOT(X)");
			assertSame("Parsing did not return an instance of the correct node", NotFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
