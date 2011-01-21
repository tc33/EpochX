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
package org.epochx.epox.ant;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.tools.ant.*;
import org.epochx.tools.eval.*;
import org.junit.*;


/**
 * Unit tests for {@link org.epochx.epox.ant.AntSkipFunction}
 */
public class AntSkipFunctionTest extends NodeTestCase {

	private AntSkipFunction antSkipChild;
	private MockNode child;
	private AntSkipFunction antSkip;
	private MockAnt ant;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new AntSkipFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		ant = new MockAnt();
		antSkip = new AntSkipFunction(ant);
		child = new MockNode();
		antSkipChild = new AntSkipFunction(child);
	}

	/**
	 * Tests that {@link org.epochx.epox.ant.AntSkipFunction#AntSkipFunction(org.epochx.tools.ant.Ant)}
	 * throws an exception for a <code>null</code> ant.
	 */
	@Test
	public void testAntSkipFunctionNull() {
		try {
			new AntSkipFunction((Ant) null);
			fail("an exception should be thrown for a null ant");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Tests that {@link org.epochx.epox.ant.AntSkipFunction#evaluate()} 
	 * correctly calls its ant's <code>skip</code> method when evaluated.
	 */
	@Test
	public void testEvaluate() {
		assertSame("unit test broken", 0, ant.getSkipCount());
		
		antSkip.evaluate();
		
		assertSame("the ant's skip method should have been called", 1, ant.getSkipCount());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.ant.AntSkipFunction#evaluate()} 
	 * correctly calls its ant's <code>skip</code> method when evaluated with
	 * a child node providing the ant.
	 */
	@Test
	public void testEvaluateChild() {
		assertSame("unit test broken", 0, ant.getSkipCount());
		
		child.setEvaluate(ant);
		antSkipChild.evaluate();
		
		assertSame("the ant's skip method should have been called", 1, ant.getSkipCount());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.ant.AntSkipFunction#getReturnType(Class...)}
	 * returns a Void data-type for the correct inputs, and <code>null</code>
	 * for incorrectly typed or incorrect number of inputs.
	 */
	@Test
	public void testGetReturnTypeSkip() {
		// Test the ant child version.
		Class<?> returnType = antSkipChild.getReturnType(Ant.class);
		assertEquals("type should be Void for an ant input", Void.class, returnType);
		
		returnType = antSkipChild.getReturnType(Integer.class);
		assertNull("non-ant input should be invalid", returnType);
		
		returnType = antSkipChild.getReturnType(Ant.class, Ant.class);
		assertNull("too many ant inputs should be invalid", returnType);
		
		// Test the non-child version.
		returnType = antSkip.getReturnType();
		assertEquals("type should be Void for no inputs", Void.class, returnType);
		
		returnType = antSkip.getReturnType(Ant.class);
		assertNull("too many ant inputs should be invalid", returnType);
	}
	
	/**
	 * Tests that this function can be parsed by the EpoxParser.
	 */
	@Test
	public void testEpoxParser() {
		EpoxParser parser = new EpoxParser();
		
		try {
			parser.declareVariable(new Variable("ANT", Ant.class));
			Node n = parser.parse("SKIP(ANT)");
			assertSame("Parsing did not return an instance of the correct node", AntSkipFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
