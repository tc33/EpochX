package org.epochx.ge.model.epox.regression;

import org.epochx.ge.model.GEAbstractModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.*;
import org.epochx.tools.grammar.Grammar;

public class QuarticRegression extends GEAbstractModel {
	
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
		GECandidateProgram program = (GECandidateProgram) p;
		
        // Execute on all possible inputs.
        double dd = 0;
        for (double x = -1; x < 1; x+=0.1){
        	Double result = (Double) evaluator.eval(program.getSourceCode(), new String[]{"X"}, new Double[]{x});
        	if (result != null) {
        		dd += Math.abs(result - fun(x));
        	} else {
        		if (!program.isValid()) {
        			dd = Double.POSITIVE_INFINITY;
        			break;
        		}
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
