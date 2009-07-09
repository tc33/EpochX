/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epochx.representation;

import java.util.*;

import com.epochx.core.*;

/**
 * A <code>CandidateProgram</code> encapsulates an individual program within a 
 * generation of a GP run.
 * 
 * <p>Instances of CandidateProgram can be requested to evaluate themselves, 
 * which will trigger an evaluation of each <code>Node</code> and their child 
 * nodes recursively down the tree. As well as the program tree itself, each 
 * CandidateProgram allows the retrieval of meta-data about the program.
 * 
 */
public class CandidateProgram<TYPE> implements Cloneable, Comparable<CandidateProgram<TYPE>> {
	
	// The root node of the program tree.
	private Node<TYPE> rootNode;
	
	// The controlling model.
	private GPModel<TYPE> model;
	
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
	public CandidateProgram(Node<TYPE> rootNode, GPModel<TYPE> model) {
		this.model = model;
		this.rootNode = rootNode;
	}
	
	/**
	 * Evaluates the candidate program by triggering a recursive evaluation of 
	 * the node tree from the root.
	 * 
	 * @return The result of the evaluation of the program.
	 */
	public TYPE evaluate() {
		return rootNode.evaluate();
	}
	
	/**
	 * Returns the root node of the node tree held by the candidate program.
	 * 
	 * @return The root node of the node tree.
	 */
	public Node<TYPE> getRootNode() {
		return rootNode;
	}
	
	/**
	 * Returns the nth node in the <code>CandidateProgram</code>. The program 
	 * tree is traversed in pre-order (depth-first), indexed from 0 so that the 
	 * root node is at node 0.
	 * 
	 * @param n The index of the node required. Indexes are from zero.
	 * @return The node at the specified index.
	 */
	public Node<TYPE> getNthNode(int n) {
		int size = getProgramLength();
		
		if(n >= size)
			throw new IndexOutOfBoundsException("Index: "+n+", Size: "+size);			
		
		return rootNode.getNthNode(n);
	}
	
	/**
	 * Replaces the node at the specified position in the CandidateProgram with 
	 * the specified node.
	 * 
	 * @param n	index of the node to replace.
	 * @param newNode node to be set at the specified position.
	 */
	public void setNthNode(int n, Node<TYPE> newNode) {
		if (n == 0) {
			// Need to test is of type <TYPE> somehow really.
			rootNode = (Node<TYPE>) newNode;
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
	public List<Node<TYPE>> getNodesAtDepth(int depth) {
		return rootNode.getNodesAtDepth(depth);
	}
	
	/**
	 * Requests the controlling model to calculate the fitness of this 
	 * <code>CandidateProgram</code>.
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
	/*public static int getProgramDepth(CandidateProgram program) {
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
	 * Compares this program to another based upon fitness. Returns a negative 
	 * integer if this program has a larger (worse) fitness value, zero if they 
	 * have equal fitnesses and a positive integer if this program has a 
	 * smaller (better) fitness value.
	 * 
	 * This is super expensive if using to sort a list. Might be possible to 
	 * improve performance if we can implement caching of fitness within a 
	 * CandidateProgram.
	 * 
	 * @param o the CandidateProgram to be compared.
	 * @return a negative integer, zero, or a positive integer if this program 
	 * has a worse, equal or better fitness respectively. 
	 */
	@Override
	public int compareTo(CandidateProgram<TYPE> o) {
		double thisFitness = this.getFitness();
		double objFitness = o.getFitness();
		
		if (thisFitness > objFitness) {
			return -1;
		} else if (thisFitness == objFitness) {
			return 0;
		} else {
			return 1;
		}
	}
	
	/**
	 * Creates and returns a copy of this program. The clone includes a deep 
	 * copy of all the program nodes, so after calling this method none of the 
	 * clones nodes will refer to the same instance.
	 * 
	 * @return a clone of this CandidateProgram instance.
	 */
	@Override
	public Object clone() {
		CandidateProgram<TYPE> clone = null;
		try {
			clone = (CandidateProgram<TYPE>) super.clone();
		} catch (CloneNotSupportedException e) {
			// This shouldn't ever happen - if it does then everything is 
			// going to blow up anyway.
		}
		
		// Deep copy node tree.
		if (this.rootNode == null) {
			clone.rootNode = null;
		} else {
			clone.rootNode = (Node<TYPE>) this.rootNode.clone();
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
	 * This equals method compares the given object to this CandidateProgram 
	 * to determine if they are equal. Equivalence is defined by their both 
	 * being instances of CandidateProgram and them having equal program node 
	 * trees according to the equals methods of the root node.
	 * 
	 * @param obj an object to be compared for equivalence.
	 * @return true if this CandidateProgram is the same as the obj argument; 
	 * false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		return rootNode.equals(((CandidateProgram<TYPE>) obj).rootNode);
	}

}
