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
package org.epochx.ge.model.epox;

import org.epochx.core.*;
import org.epochx.fitness.HitsCountEvaluator;
import org.epochx.ge.mapper.DepthFirstMapper;
import org.epochx.ge.model.GEModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.interpret.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.grammar.Grammar;

/**
 * The abstract super class for regression problems in XGE in the Epox language.
 * 
 * <p>
 * The grammar used is:
 * 
 * <blockquote><code>
 * &lt;prog&gt; ::= &lt;node&gt;
 * &lt;node&gt; ::= &lt;function&gt; | &lt;terminal&gt;
 * &lt;function&gt; ::= ADD( &lt;node&gt; , &lt;node&gt; )
 *                    | SUB( &lt;node&gt; , &lt;node&gt; )
 *                    | MUL( &lt;node&gt; , &lt;node&gt; )
 *                    | PDIV( &lt;node&gt; , &lt;node&gt; )
 * &lt;terminal&gt; ::= X
 * </code></blockquote>
 */
public abstract class Regression extends GEModel {

	/**
	 * The grammar that defines valid solution space.
	 */
	public static final String GRAMMAR_STRING = "<prog> ::= <node>\n"
			+ "<node> ::= <function> | <terminal>\n"
			+ "<function> ::= ADD( <node> , <node> ) "
			+ "| SUB( <node> , <node> ) "
			+ "| MUL( <node> , <node> ) "
			+ "| PDIV( <node> , <node> )\n"
			+ "<terminal> ::= X\n";

	// The error each point must be within.
	private static final double POINT_ERROR = 0.01;

	/**
	 * Constructs an instance of the abstract Regression model with 50 input
	 * points.
	 */
	public Regression(Evolver evolver) {
		this(evolver, 50);
	}

	/**
	 * Constructs an instance of the abstract Regression model.
	 */
	public Regression(Evolver evolver, final int noPoints) {
		super(evolver);
		
		setGrammar(new Grammar(GRAMMAR_STRING));
		
		// Generate the random inputs and the correct outputs.
		Parameters params = new Parameters(new String[]{"X"});
		
		Double[] outputs = new Double[noPoints];
		for (int i = 0; i < noPoints; i++) {
			Double[] inputs = {(getRNG().nextDouble() * 2) - 1.0};
			params.addParameterSet(inputs);
			
			outputs[i] = getCorrectResult(inputs[i]);
		}
		
		setFitnessEvaluator(new HitsCountEvaluator<GECandidateProgram>(new EpoxInterpreter<GECandidateProgram>(new DepthFirstMapper(evolver)), params, outputs, POINT_ERROR));
	}

	/*
	 * The actual function we are trying to evolve.
	 */
	public abstract double getCorrectResult(final double x);

}
