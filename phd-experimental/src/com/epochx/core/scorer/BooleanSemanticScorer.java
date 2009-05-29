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
package com.epochx.core.scorer;

import com.epochx.core.representation.*;
import com.epochx.semantics.*;
import net.sf.javabdd.*;

/**
 * The Boolean Semantic Scorer allows users to compare to representations of 
 * Boolean programs and return a value denoting relative difference. Zero would 
 * be the best score possible, i.e. denoting equivalence.
 */
public class BooleanSemanticScorer {
	
	private SemanticModule semanticModule;

	/**
	 * Constructor for the Boolean semantic scorer
	 * @param semanticModule The semantic module to be used
	 */
	public BooleanSemanticScorer(SemanticModule semanticModule) {
		this.semanticModule = semanticModule;
	}
	
	/**
	 * Calculates the percentage different between to Boolean representations
	 * @param program Program being considered
	 * @param target The target program (to set up target behaviour)
	 * @return Percentage difference 0% is best score
	 */
	public double doScore(CandidateProgram program, CandidateProgram target) {
        double score;
        BooleanRepresentation programRepresentation = (BooleanRepresentation) semanticModule.codeToBehaviour(program);
        BooleanRepresentation targetRepresentation = (BooleanRepresentation) semanticModule.codeToBehaviour(target);
        BDD thisRep = programRepresentation.getBDD();
        BDD idealRep = targetRepresentation.getBDD();
        BDD diffRep1 = thisRep.and(idealRep.not());
        BDD diffRep2 = idealRep.and(thisRep.not());
        BDD finalDiff = diffRep1.or(diffRep2);
        score = finalDiff.satCount() * 100;
        return score;
	}
}
