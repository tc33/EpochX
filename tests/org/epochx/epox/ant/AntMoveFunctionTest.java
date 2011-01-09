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
import org.junit.*;


/**
 * Unit tests for {@link org.epochx.epox.ant.AntMoveFunction}
 */
public class AntMoveFunctionTest extends NodeTestCase {

	private AntMoveFunction antMoveChild;
	private MockNode child;
	private AntMoveFunction antMove;
	private MockAnt ant;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new AntMoveFunction();
	}

	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		ant = new MockAnt();
		antMove = new AntMoveFunction(ant);
		child = new MockNode();
		antMoveChild = new AntMoveFunction(child);
	}
	
	/**
	 * Tests that {@link org.epochx.epox.ant.AntMoveFunction#AntMoveFunction(org.epochx.tools.ant.Ant)}
	 * throws an exception for a <code>null</code> ant.
	 */
	@Test
	public void testAntMoveFunctionNull() {
		try {
			new AntMoveFunction((Ant) null);
			fail("an exception should be thrown for a null ant");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Tests that {@link org.epochx.epox.ant.AntMoveFunction#evaluate()} 
	 * correctly calls its ant's <code>move</code> method when evaluated.
	 */
	@Test
	public void testEvaluate() {
		assertSame("unit test broken", 0, ant.getMoveCount());
		
		antMove.evaluate();
		
		assertSame("the ant's move method should have been called", 1, ant.getMoveCount());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.ant.AntMoveFunction#evaluate()} 
	 * correctly calls its ant's <code>move</code> method when evaluated with
	 * a child node providing the ant.
	 */
	@Test
	public void testEvaluateChild() {
		assertSame("unit test broken", 0, ant.getMoveCount());
		
		child.setEvaluate(ant);
		antMoveChild.evaluate();
		
		assertSame("the ant's move method should have been called", 1, ant.getMoveCount());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.ant.AntMoveFunction#getReturnType(Class...)}
	 * returns a Void data-type for the correct inputs, and <code>null</code>
	 * for incorrectly typed or incorrect number of inputs.
	 */
	@Test
	public void testGetReturnTypeMove() {
		// Test the ant child version.
		Class<?> returnType = antMoveChild.getReturnType(Ant.class);
		assertEquals("type should be Void for an ant input", Void.class, returnType);
		
		returnType = antMoveChild.getReturnType(Integer.class);
		assertNull("non-ant input should be invalid", returnType);
		
		returnType = antMoveChild.getReturnType(Ant.class, Ant.class);
		assertNull("too many ant inputs should be invalid", returnType);
		
		// Test the non-child version.
		returnType = antMove.getReturnType();
		assertEquals("type should be Void for no inputs", Void.class, returnType);
		
		returnType = antMove.getReturnType(Ant.class);
		assertNull("too many ant inputs should be invalid", returnType);
	}
}
