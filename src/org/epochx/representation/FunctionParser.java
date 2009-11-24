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
import java.util.regex.Pattern;

import org.epochx.action.*;
import org.epochx.representation.action.*;
import org.epochx.representation.bool.*;
import org.epochx.representation.dbl.*;

/**
 * The function parser can parse a nested function into a node tree. It can 
 * only parse those functions that it knows about.
 */
public class FunctionParser<TYPE> {
	
	private final Map<String, Class<?>> functions;
	
	private final Map<String, Class<?>> actions;
	
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
	
	public FunctionParser() {
		functions = new HashMap<String, Class<?>>();
		// Insert the Boolean functions.
		functions.put("AND", AndFunction.class);
		functions.put("IFF", IfAndOnlyIfFunction.class);
		functions.put("IF", IfFunction.class);
		functions.put("IMPLIES", ImpliesFunction.class);
		functions.put("NAND", NandFunction.class);
		functions.put("NOR", NorFunction.class);
		functions.put("NOT", NotFunction.class);
		functions.put("OR", OrFunction.class);
		functions.put("XOR", XorFunction.class);
		
		// Insert the Double functions.
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
		
		// Insert the Action functions.
		functions.put("IF-FOOD-AHEAD", IfFoodAheadFunction.class);
		functions.put("SEQ2", Seq2Function.class);
		functions.put("SEQ3", Seq3Function.class);
		
		
		actions = new HashMap<String, Class<?>>();
		// Insert the Ant actions.
		actions.put("@MOVE", AntMoveAction.class);
		actions.put("@SKIP", AntSkipAction.class);
		actions.put("@TURN-LEFT", AntTurnLeftAction.class);
		actions.put("@TURN-RIGHT", AntTurnRightAction.class);
	}
	
	public Node<TYPE> parse(String source) {

		// Locate the first bracket (straight after function name).
		int openingBracket = source.indexOf('(');
		
		// If there is no bracket then it must be a terminal.
		if (openingBracket == -1) {
			// This will blow up here if the data-type of the terminal did not match TYPE.
			return (Node<TYPE>) parseTerminal(source);
		} else {
			// Get the name of the function.
			String functionName = source.substring(0, openingBracket);
			
			// Get the comma separated arguments - without the surrounding brackets.
			String argumentStr = source.substring(openingBracket+1, source.length()-1);
			
			// Separate the arguments.
			List<String> args = splitArguments(argumentStr);
			
			// Instantiate the function node.
			Class<?> functionClass = functions.get(functionName);
			FunctionNode<TYPE> node = null;
			try {
				// This will blow up here if the data-type of the function does not match TYPE.
				node = (FunctionNode<TYPE>) functionClass.newInstance();
			} catch (InstantiationException e) {
				//TODO Do something...
			} catch (IllegalAccessException e) {
				//TODO Do something...
			}
			
			// Check the arities match.
			if (node == null || node.getArity() != args.size()) {
				throw new MalformedProgramException();
			} else {
				for (int i=0; i<args.size(); i++) {
					node.setChild(i, parse(args.get(i)));
				}
			}
			
			return node;
		}
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
	private TerminalNode<?> parseTerminal(String terminalStr) {
		if (Pattern.matches(fpRegex, terminalStr)) {
			return new TerminalNode<Double>(Double.valueOf(terminalStr));
		} else if (terminalStr.equalsIgnoreCase("true") 
				 	|| terminalStr.equalsIgnoreCase("false")) {
			return new TerminalNode<Boolean>(Boolean.valueOf(terminalStr));
		} else if (terminalStr.startsWith("@")) {
			return new TerminalNode<Action>(parseAction(terminalStr));
		} else {
			return new Variable<Boolean>(terminalStr);
		}
	}
	
	private Action parseAction(String actionStr) {
		Class<?> actionClass = actions.get(actionStr);
		Action action = null;
		try {
			// This will blow up here if the data-type of the function does not match TYPE.
			action = (Action) actionClass.newInstance();
		} catch (InstantiationException e) {
			//TODO Do something...
		} catch (IllegalAccessException e) {
			//TODO Do something...
		}
		
		return action;
	}
	
	
	private List<String> splitArguments(String argStr) {
		int depth = 0;
		
		List<String> args = new ArrayList<String>(5);
		StringBuilder buffer = new StringBuilder();
		
		for (int i=0; i<argStr.length(); i++) {
			char c = argStr.charAt(i);
			
			if (c == '(') {
				depth++;
			} else if (c == ')') {
				depth--;
			}
			if (((c == ' ') || (c == ',')) && (depth == 0)) {
				args.add(buffer.toString());
				buffer = new StringBuilder();
			} else {
				buffer.append(c);
			}
		}
		args.add(buffer.toString());
		
		return args;
	}
	
	public static void main(String[] args) {
		FunctionParser parser = new FunctionParser();
		
		//System.out.println(parser.parse("IF(ADD(1,false),NOT(true),false)").toString());
		Node<Boolean> programTree = parser.parse("XOR(D1 XOR(NOT(XOR(D0 D3)) D2))");
		
		
		
		//System.out.println(parser.parse("XOR(OR(d0,d0),NOT(d0))").toString());
	}
}
