package org.epochx.gx.model;

import org.epochx.gp.representation.*;
import org.epochx.gx.node.*;
import org.epochx.representation.*;

public class Factorial extends GXModel {

	public static final int LOWER_INPUT = 1;
	public static final int UPPER_INPUT = 10; // Max factorial that fits an int.
	public static final int NO_INPUTS = UPPER_INPUT-LOWER_INPUT+1; // +1 because UPPER_INPUT is inclusive

	private int[] sequence;
	
	private Variable x;
	
	public Factorial() {
		setSubroutineName("getFactorial");
		
		// Construct parameters.
		x = new Variable("x", Integer.class);
		
		// Generate the correct fibonnaci sequence up to the NO_INPUTS.
		sequence = new int[NO_INPUTS];
		for (int i=0; i<NO_INPUTS; i++) {
			sequence[i] = getFactorial(i);
		}
		
		addParameter(x);
	}

//	private int getFactorial(int n) {
//		int total = n;
//		
//		for (int i=(n-1); i>0; i--) {
//			total *= i;
//		}
//		
//		return total;
//	}
	
	private int getFactorial(int n) {
		int total = n;
		
		for (int i=0; i<n-1; i++) {
			total = total * (i + 1);
		}
		
		return total;
	}
	
	public static int series(int a, int b) {
		int s = 0;
		if (a > b) {
			s = b;
		} else {
			s = a;
			for (int i = 1; i <= s; i++) {
				if ((a%i == 0) && (b%i == 0)) {
					return i;
				}
			}
		}
		return -1;
	}

//	@Override
//	public double getFitness(CandidateProgram p) {
//		final GPCandidateProgram program = (GPCandidateProgram) p;
//		final Subroutine method = (Subroutine) program.getRootNode();
//		
//		int hitCount = 0;
//		
//        for (int i=LOWER_INPUT; i<=UPPER_INPUT; i++) {        	
//        	x.setValue(i);
//        	
//        	Integer result = (Integer) method.evaluate();
//        	
//        	if (result == getFactorial(i)) {
//        		hitCount++;
//        	}
//        }
//        
//        return (1.0 - ((double) hitCount/NO_INPUTS));
//	}
	
	@Override
	public double getFitness(CandidateProgram p) {
		GPCandidateProgram program = (GPCandidateProgram) p;
		final Subroutine method = (Subroutine) program.getRootNode();
		
		double score = 0.0;
		
		for (int i=LOWER_INPUT; i<=UPPER_INPUT; i++) {
	    	x.setValue(i);
	    	
	    	int expected = sequence[i-LOWER_INPUT];//getFactorial(i);
	    	Integer result = (Integer) method.evaluate();
	    	
	    	// Sum the errors.
	        score += Math.abs(result - expected);
	        
			// Give a boost to absolutely correct answers.
	        if (result != expected) {
	            score += 2000;
	        }
	        
	        // Test for integer overflow from the result.
	        if (score < 0) {
	        	score = Double.POSITIVE_INFINITY;
	        	break;
	        }
	    }
	    
	    return score;
	}
	
	@Override
	public Class<?> getReturnType() {
		return Integer.class;
	}

}
