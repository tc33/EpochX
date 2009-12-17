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
 */
package org.epochx.tools.eval;

import org.apache.bsf.*;


/**
 * An inline groovy evaluator is for evaluating single line Groovy programs 
 * where the return value is the result of execution of that one nested 
 * statement.
 * 
 * <p>In theory it is capable of executing any Groovy program in this 
 * inline form up to and including Groovy 1.6 features. 
 * 
 * <p>This evaluator uses the bean scripting framework and requires access to 
 * the following jars which should all have come with XGE: 
 * <ul>
 * 		<li>antlr-3.1.3.jar
 * 		<li>asm-2.2.jar</li>
 * 		<li>bsf.jar</li>
 * 		<li>commons-lang-2.4.jar</li>
 * 		<li>commons-logging-1.1.jar</li>
 * 		<li>groovy-1.6.3.jar</li>
 * </ul>
 */
public class GroovyEvaluator implements Evaluator, Executor {
	
	// From the Bean Scripting Framework.
	private BSFManager manager;
	
	/**
	 * Constructs a Groovy evaluator.
	 */
	public GroovyEvaluator() {
		manager = new BSFManager();
	}
	/**
	 * Evaluates the given program as a one line Ruby statement. The program 
	 * should have been evolved with a grammar enforcing a subset of the Ruby 
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
	 * the returned Object may vary from program to program.
	 */
	@Override
	public Object eval(String program, String[] argNames, Object[] argValues) {
        Object[] results = eval(program, argNames, new Object[][]{argValues});
		
        return results[0];
	}
	
	/**
	 * Evaluates the given program as a one line Ruby statement. The program 
	 * should have been evolved with a grammar enforcing a subset of the Ruby 
	 * language syntax, else there are likely to be evaluation errors. 
	 * 
	 * <p>This version of the eval method executes the CandidateProgram 
	 * multiple times. The variable names remain the same for each evaluation 
	 * but for each evaluation the variable values will come from the next 
	 * array in the argValues argument. Ruby variables with the specified names 
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
	 * the returned Object may vary from program to program.
	 */
	@Override
	public Object[] eval(String program, String[] argNames, Object[][] argValues) {
		String code = getEvalCode(program, argNames, argValues);
		
		Object[] results = null;
		
        //Evaluate
        try {
        	manager.declareBean("inputs", argValues, Object[][].class);
            results = (Object[]) manager.eval("groovy", "(groovy)", 0, 0, code);
        } catch (BSFException e) {
            System.err.println("Exception evaluating code using bsf: " + e);
            e.printStackTrace();
        }
        
        // Convert RubyArray to an Object[].
		return results;
	}

	/*
	 * Helper method to the multiple eval.
	 * 
	 * Constructs a string representing source code of a Groovy method containing 
	 * the candidate program source. The class also contains a method call to 
	 * this method for each of the different variable sets. The result of 
	 * execution of this class should be an Object array suitable 
	 * for returning from eval.
	 */
	private String getEvalCode(String source, String[] argNames, Object[][] inputs) {
        StringBuilder code = new StringBuilder();

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
        
        //System.out.println(code);
        
        return code.toString();
	}

	@Override
	public void exec(String program, String[] argNames, Object[] argValues) {
		exec(program, argNames, new Object[][]{argValues});
	}

	@Override
	public void exec(String program, String[] argNames, Object[][] argValues) {
		String code = getExecCode(program, argNames, argValues);
		
        // Execute.
        try {
        	manager.declareBean("inputs", argValues, Object[][].class);
            manager.exec("groovy", "(groovy)", 0, 0, code);
        } catch (BSFException e) {
            System.err.println("Exception evaluating code using bsf: " + e);
            e.printStackTrace();
        }
	}
	
	/*
	 * Helper method to the multiple eval.
	 * 
	 * Constructs a string representing source code of a Groovy method containing 
	 * the candidate program source. The class also contains a method call to 
	 * this method for each of the different variable sets. The result of 
	 * execution of this class should be an Object array suitable 
	 * for returning from eval.
	 */
	private String getExecCode(String source, String[] argNames, Object[][] inputs) {
		StringBuilder code = new StringBuilder();

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
        
        //System.out.println(code);
        
        return code.toString();
	}
}
