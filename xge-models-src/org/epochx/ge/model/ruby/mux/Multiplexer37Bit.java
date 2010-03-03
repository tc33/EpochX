package org.epochx.ge.model.ruby.mux;

import org.epochx.tools.grammar.Grammar;


public class Multiplexer37Bit extends Multiplexer {

	public static final String GRAMMAR_STRING = 
		"<prog> ::= <expr>\n" +
		"<expr> ::= <expr> <op> <expr> " +
				"| ( <expr> <op> <expr> ) " +
				"| <var> " +
				"| <pre-op> ( <var> ) " +
				"| ( <expr> ) ? <expr> : <expr>\n" +
		"<pre-op> ::= !\n" +
		"<op> ::= \"||\" | &&\n" +
		"<var> ::= a0 | a1 | a2 | a3 | a4 | d0 | d1 | d2 | d3 | d4 " +
				"| d5 | d6 | d7 | d8 | d9 | d10 | d11 | d12 | d13 " +
				"| d14 | d15 | d16 | d17 | d18 | d19 | d20 | d21 " +
				"| d22 | d23 | d24 | d25 | d26 | d27 | d28 | d29 " +
				"| d30 | d31\n";
	
	private Grammar grammar;
	
	public Multiplexer37Bit() {
		super(37, 5);
		
		grammar = new Grammar(GRAMMAR_STRING);
	}
	
	//TODO This is the same as for 20bit, it shouldn't be. Same for Java version.
    protected boolean chooseResult(boolean[] input) {
    	// scoring solution
        String locator = "";
        if(input[0]==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        if(input[1]==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        if(input[2]==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        if(input[3]==true) {
            locator = locator + "1";
        } else {
            locator = locator + "0";
        }
        
        int location = (15 - Integer.parseInt(locator, 2)) + 4;        
        return input[location];
    }
    
	@Override
	public Grammar getGrammar() {
		return grammar;
	}
}
