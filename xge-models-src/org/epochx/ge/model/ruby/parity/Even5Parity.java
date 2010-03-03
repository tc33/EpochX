package org.epochx.ge.model.ruby.parity;

import org.epochx.tools.grammar.Grammar;


public class Even5Parity extends EvenParity {
	
	public static final String GRAMMAR_STRING = 
		  "<prog> ::= <expr>\n" +
		  "<expr> ::= ( <expr> <op> <expr> ) " +
		  			"| <var> " +
		  			"| <pre-op> ( <var> )\n" +
		  "<pre-op> ::= !\n" +
		  "<op> ::= \"||\" | && | !=\n" +
		  "<var> ::= d0 | d1 | d2 | d3 | d4 \n";
	
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
