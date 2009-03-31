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
package com.epochx.core.representation;

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
	
	private Node<TYPE> rootNode;
	private GPModel<TYPE> model;
	
	/**
	 * Constructs a new program individual where <code>rootNode</code> is the 
	 * top most node in the program, and which may have 0 or more child nodes.
	 * 
	 * @param rootNode	the Node of the program tree which is the parent to 
	 * 					all other nodes. It may be either a FunctionNode or a 
	 * 					TerminalNode
	 */
	public CandidateProgram(Node<TYPE> rootNode, GPModel<TYPE> model) {
		this.model = model;
		this.rootNode = rootNode;
	}
	
	public TYPE evaluate() {
		return rootNode.evaluate();
	}
	
	@Override
	public String toString() {
		return rootNode.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return rootNode.equals(((CandidateProgram<TYPE>) obj).rootNode);
	}
	
	public Node<TYPE> getRootNode() {
		return rootNode;
	}
	
	public Node<?> getNthNode(int n) {
		int size = GPProgramAnalyser.getProgramLength(this);
		if(n > size)
			throw new IndexOutOfBoundsException("Index: "+n+", Size: "+size);			
		return rootNode.getNthNode(n);
	}
	
	public void setNthNode(Node<?> newNode, int n) {
		if (n == 0) {
			// Need to test is of type <TYPE> somehow really.
			rootNode = (Node<TYPE>) newNode;
		}
		int size = GPProgramAnalyser.getProgramLength(this);
		if(n > size)
			throw new IndexOutOfBoundsException("Index: "+n+", Size: "+size);		
		rootNode.setNthNode(newNode, n);
	}
	
	public List<Node<?>> getNodesAtDepth(int d) {
		return rootNode.getNodesAtDepth(d);
	}
	
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
	
	public double getFitness() {
		return model.getFitness(this);
	}
	
	/*
	 * This is super expensive if using to sort a list. Might be possible to 
	 * improve performance if we can implement caching of fitness within a 
	 * CandidateProgram.
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
}
