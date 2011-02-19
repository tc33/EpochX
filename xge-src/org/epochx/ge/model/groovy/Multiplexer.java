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
 * Grammar model for the multiplexer problems using a Groovy grammar.
 * 
 * <h4>Multiplexer problem</h4>
 * 
 * Given n binary inputValues, a program that solves the majority problem will
 * return true in all circumstances where a majority of the inputValues are true
 * (or 1), and return false whenever there is not a majority of true values.
 */
public class Multiplexer extends GEModel {

	// Incomplete grammar requiring correct number of terminals to be added.
	private static final String GRAMMAR_FRAGMENT = "<prog> ::= <expr>\n"
			+ "<expr> ::= <expr> <op> <expr> "
			+ "| ( <expr> <op> <expr> ) "
			+ "| <var> "
			+ "| <pre-op> ( <var> ) "
			+ "| ( <expr> ) ? <expr> : <expr>\n"
			+ "<pre-op> ::= !\n"
			+ "<op> ::= \"|\" | &\n"
			+ "<var> ::= ";

	// The boolean input sequences.
	private final Boolean[][] inputValues;
	private Boolean[] expectedResults;

	// The names of the inputValues used in the grammar.
	private String[] argNames;

	// No input bits.
	private int noAddressBits;
	private int noDataBits;

	/**
	 * Constructs a Multiplexer model for the given number of inputs.
	 * 
	 * @param noInputBits the number of inputs the multiplexer problem should be
	 *        for
	 */
	public Multiplexer(Evolver evolver, final int noInputBits) {
		super(evolver);
		
		// Generate the input sequences.
		inputValues = BoolUtils.generateBoolSequences(noInputBits);

		// Calculate number of address/data bits.
		setBitSizes(noInputBits);

		// Determine the input argument names.
		setArgNames(noInputBits);

		// Complete the grammar string and construct grammar instance.
		setGrammar(new Grammar(getGrammarString()));
		
		Parameters params = new Parameters(argNames);
		
		for (int i=0; i<noInputBits; i++) {
			params.addParameterSet(inputValues[i]);
			
			expectedResults[i] = multiplex(inputValues[i]);
		}
		
		setFitnessEvaluator(new HitsCountEvaluator(new GroovyInterpreter(), params, expectedResults));
	}

	/**
	 * Constructs and returns the full grammar string for the multiplexer
	 * problem with the correct number of address and data bits.
	 * 
	 * @return the grammar string for the multiplexer problem with the set
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
	 * Calculate and set the number of address and data bits.
	 */
	private void setBitSizes(final int noInputBits) {
		noAddressBits = 1;
		while (true) {
			noDataBits = (int) Math.pow(2, noAddressBits);

			if ((noAddressBits + noDataBits) == noInputBits) {
				break;
			}

			noAddressBits++;
		}
	}

	/*
	 * Set the argument names for the inputs.
	 */
	private void setArgNames(final int noInputBits) {
		argNames = new String[noInputBits];
		// Add address inputs.
		for (int i = 0; i < noAddressBits; i++) {
			argNames[i] = "a" + i;
		}
		// Add data inputs.
		for (int i = noAddressBits; i < noInputBits; i++) {
			argNames[i] = "d" + i;
		}
	}

	/*
	 * Calculate what the correct response should be for the given inputs.
	 */
	private Boolean multiplex(final Boolean[] vars) {
		// Calculate which data position to use.
		int dataPosition = 0;
		for (int i = 0; i < noAddressBits; i++) {
			if (vars[i]) {
				dataPosition += Math.pow(2, i);
			}
		}

		return vars[noAddressBits + dataPosition];
	}
}
