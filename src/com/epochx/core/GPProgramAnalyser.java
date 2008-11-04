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

import com.epochx.core.representation.*;

/**
 * 
 */
public class GPProgramAnalyser {
	
	public static int getProgramDepth(CandidateProgram program) {
		Node rootNode = program.getRootNode();
		// set depth and current depth to zero
		int currentDepth = 0;
		int depth = 0;
		depth = GPProgramAnalyser.countDepth(rootNode, (currentDepth + 1), depth);
		return depth;
	}
	
	private static int countDepth(Node rootNode, int currentDepth, int depth) {
		// set current depth to maximum if need be
		if(currentDepth>depth) {
			depth = currentDepth;
		}
		// get children and recurse
		int arity = rootNode.getArity();
		if(arity>0) {
			for(int i = 0; i<arity; i++) {
				Node childNode = rootNode.getChild(i);
				depth = GPProgramAnalyser.countDepth(childNode, (currentDepth + 1), depth);
			}
		}
		return depth;
	}
	
	public static int getProgramLength(CandidateProgram program) {
		Node rootNode = program.getRootNode();
		int length = 0;
		length = GPProgramAnalyser.countLength(rootNode, length);
		return length;
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
