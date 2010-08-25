package org.epochx.gx.model;

import org.epochx.gx.representation.*;
import org.epochx.representation.*;

public class Fibonacci extends GXModel {
	
	public static final int NO_INPUTS = 20;

	private int[] sequence;
	
	public Fibonacci() {
		// Construct parameters.
		Variable n0 = new Variable(DataType.INT, "n0", 1);
		Variable n1 = new Variable(DataType.INT, "n1", 1);
		Variable n = new Variable(DataType.INT, "n");
		
		getVariableHandler().setParameters(n0, n1, n);
		
		// Generate the correct fibonnaci sequence up to the NO_INPUTS.
		sequence = new int[NO_INPUTS];
		for (int i=0; i<NO_INPUTS; i++) {
			sequence[i] = getNthFibonnaci(i);
		}
	}

	@Override
	public double getFitness(CandidateProgram p) {
		final GXCandidateProgram program = (GXCandidateProgram) p;
		final Method method = program.getMethod();
		
		final VariableHandler vars = getVariableHandler();
		
		double score = 0;
		
        for (int i=0; i<NO_INPUTS; i++) {        	
        	vars.reset();
        	vars.setParameterValue("n", i);
        	vars.setParameterValue("n0", 1);
        	vars.setParameterValue("n1", 1);
        	
        	Integer result = (Integer) method.evaluate(vars);
        	
        	// Sum the errors.
            score += Math.abs(result - sequence[i]);
            
			// Give a boost to absolutely correct answers.
            if (result != sequence[i]) {
                score += 500;
            }
            
            // Test for integer overflow from the result.
            if (score < 0) {
            	score = Double.POSITIVE_INFINITY;
            	break;
            }
        }
        
        return score;
	}
	
	public static int getNthFibonnaci(int n0, int n1, int n) {
		for (int i=0; i<n; i++) {
			int nth = n0 + n1;
			n0 = n1;
			n1 = nth;
		}
		
		return n0;
	}
	
	public static int getNthFibonnaci(int n) {
		return getNthFibonnaci(1, 1, n);
	}
	
	public static int getNthFibonnaciSolution(int n, int n0, int n1) {
		int var0 = --n;
		for (int var1=0,var2=var1; (var1<var0 && var1<100); var1++,var2=var1) {
		var2 = ++n0;
		n = n1;
		n1 = n0;
		n0 = (--n + n0);
		}
		double var6 = (0.22557474780671372 % (0.7913649399170898 * 0.12563305253354873));
		return n1;
	}
	
	public static void main(String[] args) {
		for (int i=0; i<NO_INPUTS; i++) {
			int actual = getNthFibonnaci(i);
			int candidate = getNthFibonnaciSolution(i, 1, 1);
			int diff = Math.abs(actual - candidate);
			
			System.out.println(actual + " " + candidate + " " + diff);
		}
	}
}
