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
package com.epochx.example.evenparity;

import java.io.*;
import java.util.*;
import com.epochx.core.*;
import com.epochx.core.crossover.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;
import com.epochx.util.FileManip;
import com.epochx.semantics.*;
import com.epochx.stats.*;
import com.epochx.stats.GenerationStats.*;
import com.epochx.util.*;

/**
 * 
 */
public class Even5Parity extends GPAbstractModel<Boolean> implements GenerationStatListener {

	private List<String> inputs;
	private HashMap<String, Variable<Boolean>> variables = new HashMap<String, Variable<Boolean>>();
	
	public Even5Parity() {
		inputs = new ArrayList<String>();
		inputs = FileManip.loadInput(new File("input5bit.txt"));
		
		configure();
	}
	
	public void configure() {
		setPopulationSize(500);
		setNoGenerations(50);
		setCrossoverProbability(0.9);
		setReproductionProbability(0.1);
		setNoRuns(5);
		setPouleSize(50);
		setNoElites(50);
		setMaxDepth(6);
		setPouleSelector(new TournamentSelector<Boolean>(7, this));
		setParentSelector(new RandomSelector<Boolean>());
		setCrossover(new UniformPointCrossover<Boolean>());
		
		// Define variables.
		variables.put("D4", new Variable<Boolean>("D4"));
		variables.put("D3", new Variable<Boolean>("D3"));
		variables.put("D2", new Variable<Boolean>("D2"));
		variables.put("D1", new Variable<Boolean>("D1"));
		variables.put("D0", new Variable<Boolean>("D0"));
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
		GPController.run(new Even5Parity());
	}

	/* (non-Javadoc)
	 * @see com.epochx.stats.GenerationStatListener#generationStats(java.lang.String[])
	 */
	@Override
	public void generationStats(int gen, Object[] stats) {
		for (Object s: stats) {
			System.out.print(s);
			System.out.print(" ");
		}
		System.out.println();
	}

	/* (non-Javadoc)
	 * @see com.epochx.stats.GenerationStatListener#getStatFields()
	 */
	@Override
	public GenStatField[] getStatFields() {
		return new GenStatField[]{
				GenStatField.DEPTH_AVE,
				GenStatField.LENGTH_AVE
		};
	}
}
