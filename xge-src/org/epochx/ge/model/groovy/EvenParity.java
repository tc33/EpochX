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
package org.epochx.ge.model.groovy;

import org.epochx.core.*;
import org.epochx.fitness.HitsCountEvaluator;
import org.epochx.ge.model.GEModel;
import org.epochx.interpret.*;
import org.epochx.tools.grammar.Grammar;
import org.epochx.tools.util.BoolUtils;

/**
 * Grammar model for the even parity problems using a Groovy grammar.
 * 
 * <h4>Even parity problem</h4>
 * 
 * Given n binary inputValues, a program that solves the even-n-parity problem
 * will return true in all circumstances where an even number of the inputValues
 * are true (or 1), and return false whenever there is an odd number of true
 * inputValues.
 */
public class EvenParity extends GEModel {

	// Incomplete grammar requiring correct number of terminals to be added.
	public static final String GRAMMAR_FRAGMENT = "<prog> ::= <expr>\n"
			+ "<expr> ::= <expr> <op> <expr> "
			+ "| ( <expr> <op> <expr> ) "
			+ "| <var> "
			+ "| <pre-op> ( <var> )\n"
			+ "<pre-op> ::= !\n"
			+ "<op> ::= \"|\" | & | ^ \n"
			+ "<var> ::= ";

	// The names of the inputValues used in the grammar.
	private final String[] argNames;

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

		// Determine the input argument names.
		argNames = new String[noInputBits];
		for (int i = 0; i < noInputBits; i++) {
			argNames[i] = "d" + i;
		}

		// Complete the grammar string and construct grammar instance.
		setGrammar(new Grammar(getGrammarString()));
		
		Parameters params = new Parameters(argNames);
		
		for (int i=0; i<noInputBits; i++) {
			params.addParameterSet(inputValues[i]);
			
			expectedResults[i] = isEvenNoTrue(inputValues[i]);
		}
		
		setFitnessEvaluator(new HitsCountEvaluator(new GroovyInterpreter(), params, expectedResults));
	}

	/**
	 * Constructs and returns the full grammar string for the even parity
	 * problem with the correct number of input bits.
	 * 
	 * @return the grammar string for the even parity problem with the set
	 *         number of input bits
	 */
	public String getGrammarString() {
		final StringBuilder buffer = new StringBuilder(GRAMMAR_FRAGMENT);
		for (int i = 0; i < argNames.length; i++) {
			if (i > 0) {
				buffer.append(" | ");
			}
			buffer.append(argNames[i]);
		}
		buffer.append('\n');

		return buffer.toString();
	}

	/*
	 * Calculate what the correct response should be for the given inputs.
	 */
	private Boolean isEvenNoTrue(final Boolean[] input) {
		int noTrues = 0;

		for (final boolean b: input) {
			if (b) {
				noTrues++;
			}
		}

		return ((noTrues % 2) == 0);
	}
}
