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
	
	private int depth, currentDepth, length;
	
	public GPProgramAnalyser() {
		// save for initialisation work for other functionality
	}
	
	public int getProgramDepth(Node rootNode) {
		// set depth and current depth to zero
		currentDepth = 0;
		depth = 0;
		this.countDepth(rootNode, (currentDepth + 1));
		return depth;
	}
	
	private void countDepth(Node rootNode, int currentDepth) {
		// set current depth to maximum if need be
		if(currentDepth>depth) {
			depth = currentDepth;
		}
		// get children and recurse
		int arity = rootNode.getArity();
		if(arity>0) {
			for(int i = 0; i<arity; i++) {
				Node childNode = rootNode.getChild(i);
				this.countDepth(childNode, (currentDepth + 1));
			}
		}
	}
	
	public int getProgramLength(Node rootNode) {
		length = 0;
		this.countLength(rootNode);
		return length;
	}
	
	private void countLength(Node rootNode) {
		// increment length and count through children
		length = length + 1;
		// get children and recurse
		int arity = rootNode.getArity();
		if(arity>0) {
			for(int i = 0; i<arity; i++) {
				Node childNode = rootNode.getChild(i);
				this.countLength(childNode);
			}
		}
	}

}
