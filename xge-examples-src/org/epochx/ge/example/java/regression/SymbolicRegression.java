package org.epochx.ge.example.java.regression;

import java.io.File;

import org.epochx.core.Controller;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.mapper.DepthFirstMapper;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.tools.eval.JavaEvaluator;
import org.epochx.tools.grammar.Grammar;


public class SymbolicRegression extends GEAbstractModel {

	private Grammar grammar;
	
	private JavaEvaluator evaluator;
	
	public SymbolicRegression() {
		grammar = new Grammar(new File("example-grammars/Java/QuarticRegression.bnf"));
		evaluator = new JavaEvaluator();
		
		setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE, GenerationStatField.RUN_TIME});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM, RunStatField.RUN_TIME});

		setMutationProbability(0.01);
		setCrossoverProbability(0.9);
		setNoGenerations(50);
		setPopulationSize(500);
		setMaxChromosomeLength(10);
		
		setMapper(new DepthFirstMapper(this));
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
	
	public static void main(String[] args) {
		Controller.run(new SymbolicRegression());
	}
}
