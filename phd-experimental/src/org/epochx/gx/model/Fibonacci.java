package org.epochx.gx.model;

import java.util.*;

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
		final AST ast = program.getAST();
		
		final VariableHandler vars = getVariableHandler();
		
		double score = 0;
		
        for (int i=0; i<NO_INPUTS; i++) {        	
        	vars.reset();
        	vars.setParameterValue("n", i);
        	vars.setParameterValue("n0", 1);
        	vars.setParameterValue("n1", 1);
        	
        	Integer result = (Integer) ast.evaluate(vars);
        	
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
		int var4 = --n;
		for (int var5=0,var6=var5; (var5<var4 && var5<100); var5++,var6=var5) {
		n1 = (n1 + (1066889581 % n1));
		int var26 = (n % (n + (++n1 % -1429604798)));
		for (int var27=0,var28=var27; (var27<var26 && var27<100); var27++,var28=var27) {
		int var29 = ++n;
		}
		double var40 = (0.5389215444346025 % 0.4381355783205967);
		}
		int var36 = (((38269433 - (n1 * n)) * (((220493603 - (((--n - n1) + 168395372) % n)) % 1046684714) % --n1)) / ++n0);
		for (int var37=0,var38=var37; (var37<var36 && var37<100); var37++,var38=var37) {
		boolean var41 = (true || true);
		var38 = ((n + --n) - --n1);
		}
		int var14 = ((n % n0) % ++n1);
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
