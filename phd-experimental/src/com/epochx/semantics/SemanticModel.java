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
package com.epochx.semantics;

import com.epochx.core.GPAbstractModel;
import com.epochx.core.representation.CandidateProgram;

/**
 *
 */
public abstract class SemanticModel<TYPE> extends GPAbstractModel<TYPE> {

	private boolean doStateCheckedCrossover;
	private boolean doStateCheckedMutation;
	
	private SemanticModule<TYPE> semanticModule;
	
	public SemanticModel() {
		doStateCheckedCrossover = false;
		doStateCheckedMutation = false;
		semanticModule = null;
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
	public void setStateCheckedCrossover(boolean runStateCheck) {
		this.doStateCheckedCrossover = runStateCheck;
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
	public SemanticModule<TYPE> getSemanticModule() {
		return this.semanticModule;
	}
	
	/**
	 * Sets the semantic module for this run
	 * @param semMod The desired semantic module to use
	 */
	public void setSemanticModule(SemanticModule<TYPE> semMod) {
		this.semanticModule = semMod;
	}
	
	/**
	 * Performs semantic equivalence check to determine whether the crossover 
	 * should be accepted or not.
	 */
	public boolean acceptCrossover(CandidateProgram<TYPE>[] parents, 
								   CandidateProgram<TYPE>[] children) {
		boolean equal = false;
		if (doStateCheckedCrossover) {
			// pull out semantic module and check its not null
			SemanticModule<TYPE> semMod = getSemanticModule();
			if(semMod==null) {
				throw new IllegalArgumentException("Semantic module undefined for semantically driven crossover.");
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
		}
			
		return !equal;
	}
	
	/**
	 * Performs semantic equivalence check to determine whether the mutation 
	 * should be accepted or not.
	 */
	public boolean acceptMutation(CandidateProgram<TYPE> parent, 
								  CandidateProgram<TYPE> child) {
		boolean equal = false;
		if (doStateCheckedMutation) {
			// pull out semantic module and check its not null
			SemanticModule<TYPE> semMod = getSemanticModule();
			if(semMod==null) {
				throw new IllegalArgumentException("Semantic module undefine for semantically driven mutation.");
			}
			
			// check behaviours
			Representation p1Rep = semMod.codeToBehaviour(parent);
			Representation c1Rep = semMod.codeToBehaviour(child);
			
			if(c1Rep.equals(p1Rep)) {
				equal = true;
			}
		}
		return !equal;
	}
}
