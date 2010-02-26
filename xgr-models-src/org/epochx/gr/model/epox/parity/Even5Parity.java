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
package org.epochx.gr.model.epox.parity;

import org.epochx.tools.grammar.Grammar;

/**
 *
 */
public class Even5Parity extends EvenParity {
	
	public static final String GRAMMAR_STRING =
		"<prog> ::= <node>\n" +
		"<node> ::= <function> | <terminal>\n" +
		"<function> ::= NOT( <node> ) " +
					"| OR( <node> , <node> ) " +
					"| AND( <node> , <node> ) " +
					"| XOR( <node> , <node> )\n" +
		"<terminal> ::= d0 | d1 | d2 | d3 | d4\n";
	
	private Grammar grammar;

	public Even5Parity() {
		super(5);

		grammar = new Grammar(GRAMMAR_STRING);
	}
	
	@Override
	public Grammar getGrammar() {
		return grammar;
	}
}
