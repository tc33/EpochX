/*  
 *  Copyright 2007-2010 Tom Castle & Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming software for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 *  The latest version is available from: http:/www.epochx.org
 */
package org.epochx.example.regression;

import java.util.*;

import org.epochx.core.*;
import org.epochx.op.crossover.*;
import org.epochx.op.selection.*;
import org.epochx.representation.*;
import org.epochx.representation.dbl.*;
import org.epochx.stats.*;


/**
 * 
 */
public class QuarticRegression extends GPAbstractModel {

	private DoubleVariable x;
	
	public QuarticRegression() {
		configure();
	}
	
	public void configure() {
		// Create variables.
		x = new DoubleVariable("X");
		
		setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE, GenerationStatField.RUN_TIME});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM, RunStatField.RUN_TIME});
		
		// Setup run.
		setPopulationSize(500);
		setCrossoverProbability(0.9);
		setMutationProbability(0.0);
		setPoolSelector(new TournamentSelector(this, 7));
		setProgramSelector(new LinearRankSelector(this, 0.5));
		setCrossover(new UniformPointCrossover(this));
		setPoolSize(50);
		setNoGenerations(50);
		setNoElites(50);
		setMaxProgramDepth(17);
		setNoRuns(1);
	}

	@Override
	public List<Node> getFunctions() {
		// Define function set.
		List<Node> functions = new ArrayList<Node>();
		functions.add(new AddFunction());
		functions.add(new SubtractFunction());
		functions.add(new MultiplyFunction());
		functions.add(new ProtectedDivisionFunction());
		
		return functions;
	}

	/* (non-Javadoc)
	 * @see org.epochx.core.GPModel#getTerminals()
	 */
	@Override
	public List<Node> getTerminals() {
		// Define terminal set.
		List<Node> terminals = new ArrayList<Node>();
		terminals.add(new DoubleLiteral(5d));
		terminals.add(new DoubleLiteral(4d));
		terminals.add(new DoubleLiteral(3d));
		terminals.add(new DoubleLiteral(2d));
		terminals.add(new DoubleLiteral(1d));
		terminals.add(new DoubleLiteral(0d));
		terminals.add(new DoubleLiteral(-5d));
		terminals.add(new DoubleLiteral(-4d));
		terminals.add(new DoubleLiteral(-3d));
		terminals.add(new DoubleLiteral(-2d));
		terminals.add(new DoubleLiteral(-1d));
		
		// Define variables;
		terminals.add(x);
		
		return terminals;
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
		return x + (x*x) + (x*x*x) + (x*x*x*x);
	}
	
	public static void main(String[] args) {
		Controller.run(new QuarticRegression());
	}
}
