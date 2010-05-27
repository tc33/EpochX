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

import javax.script.*;

/**
 * 
 */
public class ScriptingInterpreter implements Interpreter {

	// From the Bean Scripting Framework.
	private ScriptEngine engine;
	
	/**
	 * Constructs a ScriptingInterpreter.
	 */
	public ScriptingInterpreter(String engineName) {
		ScriptEngineManager manager = new ScriptEngineManager();
		
		engine = manager.getEngineByName(engineName);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object eval(final String expression, final String[] argNames, final Object[] argValues) {
		Object result = null;
		
        // Evaluate.
        try {
        	for (int i=0; i<argNames.length; i++) {
        		engine.put(argNames[i], argValues[i]);
        	}
            result = engine.eval(expression);
        } catch (final ScriptException e) {
        	throw new MalformedProgramException();
        }
        
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] eval(final String expression, final String[] argNames, final Object[][] argValues) {
		final Object[] results = new Object[argValues.length];
		
        // Evaluate each argument set.
        for (int i=0; i<results.length; i++) {
        	results[i] = eval(expression, argNames, argValues[i]);
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
        // Execute each argument set.
        for (int i=0; i<argValues.length; i++) {
        	exec(program, argNames, argValues[i]);
        }
	}
	
	/**
	 * @return the script engine
	 */
	public ScriptEngine getEngine() {
		return engine;
	}
}
