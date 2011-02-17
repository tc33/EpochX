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
package org.epochx.epox.lang;

import static org.junit.Assert.*;

import org.epochx.epox.*;
import org.epochx.interpret.*;
import org.junit.*;


/**
 * Unit tests for {@link org.epochx.epox.lang.Seq2Function}
 */
public class Seq2FunctionTest extends NodeTestCase {

	private Seq2Function seq2;
	private MockNode child1;
	private MockNode child2;
	
	private StringBuilder log;
	
	/**
	 * Part of test fixture for superclass.
	 */
	@Override
	public Node getNode() {
		return new Seq2Function();
	}
	
	/**
	 * Sets up the test environment.
	 */
	@Override
	public void setUp() {
		super.setUp();
		
		log = new StringBuilder();
		
		child1 = new MockNodeLogged();
		child2 = new MockNodeLogged();
		child1.setGetIdentifier("1");
		child2.setGetIdentifier("2");
		
		seq2 = new Seq2Function(child1, child2);

		super.setUp();
	}
	
	/**
	 * Tests that {@link org.epochx.epox.lang.Seq2Function#evaluate()} 
	 * correctly evaluates all children in sequential order.
	 */
	@Test
	public void testEvaluate() {
		// Check starting point is as it should be.
		assertSame("unit test broken", 0, child1.getEvaluateCount());
		assertSame("unit test broken", 0, child2.getEvaluateCount());
		
		seq2.evaluate();
		
		// Test all were evaluated.
		assertSame("all child nodes should be evaluated by SEQ2", 1, child1.getEvaluateCount());
		assertSame("all child nodes should be evaluated by SEQ2", 1, child2.getEvaluateCount());
		
		// Test the order of the events.
		assertEquals("SEQ2 child nodes should be evaluated in sequential order", "[evaluate 1][evaluate 2]", log.toString());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.lang.Seq2Function#getReturnType(Class...)}
	 * returns a Void type for the correct inputs, and <code>null</code>
	 * for incorrectly typed or incorrect number of inputs.
	 */
	@Test
	public void testGetReturnTypeSeq2() {
		Class<?> returnType = seq2.getReturnType(Void.class, Void.class);
		assertSame("unexpected return type", Void.class, returnType);
		
		returnType = seq2.getReturnType(Void.class, Number.class);
		assertNull("all children should be of Void type", returnType);
		
		returnType = seq2.getReturnType(Void.class);
		assertNull("too few inputs should be invalid", returnType);
		
		returnType = seq2.getReturnType(Void.class, Void.class, Void.class);
		assertNull("too many inputs should be invalid", returnType);
	}

	/**
	 * Test node class which logs evaluations.
	 */
	private class MockNodeLogged extends MockNode {
		@Override
		public Object evaluate() {
			log.append("[evaluate ");
			log.append(getIdentifier());
			log.append(']');
			
			return super.evaluate();
		}
	}
	
	/**
	 * Tests that this function can be parsed by the EpoxParser.
	 */
	@Test
	public void testEpoxParser() {
		EpoxParser parser = new EpoxParser();
		
		try {
			parser.declareVariable(new Variable("X", Void.class));
			Node n = parser.parse("SEQ2(X, X)");
			assertSame("Parsing did not return an instance of the correct node", Seq2Function.class, n.getClass());
		} catch (MalformedProgramException e) {
			fail("Malformed program exception thrown when parsing");
		}
	}
}
