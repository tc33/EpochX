package org.epochx.gr.example.epox;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.gr.model.GRModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.*;
import org.epochx.tools.grammar.Grammar;
import org.epochx.tools.util.BoolUtils;


public class Multiplexer6Bit extends GRModel {
	
	/*public static final String GRAMMAR_STRING = 
		"<prog> ::= <node>\n" +
		"<node> ::= <function> | <terminal>\n" +
		"<function> ::= NOT( <node> ) " +
					"| OR( <node> , <node> ) " +
					"| AND( <node> , <node> ) " +
					"| IF( <node> , <node> , <node> )\n" +
		"<terminal> ::= a0 | a1 | d0 | d1 | d2 | d3\n";*/
	
	public static final String GRAMMAR_STRING = 
		"<prog> ::= <node>\n" +
		"<node> ::= NOT( <node> ) " +
					"| OR( <node> , <node> ) " +
					"| AND( <node> , <node> ) " +
					"| IF( <node> , <node> , <node> ) " +
					"| <terminal>\n" +
		"<terminal> ::= a0 | a1 | d0 | d1 | d2 | d3\n";
	
	private Grammar grammar;
	
	private boolean[][] inputs;
	
	private Interpreter interpreter;
	
	public Multiplexer6Bit() {
		grammar = new Grammar(GRAMMAR_STRING);
		interpreter = new EpoxInterpreter();
		
		inputs = BoolUtils.generateBoolSequences(6);
	}

	@Override
	public double getFitness(CandidateProgram p) {
		GRCandidateProgram program = (GRCandidateProgram) p;
		
        double score = 0;
        
        String[] argNames = {"a0", "a1", "d0", "d1", "d2", "d3"};
        
        // Execute on all possible inputs.
        for (boolean[] vars : inputs) {
        	// Convert to object array.
        	Boolean[] objVars = ArrayUtils.toObject(vars);
        	
        	Boolean result = (Boolean) interpreter.eval(program.getSourceCode(), argNames, objVars);
        	
            if (result != null && result == chooseResult(vars)) {
                score++;
            }
        }
        
        return 64 - score;
	}
	
    private boolean chooseResult(boolean[] input) {
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
