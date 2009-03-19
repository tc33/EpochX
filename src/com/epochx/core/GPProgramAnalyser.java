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
package com.epochx.core;

import java.util.*;

import com.epochx.core.representation.*;

/**
 * 
 */
public class GPProgramAnalyser {
	
	public static int getNoTerminals(CandidateProgram<?> program) {
		return getNoTerminals(program.getRootNode());
	}
	
	public static int getNoTerminals(Node<?> n) {
		if (n instanceof TerminalNode) {
			return 1;
		} else {
			int result = 0;
			for (int i=0; i<n.getArity(); i++) {
				result += getNoTerminals(n.getChild(i));
			}
			return result;
		}
	}
	
	public static int getNoDistinctTerminals(CandidateProgram<?> program) {
		return getNoDistinctTerminals(program.getRootNode());
	}
	
	public static int getNoDistinctTerminals(Node<?> n) {
		// Get a list of terminals.
		List<TerminalNode<?>> terminals = getTerminalNodes(n);
		
		// Remove duplicates.
		HashSet terminalHash = new HashSet(terminals);
		
		// The number left is how many distinct terminals.
		return terminalHash.size();
	}
	
	public static List<TerminalNode<?>> getTerminalNodes(Node<?> n) {
		// Alternatively we could use an array, which is quicker/more efficient in this situation?
		List<TerminalNode<?>> terminals = new ArrayList<TerminalNode<?>>();
		
		if (n instanceof TerminalNode) {
			terminals.add((TerminalNode<?>) n);
		} else {
			for (int i=0; i<n.getArity(); i++) {
				terminals.addAll(getTerminalNodes(n.getChild(i)));
			}
		}
		return terminals;
	}
	
	public static int getNoFunctions(CandidateProgram<?> program) {
		return getNoFunctions(program.getRootNode());
	}
	
	public static int getNoFunctions(Node<?> n) {
		if (n instanceof TerminalNode) {
			return 0;
		} else {
			int result = 1;
			for (int i=0; i<n.getArity(); i++) {
				result += getNoFunctions(n.getChild(i));
			}
			return result;
		}
	}
	
	public static int getNoDistinctFunctions(CandidateProgram<?> program) {
		return getNoDistinctFunctions(program.getRootNode());
	}
	
	public static int getNoDistinctFunctions(Node<?> n) {
		// Get a list of functions.
		List<FunctionNode<?>> functions = getFunctionNodes(n);
		
		// Remove duplicates - where a duplicate is a function of the same type.
		// We cannot use the FunctionNode's equals function because that will compare children too.
		List<String> functionNames = new ArrayList<String>();
		for (FunctionNode<?> f: functions) {
			String name = f.getFunctionName();
			if (!functionNames.contains(name)) {
				functionNames.add(name);
			}
		}
		
		// The number left is how many distinct functions.
		return functionNames.size();
	}
	
	public static List<FunctionNode<?>> getFunctionNodes(Node<?> n) {
		// Alternatively we could use an array, which is quicker/more efficient in this situation?
		List<FunctionNode<?>> functions = new ArrayList<FunctionNode<?>>();
		
		if (n instanceof TerminalNode) {
			// No more function nodes to count, return empty list.
		} else {
			if (n instanceof FunctionNode<?>) {
				functions.add((FunctionNode<?>) n);
			}
			for (int i=0; i<n.getArity(); i++) {
				functions.addAll(getFunctionNodes(n.getChild(i)));
			}
		}
		return functions;
	}
	
	public static int getProgramDepth(CandidateProgram<?> program) {
		Node<?> rootNode = program.getRootNode();
		// set depth and current depth to zero
		int currentDepth = 0;
		int depth = 0;
		depth = GPProgramAnalyser.countDepth(rootNode, (currentDepth + 1), depth);
		return depth;
	}
		
	private static int countDepth(Node<?> rootNode, int currentDepth, int depth) {
		// set current depth to maximum if need be
		if(currentDepth>depth) {
			depth = currentDepth;
		}
		// get children and recurse
		int arity = rootNode.getArity();
		if(arity>0) {
			for(int i = 0; i<arity; i++) {
				Node<?> childNode = rootNode.getChild(i);
				depth = GPProgramAnalyser.countDepth(childNode, (currentDepth + 1), depth);
			}
		}
		return depth;
	}
	
	/**
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
	
	public static int getProgramLength(CandidateProgram program) {
		return getProgramLength(program.getRootNode());
	}
	
    /**
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
	
	public static int getProgramLength(Node rootNode) {
		return GPProgramAnalyser.countLength(rootNode, 0);
	}
	
	private static int countLength(Node rootNode, int length) {
		// increment length and count through children
		length++;
		// get children and recurse
		int arity = rootNode.getArity();
		if(arity>0) {
			for(int i = 0; i<arity; i++) {
				Node childNode = rootNode.getChild(i);
				length = GPProgramAnalyser.countLength(childNode, length);
			}
		}
		return length;
	}

}
