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
import org.epochx.source.SourceGenerator;

import bsh.EvalError;

/**
 * A <tt>JavaInterpreter</tt> provides the facility to evaluate individuals that represent
 * Java expressions and execute multi-line Java statements. Java language features
 * up to and including version 1.5 are supported. Individuals are converted into source code
 * using a <tt>SourceGenerator</tt> whose responsibility it is that valid Java source is 
 * produced from the individual.
 * 
 * @see SourceGenerator
 * 
 * @since 2.0
 */
public class JavaInterpreter<T extends Individual> implements Interpreter<T> {

	private SourceGenerator<T> generator;
	
	// The bean shell beanShell.
	private final bsh.Interpreter beanShell;
	
	/**
	 * Constructs a <tt>JavaInterpreter</tt> with a source generator
	 * 
	 * @param generator the SourceGenerator to use to convert individuals to Java source code
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
	 * @param individual a valid Java expression that is to be evaluated.
	 * @param argNames {@inheritDoc}
	 * @param argValues {@inheritDoc}
	 * @return the return value from evaluating the expression.
	 * @throws MalformedProgramException if the given expression is not valid
	 *         according to the language's syntax rules.
	 */
	@Override
	public Object[] eval(T individual, String[] argNames, Object[][] argValues) throws MalformedProgramException {
		int noParamSets = argValues.length;
		int noParams = argNames.length;
		
		Object result[] = new Object[noParamSets];

		if (individual != null) {
			String expression = generator.getSource(individual);
			
			try {
				for (int i = 0; i < noParamSets; i++) {
					Object[] paramSet = argValues[i];
					
					// Declare all the variables.
					for (int j = 0; j < noParams; j++) {
						beanShell.set(argNames[j], paramSet[j]);
					}
	
					result[i] = beanShell.eval(expression);
				}
			} catch (EvalError e) {
				throw new MalformedProgramException();
			}
		}
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exec(T individual, String[] argNames, Object[][] argValues)
			throws MalformedProgramException {
		eval(individual, argNames, argValues);
	}
}
