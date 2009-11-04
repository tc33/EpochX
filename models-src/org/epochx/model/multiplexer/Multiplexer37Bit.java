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
package org.epochx.model.multiplexer;

import java.util.*;

import org.epochx.core.*;
import org.epochx.representation.*;
import org.epochx.representation.bool.*;
import org.epochx.util.*;


/**
 * 
 *
 */
public class Multiplexer37Bit extends GPAbstractModel<Boolean> {
	
	private HashMap<String, Variable<Boolean>> variables;
	
	public Multiplexer37Bit() {
		configure();
		
		variables = new HashMap<String, Variable<Boolean>>();
	}
	
	public void configure() {
		// Define variables.
		variables.put("D31", new Variable<Boolean>("D31"));
		variables.put("D30", new Variable<Boolean>("D30"));
		variables.put("D29", new Variable<Boolean>("D29"));
		variables.put("D28", new Variable<Boolean>("D28"));
		variables.put("D27", new Variable<Boolean>("D27"));
		variables.put("D26", new Variable<Boolean>("D26"));
		variables.put("D25", new Variable<Boolean>("D25"));
		variables.put("D24", new Variable<Boolean>("D24"));
		variables.put("D23", new Variable<Boolean>("D23"));
		variables.put("D22", new Variable<Boolean>("D22"));
		variables.put("D21", new Variable<Boolean>("D21"));
		variables.put("D20", new Variable<Boolean>("D20"));
		variables.put("D19", new Variable<Boolean>("D19"));
		variables.put("D18", new Variable<Boolean>("D18"));
		variables.put("D17", new Variable<Boolean>("D17"));
		variables.put("D16", new Variable<Boolean>("D16"));
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
		variables.put("A4", new Variable<Boolean>("A4"));
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
		terminals.add(variables.get("D31"));
		terminals.add(variables.get("D30"));
		terminals.add(variables.get("D29"));
		terminals.add(variables.get("D28"));
		terminals.add(variables.get("D27"));
		terminals.add(variables.get("D26"));
		terminals.add(variables.get("D25"));
		terminals.add(variables.get("D24"));
		terminals.add(variables.get("D23"));
		terminals.add(variables.get("D22"));
		terminals.add(variables.get("D21"));
		terminals.add(variables.get("D20"));
		terminals.add(variables.get("D19"));
		terminals.add(variables.get("D18"));
		terminals.add(variables.get("D17"));
		terminals.add(variables.get("D16"));
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
		terminals.add(variables.get("A4"));
		terminals.add(variables.get("A3"));
		terminals.add(variables.get("A2"));
		terminals.add(variables.get("A1"));
		terminals.add(variables.get("A0"));
		
		return terminals;
	}
	
	@Override
	public double getFitness(CandidateProgram<Boolean> program) {
        double score = 0;
        
        long noInputs = (long) Math.pow(2, 37);
        
        // Execute on all possible inputs.
        for (long i=0; i<noInputs; i++) {
        	boolean[] in = BoolUtils.generateBoolSequence(37, i);
        	
        	// Set the variables.
        	variables.get("A0").setValue(in[0]);
        	variables.get("A1").setValue(in[1]);
        	variables.get("A2").setValue(in[2]);
        	variables.get("A3").setValue(in[3]);
        	variables.get("A4").setValue(in[4]);
        	variables.get("D0").setValue(in[5]);
        	variables.get("D1").setValue(in[6]);
        	variables.get("D2").setValue(in[7]);
        	variables.get("D3").setValue(in[8]);
        	variables.get("D4").setValue(in[9]);
        	variables.get("D5").setValue(in[10]);
        	variables.get("D6").setValue(in[11]);
        	variables.get("D7").setValue(in[12]);
        	variables.get("D8").setValue(in[13]);
        	variables.get("D9").setValue(in[14]);
        	variables.get("D10").setValue(in[15]);
        	variables.get("D11").setValue(in[16]);
        	variables.get("D12").setValue(in[17]);
        	variables.get("D13").setValue(in[18]);
        	variables.get("D14").setValue(in[19]);
        	variables.get("D15").setValue(in[20]);
        	variables.get("D16").setValue(in[21]);
        	variables.get("D17").setValue(in[22]);
        	variables.get("D18").setValue(in[23]);
        	variables.get("D19").setValue(in[24]);
        	variables.get("D20").setValue(in[25]);
        	variables.get("D21").setValue(in[26]);
        	variables.get("D22").setValue(in[27]);
        	variables.get("D23").setValue(in[28]);
        	variables.get("D24").setValue(in[29]);
        	variables.get("D25").setValue(in[30]);
        	variables.get("D26").setValue(in[31]);
        	variables.get("D27").setValue(in[32]);
        	variables.get("D28").setValue(in[33]);
        	variables.get("D29").setValue(in[34]);
        	variables.get("D30").setValue(in[35]);
        	variables.get("D31").setValue(in[36]);
        	
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
        if(input[4]==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        
        int location = (31 - Integer.parseInt(locator, 2)) + 5;
        
        return input[location];
    }
}
