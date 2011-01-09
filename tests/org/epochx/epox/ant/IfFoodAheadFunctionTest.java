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
 * Unit tests for {@link org.epochx.epox.ant.IfFoodAheadFunction}
 */
public class IfFoodAheadFunctionTest extends NodeTestCase {

	private IfFoodAheadFunction antIfFoodChild;
	private MockNode child1;
	private MockNode child2;
	private MockNode child3;
	private IfFoodAheadFunction antIfFood;
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
		child1 = new MockNode();
		child1.setEvaluate(ant);
		child2 = new MockNode();
		child3 = new MockNode();
		antIfFood = new IfFoodAheadFunction(ant, child2, child3);
		antIfFoodChild = new IfFoodAheadFunction(child1, child2, child3);
	}

	/**
	 * Tests that {@link org.epochx.epox.ant.IfFoodAheadFunction#IfFoodAheadFunction(Ant, Node, Node)}
	 * throws an exception for a <code>null</code> ant.
	 */
	@Test
	public void testAntMoveFunctionNull() {
		try {
			new IfFoodAheadFunction((Ant) null, child2, child3);
			fail("an exception should be thrown for a null ant");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.ant.IfFoodAheadFunction#evaluate()} 
	 * correctly evaluates its first child if food is ahead of the ant.
	 */
	@Test
	public void testEvaluateFood() {
		assertSame("unit test broken", 0, child2.getEvaluateCount());
		assertSame("unit test broken", 0, child3.getEvaluateCount());
		
		// Test where food is NOT ahead.
		ant.setIsFoodAhead(false);
		antIfFood.evaluate();
		assertSame("unit test broken", 0, child2.getEvaluateCount());
		assertSame("unit test broken", 1, child3.getEvaluateCount());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.ant.IfFoodAheadFunction#evaluate()} 
	 * correctly evaluates its first child if food is ahead of the ant.
	 */
	@Test
	public void testEvaluateNoFood() {
		assertSame("unit test broken", 0, child2.getEvaluateCount());
		assertSame("unit test broken", 0, child3.getEvaluateCount());
		
		// Test where food is NOT ahead.
		ant.setIsFoodAhead(false);
		antIfFood.evaluate();
		assertSame("unit test broken", 0, child2.getEvaluateCount());
		assertSame("unit test broken", 1, child3.getEvaluateCount());
	}

	/**
	 * Tests that {@link org.epochx.epox.ant.IfFoodAheadFunction#evaluate()} 
	 * correctly evaluates its first child if food is ahead of the ant when 
	 * evaluated with a child node providing the ant.
	 */
	@Test
	public void testEvaluateChildFood() {
		assertSame("unit test broken", 0, child2.getEvaluateCount());
		assertSame("unit test broken", 0, child3.getEvaluateCount());
		
		// Test where food is ahead.
		ant.setIsFoodAhead(true);
		antIfFoodChild.evaluate();
		assertSame("unit test broken", 1, child2.getEvaluateCount());
		assertSame("unit test broken", 0, child3.getEvaluateCount());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.ant.IfFoodAheadFunction#evaluate()} 
	 * correctly evaluates its first child if food is ahead of the ant when 
	 * evaluated with a child node providing the ant.
	 */
	@Test
	public void testEvaluateChildNoFood() {
		assertSame("unit test broken", 0, child2.getEvaluateCount());
		assertSame("unit test broken", 0, child3.getEvaluateCount());
		
		// Test where food is NOT ahead.
		ant.setIsFoodAhead(false);
		antIfFoodChild.evaluate();
		assertSame("unit test broken", 0, child2.getEvaluateCount());
		assertSame("unit test broken", 1, child3.getEvaluateCount());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.ant.IfFoodAheadFunction#getReturnType(Class...)}
	 * returns a Void data-type for the correct inputs, and <code>null</code>
	 * for incorrectly typed or incorrect number of inputs.
	 */
	@Test
	public void testGetReturnTypeIfFood() {
		// Test the ant child version.
		Class<?> returnType = antIfFoodChild.getReturnType(Ant.class, Void.class, Void.class);
		assertEquals("type should be Void for an Ant and two Void inputs", Void.class, returnType);
		
		returnType = antIfFoodChild.getReturnType(Void.class, Void.class, Void.class);
		assertNull("non-Ant input for first child should be invalid", returnType);
		
		returnType = antIfFoodChild.getReturnType(Ant.class, Void.class, Void.class, Void.class);
		assertNull("too many inputs should be invalid", returnType);
		
		// Test the non-child version.
		returnType = antIfFood.getReturnType(Void.class, Void.class);
		assertEquals("type should be Void for two Void inputs inputs", Void.class, returnType);
		
		returnType = antIfFood.getReturnType(Void.class, Void.class, Void.class);
		assertNull("too many Void inputs should be invalid", returnType);
		
		returnType = antIfFood.getReturnType(Ant.class, Void.class, Void.class);
		assertNull("inputs should not valid on this version of the function", returnType);
	}
}
