package org.epochx.ge.model.ruby.mux;

import org.epochx.tools.grammar.Grammar;


public class Multiplexer6Bit extends Multiplexer {
	
	public static final String GRAMMAR_STRING = 
		"<prog> ::= <expr>\n" +
		"<expr> ::= <expr> <op> <expr> " +
				"| ( <expr> <op> <expr> ) " +
				"| <var> " +
				"| <pre-op> ( <var> ) " +
				"| ( <expr> ) ? <expr> : <expr>\n" +
		"<pre-op> ::= !\n" +
		"<op> ::= \"||\" | &&\n" +
		"<var> ::= a0 | a1 | d0 | d1 | d2 | d3\n";
	
	private Grammar grammar;
	
	public Multiplexer6Bit() {
		super(6, 2);
		
		grammar = new Grammar(GRAMMAR_STRING);
	}

    protected boolean chooseResult(boolean[] input) {
        boolean result = false;
    	// scoring solution
        if(input[0] && input[1]) {
            result = input[2];
        } else if(input[0] && !input[1]) {
            result = input[3];
        } else if(!input[0] && input[1]) {
            result = input[4];
        } else if(!input[0] && !input[1]) {
            result = input[5];
        }
        return result;
    }
    
	@Override
	public Grammar getGrammar() {
		return grammar;
	}
}
