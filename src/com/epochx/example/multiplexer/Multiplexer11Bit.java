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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.epochx.core.*;
import com.epochx.core.crossover.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;
import com.epochx.util.*;

/**
 * 
 *
 */
public class Multiplexer11Bit extends GPAbstractModel<Boolean> {

	private List<String> inputs;
	private HashMap<String, Variable<Boolean>> variables = new HashMap<String, Variable<Boolean>>();
	
	public Multiplexer11Bit() {
		inputs = new ArrayList<String>();
		inputs = FileManip.loadInput(new File("input11bit.txt"));
		
		configure();
	}
	
	public void configure() {
		setPopulationSize(500);
		setNoGenerations(50);
		setCrossoverProbability(0.9);
		setReproductionProbability(0.1);
		setNoRuns(1);
		setPouleSize(50);
		setNoElites(50);
		setMaxDepth(6);
		setPouleSelector(new TournamentSelector<Boolean>(7, this));
		setParentSelector(new RandomSelector<Boolean>());
		setCrossover(new UniformPointCrossover<Boolean>());
		
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
	}
	
	@Override
	public List<FunctionNode<?>> getFunctions() {
		// Define functions.
		List<FunctionNode<?>> functions = new ArrayList<FunctionNode<?>>();
		functions.add(new IfFunction(null, null, null));
		functions.add(new AndFunction(null, null));
		functions.add(new OrFunction(null, null));
		functions.add(new NotFunction(null));
		return functions;
	}

	@Override
	public List<TerminalNode<?>> getTerminals() {		
		// Define terminals.
		List<TerminalNode<?>> terminals = new ArrayList<TerminalNode<?>>();
		terminals.add(variables.get("D7"));
		terminals.add(variables.get("D6"));
		terminals.add(variables.get("D5"));
		terminals.add(variables.get("D4"));
		terminals.add(variables.get("D3"));
		terminals.add(variables.get("D2"));
		terminals.add(variables.get("D1"));
		terminals.add(variables.get("D0"));
		terminals.add(variables.get("A2"));
		terminals.add(variables.get("A1"));
		terminals.add(variables.get("A0"));
		
		return terminals;
	}
	
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
	}
	
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
}
