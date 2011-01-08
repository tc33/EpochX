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
 * Unit tests for {@link org.epochx.epox.Node}
 */
public abstract class NodeTestCase {

	private Node node;
	private Node[] children;
	
	/**
	 * Sets up the test environment.
	 */
	@Before
	public void setUp() throws Exception {
		node = getNode();
		
		int arity = node.getArity();
		children = new Node[arity];
		for (int i=0; i<arity; i++) {
			children[i] = new MockNode();
			node.setChild(i, children[i]);
		}
	}

	/**
	 * Returns the <code>Node</code> instance to undergo testing. Each 
	 * successive call should return a new instance, setup in the same way as 
	 * much as possible, but in a different object.
	 * 
	 * @return an instance of a <code>Node</code> implementation.
	 */
	protected abstract Node getNode();
	
	/*
	 * Expands the test node so that it has a depth of two, by increasing the 
	 * arity of each child node and adding mock nodes with arity zero as 
	 * children to those nodes. Also, returns the newly added nodes.
	 */
	private List<Node> setupTreeDepthTwo() {
		List<Node> newNodes = new ArrayList<Node>();
		int arity = node.getArity();
		for (int i=0; i<arity; i++) {
			Node child = node.getChild(i);
			Node[] grandchildren = new Node[2];
			for (int j=0; j<2; j++) {
				Node grandchild = new MockNode();
				newNodes.add(grandchild);
				grandchildren[j] = grandchild;
			}
			child.setChildren(grandchildren);
		}
		return newNodes;
	}

	/**
	 * Tests that repeated calls to {@link org.epochx.epox.Node#hashCode()},
	 * return the same integer.
	 */
	@Test
	public void testHashCodeConsistent() {
		int hash = node.hashCode();
		
		assertEquals("hashcode not consistent", hash, node.hashCode());
		assertEquals("hashcode not consistent", hash, node.hashCode());
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getChild(int)} works for indexes of 
	 * zero up to arity-1.
	 */
	@Test
	public void testGetChild() {
		int arity = node.getArity();
		Node[] children = node.getChildren();
		
		for (int i=0; i<arity; i++) {
			Node child = node.getChild(i);
			
			assertSame("child does not match expected", children[i], child);
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getNthNode(int)} returns the correct
	 * nth node for n = index-1.
	 */
	@Test
	public void testGetNthNodeLast() {
		int length = node.getLength();
		
		Node expected = node;
		if (node.isFunction()) {
			expected = children[children.length-1];
		}
		
		assertSame("nth node does not match expected last", expected, node.getNthNode(length-1));
	}
	
	/**
	 * Tests {@link org.epochx.epox.Node#getNthNode(int)} returns the node 
	 * itself for n = 0.
	 */
	@Test
	public void testGetNthNodeZero() {
		assertSame("nth node does not match expected at n=0", node, node.getNthNode(0));
	}
	
	/**
	 * Tests {@link org.epochx.epox.Node#getNthNode(int)} throws an index out of
	 * bounds exception for a value of n out of the valid range: 0 <= n < length
	 */
	@Test
	public void testGetNthNodeBounds() {
		try {
			node.getNthNode(-1);
			fail("exception not thrown for node index of -1");
		} catch (IndexOutOfBoundsException expected) {
			assertTrue(true);
		}
		
		try {
			int length = node.getLength();
			node.getNthNode(length);
			fail("exception not thrown for node index of length");
		} catch (IndexOutOfBoundsException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#setNthNode(int, Node)} correctly sets 
	 * the node at n = index-1.
	 */
	@Test
	public void testSetNthNodeLast() {
		int length = node.getLength();
		
		// Only perform test if there are nodes other than self to set.
		if (length > 1) {
			Node mockNode = new MockNode();
			
			node.setNthNode(length-1, mockNode);
			
			assertSame("node not set correctly at nth position", mockNode, node.getNthNode(length-1));
		}
	}
	
	/**
	 * Tests {@link org.epochx.epox.Node#setNthNode(int, Node)} throws an index 
	 * out of bounds exception for a value of n out of the valid 
	 * range: 0 < n < length
	 */
	@Test
	public void testSetNthNodeBounds() {
		try {
			node.setNthNode(0, node);
			fail("exception not thrown for node index of 0");
		} catch (IndexOutOfBoundsException expected) {
			assertTrue(true);
		}
		
		try {
			int length = node.getLength();
			node.setNthNode(length, node);
			fail("exception not thrown for node index of length");
		} catch (IndexOutOfBoundsException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getNthFunctionNodeIndex(int)} returns 
	 * the correct index of the last function node. 
	 */
	@Test
	public void testGetNthFunctionNodeIndexLast() {
		// Only applies to function node roots. Terminals should cause an exception.
		if (node.isFunction()) {
			int lastFunction = node.getNoFunctions() - 1;
			int expectedLastIndex = getLastFunctionNodeIndex();
			
			int lastIndex = node.getNthFunctionNodeIndex(lastFunction);
			
			assertSame("last function node's index is not as expected", expectedLastIndex, lastIndex);
		}
	}
	
	/*
	 * We can easily (although slowly) calculate the last function node to save
	 * us having to ask each child what it should be.
	 */
	private int getLastFunctionNodeIndex() {
		int length = node.getLength();
		
		// Work backwards through the nodes.
		for (int i=length-1; i>=0; i--) {
			Node n = node.getNthNode(i);
			
			if (n.isFunction()) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Tests {@link org.epochx.epox.Node#getNthFunctionNodeIndex(int)} returns 
	 * the correct index of the first function node, which should always be at
	 * index zero for a non-terminal root. 
	 */
	@Test
	public void testGetNthFunctionNodeIndexFirst() {
		// Only applies to function node roots. Terminals should cause an exception.
		if (node.isFunction()) {		
			int lastIndex = node.getNthFunctionNodeIndex(0);
			
			assertSame("first function node's index is not as expected", 0, lastIndex);
		}
	}
	
	/**
	 * Tests {@link org.epochx.epox.Node#getNthFunctionNodeIndex(int)} throws an
	 * exception for function indexes out of bounds.
	 */
	@Test
	public void testGetNthFunctionNodeIndexBounds() {
		// Test index -1.
		try {
			node.getNthFunctionNodeIndex(-1);
			fail("exception not thrown for function node at index -1");
		} catch (IndexOutOfBoundsException expected) {
			assertTrue(true);
		}
		
		// Test index noFunctions. This will also test terminals with index of 0.
		try {
			node.getNthFunctionNodeIndex(node.getNoFunctions());
			fail("exception not thrown for function node at index length");
		} catch (IndexOutOfBoundsException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getNthTerminalNodeIndex(int)} returns
	 * zero for a the 0th terminal when called upon a terminal.
	 */
	@Test
	public void testGetNthTerminalNodeIndexFirst() {
		int expectedFirstIndex = 0;
		if (node.isFunction()) {		
			expectedFirstIndex = getFirstTerminalNodeIndex();
		}
		int firstIndex = node.getNthTerminalNodeIndex(0);
		
		assertSame("first terminal node's index is not as expected", expectedFirstIndex, firstIndex);
	}
	
	/*
	 * We can easily (although slowly) calculate the first terminal node to save
	 * us having to ask each child what it should be.
	 */
	private int getFirstTerminalNodeIndex() {
		int length = node.getLength();
		
		// Work backwards through the nodes.
		for (int i=0; i<length; i++) {
			Node n = node.getNthNode(i);
			
			if (n.isTerminal()) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Tests {@link org.epochx.epox.Node#getNthTerminalNodeIndex(int)} throws an
	 * exception for terminal indexes out of bounds.
	 */
	@Test
	public void testGetNthTerminalNodeIndexBounds() {
		// Test index -1.
		try {
			node.getNthTerminalNodeIndex(-1);
			fail("exception not thrown for terminal node at index -1");
		} catch (IndexOutOfBoundsException expected) {
			assertTrue(true);
		}
		
		// Test index noTerminals.
		try {
			node.getNthTerminalNodeIndex(node.getNoTerminals());
			fail("exception not thrown for terminal node at index length");
		} catch (IndexOutOfBoundsException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getNodesAtDepth(int)} with a depth 
	 * parameter of zero returns a list containing only one element which is the
	 * test node itself.
	 */
	@Test
	public void testGetNodesAtDepthZero() {
		List<Node> nodes = node.getNodesAtDepth(0);
		
		assertSame("only one node expected at depth zero", 1, nodes.size());
		assertSame("only node at depth zero should be the root node", node, nodes.get(0));
	}
	
	/**
	 * Tests {@link org.epochx.epox.Node#getNodesAtDepth(int)} with a depth 
	 * parameter of two returns a list containing only those children at depth
	 * two.
	 */
	@Test
	public void testGetNodesAtDepthTwo() {
		// Is only applicable to function nodes.
		if (node.isFunction()) {
			// Set the nodes to create a tree of depth 2.
			List<Node> expectedNodes = setupTreeDepthTwo();
			List<Node> nodes = node.getNodesAtDepth(2);
			
			assertEquals("unexpected nodes found at depth two", expectedNodes, nodes);
		}
	}
	
	/**
	 * Tests {@link org.epochx.epox.Node#getNodesAtDepth(int)} throws an
	 * exception for depths out of bounds.
	 */
	@Test
	public void testGetNodesAtDepthBounds() {
		// Test depth -1.
		try {
			node.getNodesAtDepth(-1);
			fail("exception not thrown for nodes at depth -1");
		} catch (IndexOutOfBoundsException expected) {
			assertTrue(true);
		}
		
		// Test depth+1
		try {
			node.getNodesAtDepth(node.getDepth()+1);
			fail("exception not thrown for nodes at depth depth+1");
		} catch (IndexOutOfBoundsException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#setChild(int, org.epochx.epox.Node)}
	 * correctly sets the child at the specified index.
	 */
	@Test
	public void testSetChild() {
		if (node.isFunction()) {
			Node newNode = new MockNode();
			int index = node.getArity()-1;
			node.setChild(index, newNode);
			
			assertSame("child nodes not being set correctly", newNode, node.getChild(index));
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getNoTerminals()} returns the correct
	 * number of terminals, which for the test node should be equal to the 
	 * arity if it is a function, or 1 otherwise.
	 */
	@Test
	public void testGetNoTerminals() {
		if (node.isFunction()) {
			assertSame("incorrect number of terminals", node.getArity(), node.getNoTerminals());
		} else {
			assertSame("incorrect number of terminals", 1, node.getNoTerminals());
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getNoDistinctTerminals()} returns the 
	 * correct number of terminals, which for the test node should always be 
	 * one.
	 */
	@Test
	public void testGetNoDistinctTerminals() {
		assertSame("incorrect count of distinct terminals", 1, node.getNoDistinctTerminals());
		
		if (node.isFunction()) {
			// Update the identifiers so they're different, and count again.
			for (int i=0; i<node.getArity(); i++) {
				MockNode mockChild = (MockNode) node.getChild(i);
				mockChild.setGetIdentifier("ID" + i);
			}
			
			assertSame("incorrect count of distinct terminals", node.getArity(), node.getNoDistinctTerminals());
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getTerminalNodes()} returns only 
	 * terminal nodes, and all the terminal nodes.
	 */
	@Test
	public void testGetTerminalNodes() {
		// Change node to a depth 2.
		List<Node> expectedTerminalNodes = setupTreeDepthTwo();
		List<Node> terminalNodes = node.getTerminalNodes();
		
		if (node.isTerminal()) {
			assertSame("incorrect number of terminal nodes returned", 1, terminalNodes.size());
			assertSame("terminal nodes does not include root terminal", node, terminalNodes.get(0));
		} else {
			assertEquals("terminal nodes does not include expected terminals", expectedTerminalNodes, terminalNodes);
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getNoFunctions()} returns the correct
	 * number of functions.
	 */
	@Test
	public void testGetNoFunctions() {
		if (node.isTerminal()) {
			assertSame("incorrect number of function nodes in a terminal", 0, node.getNoFunctions());
		} else {
			// Change node to a depth 2.
			setupTreeDepthTwo();
			
			int expected = 1 + node.getArity();
			assertSame("incorrect number of function nodes", expected, node.getNoFunctions());
		}
		
		
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getNoDistinctFunctions()} returns the 
	 * correct number of functions.
	 */
	@Test
	public void testGetNoDistinctFunctions() {
		if (node.isTerminal()) {
			assertSame("incorrect count of distinct functions", 0, node.getNoDistinctFunctions());
		} else {
			assertSame("incorrect count of distinct functions", 1, node.getNoDistinctFunctions());
			
			// Change node to a depth 2.
			setupTreeDepthTwo();
			
			// Root plus the mock nodes.
			assertSame("incorrect count of distinct functions", 2, node.getNoDistinctFunctions());
			
			// Update the identifiers so they're different, and count again.
			for (int i=0; i<node.getArity(); i++) {
				MockNode mockChild = (MockNode) node.getChild(i);
				mockChild.setGetIdentifier("ID" + i);
			}
			int expected = 1 + node.getArity();
			assertSame("incorrect count of distinct functions", expected, node.getNoDistinctFunctions());
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getDepth()} returns the correct tree
	 * depth.
	 */
	@Test
	public void testGetDepth() {
		if (node.isTerminal()) {
			assertSame("incorrect tree depth", 0, node.getDepth());
		} else {
			assertSame("incorrect tree depth", 1, node.getDepth());
			
			// Change node to a depth 2.
			setupTreeDepthTwo();
			
			assertSame("incorrect tree depth", 2, node.getDepth());
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getLength()} returns the correct count
	 * of the number of nodes in the tree.
	 */
	@Test
	public void testGetLength() {
		int expected = 1+node.getArity();
		assertSame("incorrect tree length", expected, node.getLength());
		
		// Change node to a depth 2.
		setupTreeDepthTwo();
		
		expected = expected + (node.getArity() * 2);
		assertSame("incorrect tree length", expected, node.getLength());
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getReturnType()} returns 
	 * <code>null</code> for null children if the node is a function and tests
	 * that it returns non-null if it is a terminal.
	 */
	@Test
	public void testGetReturnType() {
		if (node.isFunction()) {
			node.setChild(0, null);
			
			assertNull("return type should be null if any children unset", node.getReturnType());
		} else {
			assertNotNull("return type should not ever be null for a terminal", node.getReturnType());
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#getReturnType(java.lang.Class<?>[])}
	 * returns a non-null type for a terminal.
	 */
	@Test
	public void testGetReturnTypeArray() {
		if (node.isTerminal()) {
			assertNotNull("return type should not ever be null for a terminal", node.getReturnType(new Class<?>[0]));
		}
	}
	
	/**
	 * Tests {@link org.epochx.epox.Node#getReturnType(java.lang.Class<?>[])}
	 * throws an exception if the number of inputs does not match the arity.
	 */
	@Test
	public void testGetReturnTypeArrayArity() {
		Class<?>[] inputTypes = new Class<?>[node.getArity()+1];
		
		try {
			node.getReturnType(inputTypes);
			fail("exception should be thrown for incorrect number of input types");
		} catch (IllegalArgumentException expected) {
			assertTrue(true);
		}
	}
	
	/**
	 * Tests {@link org.epochx.epox.Node#clone()} results in a node which is 
	 * equal to the original according to the equals contract, and that the 
	 * children are cloned also.
	 */
	@Test
	public void testClone() {
		Node clone = node.clone();
		
		assertEquals("cloned nodes should be equal to the original node", node, clone);
		
		for (int i=0; i<node.getArity(); i++) {
			// Children should be equal but not the same (for some they could 
			// be, but the children are MockNodes, hence we are testing that
			// each child's clone method gets called.
			assertEquals("the children of cloned nodes should be equal the original nodes children", children[i], clone.getChild(i));
			assertNotSame("cloned nodes should also clone their children", children[i], clone.getChild(i));
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#newInstance()} returns an instance of 
	 * the same type as the original node, and that any children are 
	 * <code>null</code>.
	 */
	@Test
	public void testNewInstance() {
		Node newInstance = node.newInstance();
		
		for (int i=0; i<node.getArity(); i++) {
			assertNull("any child nodes of new instances should be null", newInstance.getChild(i));
			assertEquals("new instances should be made of the same type as the original", node.getClass(), newInstance.getClass());
		}
	}

	/**
	 * Tests {@link org.epochx.epox.Node#equals(java.lang.Object)} for 
	 * reflexivity and against nulls.
	 */
	@Test
	public void testEquals() {
		// Test reflexive.
		assertTrue("equals is not reflexive", node.equals(node));
		// Test against null.
		assertFalse("node not equal to null", node.equals(null));
	}

	/**
	 * Tests {@link org.epochx.epox.Node#equals(java.lang.Object)} returns 
	 * <code>false</code> if arity of two nodes is different.
	 */
	@Test
	public void testEqualsArity() {
		Node comparison = getNode();
		
		assertNotSame("unit test broken", node, comparison);
		
		// No need to test this for a terminal, we assume arity does not change.
		if (node.isFunction()) {
			comparison.setChildren(new Node[node.getArity()+1]);
			
			assertFalse("nodes not equal if have different arities", node.equals(comparison));
			assertFalse("nodes not equal if have different arities", comparison.equals(node));
		}
	}
}
