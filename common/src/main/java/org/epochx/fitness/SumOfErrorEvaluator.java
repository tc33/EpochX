/* 
 * Copyright 2007-2011 Tom Castle & Lawrence Beadle
 * Licensed under GNU Lesser General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.fitness;

import org.epochx.Individual;
import org.epochx.interpret.*;


/**
 * 
 */
public class SumOfErrorEvaluator<T extends Individual> extends AbstractFitnessEvaluator<T> {

	private Interpreter<T> interpreter;
	
	private Parameters params;
	private double[] outputs;
	
	public SumOfErrorEvaluator(Interpreter<T> interpreter, Parameters params, double[] expectedOutputs) {
		this.interpreter = interpreter;
		this.params = params;
		this.outputs = expectedOutputs;
	}
	
	@Override
	public double getFitness(T program) {
		double errorSum = 0.0;
		
		//TODO Could optionally throw an exception or have settable penalty?
		Object[] results;
		try {
			results = interpreter.eval(program, params);
		} catch (MalformedProgramException e) {
			return Double.NaN;
		}
		
		for (int i = 0; i < outputs.length; i++) {
			Object result = results[i];
			
			if (result instanceof Double) {
				double d = (Double) result;
				final double error = Math.abs(d - outputs[i]);

				if (Double.isNaN(d)) {
					errorSum += error;
				} else {
					errorSum = Double.NaN;
					break;
				}
			}
		}

		// How good is this result?
		return errorSum;
	}

}
