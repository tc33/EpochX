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

import java.util.ArrayList;
import java.util.Random;

import com.epochx.core.representation.*;

import core.SemanticModule;

/**
 * 
 */
public abstract class Initialiser {
	
	private int depth;
	private int popSize;
	private SemanticModule semMod;
	private ArrayList<Node> syntax;
	private ArrayList<Node> terminals;
	private ArrayList<Node> functions;
	private Random rGen;
	private int noFuncs;
	private int noTerms;
	private int noSyntax;
	
	public Initialiser(ArrayList<Node> syntax, ArrayList<Node> functions, 
			ArrayList<Node> terminals, SemanticModule semMod, 
			int popSize, int depth) {
		
		// calculate no of functions and terminals
		noFuncs = functions.size();
		noTerms = terminals.size();
		noSyntax = syntax.size();
		
		// set all private variables
		this.depth = depth;
		this.popSize = popSize;
		this.syntax = syntax;
		this.functions = functions;
		this.terminals = terminals;
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
		return noFuncs;
	}
	
	public int getNoTerminals() {
		return noTerms;
	}
	
	public int getNoSyntax() {
		return noSyntax;
	}
	
	public Random getRandom() {
		return rGen;
	}
	
	public SemanticModule getSemMod() {
		return semMod;
	}

	public ArrayList<Node> getSyntax() {
		return syntax;
	}
	
	public ArrayList<Node> getFunctions() {
		return functions;
	}
	
	public ArrayList<Node> getTerminals() {
		return terminals;
	}
}
