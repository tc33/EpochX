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

import org.epochx.gp.representation.*;

/**
 * The EpochX evaluator is a special example of an evaluator in that the 
 * language it evaluates is EpochX's version of Lisp. EpochX is a strongly 
 * typed tree-based genetic programming API very similar to XGE. EpochX has 
 * built-in functions which it can piece together in a nested structure like 
 * Lisp. This evaluator is capable of evaluating any program which uses the 
 * same syntax as EpochX where the function names (and arity) match functions 
 * in EpochX's function libraries.
 */
public class EpoxEvaluator implements Evaluator {

	private FunctionParser parser;
	
	public EpoxEvaluator() {
		parser = new FunctionParser();
	}
	
	public EpoxEvaluator(FunctionParser parser) {
		this.parser = parser;
	}
	
	@Override
	public Object eval(String source, String[] argNames,
			Object[] argValues) {
		
		if (source == null) {
			return null;
		}
		
		// Remove any of the old variables.
		parser.clearAvailableVariables();
		
		for (int i=0; i<argNames.length; i++) {
			if (argValues[i] instanceof Boolean) {
				parser.addAvailableVariable(new BooleanVariable(argNames[i], (Boolean) argValues[i]));
			} else if (argValues[i] instanceof Double) {
				parser.addAvailableVariable(new DoubleVariable(argNames[i], (Double) argValues[i]));
			} else {
				//TODO Unknown variable type - throw exception.
			}
		}
		
		Node programTree = parser.parse(source);
		
		return programTree.evaluate();
	}

	@Override
	public Object[] eval(String source, String[] argNames,
			Object[][] argValues) {

		Object[] results = new Object[argValues.length];
		
		// Call the other eval method for each set of inputs.
		for (int i=0; i<argValues.length; i++) {
			results[i] = eval(source, argNames, argValues[i]);
		}
		
		return results;
	}

}
