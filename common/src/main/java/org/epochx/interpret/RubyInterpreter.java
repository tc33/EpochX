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

import javax.script.*;

import org.epochx.representation.CandidateProgram;
import org.epochx.source.SourceGenerator;

/**
 * A <code>RubyInterpreter</code> provides the facility to evaluate individual
 * Ruby expressions and execute multi-line Ruby statements.
 * 
 * <p>
 * <code>RubyInterpreter</code> extends from the <code>ScriptingInterpreter
 * </code>, adding ruby specific enhancements, including optimized performance.
 */
public class RubyInterpreter<T extends CandidateProgram> extends ScriptingInterpreter<T> {

	/**
	 * Constructs a RubyInterpreter.
	 */
	public RubyInterpreter(SourceGenerator<T> generator) {
		super(generator, "ruby");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] eval(final T program, final Parameters params)
			throws MalformedProgramException {
		int noParamSets = params.getNoParameterSets();
		
		String expression = getSourceGenerator().getSource(program);
		final String code = getEvalCode(expression, params);
		
		Object[] result = new Object[noParamSets];

		final Invocable invocableEngine = (Invocable) getEngine();
		try {
			getEngine().eval(code);
			
			for (int i=0; i<noParamSets; i++) {
				result[i] = invocableEngine.invokeFunction("expr", params.getParameterSet(i));
			}
		} catch (final ScriptException ex) {
			throw new MalformedProgramException();
		} catch (final NoSuchMethodException ex) {
			throw new MalformedProgramException();
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void exec(final T program, final Parameters params)
			throws MalformedProgramException {
		int noParamSets = params.getNoParameterSets();
		
		String source = getSourceGenerator().getSource(program);
		final String code = getExecCode(source, params);
		
		final Invocable invocableEngine = (Invocable) getEngine();
		try {
			getEngine().eval(code);
			
			for (int i=0; i<noParamSets; i++) {
				invocableEngine.invokeFunction("expr", params.getParameterSet(i));
			}
		} catch (final ScriptException ex) {
			throw new MalformedProgramException();
		} catch (final NoSuchMethodException ex) {
			throw new MalformedProgramException();
		}
	}

	/*
	 * Helper method to the multiple eval.
	 * 
	 * Constructs a string representing source code of a Ruby method
	 * containing a return statement that returns the result of evaluating
	 * the given expression.
	 */
	private String getEvalCode(final String expression, final Parameters params) {
		final StringBuffer code = new StringBuffer();

		code.append("def expr(");
		for (int i = 0; i < params.getNoParameters(); i++) {
			if (i > 0) {
				code.append(',');
			}
			code.append(params.getIdentifier(i));
		}
		code.append(")\n");

		code.append("return ");
		code.append(expression);
		code.append(";\n");
		code.append("end\n");

		return code.toString();
	}

	/*
	 * Helper method to the multiple exec.
	 * 
	 * Constructs a string representing source code of a Ruby method
	 * containing the given program.
	 */
	private String getExecCode(final String program, final Parameters params) {
		final StringBuffer code = new StringBuffer();

		// code.append("class Evaluation\n");
		code.append("def expr(");
		for (int i = 0; i < params.getNoParameters(); i++) {
			if (i > 0) {
				code.append(',');
			}
			code.append(params.getIdentifier(i));
		}
		code.append(")\n");

		code.append(program);
		code.append("\n");
		code.append("end\n");

		return code.toString();
	}
}
