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
package org.epochx.gr.model.java.mux;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.gr.model.*;
import org.epochx.gr.representation.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.JavaEvaluator;
import org.epochx.tools.grammar.Grammar;
import org.epochx.tools.util.BoolUtils;


public class Multiplexer6Bit extends GRModel {
	
	public static final String GRAMMAR_STRING = 
		"<prog> ::= <expr>\n" +
		"<expr> ::= <expr> <op> <expr> " +
				"| ( <expr> <op> <expr> ) " +
				"| <var> " +
				"| <pre-op> ( <var> ) " +
				"| ( <expr> ) ? <expr> : <expr>\n" +
		"<pre-op> ::= !\n" +
		"<op> ::= \"||\" | &&\n" +
		"<var> ::= a0 | a1 | d0 | d1 | d2 | d3\n";
	
	private Grammar grammar;
	
	private boolean[][] inputs;
	
	private JavaEvaluator evaluator;
	
	public Multiplexer6Bit() {
		grammar = new Grammar(GRAMMAR_STRING);
		evaluator = new JavaEvaluator();
		
		inputs = BoolUtils.generateBoolSequences(6);	
	}

	@Override
	public double getFitness(CandidateProgram p) {
		GRCandidateProgram program = (GRCandidateProgram) p;
		
        double score = 0;
        
        String[] argNames = {"a0", "a1", "d0", "d1", "d2", "d3"};
        
        // Execute on all possible inputs.
        for (boolean[] vars : inputs) {
        	// Convert to object array.
        	Boolean[] objVars = ArrayUtils.toObject(vars);
        	
        	Boolean result = (Boolean) evaluator.eval(program.getSourceCode(), argNames, objVars);
        	
            if (result != null && result == chooseResult(vars)) {
                score++;
            }
        }
        
        return 64 - score;
	}
	
    private boolean chooseResult(boolean[] input) {
        boolean result = false;
    	// scoring solution
        if(input[0] && input[1]) {
            result = input[2];
        } else if(input[0] && !input[1]) {
            result = input[3];
        } else if(!input[0] && input[1]) {
            result = input[4];
        } else if(!input[0] && !input[1]) {
            result = input[5];
        }
        return result;
    }
    
	@Override
	public Grammar getGrammar() {
		return grammar;
	}
}
