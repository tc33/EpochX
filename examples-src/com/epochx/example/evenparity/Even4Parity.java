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
package com.epochx.example.evenparity;

import java.util.*;

import com.epochx.core.*;
import com.epochx.op.crossover.*;
import com.epochx.op.initialisation.*;
import com.epochx.op.selection.*;
import com.epochx.representation.*;
import com.epochx.representation.bool.*;
import com.epochx.stats.RunStatField;
import com.epochx.stats.GenerationStatField;
import com.epochx.util.*;

/**
 * 
 */
public class Even4Parity extends GPAbstractModel<Boolean> {

	private boolean[][] inputs;
	
	private HashMap<String, Variable<Boolean>> variables = new HashMap<String, Variable<Boolean>>();
	
	public Even4Parity() {
		inputs = BoolUtils.generateBoolSequences(4);
		
		configure();
	}
	
	public void configure() {
		// Define variables.
		variables.put("D3", new Variable<Boolean>("D3"));
		variables.put("D2", new Variable<Boolean>("D2"));
		variables.put("D1", new Variable<Boolean>("D1"));
		variables.put("D0", new Variable<Boolean>("D0"));
		
		setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE, GenerationStatField.RUN_TIME});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM, RunStatField.RUN_TIME});
		
		setPopulationSize(500);
		setNoGenerations(50);
		setCrossoverProbability(0.8);
		setMutationProbability(0.1);
		setNoRuns(100);
		setPoolSize(50);
		setNoElites(50);
		setInitialMaxDepth(6);
		setMaxProgramDepth(17);
		setPoolSelector(new TournamentSelector<Boolean>(this, 7));
		setProgramSelector(new RandomSelector<Boolean>(this));
		setCrossover(new UniformPointCrossover<Boolean>(this));
		setInitialiser(new RampedHalfAndHalfInitialiser<Boolean>(this));
	}
	
	@Override
	public List<FunctionNode<Boolean>> getFunctions() {
		// Define functions.
		List<FunctionNode<Boolean>> functions = new ArrayList<FunctionNode<Boolean>>();
		functions.add(new IfFunction());
		functions.add(new AndFunction());
		functions.add(new OrFunction());
		functions.add(new NotFunction());
		return functions;
	}

	@Override
	public List<TerminalNode<Boolean>> getTerminals() {		
		// Define terminals.
		List<TerminalNode<Boolean>> terminals = new ArrayList<TerminalNode<Boolean>>();
		terminals.add(variables.get("D3"));
		terminals.add(variables.get("D2"));
		terminals.add(variables.get("D1"));
		terminals.add(variables.get("D0"));
		
		return terminals;
	}
	
	@Override
	public double getFitness(CandidateProgram<Boolean> program) {
        double score = 0;
        
        // Execute on all possible inputs.
        for (boolean[] in: inputs) {
        	
        	// Set the variables.
        	variables.get("D0").setValue(in[0]);
        	variables.get("D1").setValue(in[1]);
        	variables.get("D2").setValue(in[2]);
        	variables.get("D3").setValue(in[3]);
        	
            if (program.evaluate() == chooseResult(in)) {
                score++;
            }
        }
        
        return 16 - score;
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
	
	public static void main(String[] args) {
		GPController.run(new Even4Parity());
	}
}
