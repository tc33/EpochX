/*
 * Copyright 2007-2013
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
package org.epochx.epox.ant;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.interpret.*;
import org.epochx.tools.ant.*;
import org.junit.*;


/**
 * Unit tests for {@link org.epochx.epox.ant.AntTurnLeftFunction}
 */
public class AntTurnLeftFunctionTest extends NodeTestCase {

	private AntTurnLeftFunction antLeftChild;
	private MockNode child;
	private AntTurnLeftFunction antLeft;
	private MockAnt ant;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new AntTurnLeftFunction();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		ant = new MockAnt();
		antLeft = new AntTurnLeftFunction(ant);
		child = new MockNode();
		antLeftChild = new AntTurnLeftFunction(child);
	}

	/**
	 * Tests that {@link org.epochx.epox.ant.AntTurnLeftFunction#AntTurnLeftFunction(org.epochx.tools.ant.Ant)}
	 * throws an exception for a <code>null</code> ant.
	 */
	@Test
	public void testAntTurnLeftFunctionNull() {
		try {
			new AntTurnLeftFunction((Ant) null);
			fail("an exception should be thrown for a null ant");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Tests that {@link org.epochx.epox.ant.AntTurnLeftFunction#evaluate()} 
	 * correctly calls its ant's <code>antLeft</code> method when evaluated.
	 */
	@Test
	public void testEvaluate() {
		assertSame("unit test broken", 0, ant.getTurnLeftCount());
		
		antLeft.evaluate();
		
		assertSame("the ant's antLeft method should have been called", 1, ant.getTurnLeftCount());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.ant.AntTurnLeftFunction#evaluate()} 
	 * correctly calls its ant's <code>turnLeft</code> method when evaluated 
	 * with a child node providing the ant.
	 */
	@Test
	public void testEvaluateChild() {
		assertSame("unit test broken", 0, ant.getTurnLeftCount());
		
		child.setEvaluate(ant);
		antLeftChild.evaluate();
		
		assertSame("the ant's turnLeft method should have been called", 1, ant.getTurnLeftCount());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.ant.AntTurnLeftFunction#dataType(Class...)}
	 * returns a Void data-type for the correct inputs, and <code>null</code>
	 * for incorrectly typed or incorrect number of inputs.
	 */
	@Test
	public void testGetReturnTypeLeft() {
		// Test the ant child version.
		Class<?> returnType = antLeftChild.dataType(Ant.class);
		assertEquals("type should be Void for an ant input", Void.class, returnType);
		
		returnType = antLeftChild.dataType(Integer.class);
		assertNull("non-ant input should be invalid", returnType);
		
		returnType = antLeftChild.dataType(Ant.class, Ant.class);
		assertNull("too many ant inputs should be invalid", returnType);
		
		// Test the non-child version.
		returnType = antLeft.dataType();
		assertEquals("type should be Void for no inputs", Void.class, returnType);
		
		returnType = antLeft.dataType(Ant.class);
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
			Node n = parser.parse("TURN-LEFT(ANT)");
			assertSame("Parsing did not return an instance of the correct node", AntTurnLeftFunction.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
