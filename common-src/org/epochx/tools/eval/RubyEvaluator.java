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
import org.jruby.RubyArray;


/**
 * This class provides support for the evaluation and execution of Ruby source 
 * code. Both evaluation and execution with this class are currently very slow, 
 * although the multi-input versions of the evaluator/executor are just about 
 * usable in a useful way.
 */
public class RubyEvaluator implements Evaluator, Executor {

	// From the Bean Scripting Framework.
	private BSFManager manager;
	
	/**
	 * Constructs a Ruby evaluator.
	 */
	public RubyEvaluator() {
		BSFManager.registerScriptingEngine("ruby", 
				"org.jruby.javasupport.bsf.JRubyEngine", 
				new String[] { "rb" });
		
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
		
		RubyArray results = null;
		
        //Evaluate
        try {
        	manager.declareBean("inputs", argValues, Object[][].class);
            results = (RubyArray) manager.eval("ruby", "(ruby)", 0, 0, code);
        } catch (BSFException e) {
            System.err.println("Exception evaluating code using bsf: " + e);
            e.printStackTrace();
        }
        
        // Convert RubyArray to an Object[].
		return results.toArray();
	}
	
	/**
	 * Executes the given program as a multi-line Ruby statement. The program 
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
	 */
	@Override
	public void exec(String program, String[] argNames, Object[] argValues) {		
		exec(program, argNames, new Object[][]{argValues});
	}
	
	/**
	 * Executes the given program as a multi-line line Ruby statement. The 
	 * program should have been evolved with a grammar enforcing a subset of 
	 * the Ruby language syntax, else there are likely to be evaluation 
	 * errors. 
	 * 
	 * <p>This version of the exec method executes the CandidateProgram 
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
	 */
	@Override
	public void exec(String program, String[] argNames, Object[][] argValues) {
		String code = getExecCode(program, argNames, argValues);

        //Evaluate
        try {
        	manager.declareBean("inputs", argValues, Object[][].class);
            manager.exec("ruby", "(ruby)", 0, 0, code);
        } catch (BSFException e) {
            System.err.println("Exception evaluating code using bsf:" + e);
            e.printStackTrace();
        }
	}
	
	/*
	 * Helper method to the multiple eval.
	 * 
	 * Constructs a string representing source code of a Ruby method containing 
	 * the candidate program source. The class also contains a method call to 
	 * this method for each of the different variable sets. The result of 
	 * execution of this class should be an Object array suitable 
	 * for returning from eval.
	 * 
	 * The reason we do eval this long winded way even for single evaluations is
	 * to avoid the problems with the declared beans being initialised as global
	 * meaning we need to use $ notation to access them, which means using $ in 
	 * the grammar - which is fine but is more difficult to use the global $ in 
	 * the multiple evaluator.
	 */
	private String getEvalCode(String source, String[] argNames, Object[][] inputs) {
        StringBuffer code = new StringBuffer();

        //code.append("class Evaluation\n");
        code.append("def expr(");
        for (int i=0; i<argNames.length; i++) {
        	if (i > 0) {
        		code.append(',');
        	}
			code.append(argNames[i]);
		}
        code.append(")\n");
        
        code.append("return ");
        code.append(source);
        code.append(';');
        code.append("end\n");
        //code.append("end\n");
        
        //code.append("eval = Evaluation.new();");
        
        // This is where it gets tricky.       
        code.append("results = [");
        for (int i=0; i<inputs.length; i++) {
        	if (i > 0) code.append(',');
        	
        	code.append("expr(");
        	for (int j=0; j<inputs[i].length; j++) {
        		if (j > 0) {
        			code.append(',');
        		}
        		code.append("$inputs[" + i + "][" + j + ']');
        	}
        	code.append(")");
        }
        code.append("];");
        code.append("return results;");
        
        //System.out.println(code);
        
        return code.toString();
	}
	
	/*
	 * Helper method to the multiple exec.
	 * 
	 * Constructs a string representing source code of a Ruby method containing 
	 * the candidate program source. The class also contains a method call to 
	 * this method for each of the different variable sets. The result of 
	 * execution of this class should be an Object array suitable 
	 * for returning from exec.
	 * 
	 * The reason we do exec this long winded way even for single evaluations is
	 * to avoid the problems with the declared beans being initialised as global
	 * meaning we need to use $ notation to access them, which means using $ in 
	 * the grammar - which is fine but is more difficult to use the global $ in 
	 * the multiple evaluator.
	 */
	private String getExecCode(String source, String[] argNames, Object[][] inputs) {
		StringBuffer code = new StringBuffer();

        //code.append("class Evaluation\n");
        code.append("def expr(");
        for (int i=0; i<argNames.length; i++) {
        	if (i > 0) {
        		code.append(',');
        	}
			code.append(argNames[i]);
		}
        code.append(")\n");
        
        code.append(source);
        
        //code.append("\nend");
        code.append("\nend");
        
        //code.append("\neval = Evaluation.new();\n");
        for (int i=0; i<inputs.length; i++) {
        	code.append("\nexpr(");
        	for (int j=0; j<inputs[i].length; j++) {
        		if (j > 0) {
        			code.append(',');
        		}
        		code.append("$inputs[" + i + "][" + j + ']');
        	}
        	code.append(")");
        }
        
        return code.toString();
	}
}
