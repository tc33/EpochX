package org.epochx.gr.model.epox.parity;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.gr.model.*;
import org.epochx.gr.representation.*;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.EpoxEvaluator;
import org.epochx.tools.util.BoolUtils;


public abstract class EvenParity extends GRAbstractModel {
	
	private boolean[][] inputs;
	
	private EpoxEvaluator evaluator;
	
	private int noInputs;
	
	private String[] argNames;
	
	public EvenParity(int noInputBits) {		
		evaluator = new EpoxEvaluator();
		
		inputs = BoolUtils.generateBoolSequences(noInputBits);
		noInputs = (int) Math.pow(2, noInputBits);
		argNames = new String[noInputBits];
		for (int i=0; i<noInputBits; i++) {
			argNames[i] = "d" + i;
		}
	}

	@Override
	public double getFitness(CandidateProgram p) {
		GRCandidateProgram program = (GRCandidateProgram) p;
		
		double score = 0;
		
        // Execute on all possible inputs.
        for (int i=0; i<inputs.length; i++) {
        	boolean[] vars = inputs[i];
        	// Convert to object array.
        	Boolean[] objVars = ArrayUtils.toObject(vars);
        	Boolean result = (Boolean) evaluator.eval(program.getSourceCode(), argNames, objVars);

            if (result != null && result == chooseResult(vars)) {
                score++;
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
