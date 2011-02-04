package org.epochx.gx.model;

import org.epochx.gp.representation.*;
import org.epochx.gx.node.*;
import org.epochx.representation.*;

public class OverSelectionProportion extends GXModel {

	private Variable popSize;
	private Variable delimiter;
	private Variable proportion;
	
	private Integer[] popSizeValues;
	private Integer[] delimiterValues;
	private Double[] proportionValues;
	
	private double[] results;
	
	public OverSelectionProportion() {
		setSubroutineName("getOverSelectionProportion");
		
		// Construct parameters.
		popSize = new Variable("popSize", Integer.class);
		delimiter = new Variable("delimiter", Integer.class);
		proportion = new Variable("proportion", Double.class);
		
		popSizeValues = new Integer[20];
		
		//popSizeValues = new Integer[]{1, 999, 1000, 2000, 3000, 4000, 8000};
		//delimiterValues = new Integer[]{1000, 1000, 1000, 1000, 1000, 1000, 1000};
		//proportionValues = new Double[]{0.32, 0.32, 0.32, 0.32, 0.32, 0.32, 0.32};
		
		results = new double[popSizeValues.length];
		for (int i=0; i<popSizeValues.length; i++) {
			popSizeValues[i] = getRNG().nextInt(8000) + 1;
			results[i] = getOverSelectionProportion(popSizeValues[i], 1000, 0.32);
		}
		
		addParameter(popSize);
		//addParameter(delimiter);
		addParameter(proportion);
		//addParameter(new Variable("result", 0.0));
	}
	
	@Override
	public Class<?> getReturnType() {
		return Double.class;
	}

	@Override
	public double getFitness(CandidateProgram program) {
		GPCandidateProgram p = (GPCandidateProgram) program;
		
		int score = 0;
		
		for (int i=0; i<popSizeValues.length; i++) {
			popSize.setValue(popSizeValues[i]);
			delimiter.setValue(1000);
			proportion.setValue(0.32);
			
			Double result = (Double) p.evaluate();
			
        	// Sum the errors.
            score += Math.abs(result - results[i]);
            
			// Give a boost to absolutely correct answers.
            if (result != results[i]) {
                score += 500;
            }
		}
		
		return score;
	}

	public static double getOverSelectionProportion(int popSize, int delimiter, double proportion) {
		if (popSize < delimiter) {
			return 1.0;
		} else {
			proportion = proportion / (popSize / delimiter);
		}
		
		return proportion;
	}
	
	public static void main(String[] args) {
		Integer[] popSizeValues = new Integer[]{1, 999, 1000, 2000, 3000, 4000, 8000};
		Integer[] delimiterValues = new Integer[]{1000, 1000, 1000, 1000, 1000, 1000, 1000};
		Double[] proportionValues = new Double[]{0.32, 0.32, 0.32, 0.32, 0.32, 0.32, 0.32};
		
		for (int i=0; i<7; i++) {
			System.out.println(getOverSelectionProportion(popSizeValues[i], delimiterValues[i], proportionValues[i]));
		}
	}
}
