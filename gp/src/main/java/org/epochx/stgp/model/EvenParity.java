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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.stgp.model;

import java.util.*;

import org.epochx.core.*;
import org.epochx.epox.*;
import org.epochx.epox.bool.*;
import org.epochx.epox.lang.IfFunction;
import org.epochx.fitness.HitsCountEvaluator;
import org.epochx.interpret.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stgp.STGPIndividual;
import org.epochx.stgp.source.GPInterpreter;
import org.epochx.tools.util.BoolUtils;

/**
 * GP model for the even parity problems.
 * 
 * <h4>Even parity problem</h4>
 * 
 * Given n binary inputValues, a program that solves the even-n-parity problem
 * will
 * return true in all circumstances where an even number of the inputValues are
 * true
 * (or 1), and return false whenever there is an odd number of true inputValues.
 */
public class EvenParity extends GPModel {

	// The boolean input sequences.
	private final Boolean[][] inputValues;
	
	private Boolean[] expectedResults;

	/**
	 * Constructs an EvenParity model for the given number of inputs.
	 * 
	 * @param noInputBits the number of inputs the even parity problem should be
	 *        for
	 */
	public EvenParity(Evolver evolver, final int noInputBits) {
		super(evolver);
		
		// Generate the input sequences.
		inputValues = BoolUtils.generateBoolSequences(noInputBits);
		expectedResults = new Boolean[inputValues.length];

		// Define functions.
		final List<Node> syntax = new ArrayList<Node>();
		syntax.add(new IfFunction());
		syntax.add(new AndFunction());
		syntax.add(new OrFunction());
		syntax.add(new NotFunction());

		// Define terminal variables.
		String[] varNames = new String[noInputBits];

		// Add data inputs.
		for (int i = 0; i < noInputBits; i++) {
			varNames[i] = "d" + i;
			syntax.add(new Variable(varNames[i], Boolean.class));
		}
		
		Parameters params = new Parameters(varNames);
		for (int i=0; i<inputValues.length; i++) {
			params.addParameterSet(inputValues[i]);
			expectedResults[i] = isEvenNoTrue(inputValues[i]);
		}
		
		setSyntax(syntax);
		setReturnType(Boolean.class);

		setFitnessEvaluator(new HitsCountEvaluator<STGPIndividual>(new GPInterpreter(evolver), params, expectedResults));
	}

	/*
	 * Calculate what the correct response should be for the given inputs.
	 */
	private boolean isEvenNoTrue(final Boolean[] input) {
		int noTrues = 0;

		for (final boolean b: input) {
			if (b) {
				noTrues++;
			}
		}

		return ((noTrues % 2) == 0);
	}

}
