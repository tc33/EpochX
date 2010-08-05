package org.epochx.gx.model;

import java.util.*;

import org.epochx.gx.op.init.*;
import org.epochx.gx.representation.*;
import org.epochx.representation.*;

public class Fibonnaci extends GXModel {

	public Fibonnaci() {
		// Construct parameters.
		Variable n0 = new Variable(DataType.INT, "n0", 0);
		Variable n1 = new Variable(DataType.INT, "n1", 1);
		Variable n = new Variable(DataType.INT, "n");
		
		getVariableHandler().setParameters(n0, n1, n);
	}
	
	//private int fits = 0;
	
	@Override
	public double getFitness(CandidateProgram p) {
		//System.out.print(fits++ + " ");
		//long start = System.currentTimeMillis();
		System.out.println("-------------------------");
		System.out.println(ProgramGenerator.format(p.toString()));
		
		final GXCandidateProgram program = (GXCandidateProgram) p;
		final AST ast = program.getAST();
		
		final VariableHandler vars = getVariableHandler();
		
		double score = 0;
		
        for (int i=2; i<10; i++) {        	
        	//System.out.print(i + " ");
        	vars.reset();
        	vars.setParameterValue("n", i);
        	
        	Integer result = (Integer) ast.evaluate(vars);

			// Increment score for a correct response.
            if ((result != null) && (result == getNthFibonnaci(i))) {
                score++;
            }
        }
        
        /*long time = System.currentTimeMillis() - start;
        System.out.println(time);
        
        if (time > 10000) {
        	System.out.println(ProgramGenerator.format(p.toString()));
        	getFitness(p);
        }*/
        
        return 8 - score;
	}
	
	public static int getNthFibonnaci(int n) {
		int n0 = 1;
		int n1 = 1;
		for (int i=0; i<n; i++) {
			int nth = n0 + n1;
			n0 = n1;
			n1 = nth;
		}
		
		return n0;
	}
	
	public static void main(String[] args) {
		System.out.println(Fibonnaci.getNthFibonnaci(0));
		System.out.println(Fibonnaci.getNthFibonnaci(1));
		System.out.println(Fibonnaci.getNthFibonnaci(2));
		System.out.println(Fibonnaci.getNthFibonnaci(3));
		System.out.println(Fibonnaci.getNthFibonnaci(4));
		System.out.println(Fibonnaci.getNthFibonnaci(5));
		System.out.println(Fibonnaci.getNthFibonnaci(6));
	}
}
