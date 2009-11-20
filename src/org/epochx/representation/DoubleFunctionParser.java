/*  
 *  Copyright 2007-2008 Lawrence Beadle & Tom Castle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of Epoch X - (The Genetic Programming Analysis Software)
 *
 *  Epoch X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Epoch X is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with Epoch X.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.epochx.representation;

import java.util.*;
import java.util.regex.*;

import org.epochx.representation.dbl.*;

/**
 *
 */
public class DoubleFunctionParser extends FunctionParser<Double> {

	private final Map<String, Class<?>> functions;

	/*
	 * These regexps come directly from JavaDoc for Double.valueOf(String) to 
	 * check the validity of a double without having to rely on catching number
	 * format exceptions.
	 */
	private final String Digits     = "(\\p{Digit}+)";
	private final String HexDigits  = "(\\p{XDigit}+)";
	private final String Exp        = "[eE][+-]?"+Digits;
	private final String fpRegex    = ("[\\x00-\\x20]*"+
	             					  "[+-]?(" +
	             					  "NaN|" +
	             					  "Infinity|" +
	             					  "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+
	             					  "(\\.("+Digits+")("+Exp+")?)|"+
	             					  "((" +
	             					  "(0[xX]" + HexDigits + "(\\.)?)|" +
	             					  "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
	             					  ")[pP][+-]?" + Digits + "))" +
	             					  "[fFdD]?))" +
	             					  "[\\x00-\\x20]*");
	
	
	public DoubleFunctionParser() {
		functions = new HashMap<String, Class<?>>();
		functions.put("ADD", AddFunction.class);
		functions.put("ACOS", ArcCosineFunction.class);
		functions.put("ASIN", ArcSineFunction.class);
		functions.put("ATAN", ArcTangentFunction.class);
		functions.put("CVP", CoefficientPowerFunction.class);
		functions.put("COSEC", CosecantFunction.class);
		functions.put("COS", CosineFunction.class);
		functions.put("COT", CotangentFunction.class);
		functions.put("CUBE", CubeFunction.class);
		functions.put("FACTORIAL", FactorialFunction.class);
		functions.put("COSH", HyperbolicCosineFunction.class);
		functions.put("SINH", HyperbolicSineFunction.class);
		functions.put("TANH", HyperbolicTangentFunction.class);
		functions.put("INV", InvertFunction.class);
		functions.put("LOG-10", Log10Function.class);
		functions.put("LN", LogFunction.class);
		functions.put("MAX", MaxFunction.class);
		functions.put("MIN", MinFunction.class);
		functions.put("MOD", ModuloFunction.class);
		functions.put("MUL", MultiplyFunction.class);
		functions.put("POW", PowerFunction.class);
		functions.put("PDIV", ProtectedDivisionFunction.class);
		functions.put("SEC", SecantFunction.class);
		functions.put("SGN", SignumFunction.class);
		functions.put("SIN", SineFunction.class);
		functions.put("SQUARE", SquareFunction.class);
		functions.put("SQRT", SquareRootFunction.class);
		functions.put("SUB", SubtractFunction.class);
		functions.put("TAN", TangentFunction.class);
	}
	
	/**
	 * Parses the given string as a terminal. It will be treated as either a 
	 * variable or a literal value depending upon its contents. If it is a 
	 * parseable numeric value then it will be considered to be a terminal with 
	 * a Double value. if it contains in any case the values "true" or "false" 
	 * then it will be treated as a terminal with a Boolean value. All other 
	 * terminals will be treated as Variables with a label equal to the 
	 * argument string.
	 * 
	 * @param terminalStr
	 * @return
	 */
	@Override
	public TerminalNode<Double> parseTerminal(String terminalStr) {
		if (Pattern.matches(fpRegex, terminalStr)) {
			return new TerminalNode<Double>(Double.valueOf(terminalStr));
		} else {
			return new Variable<Double>(terminalStr);
		}
	}

}
