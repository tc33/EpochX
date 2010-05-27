/* 
 * Copyright 2007-2010 Tom Castle & Lawrence Beadle
 * Licensed under GNU General Public License
 * 
 * This file is part of EpochX: genetic programming software for research
 * 
 * EpochX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * EpochX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 * The latest version is available from: http:/www.epochx.org
 */
package org.epochx.tools.eval;

import bsh.*;


/**
 * A JavaInterpreter provides the facility to evaluate individual Java 
 * expressions and execute multi-line Java statements. Java language features 
 * up to and including version 1.5 are supported.
 * 
 * <p>
 * There is no publically visible constructor. A singleton instance is 
 * maintained which is accessible through the <code>getInstance()</code> method.
 */
public class JavaInterpreter implements Interpreter {
	//TODO Using singleton pattern is nice but will cause some problems when introduce threaded fitness evaluations.

	// Singleton instance.
	private static JavaInterpreter instance;
	
	// The bean shell beanShell.
	private final bsh.Interpreter beanShell;
	
	/*
	 * Constructs a JavaInterpreter.
	 */
	private JavaInterpreter() {
		beanShell = new bsh.Interpreter();
	}
	
	/**
	 * Returns a reference to the singleton <code>JavaInterpreter</code> 
	 * instance.
	 * 
	 * @return an instance of JavaInterpreter.
	 */
	public static JavaInterpreter getInstance() {
		if (instance == null) {
			instance = new JavaInterpreter();
		}
		
		return instance;
	}
	
	/**
	 * Evaluates any valid Java expression which may optionally contain the use 
	 * of any argument named in the <code>argNames</code> array which will be 
	 * provided with the associated value from the <code>argValues</code> array.
	 * The result of evaluating the expression will be returned from this 
	 * method. The runtime <code>Object</code> return type will match the type 
	 * returned by the expression.
	 * 
	 * @param expression a valid Java expression that is to be evaluated.
	 * @param argNames {@inheritDoc}
	 * @param argValues {@inheritDoc}
	 * @return the return value from evaluating the expression.
	 */
	@Override
	public Object eval(final String source, final String[] argNames, final Object[] argValues) {		
		Object result = null;
		
		if (source != null) {
			try {
				// Declare all the variables.
				for (int i=0; i<argNames.length; i++) {
					beanShell.set(argNames[i], argValues[i]);
				}
				
				result = beanShell.eval(source);
			} catch (final EvalError e) {
				throw new MalformedProgramException();
			}
		}
		return result;
	}
	
	/**
	 * Evaluates any valid Java expression which may optionally contain the use 
	 * of any argument named in the <code>argNames</code> array which will be 
	 * provided with the associated value from the <code>argValues</code> array.
	 * The result of evaluating the expression will be returned from this 
	 * method. The runtime <code>Object</code> return type will match the type 
	 * returned by the expression.
	 * 
	 * <p>This version of the eval method executes the expression 
	 * multiple times. The variable names remain the same for each evaluation 
	 * but for each evaluation the variable values will come from the next 
	 * array in the argValues argument. Java variables with the specified names 
	 * and values are automatically declared and initialised before the 
	 * generated code is run. The argument names link up with the argument value 
	 * in the same array index, so both arguments must have the same length.
	 * 
	 * @param expression a valid Java expression that is to be evaluated.
	 * @param argNames {@inheritDoc}
	 * @param argValues {@inheritDoc}
	 * @return the return values from evaluating the expression. The runtime 
	 * type of the returned Objects may vary from program to program. If the 
	 * program does not return a value then this method will return an array of 
	 * nulls.
	 */
	@Override
	public Object[] eval(final String program, final String[] argNames, final Object[][] argValues) {
		final Object[] results = new Object[argValues.length];
		
		for (int i=0; i<argValues.length; i++) {
			results[i] = eval(program, argNames, argValues[i]);
		}
		
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exec(final String program, final String[] argNames, final Object[] argValues) {
		eval(program, argNames, argValues);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exec(final String program, final String[] argNames, final Object[][] argValues) {
		eval(program, argNames, argValues);
	}
	
}
