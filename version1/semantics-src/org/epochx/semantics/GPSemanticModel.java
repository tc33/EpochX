/*  
 *  Copyright 2007-2010 Lawrence Beadle & Tom Castle
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

package org.epochx.semantics;

import java.util.ArrayList;
import java.util.List;

import org.epochx.epox.Node;
import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.GPCandidateProgram;

/**
 *
 */
public abstract class GPSemanticModel extends GPModel {

	private boolean doStateCheckedCrossover;
	private boolean doStateCheckedMutation;	
	private SemanticModule semanticModule;
	
	public GPSemanticModel() {
		doStateCheckedCrossover = false;
		doStateCheckedMutation = false;		
		semanticModule = null;
	}
	
	public List<Node> getTerminals() {
		List<Node> fullSyntax = super.getSyntax();
		List<Node> terminals = new ArrayList<Node>();
		for(Node test: fullSyntax) {
			if(test.getArity()==0) {
				terminals.add(test);
			}
		}
		return terminals;
	}
	
	public List<Node> getFunctions() {
		List<Node> fullSyntax = super.getSyntax();
		List<Node> terminals = new ArrayList<Node>();
		for(Node test: fullSyntax) {
			if(test.getArity()!=0) {
				terminals.add(test);
			}
		}
		return terminals;
	}
	
	/**
	 * Returns whether to run the crossover state checker
	 * @return TRUE if the crossover state checker should be run
	 */
	public boolean getStateCheckedCrossover() {
		return doStateCheckedCrossover;
	}
	
	/**
	 * Sets whether to run the crossover state checker
	 * @param runStateCheck TRUE if the crossover state checker should be run
	 */
	public void setStateCheckedCrossover(boolean doStateCheckedCrossover) {
		this.doStateCheckedCrossover = doStateCheckedCrossover;
	}
	
	public boolean getStateCheckedMutation() {
		return doStateCheckedMutation;
	}
	
	public void setStateCheckedMutation(boolean doStateCheckedMutation) {
		this.doStateCheckedMutation = doStateCheckedMutation;
	}
	
	/**
	 * Returns the semantic module associated with this problem
	 * @return The associate Semantic module
	 */
	public SemanticModule getSemanticModule() {
		return this.semanticModule;
	}
	
	/**
	 * Sets the semantic module for this run
	 * @param semMod The desired semantic module to use
	 */
	public void setSemanticModule(SemanticModule semMod) {
		this.semanticModule = semMod;
	}
	
	/**
	 * Performs semantic equivalence check to determine whether the crossover 
	 * should be accepted or not.
	 */
	public boolean acceptCrossover(GPCandidateProgram[] parents, 
								   GPCandidateProgram[] children) {
		boolean equal = false;
		if (doStateCheckedCrossover) {
			// pull out semantic module and check its not null
			SemanticModule semMod = getSemanticModule();
			if(semMod==null) {
				throw new IllegalArgumentException("Semantic module undefined for semantically driven crossover.");
			}
			
			//start semantic module
			// TODO find a better way of doing this
			if(semMod instanceof BooleanSemanticModule) {
				((BooleanSemanticModule) semMod).start();
			}
			
			// check behaviours
			Representation p1Rep = semMod.codeToBehaviour(parents[0]);
			Representation p2Rep = semMod.codeToBehaviour(parents[1]);
			Representation c1Rep = semMod.codeToBehaviour(children[0]);
			Representation c2Rep = semMod.codeToBehaviour(children[1]);
			if(c1Rep.equals(p1Rep) || c1Rep.equals(p2Rep)) {
				equal = true;
			}
			if(c2Rep.equals(p1Rep) || c2Rep.equals(p2Rep)) {
				equal = true;
			}
			
			// stop semantic module
			// TODO find a better way of doing this
			if(semMod instanceof BooleanSemanticModule) {
				((BooleanSemanticModule) semMod).stop();
			}
		}
			
		return !equal;
	}
	
	/**
	 * Performs semantic equivalence check to determine whether the mutation 
	 * should be accepted or not.
	 */
	public boolean acceptMutation(GPCandidateProgram parent, 
								  GPCandidateProgram child) {
		boolean equal = false;
		if (doStateCheckedMutation) {
			// pull out semantic module and check its not null
			SemanticModule semMod = getSemanticModule();
			if(semMod==null) {
				throw new IllegalArgumentException("Semantic module undefine for semantically driven mutation.");
			}
			
			//start semantic module
			// TODO find a better way of doing this
			if(semMod instanceof BooleanSemanticModule) {
				((BooleanSemanticModule) semMod).start();
			}
			
			// check behaviours
			Representation p1Rep = semMod.codeToBehaviour(parent);
			Representation c1Rep = semMod.codeToBehaviour(child);
			
			if(c1Rep.equals(p1Rep)) {
				equal = true;
			}
			
			// stop semantic module
			// TODO find a better way of doing this
			if(semMod instanceof BooleanSemanticModule) {
				((BooleanSemanticModule) semMod).stop();
			}
		}
		return !equal;
	}
}
