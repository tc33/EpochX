/*  
 *  Copyright 2009 Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of XGE: grammatical evolution for research
 *
 *  XGE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  XGE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with XGE.  If not, see <http://www.gnu.org/licenses/>.
 *//*  
 *  Copyright 2007-2010 Tom Castle & Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming software for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 *  The latest version is available from: http:/www.epochx.org
 */
package org.epochx.tools.eval;

import bsh.*;


/**
 * An inline java evaluator is for evaluating single line Java programs where 
 * the return value is the result of execution of that one nested statement.
 * 
 * <p>In theory it is capable of executing any Java program in this inline form 
 * up to and including Java 1.5 features.
 */
public class JavaEvaluator implements Evaluator, Executor {

	// The bean shell interpreter.
	private Interpreter interpreter;
	
	/**
	 * Constructs an InlineJavaEvaluator.
	 */
	public JavaEvaluator() {
		interpreter = new Interpreter();
	}
	
	/**
	 * Evaluates the given program as a one line Java statement. The program 
	 * should have been evolved with a grammar enforcing a subset of the Java 
	 * language syntax, else there are likely to be evaluation errors. 
	 * 
	 * <p>Variables with the specified names and values are automatically 
	 * declared and initialised before the generated code is run. The argument 
	 * names link up with the argument value in the same array index, so both 
	 * arguments must have the same length.
	 * 
	 * @param program the CandidateProgram to be executed.
	 * @param argNames an array of arguments that the argValues should be 
	 * 				   assigned to. The array should have equal length to the 
	 * 				   argValues array.
	 * @param argValues an array of argument values to be assigned to the 
	 * 				    specified argument names. The array should have equal 
	 * 				    length to the argNames array.
	 * @return the return value of the CandidateProgram. The runtime type of 
	 * the returned Object may vary from program to program. If the program 
	 * does not return a value then this method will return null.
	 */
	@Override
	public Object eval(String source, String[] argNames, Object[] argValues) {		
		Object result = null;
		
		if (source != null) {
			try {
				// Declare all the variables.
				for (int i=0; i<argNames.length; i++) {
					interpreter.set(argNames[i], argValues[i]);
				}
				
				result = interpreter.eval(source);
			} catch (EvalError e) {
				//e.printStackTrace();
				throw new InvalidProgramException();
			}
		}
		return result;
	}
	
	/**
	 * Evaluates the given program as a one line Java statement. The program 
	 * should have been evolved with a grammar enforcing a subset of the Java 
	 * language syntax, else there are likely to be evaluation errors. 
	 * 
	 * <p>This version of the eval method executes the CandidateProgram 
	 * multiple times. The variable names remain the same for each evaluation 
	 * but for each evaluation the variable values will come from the next 
	 * array in the argValues argument. Java variables with the specified names 
	 * and values are automatically declared and initialised before the 
	 * generated code is run. The argument names link up with the argument value 
	 * in the same array index, so both arguments must have the same length.
	 * 
	 * @param program the CandidateProgram to be executed.
	 * @param argNames an array of arguments that the argValues should be 
	 * 				   assigned to. The array should have equal length to the 
	 * 				   argValues array.
	 * @param argValues argument values to be assigned to the specified argument 
	 * 					names. Each element is an array of argument values for 
	 * 					one evaluation. As such there should be argValues.length 
	 * 					evaluations and argValues.length elements in the 
	 * 					returned Object array. The array should also have equal 
	 * 				    length to the argNames array.
	 * @return the return value of the CandidateProgram. The runtime type of 
	 * the returned Object may vary from program to program. If the program 
	 * does not return a value then this method will return an array of nulls.
	 */
	@Override
	public Object[] eval(String program, String[] argNames, Object[][] argValues) {
		Object[] results = new Object[argNames.length];
		
		for (int i=0; i<argNames.length; i++) {
			results[i] = eval(program, argNames, argValues[i]);
		}
		
		return results;
	}

	@Override
	public void exec(String program, String[] argNames, Object[] argValues) {
		eval(program, argNames, argValues);
	}

	@Override
	public void exec(String program, String[] argNames, Object[][] argValues) {
		eval(program, argNames, argValues);
	}
	
}
