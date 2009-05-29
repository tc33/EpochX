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
package com.epochx.example.multiplexer;

import java.io.*;
import java.util.*;

import com.epochx.core.*;
import com.epochx.core.crossover.*;
import com.epochx.core.initialisation.BooleanHybridSemanticallyDrivenInitialiser;
import com.epochx.core.initialisation.RampedHalfAndHalfInitialiser;
import com.epochx.core.mutation.SubtreeMutation;
import com.epochx.core.representation.*;
import com.epochx.core.scorer.BooleanSemanticScorer;
import com.epochx.core.selection.*;
import com.epochx.func.bool.*;
import com.epochx.pruning.SemanticPruner;
import com.epochx.semantics.BooleanSemanticModule;
import com.epochx.semantics.SemanticModel;
import com.epochx.stats.GenerationStatField;
import com.epochx.stats.RunStatField;
import com.epochx.util.*;

/**
 * 
 *
 */
public class Multiplexer11Bit extends SemanticModel<Boolean> {

	private List<String> inputs;
	private HashMap<String, Variable<Boolean>> variables = new HashMap<String, Variable<Boolean>>();
	private int run = 1;
	private BooleanSemanticScorer scorer;
	
	public Multiplexer11Bit() {
		inputs = new ArrayList<String>();
		inputs = FileManip.loadInput(new File("input11bit.txt"));
		
		configure();
	}
	
	public void configure() {	
		// Define variables.
		variables.put("D7", new Variable<Boolean>("D7"));
		variables.put("D6", new Variable<Boolean>("D6"));
		variables.put("D5", new Variable<Boolean>("D5"));
		variables.put("D4", new Variable<Boolean>("D4"));
		variables.put("D3", new Variable<Boolean>("D3"));
		variables.put("D2", new Variable<Boolean>("D2"));
		variables.put("D1", new Variable<Boolean>("D1"));
		variables.put("D0", new Variable<Boolean>("D0"));
		variables.put("A2", new Variable<Boolean>("A2"));
		variables.put("A1", new Variable<Boolean>("A1"));
		variables.put("A0", new Variable<Boolean>("A0"));
		
		setPopulationSize(4000);
		setNoGenerations(10);
		setCrossoverProbability(0.45);
		setMutationProbability(0.45);
		setReproductionProbability(0.1);
		setNoRuns(1);
		setPouleSize(400);
		setNoElites(400);
		setInitialMaxDepth(6);
		setMaxDepth(17);
		setPouleSelector(new TournamentSelector<Boolean>(7, this));
		setParentSelector(new RandomSelector<Boolean>());
		setCrossover(new KozaCrossover<Boolean>());
		setStateCheckedCrossover(false);
		setMutator(new SubtreeMutation<Boolean>(this));
		setStateCheckedMutation(false);
		BooleanSemanticModule semMod = new BooleanSemanticModule(getTerminals(), this);
		setSemanticModule(semMod);
		setPruner(new SemanticPruner<Boolean>(this, semMod));
		setActivatePruning(true);
		setInitialiser(new RampedHalfAndHalfInitialiser<Boolean>(this));
		
		scorer = new BooleanSemanticScorer(getSemanticModule());
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
		terminals.add(variables.get("A0"));
		terminals.add(variables.get("A1"));
		terminals.add(variables.get("A2"));
		terminals.add(variables.get("D0"));
		terminals.add(variables.get("D1"));
		terminals.add(variables.get("D2"));
		terminals.add(variables.get("D3"));
		terminals.add(variables.get("D4"));
		terminals.add(variables.get("D5"));
		terminals.add(variables.get("D6"));
		terminals.add(variables.get("D7"));
		
		return terminals;
	}	
	
	@Override
	public double getFitness(CandidateProgram<Boolean> program) {
		// set up ideal solution
	    IfFunction part1 = new IfFunction(new Variable<Boolean>("A2"), new Variable<Boolean>("D0"), new Variable<Boolean>("D1"));
	    IfFunction part2 = new IfFunction(new Variable<Boolean>("A2"), new Variable<Boolean>("D2"), new Variable<Boolean>("D3"));
	    IfFunction part3 = new IfFunction(new Variable<Boolean>("A2"), new Variable<Boolean>("D4"), new Variable<Boolean>("D5"));
	    IfFunction part4 = new IfFunction(new Variable<Boolean>("A2"), new Variable<Boolean>("D6"), new Variable<Boolean>("D7"));
	    IfFunction part5 = new IfFunction(new Variable<Boolean>("A1"), part1, part2);
	    IfFunction part6 = new IfFunction(new Variable<Boolean>("A1"), part3, part4);
	    IfFunction part0 = new IfFunction(new Variable<Boolean>("A0"), part5, part6);
	    CandidateProgram<Boolean> target = new CandidateProgram<Boolean>(part0, this);
        // do semantic scoring part
        
        double score = scorer.doScore(program, target);
        return score;
	}
	
	/**
	@Override
	public double getFitness(CandidateProgram<Boolean> program) {
        double score = 0;
        
        // Execute on all possible inputs.
        for (String run : inputs) {
        	boolean[] in = BoolTrans.doTrans(run);
        	
        	// Set the variables.
        	variables.get("A0").setValue(in[0]);
        	variables.get("A1").setValue(in[1]);
        	variables.get("A2").setValue(in[2]);
        	variables.get("D0").setValue(in[3]);
        	variables.get("D1").setValue(in[4]);
        	variables.get("D2").setValue(in[5]);
        	variables.get("D3").setValue(in[6]);
        	variables.get("D4").setValue(in[7]);
        	variables.get("D5").setValue(in[8]);
        	variables.get("D6").setValue(in[9]);
        	variables.get("D7").setValue(in[10]);
        	
            if (program.evaluate() == chooseResult(in)) {
                score++;
            }
        }
        
        return 2048 - score;
	} **/
	
    private boolean chooseResult(boolean[] input) {
    	boolean result = false;
    	// scoring solution
        if(input[0] && input[1] && input[2]) {
            result = input[3];
        } else if(input[0] && input[1]& !input[2]) {
            result = input[4];            
        } else if(input[0] && !input[1] && input[2]) {
            result = input[5];
        } else if(input[0] && !input[1] && !input[2]) {
            result = input[6];
        } else if(!input[0] && input[1] && input[2]) {
            result = input[7];
        } else if(!input[0] && input[1] && !input[2]) {
            result = input[8];
        } else if(!input[0] && !input[1] && input[2]) {
            result = input[9];
        } else if(!input[0] && !input[1] && !input[2]) {
            result = input[10];
        }
        return result;
    }
	
	public static void main(String[] args) {
		GPController.run(new Multiplexer11Bit());
	}
	
	public void runStats(int runNo, Object[] stats) {
		this.run = runNo;
		System.out.print("Run number " + runNo + " complete.");
		ArrayList<String> output = new ArrayList<String>();
		String part = run + "\t";
		for (Object s: stats) {
			part = part + s;
			part = part + "\t";
		}
		part = part + "\n";
		output.add(part);		
		FileManip.doOutput(null, output, "RunStats.txt", true);
	}

	public RunStatField[] getRunStatFields() {
		return new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.RUN_TIME, RunStatField.BEST_PROGRAM};
	}
	
	@Override
	public void generationStats(int generation, Object[] stats) {
		ArrayList<String> output = new ArrayList<String>();
		//System.out.println(run + "\t" + generation + "\t");
		String part = run + "\t" + generation + "\t";
		for (Object s: stats) {
			part = part + s;
			part = part + "\t";
		}
		part = part + "\n";
		System.out.println(part);
		output.add(part);
		FileManip.doOutput(null, output, "GenerationStats.txt", true);
	}

	@Override
	public GenerationStatField[] getGenStatFields() {
		return new GenerationStatField[]{GenerationStatField.FITNESS_AVE, GenerationStatField.FITNESS_MIN, GenerationStatField.LENGTH_AVE, GenerationStatField.REVERTED_CROSSOVERS, GenerationStatField.REVERTED_MUTATIONS};
	}
}
