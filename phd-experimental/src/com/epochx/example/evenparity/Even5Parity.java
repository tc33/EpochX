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

import java.io.File;
import java.util.*;

import com.epochx.core.*;
import com.epochx.core.crossover.UniformPointCrossover;
import com.epochx.core.mutation.PointMutation;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;
import com.epochx.func.bool.*;
import com.epochx.stats.*;
import com.epochx.util.*;

/**
 * 
 */
public class Even5Parity extends GPAbstractModel<Boolean> {

	private List<String> inputs;
	private HashMap<String, Variable<Boolean>> variables = new HashMap<String, Variable<Boolean>>();
	
	public Even5Parity() {
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
		setNoGenerations(100);
		setMutationProbability(0.1);
		setCrossoverProbability(0.85);
		setReproductionProbability(0.1);
		setNoRuns(5);
		setPouleSize(50);
		setNoElites(50);
		setMaxDepth(6);
		setPouleSelector(new TournamentSelector<Boolean>(7, this));
		setParentSelector(new RandomSelector<Boolean>());
		setCrossover(new UniformPointCrossover<Boolean>());
		setMutator(new PointMutation<Boolean>(this.getSyntax(), 0.1));
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
		System.out.print(gen);
		System.out.print(" ");
		for (Object s: stats) {
			if (s instanceof double[]) {
				double[] ds = (double[]) s;
				for (double d: ds) {
					System.out.print(d);
					System.out.print(" ");
				}
			} else {
				System.out.print(s);
				System.out.print(" ");
			}
		}
		System.out.println();
	}

	/* (non-Javadoc)
	 * @see com.epochx.stats.GenerationStatListener#getStatFields()
	 */
	@Override
	public GenerationStatField[] getGenStatFields() {
		return new GenerationStatField[]{
				GenerationStatField.RUN_TIME,
				GenerationStatField.LENGTH_AVE,
				GenerationStatField.FITNESS_MIN
		};
	}
	
	/* (non-Javadoc)
	 * @see com.epochx.stats.GenerationStatListener#generationStats(java.lang.String[])
	 */
	@Override
	public void runStats(int runNo, Object[] stats) {
		System.out.println("Run finished...");
		for (Object s: stats) {
			System.out.print(s);
			System.out.print(" ");
		}
		System.out.println();
	}
	
	public RunStatField[] getRunStatFields() {
		return new RunStatField[]{
				RunStatField.RUN_TIME,
				RunStatField.BEST_FITNESS,
				RunStatField.BEST_PROGRAM
		};
	}
	
	
	/*@Override
	public void crossoverStats(Object[] stats) {
		System.out.println("Crossover:");
		for (Object s: stats) {
			System.out.print(s);
			System.out.print(" ");
		}
		System.out.println();
	}*/
	
	/* (non-Javadoc)
	 * @see com.epochx.core.GPAbstractModel#getCrossoverStatFields()
	 */
	/*@Override
	public CrossoverStatField[] getCrossoverStatFields() {
		return new CrossoverStatField[]{
				CrossoverStatField.RUN_TIME
		};
	}*/
}