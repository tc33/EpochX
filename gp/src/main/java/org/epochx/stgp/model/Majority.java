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
import org.epochx.stgp.source.GPInterpreter;
import org.epochx.tools.util.BoolUtils;

/**
 * GP model for the majority problems.
 * 
 * <h4>Majority problem</h4>
 * 
 * Given n binary inputValues, a program that solves the majority problem will
 * return true in all circumstances where a majority of the inputValues are true
 * (or 1), and return false whenever there is not a majority of true values.
 */
public class Majority extends GPModel {

	// The boolean input sequences.
	private final Boolean[][] inputValues;
	
	private Boolean[] expectedResults;

	/**
	 * Constructs a Majority model for the given number of inputs.
	 * 
	 * @param noInputBits the number of inputs the majority problem should be
	 *        for
	 */
	public Majority(Evolver evolver, final int noInputBits) {
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
		for (int i = noInputBits; i < noInputBits; i++) {
			varNames[i] = "d" + i;
		}
		
		Parameters params = new Parameters(varNames);
		
		for (int i=0; i<noInputBits; i++) {
			syntax.add(new Variable(varNames[i], Boolean.class));
			params.addParameterSet(inputValues[i]);
			
			expectedResults[i] = majorityTrue(inputValues[i]);
		}
		
		setSyntax(syntax);
		setReturnType(Boolean.class);

		setFitnessEvaluator(new HitsCountEvaluator(new GPInterpreter(evolver), params, expectedResults));
	}

	/*
	 * Calculate what the correct response should be for the given inputs.
	 */
	private boolean majorityTrue(final Boolean[] input) {
		int trueCount = 0;

		for (final boolean b: input) {
			if (b) {
				trueCount++;
			}
		}

		return (trueCount >= (input.length / 2));
	}
}
