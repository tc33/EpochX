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
package org.epochx.gp.model.regression;

import java.util.*;

import org.epochx.gp.model.GPModel;
import org.epochx.gp.representation.*;
import org.epochx.gp.representation.dbl.*;
import org.epochx.representation.CandidateProgram;


/**
 * 
 */
public class CubicRegression extends GPModel {

	private DoubleVariable x;
	
	public CubicRegression() {
		configure();
	}
	
	public void configure() {
		// Create variables.
		x = new DoubleVariable("X");
	}

	@Override
	public List<Node> getSyntax() {
		// Define function set.
		List<Node> syntax = new ArrayList<Node>();
		syntax.add(new AddFunction());
		syntax.add(new SubtractFunction());
		syntax.add(new MultiplyFunction());
		syntax.add(new ProtectedDivisionFunction());
		
		// Define terminal set.
		syntax.add(new DoubleLiteral(5d));
		syntax.add(new DoubleLiteral(4d));
		syntax.add(new DoubleLiteral(3d));
		syntax.add(new DoubleLiteral(2d));
		syntax.add(new DoubleLiteral(1d));
		syntax.add(new DoubleLiteral(0d));
		syntax.add(new DoubleLiteral(-5d));
		syntax.add(new DoubleLiteral(-4d));
		syntax.add(new DoubleLiteral(-3d));
		syntax.add(new DoubleLiteral(-2d));
		syntax.add(new DoubleLiteral(-1d));
		
		// Define variables;
		syntax.add(x);
		
		return syntax;
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GPCandidateProgram program = (GPCandidateProgram) p;
		
		double[] inputs = new double[]{-1.0, -0.9, -0.8, -0.7, -0.6, -0.5, -0.4, -0.3, -0.2, -0.1, 0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
		int noWrong = 0;
		
		for (double in: inputs) {
			x.setValue(in);
			if ((Double) program.evaluate() != getCorrectResult(in)) {
				noWrong++;
			}
		}
		
		// How good is this result?
		return noWrong;
	}
	
	private double getCorrectResult(double x) {
		return x + (x*x) + (x*x*x);
	}
}
