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
package org.epochx.gx.model;

import org.epochx.gx.representation.*;
import org.epochx.representation.*;


/**
 * XGX model for a quartic symbolic regression problem.
 * 
 * <p>
 * The target program is the function: x + x^2 + x^3 + x^4
 */
public class QuarticRegression extends GXModel {

	// The error each point must be within.
	private static final double POINT_ERROR = 0.01;
	
	// Inputs and associated outputs.
	private double[] inputs;
	private double[] outputs;
	
	// The Variable objects for the arguments.
	private final Variable[] arguments;
	
	/**
	 * Constructs an instance of the QuarticRegression model with 50 input 
	 * points.
	 */
	public QuarticRegression(final int noPoints) {
		setMethodName("getQuarticRegression");
		setReturnType(DataType.DOUBLE);
		
		// Generate the random inputs and the correct outputs.
		inputs = new double[noPoints];
		outputs = new double[inputs.length];
		for (int i=0; i<inputs.length; i++) {
			inputs[i] = (getRNG().nextDouble() * 2) - 1.0;
			outputs[i] = getCorrectResult(inputs[i]);
		}
		
		arguments = new Variable[]{new Variable(DataType.DOUBLE, "X")};
		getVariableHandler().setParameters(arguments);
	}
	
	/**
	 * Calculates the fitness score for the given program. The fitness of a 
	 * program is the number of inputs the program incorrectly calculates the 
	 * output for. The output must be within 0.01 of the correct result to be 
	 * considered correct. All programs are evaluated against the same inputs 
	 * which are randomly selected between -1.0 and 1.0. The number of inputs 
	 * can be provided as an argument to the constructor or defaults to 50.
	 * 
	 * @param p {@inheritDoc}
	 * @return the calculated fitness for the given program.
	 */	
	@Override
	public double getFitness(final CandidateProgram p) {
		final GXCandidateProgram program = (GXCandidateProgram) p;
		final Method method = program.getMethod();
		
		final VariableHandler vars = getVariableHandler();
		
		int noWrong = 0;
		
		for (int i=0; i<inputs.length; i++) {
			vars.reset();
			vars.setParameterValue("X", inputs[i]);
			Double result = (Double) method.evaluate(vars);
			
			if (result == null) {
				noWrong++;
			} else if (Math.abs(result - outputs[i]) > POINT_ERROR) {
				noWrong++;
			}
		}
		
		// How good is this result?
		return noWrong;
	}
	
	/**
	 * The actual function we are trying to evolve.
	 */
    public double getCorrectResult(final double x){
		return x + x*x + x*x*x + x*x*x*x;
    }

}
