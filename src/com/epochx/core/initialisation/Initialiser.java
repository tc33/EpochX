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
package com.epochx.core.initialisation;

import java.util.*;

import com.epochx.core.*;
import com.epochx.core.representation.*;

import core.SemanticModule;

/**
 * 
 */
public abstract class Initialiser {
	
	private int depth;
	private int popSize;
	private SemanticModule semMod;
	private List<TerminalNode<?>> terminals;
	private List<FunctionNode<?>> functions;
	private Random rGen;
	
	public Initialiser(GPConfig config, SemanticModule semMod) {
		
		// set all private variables
		this.depth = config.getDepth();
		this.popSize = config.getPopulationSize();
		this.functions = config.getFunctions();
		this.terminals = config.getTerminals();
		this.semMod = semMod;
		rGen = new Random();		
	}
	
	public int getDepth() {
		return depth;
	}
	
	public int getPopSize() {
		return popSize;
	}
	
	public int getNoFunctions() {
		return functions.size();
	}
	
	public int getNoTerminals() {
		return terminals.size();
	}
	
	public int getNoSyntax() {
		return functions.size() + terminals.size();
	}
	
	public Random getRandom() {
		return rGen;
	}
	
	public SemanticModule getSemMod() {
		return semMod;
	}

	public ArrayList<Node> getSyntax() {
		ArrayList allNodes = new ArrayList<Node>(terminals);
		allNodes.addAll(functions);
		return allNodes;
	}
	
	public List<FunctionNode<?>> getFunctions() {
		return functions;
	}
	
	public List<TerminalNode<?>> getTerminals() {
		return terminals;
	}
}
