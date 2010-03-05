/*  
 *  Copyright 2007-2010 Tom Castle & Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming software for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 *  The latest version is available from: http:/www.epochx.org
 */
package org.epochx.ge.example.groovy.parity;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.core.Controller;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.op.selection.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.tools.eval.GroovyEvaluator;
import org.epochx.tools.grammar.Grammar;
import org.epochx.tools.util.BoolUtils;



public class Even5Parity extends GEAbstractModel {

	private GroovyEvaluator evaluator;
	
	private Grammar grammar;
	
	//private boolean[][] boolVars;
	private boolean[][] inputs;
	
	public Even5Parity() {
		evaluator = new GroovyEvaluator();
		grammar = new Grammar(new File("example-grammars/Groovy/EvenFiveParity.bnf"));	
		
		inputs = BoolUtils.generateBoolSequences(5);
		
		setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM});
		setNoRuns(1);
		setNoElites(50);
		setNoGenerations(50);
		setPopulationSize(500);
		setMaxProgramDepth(17);
		setMutationProbability(0.1);
		setCrossoverProbability(0.9);
		setProgramSelector(new TournamentSelector(this, 7));
		setPoolSelector(new RandomSelector(this));
		setPoolSize(50);
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		String[] argNames = new String[]{"d0", "d1", "d2", "d3", "d4"};
        
    	// Convert to object array.
		Boolean[][] objInputs = new Boolean[inputs.length][];
    	for (int i=0; i<objInputs.length; i++) {
    		objInputs[i] = ArrayUtils.toObject(inputs[i]);
    	}
		
		Object[] results = evaluator.eval(program.getSourceCode(), argNames, objInputs);
		
		double score = 0;
		for (int i=0; i<inputs.length; i++) {
			if ((results[i] != null) && ((Boolean) results[i] == chooseResult(inputs[i]))) {
				score++;
			}
		}
        
        return 32 - score;
	}
	
    private boolean chooseResult(boolean[] input) {
        // scoring solution
        int eCount = 0;
        for(int i = 0; i<input.length; i++) {
            if(input[i]==true) {
                eCount++;
            }
        }
        if(eCount%2==0) {
            return true;
        } else {
            return false;
        }
    }
    
	@Override
	public Grammar getGrammar() {
		return grammar;
	}

	public static void main(String[] args) {
		Controller.run(new Even5Parity());
	}
}
