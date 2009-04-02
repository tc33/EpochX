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

import com.epochx.core.*;
import com.epochx.core.crossover.*;
import com.epochx.core.initialisation.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;
import com.epochx.func.dbl.*;
import com.epochx.stats.GenerationStats.*;
import com.epochx.stats.RunStats.*;

/**
 * 
 */
public class RegressionModel extends GPAbstractModel<Double> {

	private Variable<Double> x;
	
	public RegressionModel() {
		configure();
	}
	
	public void configure() {
		// Create variables.
		this.x = new Variable<Double>("X");
		
		// Setup run.
		setPopulationSize(1000);
		setCrossoverProbability(0.85);
		setMutationProbability(0.1);
		setReproductionProbability(0.05);
		setInitialiser(new GrowInitialiser<Double>(this));
		//setPouleSelector(new TournamentSelector<Double>(4, this));
		setParentSelector(new LinearRankSelector<Double>(0.5));
		setPouleSelector(new RandomSelector<Double>());
		setCrossover(new UniformPointCrossover<Double>());
		setPouleSize(10);
		setNoGenerations(1000);
		setNoElites(10);
		setMaxDepth(10);
		setNoRuns(3);
	}

	@Override
	public List<FunctionNode<?>> getFunctions() {
		// Define function set.
		List<FunctionNode<?>> functions = new ArrayList<FunctionNode<?>>();
		functions.add(new AddFunction());
		functions.add(new SubtractFunction());
		functions.add(new MultiplyFunction());
		functions.add(new ProtectedDivisionFunction());
		functions.add(new ExponentFunction());
		functions.add(new InvertFunction());
		functions.add(new LogFunction());
		functions.add(new SineFunction());
		functions.add(new CosineFunction());
		
		return functions;
	}

	/* (non-Javadoc)
	 * @see com.epochx.core.GPModel#getTerminals()
	 */
	@Override
	public List<TerminalNode<?>> getTerminals() {
		// Define terminal set.
		List<TerminalNode<?>> terminals = new ArrayList<TerminalNode<?>>();
//		terminals.add(new TerminalNode<Double>(5d));
//		terminals.add(new TerminalNode<Double>(4d));
//		terminals.add(new TerminalNode<Double>(3d));
//		terminals.add(new TerminalNode<Double>(2d));
		terminals.add(new TerminalNode<Double>(1d));
//		terminals.add(new TerminalNode<Double>(0d));
//		terminals.add(new TerminalNode<Double>(-5d));
//		terminals.add(new TerminalNode<Double>(-4d));
//		terminals.add(new TerminalNode<Double>(-3d));
//		terminals.add(new TerminalNode<Double>(-2d));
//		terminals.add(new TerminalNode<Double>(-1d));
		
		// Define variables;
		terminals.add(x);
		
		return terminals;
	}
	
	@Override
	public double getFitness(CandidateProgram<Double> program) {
		double[] inputs = new double[]{-1.0, -0.9, -0.8, -0.7, -0.6, -0.5, -0.4, -0.3, -0.2, -0.1, 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
		int noWrong = 0;
		
		for (double in: inputs) {
			x.setValue(in);
			if (program.evaluate() != getCorrectResult(in)) {
				noWrong++;
			}
		}
		
		// How good is this result?
		return noWrong;
	}
	
	private double getCorrectResult(double x) {
		return x + (x*x) + (x*x*x) + (x*x*x*x);
		//return Math.pow(x, 2) / 2;
	}
	
	public void runStats(int runNo, Object[] stats) {
		System.out.print("Run number " + runNo + " complete.");
		for (Object s: stats) {
			System.out.print(s);
			System.out.print(" ");
		}
		System.out.println();
	}

	public RunStatField[] getRunStatFields() {
		return new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM};
	}
	
	public void generationStats(int genNo, Object[] stats) {
		System.out.print(genNo + " ");
		for (Object s: stats) {
			System.out.print(s);
			System.out.print(" ");
		}
		System.out.println();
	}
	
	@Override
	public GenStatField[] getGenStatFields() {
		return new GenStatField[]{GenStatField.FITNESS_MIN, GenStatField.FITNESS_AVE, GenStatField.DEPTH_AVE, GenStatField.LENGTH_AVE, GenStatField.RUN_TIME};
	}
	
	public static void main(String[] args) {
		GPController.run(new RegressionModel());
	}
}
