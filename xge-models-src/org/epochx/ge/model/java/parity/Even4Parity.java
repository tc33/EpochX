package org.epochx.ge.model.java.parity;

import org.epochx.tools.grammar.Grammar;


public class Even4Parity extends EvenParity {
	
	public static final String GRAMMAR_STRING = 
		"<prog> ::= <expr>\n" +
		"<expr> ::= <expr> <op> <expr> " +
				"| ( <expr> <op> <expr> ) " +
				"| <var> " +
				"| <pre-op> ( <var> )\n" +
		"<pre-op> ::= !\n" +
		"<op> ::= \"||\" | && | !=\n" +
		"<var> ::= d0 | d1 | d2 | d3 \n";
	
	private Grammar grammar;
	
	public Even4Parity() {
		super(4);
		
		grammar = new Grammar(GRAMMAR_STRING);
	}
    
	@Override
	public Grammar getGrammar() {
		return grammar;
	}
}
