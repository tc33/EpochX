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

import org.apache.bsf.*;


/**
 * A GroovyInterpreter provides the facility to evaluate individual Groovy 
 * expressions and execute multi-line Groovy statements. Groovy language 
 * features up to and including version 1.6 are supported.
 * 
 * <p>
 * There is no publically visible constructor. A singleton instance is 
 * maintained which is accessible through the <code>getInstance()</code> method.
 */
public class GroovyInterpreter implements Interpreter {
	
	// Singleton instance.
	private static GroovyInterpreter instance;
	
	// From the Bean Scripting Framework.
	private BSFManager manager;
	
	/*
	 * Constructs a GroovyInterpreter.
	 */
	private GroovyInterpreter() {
		manager = new BSFManager();
	}
	
	/**
	 * Returns a reference to the singleton <code>GroovyInterpreter</code> 
	 * instance.
	 * 
	 * @return an instance of GroovyInterpreter.
	 */
	public static GroovyInterpreter getInstance() {
		if (instance == null) {
			instance = new GroovyInterpreter();
		}
		
		return instance;
	}
	
	/**
	 * Evaluates any valid Groovy expression which may optionally contain the 
	 * use of any argument named in the <code>argNames</code> array which will 
	 * be pre-declared and assigned to the associated value taken from the 
	 * <code>argValues</code> array. The result of evaluating the expression 
	 * will be returned from this method. The runtime <code>Object</code> return
	 * type will match the type returned by the expression.
	 * 
	 * @param expression a valid Groovy expression that is to be evaluated.
	 * @param argNames {@inheritDoc}
	 * @param argValues {@inheritDoc}
	 * @return the return value from evaluating the expression.
	 */
	@Override
	public Object eval(final String expression, final String[] argNames, final Object[] argValues) {
        Object[] results = eval(expression, argNames, new Object[][]{argValues});
		
        return results[0];
	}
	
	/**
	 * Evaluates any valid Groovy expression which may optionally contain the 
	 * use of any argument named in the <code>argNames</code> array which will 
	 * be pre-declared and assigned to the associated value taken from the 
	 * <code>argValues</code> array. The result of evaluating the expression 
	 * will be returned from this method. The runtime <code>Object</code> return
	 * type will match the type returned by the expression.
	 * 
	 * <p>This version of the <code>eval</code> method evaluates the expression 
	 * multiple times. The variable names remain the same for each evaluation 
	 * but for each evaluation the variable values will come from the next 
	 * array in the <code>argValues</code> argument. Groovy variables with the 
	 * specified names and values are automatically declared and initialised 
	 * before the generated code is run. The argument names link up with the 
	 * argument value in the same array index, so both arguments must have the 
	 * same length.
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
	public Object[] eval(final String expression, final String[] argNames, final Object[][] argValues) {
		final String code = getEvalCode(expression, argNames, argValues);
		
		Object[] results = null;
		
        //Evaluate
        try {
        	manager.declareBean("inputs", argValues, Object[][].class);
            results = (Object[]) manager.eval("groovy", "(groovy)", 0, 0, code);
        } catch (BSFException e) {
            System.err.println("Exception evaluating code using bsf: " + e);
            e.printStackTrace();
        }
        
		return results;
	}

	/*
	 * Helper method to the multiple eval.
	 * 
	 * Constructs a string representing source code of a Groovy method 
	 * containing the expression to be evaluated. The class also contains a 
	 * method call to this method for each of the different variable sets. The 
	 * result of execution of this class should be an Object array suitable 
	 * for returning from eval.
	 */
	private String getEvalCode(final String source, final String[] argNames, final Object[][] inputs) {
        final StringBuilder code = new StringBuilder();

        code.append("public class Evaluation {");
        code.append("public Object expr(");
        for (int i=0; i<argNames.length; i++) {
        	if (i > 0) {
        		code.append(',');
        	}
			code.append("Object ");
			code.append(argNames[i]);
		}
        code.append(") {");
        
        code.append("return ");
        code.append(source);
        code.append(';');

        code.append("}}\nEvaluation eval = new Evaluation();");
        
        // This is where it gets tricky.
        code.append("Object[] results = new Object["+inputs.length+"];");
        for (int i=0; i<inputs.length; i++) {
        	code.append("results[");
        	code.append(i);
        	code.append("] = eval.expr(");
        	for (int j=0; j<inputs[i].length; j++) {
        		if (j > 0) {
        			code.append(',');
        		}
        		code.append("inputs[" + i + "][" + j + ']');
        	}
        	code.append(");");
        }
        code.append("return results;");
        
        return code.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exec(final String program, final String[] argNames, final Object[] argValues) {
		exec(program, argNames, new Object[][]{argValues});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exec(final String program, final String[] argNames, final Object[][] argValues) {
		final String code = getExecCode(program, argNames, argValues);
		
        // Execute.
        try {
        	manager.declareBean("inputs", argValues, Object[][].class);
            manager.exec("groovy", "(groovy)", 0, 0, code);
        } catch (final BSFException e) {
            System.err.println("Exception evaluating code using bsf: " + e);
            e.printStackTrace();
        }
	}
	
	/*
	 * Helper method to the multiple exec.
	 * 
	 * Constructs a string representing source code of a Groovy method 
	 * containing the program source. The class also contains a method call to 
	 * this method for each of the different variable sets.
	 */
	private String getExecCode(final String source, final String[] argNames, final Object[][] inputs) {
		final StringBuilder code = new StringBuilder();

        code.append("public class Execution {");
        code.append("public Object expr(");
        for (int i=0; i<argNames.length; i++) {
        	if (i > 0) {
        		code.append(',');
        	}
			code.append("Object ");
			code.append(argNames[i]);
		}
        code.append(") {");

        code.append(source);

        code.append("}}\nExecution exec = new Execution();");
        // This is where it gets tricky.
        for (int i=0; i<inputs.length; i++) {
        	code.append("exec.expr(");
        	for (int j=0; j<inputs[i].length; j++) {
        		if (j > 0) {
        			code.append(',');
        		}
        		code.append("inputs[" + i + "][" + j + ']');
        	}
        	code.append(");");
        }
        
        return code.toString();
	}
}
