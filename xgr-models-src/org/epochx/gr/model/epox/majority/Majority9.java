/*  
 *  Copyright 2009 Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of XGE: grammatical evolution for research
 *
 *  XGE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XGE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with XGE.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.gr.model.epox.majority;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.gr.model.GRModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.*;
import org.epochx.tools.grammar.Grammar;
import org.epochx.tools.util.BoolUtils;

/**
 *
 */
public class Majority9 extends GRModel {

	public static final String GRAMMAR_STRING =
		"<prog> ::= <node>\n" +
		"<node> ::= <function> | <terminal>\n" +
		"<function> ::= NOT( <node> ) " +
					"| OR( <node> , <node> ) " +
					"| AND( <node> , <node> ) " +
					"| IF( <node> , <node> , <node> )\n" +
		"<terminal> ::= d0 | d1 | d2 | d3 | d4 | d5 | d6 | d7 | d8\n";
	
	private Grammar grammar;
	
	private boolean[][] inputs;
	
	private Evaluator evaluator;
	
	public Majority9() {
		grammar = new Grammar(GRAMMAR_STRING);
		evaluator = new EpoxEvaluator();
		
		inputs = BoolUtils.generateBoolSequences(9);
	}

	@Override
	public double getFitness(CandidateProgram p) {
		GRCandidateProgram program = (GRCandidateProgram) p;
		
		double score = 0;
        
		String[] argNames = {"d0", "d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8"};
		
        // Execute on all possible inputs.
		for (boolean[] vars : inputs) {
        	// Convert to object array.
        	Boolean[] objVars = ArrayUtils.toObject(vars);
        	
        	Boolean result = (Boolean) evaluator.eval(program.getSourceCode(), argNames, objVars);
        	
            if (result != null && result == chooseResult(vars)) {
                score++;
            }
        }
        
        return 512 - score;
	}
	
    private boolean chooseResult(boolean[] input) {
    	// scoring solution
        int len = input.length;
        int trueCount = 0;
        for(int i = 0; i<len; i++) {
            if(input[i]) {
                trueCount++;
            }
        }
        
        if(trueCount>=(len/2)) {
            return true;
        } else {
            return false;
        }
    }

	@Override
	public Grammar getGrammar() {
		return grammar;
	}
}
