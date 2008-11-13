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
import com.epochx.core.initialisation.*;
import com.epochx.core.representation.*;
import com.epochx.core.selection.*;

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
		setPopulationSize(600);
		setCrossoverProbability(0.9);
		setMutationProbability(0.0);
		setReproductionProbability(0.1);
		setInitialiser(new GrowInitialiser<Double>(this, null));
		setPouleSelector(new TournamentSelector<Double>(4, this));
		setPouleSize(50);
		setNoGenerations(100);
		setMaxDepth(10);
		setNoRuns(10);
	}

	@Override
	public List<FunctionNode<?>> getFunctions() {
		// Define function set.
		List<FunctionNode<?>> functions = new ArrayList<FunctionNode<?>>();
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
	public List<TerminalNode<?>> getTerminals() {
		// Define terminal set.
		List<TerminalNode<?>> terminals = new ArrayList<TerminalNode<?>>();
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
		double[] inputs = new double[]{0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
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
		return Math.pow(x, 2) / 2;
	}
	
	public static void main(String[] args) {
		GPController.run(new RegressionModel());
	}
}
