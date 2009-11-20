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

/**
 * The function parser can parse a nested function into a node tree. It can 
 * only parse those functions that it knows about.
 */
public abstract class FunctionParser<TYPE> {
	
	public FunctionParser() {
		
	}
	
	public Node<TYPE> parse(String source, Map<String, Class<?>> functions) {

		// Locate the first bracket (straight after function name).
		int openingBracket = source.indexOf('(');
		
		// If there is no bracket then it must be a terminal.
		if (openingBracket == -1) {
			return parseTerminal(source);
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
					node.setChild(i, parse(args.get(i), functions));
				}
			}
			
			return node;
		}
	}
	
	public abstract TerminalNode<TYPE> parseTerminal(String terminalStr);
	
	
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
}
