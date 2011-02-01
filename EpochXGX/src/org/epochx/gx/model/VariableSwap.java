package org.epochx.gx.model;

import org.epochx.gp.representation.*;
import org.epochx.gx.node.*;
import org.epochx.representation.*;

public class VariableSwap extends GXModel {

	private Variable var1;
	private Variable var2;
	
	private Object[] var1Values;
	private Object[] var2Values;
	
	public VariableSwap() {
		setSubroutineName("swapVars");
		
		// Construct parameters.
		var1 = new Variable("var1", Object.class);
		var2 = new Variable("var2", Object.class);
		
		var1Values = new Object[]{4, true, 2.1, null, "rabbit"};
		var2Values = new Object[]{2, false, 3.2, new Object(), "carrot"};
		
		addParameter(var1);
		addParameter(var2);
	}
	
	@Override
	public Class<?> getReturnType() {
		return Void.class;
	}

	@Override
	public double getFitness(CandidateProgram program) {
		GPCandidateProgram p = (GPCandidateProgram) program;
		
		double score = (var1Values.length * 2);
		
		for (int i=0; i<var1Values.length; i++) {
			var1.setValue(var1Values[i]);
			var2.setValue(var2Values[i]);
			
			p.evaluate();
			
			if (var1.getValue() == var2Values[i]) {
				score--;
			}
			if (var2.getValue() == var1Values[i]) {
				score--;
			}
		}
		
		return score;
	}

}
