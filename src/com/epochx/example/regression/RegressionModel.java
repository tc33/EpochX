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
import com.epochx.core.representation.*;

/**
 * 
 */
public class RegressionModel implements GPModel<Double> {

	@Override
	public GPConfig getConfiguration() {
		GPConfig config = new GPConfig();		
		
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
		
		// Define function set.
		List<FunctionNode<Double>> functions = new ArrayList<FunctionNode<Double>>();
		functions.add(new AddFunction(null, null));
		functions.add(new SubtractFunction(null, null));
		functions.add(new MultiplyFunction(null, null));
		functions.add(new ProtectedDivisionFunction(null, null));
		
		return config;
	}

	@Override
	public double getFitness(CandidateProgram<Double> program) {
		Double result = program.evaluate();
		
		// How good is this result?
		return 0;
	}
}
