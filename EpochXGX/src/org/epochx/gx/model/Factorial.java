package org.epochx.gx.model;

import org.epochx.gp.representation.*;
import org.epochx.gx.node.*;
import org.epochx.representation.*;

public class Factorial extends GXModel {

	public static final int LOWER_INPUT = 1;
	public static final int UPPER_INPUT = 12; // Max factorial that fits an int.
	public static final int NO_INPUTS = UPPER_INPUT-LOWER_INPUT;

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

	private int getFactorial(int n) {
		int total = n;
		
		for (int i=(n-1); i>0; i--) {
			total *= i;
		}
		
		return total;
	}

	@Override
	public double getFitness(CandidateProgram p) {
		final GPCandidateProgram program = (GPCandidateProgram) p;
		final Subroutine method = (Subroutine) program.getRootNode();
		
		int hitCount = 0;
		
        for (int i=LOWER_INPUT; i<=UPPER_INPUT; i++) {        	
        	x.setValue(i);
        	
        	Integer result = (Integer) method.evaluate();
        	
        	if (result == getFactorial(i)) {
        		hitCount++;
        	}
        }
        
        return (1.0 - ((double) hitCount/NO_INPUTS));
	}
	
	@Override
	public Class<?> getReturnType() {
		return Integer.class;
	}

	public static void main(String[] args) {
		Factorial f = new Factorial();
		for (int i=0; i<12; i++) {
			System.out.println(f.getFactorial(i));
		}
	}
}
