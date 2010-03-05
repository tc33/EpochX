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
package org.epochx.ge.example.bf;

import java.io.File;

import org.epochx.core.Controller;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.op.selection.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.tools.eval.BrainfuckEvaluator;
import org.epochx.tools.grammar.Grammar;
import org.epochx.tools.util.BoolUtils;


public class Even4Parity extends GEAbstractModel {

	private BrainfuckEvaluator evaluator;
	
	private Grammar grammar;
	
	private boolean[][] inputs;
	
	public Even4Parity() {
		evaluator = new BrainfuckEvaluator();
		grammar = new Grammar(new File("example-grammars/Brainfuck/brainfuck.bnf"));	
		
		inputs = BoolUtils.generateBoolSequences(4);
		
		setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM});
		setNoRuns(1);
		setNoElites(50);
		setNoGenerations(1000);
		setPopulationSize(2000);
		setMaxProgramDepth(17);
		setMutationProbability(0.1);
		setCrossoverProbability(0.9);
		setInitialiser(new RampedHalfAndHalfInitialiser(this));
		setProgramSelector(new TournamentSelector(this, 7));
		setPoolSelector(new RandomSelector(this));
		setPoolSize(50);
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		double score = 0;
		
        // Execute on all possible inputs.
        for (int i=0; i<inputs.length; i++) {
        	boolean[] vars = inputs[i];   	
        	// Convert inputs to byte array.
        	Byte[] inputBytes = convertToBytes(vars);
        	//String[] argNames = new String[]{"d0", "d1", "d2", "d3"};
        	Byte result = (Byte) evaluator.eval(program.getSourceCode(), null, inputBytes);

            if (result != null && result == chooseResult(vars)) {
                score++;
            } else if (!program.isValid()) {
            	score = 0;
            	break;
            }
        }
        
        return 16 - score;
	}
	
	private Byte[] convertToBytes(boolean[] bools) {
		Byte[] bytes = new Byte[bools.length];
		for (int i=0; i<bools.length; i++) {
			bytes[i] = (byte) (bools[i] ? 1 : 0);
		}
		return bytes;
	}
	
    private byte chooseResult(boolean[] input) {
        // scoring solution
        int eCount = 0;
        for(int i = 0; i<input.length; i++) {
            if(input[i]==true) {
                eCount++;
            }
        }
        if(eCount%2==0) {
            return 1;
        } else {
            return 0;
        }
    }

	@Override
	public Grammar getGrammar() {
		return grammar;
	}

	public static void main(String[] args) {
		Controller.run(new Even4Parity());
	}
}
