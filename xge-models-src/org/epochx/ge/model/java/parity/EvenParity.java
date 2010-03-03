package org.epochx.ge.model.java.parity;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.ge.model.GEAbstractModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.JavaEvaluator;
import org.epochx.tools.util.BoolUtils;


public abstract class EvenParity extends GEAbstractModel {
	
	private boolean[][] inputs;
	
	private JavaEvaluator evaluator;
	
	private int noInputs;
	
	private String[] argNames;
	
	public EvenParity(int noInputBits) {		
		evaluator = new JavaEvaluator();
		
		inputs = BoolUtils.generateBoolSequences(noInputBits);
		noInputs = (int) Math.pow(2, noInputBits);
		argNames = new String[noInputBits];
		for (int i=0; i<noInputBits; i++) {
			argNames[i] = "d" + i;
		}
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
        	Boolean result = (Boolean) evaluator.eval(program.getSourceCode(), argNames, objVars);

            if (result != null && result == chooseResult(vars)) {
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
