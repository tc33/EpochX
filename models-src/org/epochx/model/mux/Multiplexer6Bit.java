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
package org.epochx.model.mux;

import java.util.*;

import org.epochx.core.GPAbstractModel;
import org.epochx.representation.*;
import org.epochx.representation.bool.*;
import org.epochx.tools.util.BoolUtils;


/**
 * 
 */
public class Multiplexer6Bit extends GPAbstractModel<Boolean> {

	private boolean[][] inputs;
	
	private HashMap<String, Variable<Boolean>> variables;	
	
	public Multiplexer6Bit() {
		inputs = BoolUtils.generateBoolSequences(6);
		variables = new HashMap<String, Variable<Boolean>>();
		
		configure();
	}
	
	public void configure() {
		// Define variables.
		variables.put("D3", new Variable<Boolean>("D3"));
		variables.put("D2", new Variable<Boolean>("D2"));
		variables.put("D1", new Variable<Boolean>("D1"));
		variables.put("D0", new Variable<Boolean>("D0"));
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
		terminals.add(variables.get("D3"));
		terminals.add(variables.get("D2"));
		terminals.add(variables.get("D1"));
		terminals.add(variables.get("D0"));
		terminals.add(variables.get("A1"));
		terminals.add(variables.get("A0"));
		
		return terminals;
	}
	
	@Override
	public double getFitness(GPCandidateProgram<Boolean> program) {
        double score = 0;
        
        // Execute on all possible inputs.
        for (boolean[] in: inputs) {        	
        	// Set the variables.
        	variables.get("A0").setValue(in[0]);
        	variables.get("A1").setValue(in[1]);
        	variables.get("D0").setValue(in[2]);
        	variables.get("D1").setValue(in[3]);
        	variables.get("D2").setValue(in[4]);
        	variables.get("D3").setValue(in[5]);
        	
            if (program.evaluate() == chooseResult(in)) {
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
}