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
package org.epochx.epox;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;


/**
 * Unit tests for {@link org.epochx.epox.NodeUtils}
 */
public class NodeUtilsTest {

	/**
	 * Sets up the test environment.
	 */
	@Before
	public void setUp() {
	}

	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#getTerminals(java.util.List)}
	 * throws an exception for a null syntax.
	 */
	@Test
	public void testGetTerminals() {
		List<Node> syntax = new ArrayList<Node>();
		MockNode child1 = new MockNode();
		MockNode child2 = new MockNode();
		MockNode child3 = new MockNode();
		
		child1.setChildren(new Node[]{null});
		
		syntax.add(child1);
		syntax.add(child2);
		syntax.add(child3);
		
		List<Node> terminals = NodeUtils.getTerminals(syntax);
		assertSame("unexpected number of terminals returned", 2, terminals.size());
		assertTrue("terminal missing from list of terminals", terminals.contains(child2));
		assertTrue("terminal missing from list of terminals", terminals.contains(child3));
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#getTerminals(java.util.List)}
	 * returns an empty list for an empty syntax.
	 */
	@Test
	public void testGetTerminalsEmpty() {
		List<Node> terminals = NodeUtils.getTerminals(new ArrayList<Node>());
		assertNotNull("An empty list should be returned for an empty syntax", terminals);	
		assertTrue("An empty list should be returned for an empty syntax", terminals.isEmpty());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#getTerminals(java.util.List)}
	 * throws an exception for a null syntax.
	 */
	@Test
	public void testGetTerminalsNull() {
		try {
			NodeUtils.getTerminals(null);
			fail("An exception should be thrown for a null syntax");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#getFunctions(java.util.List)}
	 * throws an exception for a null syntax.
	 */
	@Test
	public void testGetFunctions() {
		List<Node> syntax = new ArrayList<Node>();
		
		MockNode child1 = new MockNode();
		child1.setChildren(new Node[]{null});
		
		syntax.add(child1);
		syntax.add(new MockNode());
		syntax.add(new MockNode());
		
		List<Node> functions = NodeUtils.getFunctions(syntax);
		assertSame("unexpected number of functions returned", 1, functions.size());
		assertTrue("function missing from list of functions", functions.contains(child1));
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#getFunctions(java.util.List)}
	 * returns an empty list for an empty syntax.
	 */
	@Test
	public void testGetFunctionsEmpty() {
		List<Node> functions = NodeUtils.getFunctions(new ArrayList<Node>());
		assertNotNull("An empty list should be returned for an empty syntax", functions);	
		assertTrue("An empty list should be returned for an empty syntax", functions.isEmpty());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#getFunctions(java.util.List)}
	 * throws an exception for a null syntax.
	 */
	@Test
	public void testGetFunctionsNull() {
		try {
			NodeUtils.getFunctions(null);
			fail("An exception should be thrown for a null syntax");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#intRange(int, int, int)}.
	 */
	@Test
	public void testIntRangeNegQuantity() {
		try {
			NodeUtils.intRange(0, 1, -1);
			fail("exception should have been thrown for a negative quantity");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#intRange(int, int, int)} 
	 * returns an empty list for a quantity of zero.
	 */
	@Test
	public void testIntRangeEmpty() {
		List<Literal> literals = NodeUtils.intRange(0, 1, 0);

		assertTrue("a quantity of 0 should return an empty list", literals.isEmpty());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#intRange(int, int, int)} 
	 * returns a list of literals with the correct values.
	 */
	@Test
	public void testIntRange() {
		List<Literal> literals = NodeUtils.intRange(0, 2, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Integer[]{0, 2, 4}));
		
		literals = NodeUtils.intRange(-3, 3, 4);
		assertTrue("unexpected literal values", literalsEqual(literals, new Integer[]{-3, 0, 3, 6}));
		
		literals = NodeUtils.intRange(3, -2, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Integer[]{3, 1, -1}));
		
		literals = NodeUtils.intRange(-3, -2, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Integer[]{-3, -5, -7}));
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#longRange(long, long, int)}.
	 */
	@Test
	public void testLongRangeNegQuantity() {
		try {
			NodeUtils.longRange(0L, 1L, -1);
			fail("exception should have been thrown for a negative quantity");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#longRange(long, long, int)} 
	 * returns an empty list for a quantity of zero.
	 */
	@Test
	public void testLongRangeEmpty() {
		List<Literal> literals = NodeUtils.longRange(0L, 1L, 0);

		assertTrue("a quantity of 0 should return an empty list", literals.isEmpty());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#longRange(long, long, int)} 
	 * returns a list of literals with the correct values.
	 */
	@Test
	public void testLongRange() {
		List<Literal> literals = NodeUtils.longRange(0L, 2L, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Long[]{0L, 2L, 4L}));
		
		literals = NodeUtils.longRange(-3L, 3L, 4);
		assertTrue("unexpected literal values", literalsEqual(literals, new Long[]{-3L, 0L, 3L, 6L}));
		
		literals = NodeUtils.longRange(3L, -2L, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Long[]{3L, 1L, -1L}));
		
		literals = NodeUtils.longRange(-3L, -2L, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Long[]{-3L, -5L, -7L}));
	}

	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#doubleRange(double, double, int)}.
	 */
	@Test
	public void testDoubleRangeNegQuantity() {
		try {
			NodeUtils.doubleRange(0.0, 0.1, -1);
			fail("exception should have been thrown for a negative quantity");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#doubleRange(double, double, int)} 
	 * returns an empty list for a quantity of zero.
	 */
	@Test
	public void testDoubleRangeEmpty() {
		List<Literal> literals = NodeUtils.doubleRange(0.0, 0.1, 0);

		assertTrue("a quantity of 0 should return an empty list", literals.isEmpty());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#doubleRange(double, double, int)} 
	 * returns a list of literals with the correct values.
	 */
	@Test
	public void testDoubleRange() {
		List<Literal> literals = NodeUtils.doubleRange(0.0, 1.23, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Double[]{0.0, 1.23, 2.46}));
		
		literals = NodeUtils.doubleRange(-0.9, 2.0, 4);
		assertTrue("unexpected literal values", literalsEqual(literals, new Double[]{-0.9, 1.1, 3.1, 5.1}));
		
		literals = NodeUtils.doubleRange(2.5, -0.9, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Double[]{2.5, 1.6, 0.7}));
		
		literals = NodeUtils.doubleRange(-3.3, -2.1, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Double[]{-3.3, -5.4, -7.5}));
	}

	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#floatRange(float, float, int)}.
	 */
	@Test
	public void testFloatRangeNegQuantity() {
		try {
			NodeUtils.floatRange(0.0f, 0.1f, -1);
			fail("exception should have been thrown for a negative quantity");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#floatRange(float, float, int)} 
	 * returns an empty list for a quantity of zero.
	 */
	@Test
	public void testFloatRangeEmpty() {
		List<Literal> literals = NodeUtils.floatRange(0.0f, 0.1f, 0);

		assertTrue("a quantity of 0 should return an empty list", literals.isEmpty());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#floatRange(float, float, int)} 
	 * returns a list of literals with the correct values.
	 */
	@Test
	public void testFloatRange() {
		List<Literal> literals = NodeUtils.floatRange(0.0f, 1.23f, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Float[]{0.0f, 1.23f, 2.46f}));
		
		literals = NodeUtils.floatRange(-0.9f, 2.0f, 4);
		assertTrue("unexpected literal values", literalsEqual(literals, new Float[]{-0.9f, 1.1f, 3.1f, 5.1f}));
		
		literals = NodeUtils.floatRange(2.2f, -1.1f, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Float[]{2.2f, 1.1f, 0.0f}));
		
		literals = NodeUtils.floatRange(-3.3f, -2.2f, 3);
		assertTrue("unexpected literal values", literalsEqual(literals, new Float[]{-3.3f, -5.5f, -7.7f}));
	}

	/*
	 * Helper method to test whether the given list of literals have values 
	 * which are equal to the array of values given, in the same positions.
	 */
	private boolean literalsEqual(List<Literal> literals, Object[] values) {
		if (literals.size() != values.length) {
			return false;
		}
		
		for (int i=0; i<values.length; i++) {
			if (!literals.get(i).getValue().equals(values[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#createVariables(java.lang.Class, java.lang.String[])}
	 * throws an exception for a null variableNames parameter.
	 */
	@Test
	public void testCreateVariablesNull() {
		try {
			NodeUtils.createVariables(Boolean.class, (String[]) null);
			fail("exception should have been thrown for null variableNames");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#createVariables(java.lang.Class, java.lang.String[])}
	 * returns an empty list for no variableNames.
	 */
	@Test
	public void testCreateVariablesEmpty() {
		List<Variable> vars = NodeUtils.createVariables(Boolean.class);

		assertTrue("a quantity of 0 should return an empty list", vars.isEmpty());
	}
	
	/**
	 * Tests that {@link org.epochx.epox.NodeUtils#createVariables(java.lang.Class, java.lang.String[])}
	 * returns a list of variables with the correct name and data-type.
	 */
	@Test
	public void testCreateVariables() {
		Class<?> datatype = Boolean.class;
		String[] varNames = {"A", "B", "C"};
		List<Variable> vars = NodeUtils.createVariables(datatype, varNames);
		
		assertSame("wrong number of variables returned", 3, vars.size());
		for (int i=0; i<3; i++) {
			assertEquals("unexpected variable names", varNames[i], vars.get(i).getIdentifier());
			assertSame("unexpected variable names", datatype, vars.get(i).getReturnType());
		}
	}
}
