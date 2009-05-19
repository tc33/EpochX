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
import java.util.*;

import com.epochx.core.*;
import com.epochx.core.crossover.UniformPointCrossover;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;
import com.epochx.func.bool.*;
import com.epochx.util.*;

/**
 * 
 *
 */
public class Multiplexer20Bit extends GPAbstractModel<Boolean> {

	private List<String> inputs;
	private HashMap<String, Variable<Boolean>> variables = new HashMap<String, Variable<Boolean>>();
	
	public Multiplexer20Bit() {
		inputs = new ArrayList<String>();
		inputs = FileManip.loadInput(new File("input20bit.txt"));
		
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
		variables.put("D15", new Variable<Boolean>("D15"));
		variables.put("D14", new Variable<Boolean>("D14"));
		variables.put("D13", new Variable<Boolean>("D13"));
		variables.put("D12", new Variable<Boolean>("D12"));
		variables.put("D11", new Variable<Boolean>("D11"));
		variables.put("D10", new Variable<Boolean>("D10"));
		variables.put("D9", new Variable<Boolean>("D9"));
		variables.put("D8", new Variable<Boolean>("D8"));
		variables.put("D7", new Variable<Boolean>("D7"));
		variables.put("D6", new Variable<Boolean>("D6"));
		variables.put("D5", new Variable<Boolean>("D5"));
		variables.put("D4", new Variable<Boolean>("D4"));
		variables.put("D3", new Variable<Boolean>("D3"));
		variables.put("D2", new Variable<Boolean>("D2"));
		variables.put("D1", new Variable<Boolean>("D1"));
		variables.put("D0", new Variable<Boolean>("D0"));
		variables.put("A3", new Variable<Boolean>("A3"));
		variables.put("A2", new Variable<Boolean>("A2"));
		variables.put("A1", new Variable<Boolean>("A1"));
		variables.put("A0", new Variable<Boolean>("A0"));
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
		terminals.add(variables.get("D15"));
		terminals.add(variables.get("D14"));
		terminals.add(variables.get("D13"));
		terminals.add(variables.get("D12"));
		terminals.add(variables.get("D11"));
		terminals.add(variables.get("D10"));
		terminals.add(variables.get("D9"));
		terminals.add(variables.get("D8"));
		terminals.add(variables.get("D7"));
		terminals.add(variables.get("D6"));
		terminals.add(variables.get("D5"));
		terminals.add(variables.get("D4"));
		terminals.add(variables.get("D3"));
		terminals.add(variables.get("D2"));
		terminals.add(variables.get("D1"));
		terminals.add(variables.get("D0"));
		terminals.add(variables.get("A3"));
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
        	variables.get("A3").setValue(in[3]);
        	variables.get("D0").setValue(in[4]);
        	variables.get("D1").setValue(in[5]);
        	variables.get("D2").setValue(in[6]);
        	variables.get("D3").setValue(in[7]);
        	variables.get("D4").setValue(in[8]);
        	variables.get("D5").setValue(in[9]);
        	variables.get("D6").setValue(in[10]);
        	variables.get("D7").setValue(in[11]);
        	variables.get("D8").setValue(in[12]);
        	variables.get("D9").setValue(in[13]);
        	variables.get("D10").setValue(in[14]);
        	variables.get("D11").setValue(in[15]);
        	variables.get("D12").setValue(in[16]);
        	variables.get("D13").setValue(in[17]);
        	variables.get("D14").setValue(in[18]);
        	variables.get("D15").setValue(in[19]);
        	
            if (program.evaluate() == chooseResult(in)) {
                score++;
            }
        }
        
        return 1048576 - score;
	}
	
    private boolean chooseResult(boolean[] input) {
    	// scoring solution
        String locator = "";
        if(input[0]==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        if(input[1]==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        if(input[2]==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        if(input[3]==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        
        int location = (15 - Integer.parseInt(locator, 2)) + 4;        
        return input[location];
    }
	
	public static void main(String[] args) {
		System.out.println("20 Bit MUX running...");
		GPController.run(new Multiplexer20Bit());
	}
}
