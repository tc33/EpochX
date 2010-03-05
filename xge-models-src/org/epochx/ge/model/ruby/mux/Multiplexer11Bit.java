/*  
 *  Copyright 2007-2010 Tom Castle & Lawrence Beadle
 *  Licensed under GNU General Public License
 * 
 *  This file is part of EpochX: genetic programming software for research
 *
 *  EpochX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  EpochX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with EpochX. If not, see <http://www.gnu.org/licenses/>.
 * 
 *  The latest version is available from: http:/www.epochx.org
 */
package org.epochx.ge.model.ruby.mux;

import org.epochx.tools.grammar.Grammar;


public class Multiplexer11Bit extends Multiplexer {
	
	public static final String GRAMMAR_STRING = 
		"<prog> ::= <expr>\n" +
		"<expr> ::= <expr> <op> <expr> " +
				"| ( <expr> <op> <expr> ) " +
				"| <var> " +
				"| <pre-op> ( <var> ) " +
				"| ( <expr> ) ? <expr> : <expr>\n" +
		"<pre-op> ::= !\n" +
		"<op> ::= \"||\" | &&\n" +
		"<var> ::= a0 | a1 | a2 | d0 | d1 | d2 | d3 | d4 | d5 | d6 | d7\n";
	
	private Grammar grammar;
	
	public Multiplexer11Bit() {
		super(11, 3);
		
		grammar = new Grammar(GRAMMAR_STRING);
	}
	
    protected boolean chooseResult(boolean[] input) {
    	boolean result = false;
    	// scoring solution
        if(input[0] && input[1] && input[2]) {
            result = input[3];
        } else if(input[0] && input[1]& !input[2]) {
            result = input[4];            
        } else if(input[0] && !input[1] && input[2]) {
            result = input[5];
        } else if(input[0] && !input[1] && !input[2]) {
            result = input[6];
        } else if(!input[0] && input[1] && input[2]) {
            result = input[7];
        } else if(!input[0] && input[1] && !input[2]) {
            result = input[8];
        } else if(!input[0] && !input[1] && input[2]) {
            result = input[9];
        } else if(!input[0] && !input[1] && !input[2]) {
            result = input[10];
        }
        return result;
    }
    
	@Override
	public Grammar getGrammar() {
		return grammar;
	}

}
