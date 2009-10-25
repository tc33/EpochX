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
package com.epochx.example.regression;

import java.util.*;

import com.epochx.core.GPController;
import com.epochx.op.crossover.*;
import com.epochx.op.initialisation.*;
import com.epochx.op.mutation.*;
import com.epochx.op.selection.*;
import com.epochx.representation.*;
import com.epochx.representation.dbl.*;
import com.epochx.semantics.*;
import com.epochx.stats.*;
import com.epochx.util.FileManip;

/**
 * 
 */
public class RegressionModelQUART extends SemanticModel<Double> {

	private Variable<Double> x;
	private int run = 1;
	
	public RegressionModelQUART() {
		configure();
	}
	
	public void configure() {
		// Create variables.
		this.x = new Variable<Double>("X");
		
		// Setup run.
		setPopulationSize(4000);
		setNoGenerations(50);
		setCrossoverProbability(0.9);
		setMutationProbability(0.0);
		setNoRuns(100);
		setPoolSize(400);
		setNoElites(400);
		setInitialMaxDepth(6);
		setMaxProgramDepth(17);
		setPoolSelector(new TournamentSelector<Double>(this, 7));
		setProgramSelector(new RandomSelector<Double>(this));
		setCrossover(new UniformPointCrossover<Double>(this));
		setStateCheckedCrossover(false);
		setMutator(new SubtreeMutation<Double>(this));
		setStateCheckedMutation(false);
		RegressionSemanticModule semMod = new RegressionSemanticModule(getTerminals(), this);
		setSemanticModule(semMod);
		setInitialiser(new RampedHalfAndHalfInitialiser<Double>(this));
	}

	@Override
	public List<FunctionNode<Double>> getFunctions() {
		// Define function set.
		List<FunctionNode<Double>> functions = new ArrayList<FunctionNode<Double>>();
		functions.add(new AddFunction(null, null));
		functions.add(new SubtractFunction(null, null));
		functions.add(new MultiplyFunction(null, null));
		functions.add(new ProtectedDivisionFunction(null, null));
		
		return functions;
	}

	/* (non-Javadoc)
	 * @see com.epochx.core.GPModel#getTerminals()
	 */
	@Override
	public List<TerminalNode<Double>> getTerminals() {
		// Define terminal set.
		List<TerminalNode<Double>> terminals = new ArrayList<TerminalNode<Double>>();
		terminals.add(new TerminalNode<Double>(5d));
		terminals.add(new TerminalNode<Double>(4d));
		terminals.add(new TerminalNode<Double>(3d));
		terminals.add(new TerminalNode<Double>(2d));
		terminals.add(new TerminalNode<Double>(1d));
		terminals.add(new TerminalNode<Double>(0d));
		terminals.add(new TerminalNode<Double>(-5d));
		terminals.add(new TerminalNode<Double>(-4d));
		terminals.add(new TerminalNode<Double>(-3d));
		terminals.add(new TerminalNode<Double>(-2d));
		terminals.add(new TerminalNode<Double>(-1d));
		
		// Define variables;
		terminals.add(x);
		
		return terminals;
	}
	
	@Override
	public double getFitness(CandidateProgram<Double> program) {
		double[] inputs = new double[21];
		double[] absError = new double[21];
		double startValue = -5;
		
		for(int i = 0; i<=20; i++) {
			inputs[i] = startValue;
			startValue = startValue + 0.5;
		}
		
		for(int i = 0; i<=20; i++) {
			double v = i;
			x.setValue(v);
			absError[i] = Math.abs(this.getCorrectResult(inputs[i]) - program.evaluate());
		}
		
		double totalAbsError = 0;
		for(int i = 0; i<=20; i++) {
			totalAbsError = totalAbsError + absError[i];
		}
		
		// How good is this result?
		return totalAbsError;
	}
	
	private double getCorrectResult(double x) {
		return x + (x*x) + (x*x*x) + (x*x*x*x);
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
		return new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM};
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
		FileManip.doOutput(null, output, "GenerationStats.txt", true);
	}

	@Override
	public GenerationStatField[] getGenStatFields() {
		return new GenerationStatField[]{GenerationStatField.FITNESS_AVE, GenerationStatField.FITNESS_MIN, GenerationStatField.LENGTH_AVE};
	}
	
	public static void main(String[] args) {
		GPController.run(new RegressionModelQUART());
	}
}
