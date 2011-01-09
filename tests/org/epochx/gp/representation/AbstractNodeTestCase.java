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
package org.epochx.gp.representation;

import static org.junit.Assert.*;

import java.util.List;

import org.epochx.epox.*;
import org.junit.*;

/**
 * 
 */
public abstract class AbstractNodeTestCase {

	private Node node;

	public abstract Node getNode();

	@Before
	public void setUp() {
		node = getNode();

		// Ensure children are filled.
		final int arity = node.getArity();
		for (int i = 0; i < arity; i++) {
			node.setChild(i, new Literal(true));
		}
	}

	/**
	 * Test that getting an nth node of zero returns the node itself.
	 */
	@Test
	public void testGetNthNodeZero() {
		assertSame("node not its own 0th node", node, node.getNthNode(0));
	}

	/**
	 * Test that an index out of bounds exception is thrown when trying to get
	 * a node at a negative index.
	 */
	@Test
	public void testGetNthNodeNegativeIndex() {
		try {
			node.getNthNode(-1);
			fail("Exception not thrown for negative index");
		} catch (final IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Test that an index out of bounds exception is thrown when trying to get
	 * a node an index beyond the tree's length - 1.
	 */
	@Test
	public void testGetNthTooLarge() {
		try {
			node.getNthNode(node.getLength());
			fail("Exception not thrown for index >= length");
		} catch (final IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Test that an index out of bounds exception is thrown when trying to set
	 * a node at a negative index.
	 */
	@Test
	public void testSetNthNodeNegativeIndex() {
		try {
			node.setNthNode(-1, new Literal(false));
			fail("Exception not thrown for negative index");
		} catch (final IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Test that an index out of bounds exception is thrown when trying to set
	 * a node at an index beyond the tree's length - 1.
	 */
	@Test
	public void testSetNthNodeTooLarge() {
		try {
			node.setNthNode(node.getLength(), new Literal(false));
			fail("Exception not thrown for index >= length");
		} catch (final IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Test that an index out of bounds exception is thrown when trying to set
	 * a node at an index of zero. A node cannot replace itself.
	 */
	@Test
	public void testSetNthNodeZero() {
		try {
			node.setNthNode(0, new Literal(false));
			fail("Exception not thrown for index == 0, cannot replace self");
		} catch (final IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Tests that setting the nth node at a valid index does not throw an
	 * exception and the node is set correctly.
	 */
	@Test
	public void testSetNthNodeLast() {
		try {
			final Node newChild = new Literal(false);
			final int lastIndex = node.getLength() - 1;
			node.setNthNode(lastIndex, newChild);
			assertSame("nth node in node tree not set when n is the last node index", newChild, node.getNthNode(lastIndex));
		} catch (final Exception e) {
			fail("Exception thrown when setting last node in node tree");
		}
	}

	/**
	 * Test that an index out of bounds exception is thrown when trying to get
	 * nodes at a negative depth.
	 */
	@Test
	public void testGetNodesAtDepthNegative() {
		try {
			node.getNodesAtDepth(-1);
			fail("Exception not thrown for negative depth");
		} catch (final IndexOutOfBoundsException e) {
		}
	}

	/**
	 * Test that just the root node is returned when requesting nodes at depth
	 * 0.
	 */
	@Test
	public void testGetNodesAtDepthZero() {
		final List<Node> nodes = node.getNodesAtDepth(0);
		assertEquals("more than one node at depth zero", 1, nodes.size());
		assertSame("current node not returned for depth zero", node, nodes.get(0));
	}

	/**
	 * Test that an empty list is returned if retrieving nodes at a depth
	 * greater than the programs maximum depth.
	 */
	@Test
	public void testGetNodesAtDepthTooLarge() {
		try {
			final List<Node> nodes = node.getNodesAtDepth(node.getDepth() + 1);
			assertEquals("empty list not returned for nodes greater than maximum depth in program", 0, nodes.size());
		} catch (final IndexOutOfBoundsException e) {
			fail("Exception thrown for depth greater than the maximum depth");
		}
	}
}
