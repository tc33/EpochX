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
package org.epochx.gp.model.majority;

import java.util.*;

import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.*;
import org.epochx.gp.representation.bool.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.util.BoolUtils;


/**
 * 
 */
public class Majority5 extends GPModel {

	private boolean[][] inputs;
	
	private HashMap<String, BooleanVariable> variables;
	
	public Majority5() {
		inputs = BoolUtils.generateBoolSequences(5);
		variables = new HashMap<String, BooleanVariable>();
		
		configure();
	}
	
	private void configure() {
		// Define variables.
		variables.put("D4", new BooleanVariable("D4"));
		variables.put("D3", new BooleanVariable("D3"));
		variables.put("D2", new BooleanVariable("D2"));
		variables.put("D1", new BooleanVariable("D1"));
		variables.put("D0", new BooleanVariable("D0"));
		
		// Define functions.
		List<Node> syntax = new ArrayList<Node>();
		syntax.add(new IfFunction());
		syntax.add(new AndFunction());
		syntax.add(new OrFunction());
		syntax.add(new NotFunction());
	
		// Define terminals.
		syntax.add(variables.get("D4"));
		syntax.add(variables.get("D3"));
		syntax.add(variables.get("D2"));
		syntax.add(variables.get("D1"));
		syntax.add(variables.get("D0"));
		
		setSyntax(syntax);
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GPCandidateProgram program = (GPCandidateProgram) p;
		
        double score = 0;
        
        // Execute on all possible inputs.
        for (boolean[] in: inputs) {        	
        	// Set the variables.
        	variables.get("D0").setValue(in[0]);
        	variables.get("D1").setValue(in[1]);
        	variables.get("D2").setValue(in[2]);
        	variables.get("D3").setValue(in[3]);
        	variables.get("D4").setValue(in[4]);
        	
            if ((Boolean) program.evaluate() == chooseResult(in)) {
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
}
