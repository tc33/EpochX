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
import com.epochx.core.crossover.UniformPointCrossover;
import com.epochx.core.initialisation.RegressionSemanticallyDrivenInitialiser;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;
import com.epochx.func.dbl.*;
import com.epochx.semantics.*;
import com.epochx.stats.*;

/**
 * 
 */
public class RegressionModel extends SemanticModel<Double> {

	private Variable<Double> x;
	
	public RegressionModel() {
		configure();
	}
	
	public void configure() {
		// Create variables.
		this.x = new Variable<Double>("X");
		
		// Setup run.
		setPopulationSize(500);
		setCrossoverProbability(0.9);
		setReproductionProbability(0.1);
		setSemanticModule(new RegressionSemanticModule(getTerminals(), this));
		setInitialiser(new RegressionSemanticallyDrivenInitialiser(this, getSemanticModule()));
		setPouleSelector(new TournamentSelector<Double>(7, this));
		setParentSelector(new LinearRankSelector<Double>(0.5));
		//setPouleSelector(new RandomSelector<Double>());
		setCrossover(new UniformPointCrossover<Double>());
		setPouleSize(50);
		setNoGenerations(50);
		setNoElites(50);
		setMaxDepth(17);
		setNoRuns(1);
	}

	@Override
	public List<FunctionNode<Double>> getFunctions() {
		// Define function set.
		List<FunctionNode<Double>> functions = new ArrayList<FunctionNode<Double>>();
		functions.add(new AddFunction());
		functions.add(new SubtractFunction());
		functions.add(new MultiplyFunction());
		functions.add(new ProtectedDivisionFunction());
		
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
	public GenerationStatField[] getGenStatFields() {
		return new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.DEPTH_AVE, GenerationStatField.LENGTH_AVE, GenerationStatField.RUN_TIME};
	}
	
	public static void main(String[] args) {
		GPController.run(new RegressionModel());
	}
}
