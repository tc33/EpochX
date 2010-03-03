package org.epochx.ge.example.java.parity;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.core.Controller;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.op.selection.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.tools.eval.JavaEvaluator;
import org.epochx.tools.grammar.Grammar;
import org.epochx.tools.util.BoolUtils;



public class Even4Parity extends GEAbstractModel {
	
	private Grammar grammar;
	
	private boolean[][] inputs;
	
	private JavaEvaluator evaluator;
	
	public Even4Parity() {
		//getRNG().setSeed(10001);
		grammar = new Grammar(new File("example-grammars/Java/EvenFourParity.bnf"));
		evaluator = new JavaEvaluator();
		
		inputs = BoolUtils.generateBoolSequences(4);
		
		setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE, GenerationStatField.RUN_TIME});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM, RunStatField.RUN_TIME});
		
		setNoRuns(1);
		setNoElites(50);
		setNoGenerations(50);
		setPopulationSize(500);
		setMaxProgramDepth(17);
		setMaxInitialProgramDepth(6);
		setMutationProbability(0.4);
		setCrossoverProbability(0.4);
		setProgramSelector(new TournamentSelector(this, 7));
		setPoolSelector(new RandomSelector(this));
		setPoolSize(50);
		setInitialiser(new RampedHalfAndHalfInitialiser(this));
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		double score = 0;
		
        // Execute on all possible inputs.
        for (int i=0; i<inputs.length; i++) {
        	boolean[] vars = inputs[i];
        	// Convert to object array.
        	Boolean[] objVars = ArrayUtils.toObject(vars);
        	String[] argNames = new String[]{"d0", "d1", "d2", "d3"};
        	Boolean result = (Boolean) evaluator.eval(program.getSourceCode(), argNames, objVars);

            if (result != null && result == chooseResult(vars)) {
                score++;
            } else if (!program.isValid()) {
            	score = 0;
            	break;
            }
        }
        
        return 16 - score;
	}

    private boolean chooseResult(boolean[] input) {
        // scoring solution
        int eCount = 0;
        for(int i = 0; i<input.length; i++) {
            if(input[i]==true) {
                eCount++;
            }
        }
        if(eCount%2==0) {
            return true;
        } else {
            return false;
        }
    }
    
	@Override
	public Grammar getGrammar() {
		return grammar;
	}
	
	public static void main(String[] args) {
		Controller.run(new Even4Parity());
	}

}
