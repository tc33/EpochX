package org.epochx.ge.model.ruby.parity;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.ge.model.GEAbstractModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.RubyEvaluator;
import org.epochx.tools.util.BoolUtils;


public abstract class EvenParity extends GEAbstractModel {
	
	private boolean[][] inputs;
	
	private RubyEvaluator evaluator;
	
	private int noInputs;
	
	private String[] argNames;
	
	public EvenParity(int noInputBits) {		
		evaluator = new RubyEvaluator();
		
		inputs = BoolUtils.generateBoolSequences(noInputBits);
		noInputs = (int) Math.pow(2, noInputBits);
		argNames = new String[noInputBits];
		for (int i=0; i<noInputBits; i++) {
			argNames[i] = "d" + i;
		}
	}

	/*@Override
	public double getFitness(GECandidateProgram program) {
		double score = 0;
		
		// Convert to object array.
		Boolean[][] objInputs = new Boolean[inputs.length][];
    	for (int i=0; i<objInputs.length; i++) {
    		objInputs[i] = ArrayUtils.toObject(inputs[i]);
    	}
    	
		Object[] results = evaluator.eval(program, argNames, objInputs);
        for (int i=0; i<results.length; i++) {
	    	boolean[] vars = inputs[i];
	
	        if (results[i] != null && ((Boolean) results[i] == chooseResult(vars))) {
	            score++;
	        } else if (!program.isValid()) {
	        	score = 0;
	        	break;
	        }
	    }
        
        return noInputs - score;
	}*/
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
		double score = 0;
		
		// Convert to object array.
		Boolean[][] objInputs = new Boolean[inputs.length][];
    	for (int i=0; i<objInputs.length; i++) {
    		objInputs[i] = ArrayUtils.toObject(inputs[i]);
    	}
    	
		Object[] results = new Object[objInputs.length];
        for (int i=0; i<objInputs.length; i++) {
        	results[i] = evaluator.eval(program.getSourceCode(), argNames, objInputs[i]);
        }
		
        for (int i=0; i<results.length; i++) {
	    	boolean[] vars = inputs[i];
	
	        if (results[i] != null && ((Boolean) results[i] == chooseResult(vars))) {
	            score++;
	        } else if (!program.isValid()) {
	        	score = 0;
	        	break;
	        }
	    }
        
        return noInputs - score;
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
}
