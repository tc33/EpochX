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
package org.epochx.interpret;

import org.epochx.epox.*;
import org.epochx.representation.CandidateProgram;

/**
 * An <code>EpoxInterpreter</code> provides the facility to evaluate individual
 * Epox expressions. Epox is the lisp like language used by the tree based
 * representation in EpochX. This allows the grammar based representations to
 * evolve programs that are directly equivalent to the tree based system.
 * 
 * <p>
 * The program strings are parsed by the <code>EpoxParser</code> into the same
 * program trees used by the tree GP aspect of EpochX, which can then be
 * evaluated internally. The Epox language is extendable and this interpreter
 * will correctly evaluate any new functions or data-types which have been added
 * to the <code>EpoxParser</code> that is used here.
 * 
 * @see EpoxParser
 */
public class EpoxInterpreter implements Interpreter {

	// The Epox language parser.
	private final EpoxParser parser;

	/**
	 * Constructs a new <code>EpoxInterpreter</code> with a new
	 * <code>EpoxParser</code>.
	 */
	public EpoxInterpreter() {
		parser = new EpoxParser();
	}

	/**
	 * Constructs a new <code>EpoxInterpreter</code> using the given parser to
	 * parse the program strings into Epox program trees for evaluation.
	 * 
	 * @param parser the Epox language parser.
	 */
	public EpoxInterpreter(final EpoxParser parser) {
		this.parser = parser;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] eval(CandidateProgram program, Parameters params) throws MalformedProgramException {		
		int noParamSets = params.getNoParameterSets();
		int noParams = params.getNoParameters();
		
		String expressionStr = program.toString();
		
		Object[] results = new Object[noParamSets];
		for (int i=0; i<noParamSets; i++) {
			// Remove any of the old variables.
			parser.undeclareAllVariables();
			
			Object[] paramSet = params.getParameterSet(i);
			
			// Set the values of this round of variables.
			for (int j=0; j<noParams; j++) {
				parser.declareVariable(new Variable(params.getIdentifier(j), paramSet[j]));
			}
			
			final Node programTree = parser.parse(expressionStr);
			
			// Evaluate the program tree.
			results[i] = programTree.evaluate();
		}
		
		return results;
	}

	/**
	 * Not supported by <code>EpoxInterpreter</code>. Calling will throw an
	 * <code>IllegalStateException</code>.
	 */
	@Override
	public void exec(CandidateProgram program, Parameters params) {
		throw new IllegalStateException("method not supported");
	}

	/**
	 * Returns the <code>EpoxParser</code> used to parse the program strings.
	 * 
	 * @return the Epox parser
	 */
	public EpoxParser getParser() {
		return parser;
	}
}
