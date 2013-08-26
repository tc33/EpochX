/*
 * Copyright 2007-2013
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

import java.util.*;

import org.epochx.interpret.MalformedProgramException;

/**
 * This parser is for parsing valid Epox programs into a node tree. It is only
 * able to parse those node types which have been declared through the parser's
 * <tt>declare</tt> methods.
 * 
 * @see Node
 */
public class EpoxParser {

	private final Map<String, Node> nodes;

	/**
	 * Constructs an <code>EpoxParser</code> with no nodes declared
	 */
	public EpoxParser() {
		nodes = new HashMap<String, Node>();
	}

	/**
	 * Parses an Epox program string as an executable <tt>Node</tt> tree.
	 * The given <tt>source</tt> parameter must contain a valid Epox program
	 * string, comprised only of nodes that have been declared. The data-types 
	 * of each function's inputs must correspond to valid data-types, otherwise 
	 * a <tt>MalformedProgramException</tt> will be thrown.
	 * 
	 * @param source the program string to be parsed as an Epox program.
	 * @return a <tt>Node</tt> which is the root of a tree which is
	 *         equivalent to the provided source string. A <tt>null</tt>
	 *         value will be returned if the <tt>source</tt> parameter is
	 *         <tt>null</tt>.
	 * @throws MalformedProgramException if the given string does not represent
	 *         a valid Epox program.
	 */
	public Node parse(String source) throws MalformedProgramException {
		if (source == null) {
			return null;
		}

		// Locate the first bracket (straight after function name)
		int openingBracket = source.indexOf('(');

		String identifier = null;
		List<String> args = null;

		// If there is no bracket then it must be a terminal
		if (openingBracket == -1) {
			identifier = source;
			args = new ArrayList<String>();
		} else {
			// Get the name of the function
			identifier = source.substring(0, openingBracket).trim();

			// Locate the closing bracket
			int closingBracket = source.lastIndexOf(')');

			// Get the comma separated arguments - without brackets
			String argumentStr = source.substring(openingBracket + 1, closingBracket);

			// Separate the arguments
			args = splitArguments(argumentStr);
		}

		// Construct the node
		Node node = nodes.get(identifier).newInstance();

		// Check the arities match
		if (node == null) {
			throw new MalformedProgramException("unknown node type: " + identifier);
		} else if (node.getArity() != args.size()) {
			throw new MalformedProgramException("unexpected arity for node: " + identifier + "(expected: "
					+ node.getArity() + ", found: " + args.size() + ")");
		} else {
			// Recursively parse and set each child node
			for (int i = 0; i < args.size(); i++) {
				node.setChild(i, parse(args.get(i)));
			}

			// Validate the node's input data-types
			if (node.dataType() == null) {
				if (node.isNonTerminal()) {
					throw new MalformedProgramException("Input data-types for " + identifier + " are invalid");
				} else {
					throw new MalformedProgramException("Data-type of terminal " + identifier + " is null");
				}
			}
		}

		return node;
	}

	/**
	 * Declares a node so that the parser is able to parse nodes of this type. 
	 * The identifier of the node is used to identify nodes of this type in the
	 * source.
	 * 
	 * @param node an instance of the node type to declare
	 */
	public void declare(Node node) {
		nodes.put(node.getIdentifier(), node);
	}
	
	/**
	 * Removes a node so that the parser will no longer parse nodes of this 
	 * type. The identifier of the node must match the identifier from when it 
	 * was declared.
	 * 
	 * @param node an instance of a node type that should no longer be parsed
	 */
	public void undeclare(Node node) {
		nodes.remove(node.getIdentifier());
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
