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
 * The latest version is available from: http://www.epochx.org
 */
package org.epochx.epox;

import java.text.*;
import java.util.*;

import org.epochx.epox.ant.*;
import org.epochx.epox.bool.*;
import org.epochx.epox.lang.*;
import org.epochx.epox.math.*;
import org.epochx.epox.trig.*;
import org.epochx.tools.eval.MalformedProgramException;

/**
 * This parser is for parsing valid Epox programs into a node tree. It is only 
 * able to parse those node types which are part of the standard language, or 
 * which have been added to the parser through its <code>addFunction</code> 
 * method. It is also only able to construct instances of nodes which require no
 * arguments provided to their constructor, this excludes all nodes which can 
 * handle multiple arities, such as the {@link SeqNFunction} function. Use the 
 * explicit arity versions instead such as {@link Seq2Function}.
 * 
 * <p>Variables may be named with any String. There will be no name clash 
 * between variables and functions. There could however be a clash between 
 * variables and literals, so variable names that could be parsed as any valid 
 * literal value should be avoided.
 * 
 * @see Node
 */
public class EpoxParser {

	// The functions the parser knows about and their identifiers.
	private final Map<String, Class<? extends Node>> functions;
	
	// More functions, but held by a node instance rather than class.
	private final Map<String, Node> functionNodes;

	// Known variables, any variables that are not provided will be created.
	private final Map<String, Variable> variables;

	/**
	 * Constructs an <code>EpoxParser</code>.
	 */
	public EpoxParser() {
		variables = new HashMap<String, Variable>();
		functions = new HashMap<String, Class<? extends Node>>();
		functionNodes = new HashMap<String, Node>();

		initialiseKnownFunctions();
	}

	private void initialiseKnownFunctions() {
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
		functions.put("ASIN", ArcSineFunction.class);
		functions.put("ATAN", ArcTangentFunction.class);
		functions.put("ARCOSH", AreaHyperbolicCosineFunction.class);
		functions.put("ARSINH", AreaHyperbolicSineFunction.class);
		functions.put("ARTANH", AreaHyperbolicTangentFunction.class);
		functions.put("COSEC", CosecantFunction.class);
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
			identifier = source.substring(0, openingBracket);

			// Locate the closing bracket.
			final int closingBracket = source.lastIndexOf(')');

			// Get the comma separated arguments - without the surrounding
			// brackets.
			final String argumentStr = source.substring(openingBracket + 1, closingBracket);

			// Separate the arguments.
			args = splitArguments(argumentStr);
		}

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
			for (int i = 0; i < args.size(); i++) {
				node.setChild(i, parse(args.get(i)));
			}
		}

		return node;
	}

	/*
	 * We do lazy initialisation, so if the object hasn't been initialised yet,
	 * we do it now.
	 */
	private Node initialiseFunction(final String name) throws MalformedProgramException {
		// Attempt to find function in node collection first.
		Node node = initialiseFunctionNode(name);

		// If that failed, then fall back to trying the classes, and thr.
		if (node == null) {
			node = initialiseFunctionClass(name);
		}
		
		if (node == null) {
			// Function unknown - error.
			throw new MalformedProgramException("Unknown function " + name + " encountered");
		}

		return node;
	}
	
	private Node initialiseFunctionNode(final String name) {
		// The function node we're going to create.
		Node node = null;
		
		// Try to instantiate from the functionNode collection first.
		final Node functionNode = functionNodes.get(name);
		
		if (functionNode != null) {
			node = functionNode.newInstance();
		}
		
		return node;
	}
	
	private Node initialiseFunctionClass(final String name) throws MalformedProgramException {
		// The function node we're hopefully going to create.
		Node node = null;
		
		// Attempt to find the function in the list of simple functions.
		final Class<?> functionClass = functions.get(name);

		if (functionClass != null) {
			// Instantiate the function node.
			try {
				node = (Node) functionClass.newInstance();
			} catch (final InstantiationException e) {
				assert false;
			} catch (final IllegalAccessException e) {
				assert false;
			}
		}

		return node;
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
	private Node parseTerminal(final String terminalStr) {
		Node node = variables.get(terminalStr);

		if (node == null) {
			node = parseLiteral(terminalStr);
		}

		return node;
	}

	/**
	 * Should return null if the string cannot be parsed as any type of valid
	 * literal.
	 * 
	 * @param literalStr
	 * @return
	 */
	protected Literal parseLiteral(final String literalStr) {
		Literal literal = null;
		if (literalStr.equalsIgnoreCase("true") || literalStr.equalsIgnoreCase("false")) {
			literal = new Literal(Boolean.valueOf(literalStr));
		} else if (literalStr.startsWith("\"") && literalStr.endsWith("\"") && (literalStr.length() > 1)) {
			literal = new Literal(literalStr.substring(1, literalStr.length() - 2));
		} else if ((literalStr.length() == 3) && literalStr.startsWith("'") && literalStr.endsWith("'")) {
			// TODO This isn't going to catch escaped characters because they
			// will have been converted to e.g. \\x
			literal = new Literal(literalStr.charAt(1));
		} else {
			final Number n = NumberFormat.getInstance().parse(literalStr, new ParsePosition(0));

			// If possible, then use int instead of long.
			if (n instanceof Long) {
				final long l = (Long) n;
				if ((l <= Integer.MAX_VALUE) && (l >= Integer.MIN_VALUE)) {
					literal = new Literal((int) l);
				}
			}

			if ((literal == null) && (n != null)) {
				literal = new Literal(n);
			}
		}

		if (literal == null) {
			literal = parseObjectLiteral(literalStr);
		}

		return literal;
	}

	/**
	 * Can be overridden to provide support for other literal types.
	 * 
	 * @param literalStr
	 * @return
	 */
	protected Literal parseObjectLiteral(final String literalStr) {
		return null;
	}

	public void addFunction(final String name, final Class<? extends Node> functionClass) {
		functions.put(name, functionClass);
	}
	
	public void addFunction(final String name, final Node functionNode) {
		
	}

	public void addAvailableVariables(final List<Variable> variables) {
		for (final Variable v: variables) {
			addAvailableVariable(v);
		}
	}

	public void addAvailableVariable(final Variable variable) {
		variables.put(variable.getIdentifier(), variable);
	}

	public void clearAvailableVariables() {
		variables.clear();
	}

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
