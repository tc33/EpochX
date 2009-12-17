package org.epochx.ge.example.java.parity;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.core.Controller;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.mapper.DepthFirstMapper;
import org.epochx.ge.op.init.RampedHalfAndHalfInitialiser;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.op.selection.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.stats.*;
import org.epochx.tools.eval.JavaEvaluator;
import org.epochx.tools.grammar.Grammar;
import org.epochx.tools.util.BoolUtils;



public class Even5Parity extends GEAbstractModel {

	private Grammar grammar;
	
	private boolean[][] inputs;
	
	private JavaEvaluator evaluator;
	
	public Even5Parity() {
		grammar = new Grammar(new File("example-grammars/Java/EvenFiveParity.bnf"));
		evaluator = new JavaEvaluator();
		
		inputs = BoolUtils.generateBoolSequences(5);
		
		//setGenStatFields(new GenerationStatField[]{GenerationStatField.RUN_TIME, GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.FITNESS_STDEV, GenerationStatField.LENGTH_AVE, GenerationStatField.LENGTH_STDEV, GenerationStatField.BEST_PROGRAM});
		setGenStatFields(new GenerationStatField[]{GenerationStatField.FITNESS_MIN, GenerationStatField.FITNESS_AVE, GenerationStatField.LENGTH_AVE});
		setRunStatFields(new RunStatField[]{RunStatField.BEST_FITNESS, RunStatField.BEST_PROGRAM});
		setNoRuns(100);
		setNoElites(10);
		setNoGenerations(100);
		setPopulationSize(100);
		setMaxProgramDepth(10);
		setMutationProbability(0.1);
		setCrossoverProbability(0.9);
		setProgramSelector(new TournamentSelector(this, 7));
		setPoolSelector(new RandomSelector(this));
		setPoolSize(20);
		setInitialiser(new RampedHalfAndHalfInitialiser(this));
		
		setMapper(new DepthFirstMapper(this));
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
        	String[] argNames = new String[]{"d0", "d1", "d2", "d3", "d4"};
        	Boolean result = (Boolean) evaluator.eval(program.getSourceCode(), argNames, objVars);

            if (result != null && result == chooseResult(vars)) {
                score++;
            } else if (!program.isValid()) {
            	score = 0;
            	break;
            }
        }
        
        return 32 - score;
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
   
    private int successCount = 0;
    
    @Override
    public void runStats(int runNo, Object[] stats) {
    	// Call super so we still print to screen.
    	super.runStats(runNo, stats);
    	
    	double fitness = (Double) stats[0];
    	if (fitness == 0.0) {
    		successCount++;
    	}
    	System.out.println(successCount);
    }
    
	@Override
	public Grammar getGrammar() {
		return grammar;
	}
	
	public static void main(String[] args) {
		Controller.run(new Even5Parity());
	}
}
