package org.epochx.ge.model.ruby.regression;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.RubyEvaluator;
import org.epochx.tools.grammar.Grammar;


public class SymbolicRegression extends GEAbstractModel {

	public static final String GRAMMAR_STRING = 
		"<expr> ::= ( <expr> <op> <expr> ) | <var>\n" +
		"<op>   ::= + | - | * \n" +
		"<var>  ::= X | 1.0 \n";
	
	private Grammar grammar;
	
	private RubyEvaluator evaluator;
	
	private double[][] inputs;
	
	public SymbolicRegression() {
		grammar = new Grammar(GRAMMAR_STRING);
		evaluator = new RubyEvaluator();
		
		inputs = generateInputs(-1.0, 1.0, 0.1);
	}

	private double[][] generateInputs(double from, double to, double period) {
		// Get the absolute difference between to and from.
		double diff = Math.abs(to - from);
		int noInputs = ((int) Math.floor(diff/period)) + 1;
		double[][] inputs = new double[noInputs][];
		
		for (int i=0; i<noInputs; i++){
        	inputs[i] = new double[]{from + (period * i)};
		}
		
		return inputs;
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
        // Execute on all possible inputs.
        double dd = 0;
        
		// Convert to object array.
		Double[][] objInputs = new Double[inputs.length][];
    	for (int i=0; i<objInputs.length; i++) {
    		objInputs[i] = ArrayUtils.toObject(inputs[i]);
    	}
    	
    	// JRuby seems to return the result as a Float.
    	Object[] results = evaluator.eval(program.getSourceCode(), new String[]{"x"}, objInputs);
        
        for (int i=0; i<results.length; i++){
        	if (results[i] != null) {
        		dd += Math.abs((Float) results[i] - fun(inputs[i][0]));
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
