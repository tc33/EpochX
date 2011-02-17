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

import org.epochx.interpret.*;
import org.epochx.representation.CandidateProgram;


/**
 * 
 */
public class HitsCountEvaluator extends FitnessEvaluator {

	// The error each point must be within.
	private double pointError;
	
	private Interpreter interpreter;
	
	private Parameters params;
	private Object[] expectedOutputs;
	
	private double malformedPenalty;
	
	public HitsCountEvaluator(Interpreter interpreter, Parameters params, Object[] expectedOutputs) {
		this.interpreter = interpreter;
		this.params = params;
		this.expectedOutputs = expectedOutputs;
		
		malformedPenalty = expectedOutputs.length;
	}
	
	public HitsCountEvaluator(Interpreter interpreter, Parameters params, Double[] expectedOutputs, double error) {
		this(interpreter, params, expectedOutputs);
		
		this.pointError = error;
	}
	
	@Override
	public double getFitness(CandidateProgram program) {
		double noWrong = 0.0;
		
		Object[] results;
		try {
			results = interpreter.eval(program, params);
		} catch (MalformedProgramException e) {
			return malformedPenalty;
		}
		
		for (int i = 0; i < expectedOutputs.length; i++) {
			Object result = results[i];

			if (isHit(result, expectedOutputs[i])) {
				noWrong++;
			}
		}

		return noWrong;
	}
	
	public double getMalformedProgramPenalty() {
		return malformedPenalty;
	}
	
	public void setMalformedProgramPenalty(double malformedPenalty) {
		this.malformedPenalty = malformedPenalty;
	}
	
	public double getPointError() {
		return pointError;
	}
	
	public void setPointError(double pointError) {
		this.pointError = pointError;
	}

	protected boolean isHit(Object result, Object expectedResult) {
		if (result instanceof Double && expectedResult instanceof Double) {
			double dblResult = (Double) result;
			double dblExpectedResult = (Double) expectedResult;
			
			double error = Math.abs(dblResult - dblExpectedResult);
			
			return (error == Double.NaN || error <= pointError);
		} else {
			return result.equals(expectedResult);
		}
	}
}
