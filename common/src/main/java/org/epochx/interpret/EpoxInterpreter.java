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
 * 
 * @since 2.0
 */
public class EpoxInterpreter<T extends Individual> implements Interpreter<T> {

	// The Epox language parser.
	private EpoxParser parser;
	
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
	public EpoxInterpreter(SourceGenerator<T> generator, EpoxParser parser) {
		this.parser = parser;
	}

	/**
	 * Evaluates any valid Epox expression which may optionally contain the
	 * use of any argument named in the <tt>argNames</tt> array which will
	 * be pre-declared and assigned to the associated value taken from the
	 * <tt>argValues</tt> array. 
	 * 
	 * The expression will be evaluated once for each set of <tt>argValues</tt>. 
	 * The object array returned will contain the result of each of these 
	 * evaluations in order.
	 * 
	 * Any functions or terminals (except the input variables) which are not 
	 * part of the built-in language, must be declared in the EpoxParser before
	 * being used here.
	 * 
	 * @param expression an individual representing a valid Epox expression that 
	 * is to be evaluated.
	 * @param argNames {@inheritDoc}
	 * @param argValues {@inheritDoc}
	 * @return the return values from evaluating the expression. The runtime
	 *         type of the returned Objects may vary from program to program. If
	 *         the program does not return a value then this method will return an
	 *         array of nulls.
	 */
	@Override
	public Object[] eval(T program, String[] argNames, Object[][] argValues) throws MalformedProgramException {		
		int noParamSets = argValues.length;
		int noParams = argNames.length;
		
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
			
			Object[] paramSet = argValues[i];
			
			// Set the values of this round of variables.
			for (int j=0; j<noParams; j++) {
				declaredVariables[j] = new VariableNode(new Variable(argNames[j], paramSet[j]));
				parser.declare(declaredVariables[j]);
			}
			
			Node programTree = parser.parse(expression);
			
			// Evaluate the program tree.
			results[i] = programTree.evaluate();
		}
		
		return results;
	}

	/**
	 * Not supported by <code>EpoxInterpreter</code>. Calling will throw an
	 * <code>IllegalStateException</code>.
	 * @throws MalformedProgramException 
	 */
	@Override
	public void exec(T program, String[] argNames, Object[][] argValues) throws MalformedProgramException {
		eval(program, argNames, argValues);
	}

	/**
	 * Returns the <code>EpoxParser</code> used to parse the program strings.
	 * 
	 * @return the Epox parser
	 */
	public EpoxParser getParser() {
		return parser;
	}
	
	/**
	 * Returns the source generator being used to convert individuals to source code.
	 * 
	 * @return the current source generator
	 */
	public SourceGenerator<T> getSourceGenerator() {
		return generator;
	}
	
	/**
	 * Sets the source generator to use to convert individuals to source code
	 * 
	 * @param the source generator to set
	 */
	public void setSourceGenerator(SourceGenerator<T> generator) {
		this.generator = generator;
	}
}
