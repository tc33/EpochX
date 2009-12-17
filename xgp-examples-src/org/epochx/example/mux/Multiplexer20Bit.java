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
package org.epochx.example.mux;

import java.util.*;

import org.epochx.core.*;
import org.epochx.op.crossover.UniformPointCrossover;
import org.epochx.op.selection.*;
import org.epochx.representation.*;
import org.epochx.representation.bool.*;
import org.epochx.stats.*;
import org.epochx.tools.util.BoolUtils;


/**
 * 
 *
 */
public class Multiplexer20Bit extends GPAbstractModel {

	private boolean[][] inputs;
	
	private HashMap<String, BooleanVariable> variables = new HashMap<String, BooleanVariable>();
	
	public Multiplexer20Bit() {
		inputs = BoolUtils.generateBoolSequences(20);
		
		configure();
	}
	
	public void configure() {
		// Define variables.
		variables.put("D15", new BooleanVariable("D15"));
		variables.put("D14", new BooleanVariable("D14"));
		variables.put("D13", new BooleanVariable("D13"));
		variables.put("D12", new BooleanVariable("D12"));
		variables.put("D11", new BooleanVariable("D11"));
		variables.put("D10", new BooleanVariable("D10"));
		variables.put("D9", new BooleanVariable("D9"));
		variables.put("D8", new BooleanVariable("D8"));
		variables.put("D7", new BooleanVariable("D7"));
		variables.put("D6", new BooleanVariable("D6"));
		variables.put("D5", new BooleanVariable("D5"));
		variables.put("D4", new BooleanVariable("D4"));
		variables.put("D3", new BooleanVariable("D3"));
		variables.put("D2", new BooleanVariable("D2"));
		variables.put("D1", new BooleanVariable("D1"));
		variables.put("D0", new BooleanVariable("D0"));
		variables.put("A3", new BooleanVariable("A3"));
		variables.put("A2", new BooleanVariable("A2"));
		variables.put("A1", new BooleanVariable("A1"));
		variables.put("A0", new BooleanVariable("A0"));
		
		setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE, GenerationStatField.RUN_TIME});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM, RunStatField.RUN_TIME});
		
		setPopulationSize(500);
		setNoGenerations(50);
		setCrossoverProbability(0.9);
		setMutationProbability(0.0);
		setNoRuns(1);
		setPoolSize(50);
		setNoElites(50);
		setMaxProgramDepth(6);
		setPoolSelector(new TournamentSelector(this, 7));
		setProgramSelector(new RandomSelector(this));
		setCrossover(new UniformPointCrossover(this));
	}
	
	@Override
	public List<Node> getFunctions() {
		// Define functions.
		List<Node> functions = new ArrayList<Node>();
		functions.add(new IfFunction());
		functions.add(new AndFunction());
		functions.add(new OrFunction());
		functions.add(new NotFunction());
		return functions;
	}

	@Override
	public List<Node> getTerminals() {		
		// Define terminals.
		List<Node> terminals = new ArrayList<Node>();
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
	public double getFitness(CandidateProgram p) {
		GPCandidateProgram program = (GPCandidateProgram) p;
		
        double score = 0;
        
        // Execute on all possible inputs.
        for (boolean[] in: inputs) {        	
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
        	
            if ((Boolean) program.evaluate() == chooseResult(in)) {
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
		Controller.run(new Multiplexer20Bit());
	}
}
