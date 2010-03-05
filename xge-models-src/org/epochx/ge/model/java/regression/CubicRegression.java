/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.ge.model.java.regression;

import org.epochx.ge.model.GEAbstractModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.JavaEvaluator;
import org.epochx.tools.grammar.Grammar;

public class CubicRegression extends GEAbstractModel {
	
	public static final String GRAMMAR_STRING = 
		"<expr> ::= ( <expr> <op> <expr> ) | <var>\n" +
		"<op>   ::= + | - | * \n" +
		"<var>  ::= X | 1.0 \n";
	
	private Grammar grammar;
	
	private JavaEvaluator evaluator;
	
	public CubicRegression() {
		grammar = new Grammar(GRAMMAR_STRING);
		evaluator = new JavaEvaluator();
	}

	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
        // Execute on all possible inputs.
        double dd = 0;
        for (double x = -1; x < 1; x+=0.1){
        	Double result = (Double) evaluator.eval(program.getSourceCode(), new String[]{"X"}, new Double[]{x});
        	if (result != null) {
        		dd += Math.abs(result - fun(x));
        	} else {
        		if (!program.isValid()) {
        			dd = Double.POSITIVE_INFINITY;
        			break;
        		}
        	}
        }
        return dd;
	}	

    public double fun(double x){
        return x + x*x + x*x*x;
    }
    
	@Override
	public Grammar getGrammar() {
		return grammar;
	}
}
