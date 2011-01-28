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
package org.epochx.epox;

import java.text.*;
import java.util.*;

import org.apache.commons.lang.*;
import org.epochx.epox.ant.*;
import org.epochx.epox.bool.*;
import org.epochx.epox.lang.*;
import org.epochx.epox.math.*;
import org.epochx.epox.trig.*;
import org.epochx.tools.eval.MalformedProgramException;

/**
 * This parser is for parsing valid Epox programs into a node tree. It is only 
 * able to parse those node types which are part of the standard language, or 
 * which have been added to the parser through its <code>declareXxx</code> 
 * methods. It is also only able to construct instances of standard nodes which 
 * require no additional arguments provided to their constructor, this excludes 
 * all nodes which can handle multiple arities, such as the {@link SeqNFunction}
 * function. Instead, use the explicit arity versions such as 
 * {@link Seq2Function}, or provide an already constructed instance of the node
 * to the parser through the <code>declareFunction(String, Node)</code> method.
 * 
 * <p>Variables may be named with any String. There will be no name clash 
 * between variables and functions. There could however be a clash between 
 * variables and literals, so variable names that could be parsed as any valid 
 * literal value should be avoided.
 * 
 * <p>Literals will be parsed by first matching against any user declared 
 * literals, then by attempting to parse the value as a Java primitive.
 * 
 * @see Node
 */
public class EpoxParser {

	// Declared functions.
	private final Map<String, Class<? extends Node>> functions;
	
	// More functions, but held by a node instance rather than class.
	private final Map<String, Node> functionNodes;

	// Declared variables.
	private final Map<String, Variable> variables;
	
	// Declared literal values.
	private final Map<String, Object> literalValues;

	/**
	 * Constructs an <code>EpoxParser</code> with all standard Epox functions.
	 */
	public EpoxParser() {
		variables = new HashMap<String, Variable>();
		functions = new HashMap<String, Class<? extends Node>>();
		functionNodes = new HashMap<String, Node>();
		literalValues = new HashMap<String, Object>();

		initialiseStandardFunctions();
	}

	/*
	 * Adds the standard Epox functions to the map of known functions.
	 */
	private void initialiseStandardFunctions() {
		// Insert the Boolean functions.
		functions.put("AND", AndFunction.class);
		functions.put("IFF", IfAndOnlyIfFunction.class);
		functions.put("IMPLIES", ImpliesFunction.class);
		functions.put("NAND", NandFunction.class);
		functions.put("NOR", NorFunction.class);
		functions.put("NOT", NotFunction.class);
		functions.put("OR", OrFunction.class);
		functions.put("XOR", XorFunction.class);

		// Insert the Trig functions.
		functions.put("ARCCSC", ArcCosecantFunction.class);
		functions.put("ARCCOS", ArcCosineFunction.class);
		functions.put("ARCCOT", ArcCotangentFunction.class);
		functions.put("ARCSEC", ArcSecantFunction.class);
		functions.put("ARCSIN", ArcSineFunction.class);
		functions.put("ARCTAN", ArcTangentFunction.class);
		functions.put("ARCOSH", AreaHyperbolicCosineFunction.class);
		functions.put("ARSINH", AreaHyperbolicSineFunction.class);
		functions.put("ARTANH", AreaHyperbolicTangentFunction.class);
		functions.put("CSC", CosecantFunction.class);
		functions.put("COS", CosineFunction.class);
		functions.put("COT", CotangentFunction.class);
		functions.put("COSH", HyperbolicCosineFunction.class);
		functions.put("SINH", HyperbolicSineFunction.class);
		functions.put("TANH", HyperbolicTangentFunction.class);
		functions.put("SEC", SecantFunction.class);
		functions.put("SIN", SineFunction.class);
		functions.put("TAN", TangentFunction.class);
		
		// Insert the Double functions.
		functions.put("ABS", AbsoluteFunction.class);
		functions.put("ADD", AddFunction.class);
		functions.put("CVP", CoefficientPowerFunction.class);
		functions.put("CUBE", CubeFunction.class);
		functions.put("CBRT", CubeRootFunction.class);
		functions.put("EXP", ExponentialFunction.class);
		functions.put("FACTORIAL", FactorialFunction.class);
		functions.put("GT", GreaterThanFunction.class);
		functions.put("INV", InvertProtectedFunction.class);
		functions.put("LOG-10", Log10Function.class);
		functions.put("LN", LogFunction.class);
		functions.put("LT", LessThanFunction.class);
		functions.put("MAX", Max2Function.class);
		functions.put("MIN", Min2Function.class);
		functions.put("MAX3", Max3Function.class);
		functions.put("MIN3", Min3Function.class);
		functions.put("MOD", ModuloProtectedFunction.class);
		functions.put("MUL", MultiplyFunction.class);
		functions.put("POW", PowerFunction.class);
		functions.put("PDIV", DivisionProtectedFunction.class);
		functions.put("SGN", SignumFunction.class);
		functions.put("SQUARE", SquareFunction.class);
		functions.put("SQRT", SquareRootFunction.class);
		functions.put("SUB", SubtractFunction.class);
		
		// Insert the Ant functions.
		functions.put("IF-FOOD-AHEAD", IfFoodAheadFunction.class);
		functions.put("MOVE", AntMoveFunction.class);
		functions.put("TURN-LEFT", AntTurnLeftFunction.class);
		functions.put("TURN-RIGHT", AntTurnRightFunction.class);
		functions.put("SKIP", AntSkipFunction.class);

		// Insert the Lang functions.
		functions.put("IF", IfFunction.class);
		functions.put("SEQ2", Seq2Function.class);
		functions.put("SEQ3", Seq3Function.class);
	}

	/**
	 * Parses an Epox program string as an executable <code>Node</code> tree. 
	 * The given <code>source</code> parameter must contain a valid Epox program
	 * string, comprised only of functions, variables and literals that have 
	 * been declared. The data-types of each function's inputs must correspond 
	 * to valid data-types, otherwise a <code>MalformedProgramException</code>
	 * will be thrown.
	 * 
	 * <p>If the source parameter is <code>null</code>, then <code>null</code>
	 * will be returned.
	 * 
	 * @param source the program string to be parsed as an Epox program.
	 * @return a <code>Node</code> which is the root of a tree which is 
	 * equivalent to the provided source string. A <code>null</code> value will
	 * be returned if the <code>source</code> parameter is <code>null</code>.
	 * @throws MalformedProgramException if the given string does not represent
	 * a valid Epox program.
	 */
	public Node parse(final String source) throws MalformedProgramException {
		if (source == null) {
			return null;
		}

		// Locate the first bracket (straight after function name).
		final int openingBracket = source.indexOf('(');

		String identifier = null;
		List<String> args = null;

		boolean terminal = false;

		// If there is no bracket then it must be a terminal.
		if (openingBracket == -1) {
			identifier = source;
			args = new ArrayList<String>();
			terminal = true;
		} else {
			// Get the name of the function.
			identifier = source.substring(0, openingBracket).trim();

			// Locate the closing bracket.
			final int closingBracket = source.lastIndexOf(')');

			// Get the comma separated arguments - without brackets.
			final String argumentStr = source.substring(openingBracket + 1, closingBracket);

			// Separate the arguments.
			args = splitArguments(argumentStr);
		}

		// Construct the node.
		Node node;
		if (terminal) {
			node = parseTerminal(identifier);
		} else {
			node = initialiseFunction(identifier);
		}

		// Check the arities match.
		if ((node == null) || (node.getArity() != args.size())) {
			throw new MalformedProgramException();
		} else {
			// Recursively parse and set each child node.
			for (int i = 0; i < args.size(); i++) {
				node.setChild(i, parse(args.get(i)));
			}
			
			// Validate the node's input data-types.
			if (node.getReturnType() == null) {
				if (node.isFunction()) {
					throw new MalformedProgramException("Input data-types for " + node.getIdentifier() + " are invalid");
				} else {
					throw new MalformedProgramException("Data-type of terminal " + node.getIdentifier() + " is null");
				}
			}
		}

		return node;
	}

	/*
	 * Constructs and returns an instance of the function which matches the 
	 * given name. The node collection is searched for an instance first, then 
	 * the function class collection.
	 */
	private Node initialiseFunction(final String name) throws MalformedProgramException {
		// Attempt to find function in node collection first.
		Node node = initialiseFunctionNode(name);

		// If that failed, then fall back to trying the classes.
		if (node == null) {
			node = initialiseFunctionClass(name);
		}
		
		if (node == null) {
			// Function unknown - error.
			throw new MalformedProgramException("Unknown function " + name + " encountered");
		}

		return node;
	}
	
	/*
	 * Constructs a new instance of the function node that matches the given 
	 * name. If no function nodes match, then null is returned.
	 */
	private Node initialiseFunctionNode(final String name) {
		Node node = null;
		
		// Find the function node instance.
		final Node functionNode = functionNodes.get(name);
		
		// Construct a new instance of that node.
		if (functionNode != null) {
			node = functionNode.newInstance();
		}
		
		return node;
	}
	
	/*
	 * Constructs a new instance of the function class that matches the given
	 * name. If no function classes match, then null is returned.
	 */
	private Node initialiseFunctionClass(final String name) throws MalformedProgramException {
		Node node = null;
		
		// Find the function class.
		final Class<?> functionClass = functions.get(name);

		if (functionClass != null) {
			// Instantiate as a function node.
			try {
				node = (Node) functionClass.newInstance();
			} catch (final InstantiationException e) {
				assert false;
				throw new MalformedProgramException("Attempting to use invalid function " + name);
			} catch (final IllegalAccessException e) {
				assert false;
				throw new MalformedProgramException("Attempting to use invalid function " + name);
			}
		}

		return node;
	}

	/*
	 * Parses the given string as a terminal - either a variable or a literal. 
	 * If the given string matches the name of a declared variable, then that 
	 * variable instance will be returned. Otherwise an attempt will be made to
	 * parse the string as a literal. The resulting node will be returned. If 
	 * the string does not match either a known variable or literal, then null
	 * will be returned.
	 */
	private Node parseTerminal(final String terminalStr) {
		Node node = variables.get(terminalStr);

		if (node == null) {
			node = parseLiteral(terminalStr);
		}

		return node;
	}

	/**
	 * Parses the given string as the value of a literal. Any user declared 
	 * literals are checked first, if any match, then a new Literal node is 
	 * constructed with the provided <code>Object</code> as the value. If no 
	 * match is made then an attempt will be made to parse the string as one of
	 * the following types: (boolean, String, char, int, long, double, float), 
	 * which will become the value of a newly constructed Literal, which will be 
	 * returned. If parsing fails, then <code>null</code> will be returned. 
	 * 
	 * @param literalStr the string to be parsed as some type of 
	 * <code>Literal</code>
	 * @return a new <code>Literal</code> instance with a value as parsed, or 
	 * <code>null</code> if the string was unable to be parsed.
	 */
	protected Literal parseLiteral(String literalStr) {
		Literal literal = null;
		
		// Remove excess spaces.
		literalStr = literalStr.trim();
		
		if (literalValues.containsKey(literalStr)) {
			literal = new Literal(literalValues.get(literalStr));
		} else if (literalStr.equalsIgnoreCase("true") || literalStr.equalsIgnoreCase("false")) {
			literal = new Literal(Boolean.valueOf(literalStr));
		} else if (literalStr.startsWith("\"") && literalStr.endsWith("\"") && (literalStr.length() > 1)) {
			literal = new Literal(literalStr.substring(1, literalStr.length() - 1));
		} else if ((literalStr.startsWith("'") && literalStr.endsWith("'") && (literalStr.length() == 3))) {
			// Non-escaped char.
			literal = new Literal(literalStr.charAt(1));
		} else if ((literalStr.startsWith("'\\") && literalStr.endsWith("'") && literalStr.length() == 4)) {
			// Escaped char - unescape first.
			literalStr = StringEscapeUtils.unescapeJava(literalStr);

			literal = new Literal(literalStr.charAt(1));
		} else {
			literal = parseNumericLiteral(literalStr);
		}

		return literal;
	}
	
	/**
	 * Attempts to parse the given string as a number, and wraps it in a new
	 * <code>Literal</code> instance that is returned. If unable to parse the 
	 * given string as a number, then this method will return <code>null</code>.
	 * 
	 * <p>The string will be parsed as follows:
	 * 
	 * <ul>
	 * <li>If the string contains a numeric value (with or without decimal) 
	 * followed by a 'd' or 'D' character, then the literal will have a Double
	 * value.</li>
	 * <li>If the string contains a numeric value (with or without decimal) 
	 * followed by an 'f' or 'F' character, then the literal will have a Float
	 * value.</li>
	 * <li>If the string contains a numeric value followed by an 'l' or 'L' 
	 * character, or if the value is an integer value out of range of the Java
	 * int type, then the literal will have a Long value.</li>
	 * <li>If the string contains an integer value in range of the Java int type
	 * then the literal will have an Integer value.</li>
	 * </ul>
	 *  
	 * @param numericStr a string containing a numeric value to be parsed as a 
	 * <code>Literal</code>.
	 * @return a new <code>Literal</code> with a numeric value. Or 
	 * <code>null</code> if the given <code>numericStr</code> could not be 
	 * parsed as a numeric value.
	 */
	protected Literal parseNumericLiteral(String numericStr) {
		Literal literal = null;
		
		// Parse as a number - this will not throw an exception.
		final Number n = NumberFormat.getInstance().parse(numericStr, new ParsePosition(0));

		if (n instanceof Long) {
			// If ended with 'D' or 'd', then use double instead of long.
			if (numericStr.endsWith("D") || numericStr.endsWith("d")) {
				literal = new Literal(n.doubleValue());
			}
			
			// If ended with 'F' or 'f', then use float instead of long.
			else if (numericStr.endsWith("F") || numericStr.endsWith("f")) {
				literal = new Literal(n.floatValue());
			}
			
			// If within range, and not ending with 'L' or 'l', use int instead of long.
			else if (!numericStr.endsWith("L") && !numericStr.endsWith("l")) {
				long longValue = n.longValue();
				if ((longValue <= Integer.MAX_VALUE) && (longValue >= Integer.MIN_VALUE)) {
					literal = new Literal(n.intValue());
				}
			}
		} else if (n instanceof Double) {
			// If ended with 'F' or 'f', then use float instead of double.
			if (numericStr.endsWith("F") || numericStr.endsWith("f")) {
				literal = new Literal(n.floatValue());
			}
		}

		// For all other types - construct the new literal.
		if ((literal == null) && (n != null)) {
			literal = new Literal(n);
		}
		
		return literal;
	}
	
	/**
	 * Declares a new literal that may be parsed as the given literal value. The
	 * <code>literalStr</code> should not match the name of any declared 
	 * variables. When parsing an Epox program, the given string found as a 
	 * terminal, will be parsed as a new <code>Literal</code> instance of the 
	 * given value. The new literal's value will be a shallow copy of the given 
	 * <code>Object</code>. If a literal with an equal <code>literalStr</code>
	 * has previously been declared, then it will be overwritten.
	 * 
	 * @param literalStr the string representation that if matched against a 
	 * terminal in an Epox program undergoing parsing, will be constructed as a
	 * <code>Literal</code> of the given <code>Object</code> value.
	 * @param literalValue the <code>Object</code> value to assign a new
	 * <code>Literal</code> that matches the given <code>literalStr</code>.
	 */
	public void declareLiteral(final String literalStr, final Object literalValue) {
		literalValues.put(literalStr, literalValue);
	}
	
	/**
	 * Removes the given <code>literalStr</code> from those that can be parsed.
	 * Any future terminals that match the given <code>literalStr</code> will
	 * no longer be parsed as a new <code>Literal</code>.
	 * 
	 * @param literalStr the literal string that should no longer be parsed as
	 * a <code>Literal</code>.
	 * @return the previously declared literal value that was removed, or 
	 * <code>null</code> if nothing was removed.
	 */
	public Object undeclareLiteral(final String literalStr) {
		return literalValues.remove(literalStr);
	}

	/**
	 * Declares a new function that can be constructed when a function of the
	 * given name is parsed. During parsing if a function is encountered with an
	 * identifier that matches the provided <code>name</code> parameter, then a
	 * new instance of the given <code>Node</code> class is constructed. Note 
	 * that any functions provided as nodes to the 
	 * {@link EpoxParser#declareFunction(String, Node)} method, will be 
	 * constructed in preference to those declared through a call to this 
	 * method. Declaring a new function using this method with a name that is 
	 * equal to any previously declared functions, will replace the existing 
	 * function - this applies to the standard Epox functions too, which may be
	 * replaced through this method.
	 * 
	 * @param name the identifier to match when parsing the provided function.
	 * @param functionClass a <code>Class</code> from which a new node will be
	 * constructed, upon parsing of an Epox function with the given name.
	 */
	public void declareFunction(final String name, final Class<? extends Node> functionClass) {
		functions.put(name, functionClass);
	}
	
	/**
	 * Declares a new function that can be constructed when a function of the
	 * given name is parsed. During parsing if a function is encountered with an
	 * identifier that matches the provided <code>name</code> parameter, then a
	 * new instance of the given <code>Node</code> is constructed. Note 
	 * that functions provided through this method will be constructed in 
	 * preference to those declared through a call to the 
	 * {@link EpoxParser#declareFunction(String, Class)} method. Declaring a new
	 * function using this method with a name that is equal to any previously 
	 * declared functions, will replace the existing function. The standard Epox
	 * functions will also be overridden, since functions declared through this
	 * method are given preference.
	 * 
	 * @param name the identifier to match when parsing the provided function.
	 * @param functionNode a <code>Node</code> from which a new instance will be
	 * constructed upon parsing of an Epox function with the given name.
	 */
	public void declareFunction(final String name, final Node functionNode) {
		functionNodes.put(name, functionNode);
	}
	
	/**
	 * Removes any functions of the given name from those that can parsed. This
	 * includes those functions declared through either of the 
	 * <code>declareFunction</code> methods, or the standard Epox functions.
	 * 
	 * @param name the identifier of the function that should no longer be 
	 * parsed.
	 */
	public void undeclareFunction(final String name) {
		functions.remove(name);
		functionNodes.remove(name);
	}

	/**
	 * Declares the given variables, so that any terminals that match the 
	 * identifiers of the given variables will be parsed as such.
	 * 
	 * @param variables the variables that should be declared.
	 */
	public void declareVariables(final List<Variable> variables) {
		if (variables == null) {
			throw new IllegalArgumentException("variables should be non-null");
		}
		
		for (final Variable v: variables) {
			declareVariable(v);
		}
	}

	/**
	 * Declares the given variable, so that any terminals that match the given
	 * variable's identifier will be parsed as that variable.
	 * 
	 * @param variable the variable that should be declared.
	 */
	public void declareVariable(final Variable variable) {
		if (variable != null) {
			variables.put(variable.getIdentifier(), variable);
		}
	}
	
	/**
	 * Removes the given variable from those that are parseable. The variable's
	 * identifier will no longer be parsed as that variable.
	 * 
	 * @param variable the variable to be undeclared.
	 */
	public void undeclareVariable(final Variable variable) {
		if (variable != null) {
			variables.remove(variable.getIdentifier());
		}
	}

	/**
	 * Removes all variables from those that are parseable. The parser will no 
	 * longer recognise any variables.
	 */
	public void undeclareAllVariables() {
		variables.clear();
	}

	/*
	 * Splits a string that represents zero or more arguments to a function, 
	 * separated by either commas or spaces, into the separate arguments. Each
	 * argument may contain nested method calls, which themselves have multiple
	 * arguments - this method splits just the top level of arguments so that 
	 * they can themselves be parsed individually.
	 */
	private List<String> splitArguments(String argStr) {
		int depth = 0;

		final List<String> args = new ArrayList<String>(5);
		StringBuilder buffer = new StringBuilder();

		argStr = argStr.trim();

		for (int i = 0; i < argStr.length(); i++) {
			final char c = argStr.charAt(i);

			if (c == '(') {
				depth++;
			} else if (c == ')') {
				depth--;
			}
			if (((c == ' ') || (c == ',')) && (depth == 0)) {
				args.add(buffer.toString());
				buffer = new StringBuilder();
				while (argStr.charAt(i + 1) == ' ') {
					i++;
				}
			} else {
				buffer.append(c);
			}
		}

		if (buffer.length() > 0) {
			args.add(buffer.toString());
		}

		return args;
	}
}
