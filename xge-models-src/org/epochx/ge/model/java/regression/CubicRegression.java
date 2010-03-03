package org.epochx.ge.model.java.regression;

import org.epochx.ge.model.GEAbstractModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.JavaEvaluator;
import org.epochx.tools.grammar.Grammar;

public class CubicRegression extends GEAbstractModel {
	
	public static final String GRAMMAR_STRING = 
		"<expr> ::= ( <expr> <op> <expr> ) | <var>\n" +
		"<op>   ::= + | - | * \n" +
		"<var>  ::= X | 1.0 \n";
	
	private Grammar grammar;
	
	private JavaEvaluator evaluator;
	
	public CubicRegression() {
		grammar = new Grammar(GRAMMAR_STRING);
		evaluator = new JavaEvaluator();
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
        return x + x*x + x*x*x;
    }
    
	@Override
	public Grammar getGrammar() {
		return grammar;
	}
}
