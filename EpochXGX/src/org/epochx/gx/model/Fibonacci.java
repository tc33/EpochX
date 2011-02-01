package org.epochx.gx.model;

import org.epochx.gp.representation.*;
import org.epochx.gx.node.*;
import org.epochx.gx.node.Variable;
import org.epochx.representation.*;

public class Fibonacci extends GXModel {
	
	public static final int NO_INPUTS = 20;

	private int[] sequence;
	
	private Variable n0;
	private Variable n1;
	private Variable n;
	
	public Fibonacci() {
		setSubroutineName("getNthFibonacci");
		
		// Construct parameters.
		n0 = new Variable("n0", 1);
		n1 = new Variable("n1", 1);
		n = new Variable("n", Integer.class);
		
		// Generate the correct fibonnaci sequence up to the NO_INPUTS.
		sequence = new int[NO_INPUTS];
		for (int i=0; i<NO_INPUTS; i++) {
			sequence[i] = getNthFibonnaci(i);
		}
		
		addParameter(n0);
		addParameter(n1);
		addParameter(n);
	}

	@Override
	public double getFitness(CandidateProgram p) {
		final GPCandidateProgram program = (GPCandidateProgram) p;
		final Subroutine method = (Subroutine) program.getRootNode();
		
		double score = 0;
		
        for (int i=0; i<NO_INPUTS; i++) {        	
        	n.setValue(i);
        	n0.setValue(1);
        	n1.setValue(1);
        	
        	Integer result = (Integer) method.evaluate();
        	
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
	
	@Override
	public Class<?> getReturnType() {
		return Integer.class;
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
