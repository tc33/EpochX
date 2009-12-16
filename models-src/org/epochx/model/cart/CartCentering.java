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
package org.epochx.model.cart;

import java.util.*;

import org.epochx.core.*;
import org.epochx.op.crossover.*;
import org.epochx.op.mutation.*;
import org.epochx.op.selection.*;
import org.epochx.representation.*;
import org.epochx.representation.dbl.*;
import org.epochx.stats.*;

/**
 *
 */
public class CartCentering extends GPAbstractModel {

	// The rocket force in newtons.
	private static final double F = 1.0;
	
	// The mass of the cart in kg.
	private static final double m = 2.0;
	
	// How close to zero the position(m) and velocity(ms^-1) need to get.
	private static final double targetRange = 0.05;
	
	// The acceleration based upon a = F/m
	private final double a;
	
	private DoubleVariable varX;
	private DoubleVariable varV;
	
	private double[] testPositions;
	private double[] testVelocities;
	
	public CartCentering() {
		a = F/m;
		
		testPositions = new double[20];
		testVelocities = new double[20];
		
		// Generate random test data in range -0.75 to +0.75.
		for (int i=0; i<testPositions.length; i++) {
			testPositions[i] = (Math.random() * 1.5) - 0.75;
			testVelocities[i] = (Math.random() * 1.5) - 0.75;
		}
		
		configure();
	}
	
	public void configure() {
		// Create variables.
		varX = new DoubleVariable("X");
		varV = new DoubleVariable("V");
	}

	@Override
	public List<Node> getFunctions() {
		// Define function set.
		List<Node> functions = new ArrayList<Node>();
		functions.add(new AddFunction());
		functions.add(new SubtractFunction());
		functions.add(new MultiplyFunction());
		functions.add(new ProtectedDivisionFunction());
		functions.add(new GreaterThanFunction());
		functions.add(new AbsoluteFunction());
		
		return functions;
	}

	@Override
	public List<Node> getTerminals() {
		// Define terminal set.
		List<Node> terminals = new ArrayList<Node>();
		
		// Define variables;
		terminals.add(varX);
		terminals.add(varV);
		
		return terminals;
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GPCandidateProgram program = (GPCandidateProgram) p;
		
		// Total time taken for all fitness cases in ms.
		int totalTime = 0;

		/*FunctionParser<Double> parser = new FunctionParser<Double>();
		parser.addAvailableVariable(varV);
		parser.addAvailableVariable(varX);
		Node<Double> nodeTree = parser.parse("GT(MUL(-1.0 X) MUL(V ABS(V)))");
		program = new CandidateProgram<Double>(nodeTree, this);*/
		
		for (int i=0; i<testPositions.length; i++) {
			// Set the initial variable values.
			varX.setValue(testPositions[i]);
			varV.setValue(testVelocities[i]);
			
			/*
			 * Note that we use milliseconds to avoid problems with accuracy of doubles.
			 */
			
			// Milliseconds to increment each timestep.
			int msecs = 20;
			// Max time allowed (ten seconds in millisecs).
			int maxTime = 10000;
			// Convert timestep to seconds for force calculation.
			double timestep = (double) msecs / 1000;
			// Amount of time passed (ms).
			int t = 0;
			
			while (t < maxTime) {
				// Is the cart centered?
				if ((varV.getValue() >= 0-targetRange) 
						&& (varV.getValue() <= 0+targetRange) 
						&& (varX.getValue() >= 0-targetRange)
						&& (varX.getValue() <= 0+targetRange)) {
					break;
				}
				
				// Execute with current position and velocity.
				double u = (Double) program.evaluate();
				
				// Make 'u' either +1.0 or -1.0 using sign value.
				u = Math.signum(u);
				
				// Calculate new position and velocity based on u.
				double v = varV.getValue() + (timestep * (a*u));
				double x = varX.getValue() + (timestep * varV.getValue());
				
				//System.out.println(v + " " + x + " " + u);
				
				// Update the program variables.
				varV.setValue(v);
				varX.setValue(x);
				
				// Increase time by 0.02 seconds.
				t += msecs;
			}
			
			totalTime += t;
		}
		
		// Return total time in seconds.
		return ((double) totalTime) / 1000;
	}
	
	public static void main(String[] args) {
		GPAbstractModel model = new CartCentering();
		model.setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE, GenerationStatField.RUN_TIME});
		model.setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM, RunStatField.RUN_TIME});
		
		model.setPopulationSize(500);
		model.setNoGenerations(100);
		model.setMutationProbability(0.1);
		model.setCrossoverProbability(0.9);
		model.setNoRuns(5);
		model.setPoolSize(50);
		model.setNoElites(50);
		model.setMaxProgramDepth(5);
		model.setPoolSelector(new TournamentSelector(model, 7));
		model.setProgramSelector(new RandomSelector(model));
		model.setCrossover(new UniformPointCrossover(model));
		model.setMutator(new PointMutation(model, 0.1));
		
		Controller.run(model);
	}
	
	private double solution(double x, double v) {
		if ((-1.0*x) > (v*Math.abs(v))) {
			return 1.0;
		} else {
			return -1.0;
		}
	}

}
