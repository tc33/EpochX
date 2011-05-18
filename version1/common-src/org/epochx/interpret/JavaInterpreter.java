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

import org.epochx.representation.CandidateProgram;
import org.epochx.source.SourceGenerator;

import bsh.EvalError;

/**
 * A JavaInterpreter provides the facility to evaluate individual Java
 * expressions and execute multi-line Java statements. Java language features
 * up to and including version 1.5 are supported.
 * 
 * <p>
 * There is no publically visible constructor. A singleton instance is
 * maintained which is accessible through the <code>getInstance()</code> method.
 */
public class JavaInterpreter<T extends CandidateProgram> implements Interpreter<T> {

	private SourceGenerator<T> generator;
	
	// The bean shell beanShell.
	private final bsh.Interpreter beanShell;

	/**
	 * Constructs a JavaInterpreter.
	 */
	public JavaInterpreter(SourceGenerator<T> generator) {
		this.generator = generator;
		
		beanShell = new bsh.Interpreter();
	}

	/**
	 * Evaluates any valid Java expression which may optionally contain the use
	 * of any argument named in the <code>argNames</code> array which will be
	 * provided with the associated value from the <code>argValues</code> array.
	 * The result of evaluating the expression will be returned from this
	 * method. The runtime <code>Object</code> return type will match the type
	 * returned by the expression.
	 * 
	 * @param program a valid Java expression that is to be evaluated.
	 * @param argNames {@inheritDoc}
	 * @param argValues {@inheritDoc}
	 * @return the return value from evaluating the expression.
	 * @throws MalformedProgramException if the given expression is not valid
	 *         according to the language's syntax rules.
	 */
	@Override
	public Object[] eval(final T program, Parameters params) throws MalformedProgramException {
		int noParamSets = params.getNoParameterSets();
		int noParams = params.getNoParameters();
		
		Object result[] = new Object[noParamSets];

		if (program != null) {
			String expression = generator.getSource(program);
			
			try {
				for (int i = 0; i < noParamSets; i++) {
					Object[] paramSet = params.getParameterSet(i);
					
					// Declare all the variables.
					for (int j = 0; j < noParams; j++) {
						beanShell.set(params.getIdentifier(j), paramSet[j]);
					}
	
					result[i] = beanShell.eval(expression);
				}
			} catch (final EvalError e) {
				throw new MalformedProgramException();
			}
		}
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exec(final T program, Parameters params)
			throws MalformedProgramException {
		eval(program, params);
	}
}
