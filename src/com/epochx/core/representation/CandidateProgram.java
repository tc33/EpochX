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
public class CandidateProgram<TYPE> {
	
	private Node<TYPE> rootNode;
	
	/**
	 * Constructs a new program individual where <code>rootNode</code> is the 
	 * top most node in the program, and which may have 0 or more child nodes.
	 * 
	 * @param rootNode	the Node of the program tree which is the parent to 
	 * 					all other nodes. It may be either a FunctionNode or a 
	 * 					TerminalNode
	 */
	public CandidateProgram(Node<TYPE> rootNode) {
		this.rootNode = rootNode;
	}
	
	// TODO Evaluation Code
	
}
