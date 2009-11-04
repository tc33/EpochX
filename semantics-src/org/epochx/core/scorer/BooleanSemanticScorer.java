/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.core.scorer;

import org.epochx.core.scorer.SemanticScorer;
import org.epochx.representation.*;
import org.epochx.semantics.*;

import net.sf.javabdd.*;

/**
 * The Boolean Semantic Scorer allows users to compare to representations of 
 * Boolean programs and return a value denoting relative difference. Zero would 
 * be the best score possible, i.e. denoting equivalence.
 */
public class BooleanSemanticScorer extends SemanticScorer {
	
	private SemanticModule semanticModule;

	/**
	 * Constructor for the Boolean semantic scorer
	 * @param semanticModule The semantic module to be used
	 */
	public BooleanSemanticScorer(SemanticModule semanticModule) {
		super(semanticModule);
	}

	/* (non-Javadoc)
	 * @see org.epochx.core.scorer.SemanticScorer#doScore(org.epochx.representation.CandidateProgram, org.epochx.representation.CandidateProgram)
	 */
	public double doScore(CandidateProgram program1, CandidateProgram program2) {
        double score;
        getSemanticModule().start();
        BooleanRepresentation program1Representation = (BooleanRepresentation) getSemanticModule().codeToBehaviour(program1);
        BooleanRepresentation program2Representation = (BooleanRepresentation) getSemanticModule().codeToBehaviour(program2);
        BDD rep1 = program1Representation.getBDD();
        BDD rep2 = program2Representation.getBDD();
        BDD diffRep1 = rep1.and(rep2.not());
        BDD diffRep2 = rep2.and(rep1.not());
        BDD finalDiff = diffRep1.or(diffRep2);
        score = finalDiff.satCount() * 100;
        getSemanticModule().stop();
        return score;
	}
}
