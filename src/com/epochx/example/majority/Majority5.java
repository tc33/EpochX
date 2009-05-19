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
package com.epochx.example.majority;

import java.io.File;
import java.util.*;

import com.epochx.core.GPController;
import com.epochx.core.crossover.UniformPointCrossover;
import com.epochx.core.initialisation.RampedHalfAndHalfInitialiser;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;
import com.epochx.func.bool.*;
import com.epochx.semantics.*;
import com.epochx.stats.GenerationStatField;
import com.epochx.util.*;

/**
 * 
 */
public class Majority5 extends SemanticModel<Boolean> {

	private List<String> inputs;
	private HashMap<String, Variable<Boolean>> variables = new HashMap<String, Variable<Boolean>>();
	private int run = 1;
	
	public Majority5() {
		inputs = new ArrayList<String>();
		inputs = FileManip.loadInput(new File("input5bit.txt"));
		
		configure();
	}
	
	public void configure() {
		
		// Define variables.
		variables.put("D4", new Variable<Boolean>("D4"));
		variables.put("D3", new Variable<Boolean>("D3"));
		variables.put("D2", new Variable<Boolean>("D2"));
		variables.put("D1", new Variable<Boolean>("D1"));
		variables.put("D0", new Variable<Boolean>("D0"));
		
		setPopulationSize(500);
		setNoGenerations(50);
		setCrossoverProbability(0.9);
		setReproductionProbability(0.1);
		setNoRuns(100);
		setPouleSize(50);
		setNoElites(50);
		setInitialMaxDepth(6);
		setMaxDepth(17);
		setPouleSelector(new TournamentSelector<Boolean>(7, this));
		setParentSelector(new RandomSelector<Boolean>());
		setCrossover(new UniformPointCrossover<Boolean>());
		setStateCheckedCrossover(false);
		setSemanticModule(new BooleanSemanticModule(getTerminals(), this));
		setInitialiser(new RampedHalfAndHalfInitialiser<Boolean>(this));
	}
	
	@Override
	public List<FunctionNode<?>> getFunctions() {
		// Define functions.
		List<FunctionNode<?>> functions = new ArrayList<FunctionNode<?>>();
		functions.add(new IfFunction());
		functions.add(new AndFunction());
		functions.add(new OrFunction());
		functions.add(new NotFunction());
		return functions;
	}

	@Override
	public List<TerminalNode<?>> getTerminals() {		
		// Define terminals.
		List<TerminalNode<?>> terminals = new ArrayList<TerminalNode<?>>();
		terminals.add(variables.get("D4"));
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
        for (String run : inputs) {
        	boolean[] in = BoolTrans.doTrans(run);
        	
        	// Set the variables.
        	variables.get("D0").setValue(in[0]);
        	variables.get("D1").setValue(in[1]);
        	variables.get("D2").setValue(in[2]);
        	variables.get("D3").setValue(in[3]);
        	variables.get("D4").setValue(in[4]);
        	
            if (program.evaluate() == chooseResult(in)) {
                score++;
            }
        }
        
        return 32 - score;
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
	
	public static void main(String[] args) {
		GPController.run(new Majority5());
	}
	
	@Override
	public void runStats(int run, Object[] stats) {
		this.run = run + 1;
	}
	
	@Override
	public void generationStats(int generation, Object[] stats) {
		ArrayList<String> output = new ArrayList<String>();
		System.out.println(run + "\t" + generation + "\t");
		String part = run + "\t" + generation + "\t";
		for (Object s: stats) {
			part = part + s;
			part = part + "\t";
		}
		part = part + "\n";
		output.add(part);
		FileManip.doOutput(null, output, "output.txt", true);
	}

	@Override
	public GenerationStatField[] getGenStatFields() {
		return new GenerationStatField[]{GenerationStatField.FITNESS_AVE, GenerationStatField.FITNESS_MIN, GenerationStatField.LENGTH_AVE};
	}
}
