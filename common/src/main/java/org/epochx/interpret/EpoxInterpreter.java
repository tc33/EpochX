/*
 * Copyright 2007-2013
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

import org.epochx.Individual;
import org.epochx.epox.*;
import org.epochx.source.SourceGenerator;

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
public class EpoxInterpreter<T extends Individual> implements Interpreter<T> {

	// The Epox language parser.
	private final EpoxParser parser;
	
	private SourceGenerator<T> generator;

	/**
	 * Constructs a new <code>EpoxInterpreter</code> with a new
	 * <code>EpoxParser</code>.
	 */
	public EpoxInterpreter(SourceGenerator<T> generator) {
		parser = new EpoxParser();
	}

	/**
	 * Constructs a new <code>EpoxInterpreter</code> using the given parser to
	 * parse the program strings into Epox program trees for evaluation.
	 * 
	 * @param parser the Epox language parser.
	 */
	public EpoxInterpreter(SourceGenerator<T> generator, final EpoxParser parser) {
		this.parser = parser;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] eval(T program, Parameters params) throws MalformedProgramException {		
		int noParamSets = params.getNoParameterSets();
		int noParams = params.getNoParameters();
		
		// Keep a record of the variable nodes that get declared
		VariableNode[] declaredVariables = new VariableNode[noParams];
		
		// Get program source.		
		String expression = generator.getSource(program);
		
		Object[] results = new Object[noParamSets];
		for (int i=0; i<noParamSets; i++) {
			// Remove any of the old variables.
			for (int j=0; j<noParams; j++) {
				parser.undeclare(declaredVariables[j]);
			}
			
			Object[] paramSet = params.getParameterSet(i);
			
			// Set the values of this round of variables.
			for (int j=0; j<noParams; j++) {
				declaredVariables[j] = new VariableNode(new Variable(params.getIdentifier(j), paramSet[j]));
				parser.declare(declaredVariables[j]);
			}
			
			final Node programTree = parser.parse(expression);
			
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
	public void exec(T program, Parameters params) {
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
