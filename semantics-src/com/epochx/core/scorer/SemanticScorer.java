/*  
 *  Copyright 2007-2009 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
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

package com.epochx.core.scorer;

import com.epochxge.representation.CandidateProgram;
import com.epochx.semantics.SemanticModule;

/**
 * The Semantic Scorer allows users to compare to representations of 
 * programs and return a value denoting relative difference. Zero would 
 * be the best score possible, i.e. denoting equivalence.
 */
public abstract class SemanticScorer {
	
	private SemanticModule semanticModule;
	
	/**
	 * General constructor for semantic scorers
	 * @param semanticModule
	 */
	public SemanticScorer(SemanticModule semanticModule) {
		this.semanticModule = semanticModule;
	}
	
	
	/**
	 * Returns the current Semantic Module
	 * @return The current Semantic Module
	 */
	public SemanticModule getSemanticModule() {
		return semanticModule;
	}
	
	/**
	 * Calculates the different between to behavioural representations
	 * @param program1 The 1st program being considered
	 * @param program2 The 1st program being considered
	 * @return The difference between the two behaviours - a difference of 0 indicates equivalence
	 */
	public abstract double doScore(CandidateProgram program1, CandidateProgram program2);

}
