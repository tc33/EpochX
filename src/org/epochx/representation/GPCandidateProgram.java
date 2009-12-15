/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.representation;

import java.util.*;

import org.epochx.core.*;


/**
 * A <code>GPCandidateProgram</code> encapsulates an individual program within a 
 * generation of a GP run.
 * 
 * <p>Instances of GPCandidateProgram can be requested to evaluate themselves, 
 * which will trigger an evaluation of each <code>Node</code> and their child 
 * nodes recursively down the tree. As well as the program tree itself, each 
 * GPCandidateProgram allows the retrieval of meta-data about the program.
 */
public class GPCandidateProgram extends CandidateProgram {
	
	// The root node of the program tree.
	private Node rootNode;
	
	// The controlling model.
	private GPModel model;
	
	/**
	 * Constructs a new program individual where <code>rootNode</code> is the 
	 * top most node in the program, and which may have 0 or more child nodes.
	 * 
	 * @param rootNode	the Node of the program tree which is the parent to 
	 * 					all other nodes. It may be either a FunctionNode or a 
	 * 					TerminalNode
	 * @param model the controlling model which provides the configuration 
	 * 				parameters for the run. 				
	 */
	public GPCandidateProgram(Node rootNode, GPModel model) {
		this.model = model;
		this.rootNode = rootNode;
	}
	
	/**
	 * Evaluates the candidate program by triggering a recursive evaluation of 
	 * the node tree from the root.
	 * 
	 * @return The result of the evaluation of the program.
	 */
	public Object evaluate() {
		return rootNode.evaluate();
	}
	
	/**
	 * Returns the root node of the node tree held by the candidate program.
	 * 
	 * @return The root node of the node tree.
	 */
	public Node getRootNode() {
		return rootNode;
	}
	
	/**
	 * Returns the nth node in the <code>GPCandidateProgram</code>. The program 
	 * tree is traversed in pre-order (depth-first), indexed from 0 so that the 
	 * root node is at node 0.
	 * 
	 * @param n The index of the node required. Indexes are from zero.
	 * @return The node at the specified index.
	 */
	public Node getNthNode(int n) {
		int size = getProgramLength();
		
		if(n >= size)
			throw new IndexOutOfBoundsException("Index: "+n+", Size: "+size);			
		
		return rootNode.getNthNode(n);
	}
	
	/**
	 * Replaces the node at the specified position in the GPCandidateProgram with 
	 * the specified node.
	 * 
	 * @param n	index of the node to replace.
	 * @param newNode node to be set at the specified position.
	 */
	public void setNthNode(int n, Node newNode) {
		if (n == 0) {
			// Need to test is of type  somehow really.
			rootNode = (Node) newNode;
		}
		
		// Check the index is within bounds.
		int size = getProgramLength();
		if(n >= size)
			throw new IndexOutOfBoundsException("Index: "+n+", Size: "+size);		
		
		rootNode.setNthNode(n, newNode);
	}
	
	/**
	 * Retrieves all the nodes in the program tree at a specified depth.
	 * 
	 * @param depth the specified depth of the nodes.
	 * @return a List of all the nodes at the specified depth.
	 */
	public List<Node> getNodesAtDepth(int depth) {
		return rootNode.getNodesAtDepth(depth);
	}
	
	/**
	 * Requests the controlling model to calculate the fitness of this 
	 * <code>GPCandidateProgram</code>.
	 * 
	 * @return the fitness of this candidate program according to the model.
	 */
	public double getFitness() {
		return model.getFitness(this);
	}
	
	/**
	 * Returns a count of how many terminal nodes are in the program tree.
	 * 
	 * @return the number of terminal nodes in this program.
	 */
	public int getNoTerminals() {
		return getRootNode().getNoTerminals();
	}
	
	/**
	 * Returns a count of how many unique terminal nodes are in the program 
	 * tree.
	 * 
	 * @return the number of unique terminal nodes in this program.
	 */
	public int getNoDistinctTerminals() {
		return getRootNode().getNoDistinctTerminals();
	}
	
	/**
	 * Returns a count of how many function nodes are in the program tree.
	 * 
	 * @return the number of function nodes in this program.
	 */
	public int getNoFunctions() {
		return getRootNode().getNoFunctions();
	}
	
	/**
	 * Returns a count of how many unique function nodes are in the program 
	 * tree.
	 * 
	 * @return the number of unique function nodes in this program.
	 */
	public int getNoDistinctFunctions() {
		return getRootNode().getNoDistinctFunctions();
	}
	
	/**
	 * Returns the depth of deepest node in the program tree.
	 * 
	 * @return the depth of the program.
	 */
	public int getProgramDepth() {
		return getRootNode().getDepth();
	}

	/*
	 * ALTERNATIVE IMPLEMENTATION
	 * Determines the maximum depth of a program.
	 * @param program
	 * @return
	 */
	/*public static int getProgramDepth(GPCandidateProgram program) {
        // Flatten the tree.
		String flatProg = program.toString();
		
		int count = 0;
        int maxDepth = 0;
        // count by brackets
        for(int i=0; i<flatProg.length(); i++) {
            char c = flatProg.charAt(i);
        	if(c == '(') {
                count++;
                if(count>maxDepth) {
                    maxDepth = count;
                }
            }
            if(c == ')') {
                count--;
            }
        }
        return maxDepth;
	}*/
	
	/**
	 * Returns the number of nodes in the program tree.
	 * 
	 * @return the number of nodes in the program tree.
	 */
	public int getProgramLength() {
		return getRootNode().getLength();
	}
	
    /*
     * ALTERNATIVE IMPLEMENTATION
     * Calculates the length - that is the number of nodes - of the program.
     * @param prog The program to be measured
     * @return The length of the program
     */
    /*public static int getProgramLength(Node rootNode) {
        // Flatten tree and split at spaces or brackets.
    	String[] flatTree = rootNode.toString().split("(\\s|\\(|\\))+");
    	
    	// Count how many tokens there are.
    	return flatTree.length;
    }*/
	
	/**
	 * Creates and returns a copy of this program. The clone includes a deep 
	 * copy of all the program nodes, so after calling this method none of the 
	 * clones nodes will refer to the same instance.
	 * 
	 * @return a clone of this GPCandidateProgram instance.
	 */
	@Override
	public GPCandidateProgram clone() {
		GPCandidateProgram clone = (GPCandidateProgram) super.clone();
		
		// Deep copy node tree.
		if (this.rootNode == null) {
			clone.rootNode = null;
		} else {
			clone.rootNode = (Node) this.rootNode.clone();
		}
			
		// Shallow copy the model.
		clone.model = this.model;
		
		return clone;
	}
	
	/**
	 * Return a string representation of the program node tree.
	 * 
	 * @return a string representation of this program instance.
	 */
	@Override
	public String toString() {
		return rootNode.toString();
	}
	
	/**
	 * This equals method compares the given object to this GPCandidateProgram 
	 * to determine if they are equal. Equivalence is defined by their both 
	 * being instances of GPCandidateProgram and them having equal program node 
	 * trees according to the equals methods of the root node.
	 * 
	 * @param obj an object to be compared for equivalence.
	 * @return true if this GPCandidateProgram is the same as the obj argument; 
	 * false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		return rootNode.equals(((GPCandidateProgram) obj).rootNode);
	}

}
