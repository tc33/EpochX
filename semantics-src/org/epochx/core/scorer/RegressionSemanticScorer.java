/*  
 *  Copyright 2007-2009 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming for research
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

import java.util.ArrayList;

import org.epochx.representation.*;
import org.epochx.representation.dbl.CoefficientPowerFunction;
import org.epochx.semantics.*;


public class RegressionSemanticScorer extends SemanticScorer {

	public RegressionSemanticScorer(SemanticModule semanticModule) {
		super(semanticModule);
	}

	@Override
	public double doScore(CandidateProgram program1, CandidateProgram program2) {
		// generate representations
		RegressionRepresentation rep1 = (RegressionRepresentation) getSemanticModule().codeToBehaviour(program1);
		RegressionRepresentation rep2 = (RegressionRepresentation) getSemanticModule().codeToBehaviour(program2);
		// pull out the CVP strings
		ArrayList<CoefficientPowerFunction> cvps1 = rep1.getRegressionRepresentation();
		ArrayList<CoefficientPowerFunction> cvps2 = rep2.getRegressionRepresentation();		
		// work out which is longer
		int length = 0;
		if(cvps1.size()>=cvps2.size()) {
			length = cvps1.size();
		} else {
			length = cvps2.size();
		}		
		// do difference calculation
		double score = 0;
		CoefficientPowerFunction blank = new CoefficientPowerFunction(new TerminalNode<Double>(0d), new Variable<Double>("X"), new TerminalNode<Double>(0d));
		for(int i = 0; i<length; i++) {
			CoefficientPowerFunction p1;
			CoefficientPowerFunction p2;
			// pull i th cvp from cvps1
			if(i<cvps1.size()) {
				p1 = cvps1.get(i);
			} else {
				p1 = blank;
			}
			// pull i th cvp from cvps2
			if(i<cvps2.size()) {
				p2 = cvps2.get(i);
			} else {
				p2 = blank;
			}
			double coefDiff = Math.abs(p1.getChild(0).evaluate() - p2.getChild(0).evaluate());
			double powerDiff = Math.abs(p1.getChild(2).evaluate() - p2.getChild(2).evaluate());
			score = score + coefDiff + powerDiff;
		}
		
		return score;
	}

}