package org.epochx.ge.model.ruby.mux;

import org.apache.commons.lang.ArrayUtils;
import org.epochx.ge.core.GEAbstractModel;
import org.epochx.ge.representation.GECandidateProgram;
import org.epochx.representation.CandidateProgram;
import org.epochx.tools.eval.RubyEvaluator;
import org.epochx.tools.util.BoolUtils;


public abstract class Multiplexer extends GEAbstractModel {

	private boolean[][] inputs;
	
	private int noInputs;
	
	private RubyEvaluator evaluator;
	
	private String[] argNames;
	
	public Multiplexer(int noInputBits, int noAddressBits) {
		evaluator = new RubyEvaluator();
		
		inputs = BoolUtils.generateBoolSequences(noInputBits);
		noInputs = (int) Math.pow(2, noInputBits);
		argNames = new String[noInputBits];
		for (int i=0; i<noAddressBits; i++) {
			argNames[i] = "a" + i;
		}
		for (int i=0; i<(noInputBits-noAddressBits); i++) {
			argNames[i+noAddressBits] = "d" + i;
		}
	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GECandidateProgram program = (GECandidateProgram) p;
		
        double score = 0;

		// Convert to object array.
		Boolean[][] objInputs = new Boolean[inputs.length][];
    	for (int i=0; i<objInputs.length; i++) {
    		objInputs[i] = ArrayUtils.toObject(inputs[i]);
    	}
    	
		Object[] results = evaluator.eval(program.getSourceCode(), argNames, objInputs);
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

	protected abstract boolean chooseResult(boolean[] inputs);

}
