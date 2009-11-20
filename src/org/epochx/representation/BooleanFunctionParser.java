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

import org.epochx.representation.bool.*;

/**
 *
 */
public class BooleanFunctionParser extends FunctionParser<Boolean> {

	private final Map<String, Class<?>> functions;
	
	public BooleanFunctionParser() {
		functions = new HashMap<String, Class<?>>();
		functions.put("AND", AndFunction.class);
		functions.put("IFF", IfAndOnlyIfFunction.class);
		functions.put("IF", IfFunction.class);
		functions.put("IMPLIES", ImpliesFunction.class);
		functions.put("NAND", NandFunction.class);
		functions.put("NOR", NorFunction.class);
		functions.put("NOT", NotFunction.class);
		functions.put("OR", OrFunction.class);
		functions.put("XOR", XorFunction.class);
	}
	
	public Node<Boolean> parseBoolean(String source) {
		return (Node<Boolean>) parse(source, functions);
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
	public TerminalNode<Boolean> parseTerminal(String terminalStr) {
		if (terminalStr.equalsIgnoreCase("true") 
				 	|| terminalStr.equalsIgnoreCase("false")) {
			return new TerminalNode<Boolean>(Boolean.valueOf(terminalStr));
		} else {
			return new Variable<Boolean>(terminalStr);
		}
	}
	
	public static void main(String[] args) {
		BooleanFunctionParser parser = new BooleanFunctionParser();
		
		System.out.println(parser.parseBoolean("IF(AND(true,false),NOT(true),false)").toString());
	}
	
}
