package org.epochx.gr.model.epox;

import org.epochx.gr.model.GRModel;
import org.epochx.gr.representation.GRCandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.*;
import org.epochx.tools.grammar.Grammar;

public class QuarticRegression extends GRModel {
	
	public static final String GRAMMAR_STRING = 
		"<prog> ::= <node>\n" +
		"<node> ::= <function> | <terminal>\n" +
		"<function> ::= ADD( <node> , <node> ) " +
					"| SUB( <node> , <node> ) " +
					"| MUL( <node> , <node> ) " +
					"| PDIV( <node> , <node> )\n" +
		"<terminal> ::= X | 1.0\n";
	
	private Grammar grammar;
	
	private Evaluator evaluator;
	
	public QuarticRegression() {
		grammar = new Grammar(GRAMMAR_STRING);
		evaluator = new EpoxEvaluator();
	}

	@Override
	public double getFitness(CandidateProgram p) {
		GRCandidateProgram program = (GRCandidateProgram) p;
		
        // Execute on all possible inputs.
        double dd = 0;
        for (double x = -1; x < 1; x+=0.1){
        	Double result = (Double) evaluator.eval(program.getSourceCode(), new String[]{"X"}, new Double[]{x});
        	if (result != null) {
        		dd += Math.abs(result - fun(x));
        	}
        }
        return dd;
	}	

    public double fun(double x){
        return x + x*x + x*x*x + x*x*x*x;
    }
    
	@Override
	public Grammar getGrammar() {
		return grammar;
	}
}
