package org.epochx.gx.representation;

import org.epochx.tools.random.*;

public class TimesLoop implements Statement {

	private final static int MAX_ITERATIONS = 100;
	
	private Declaration indexCoverVarDecl;
	private Declaration indexVarDecl;
	private Declaration endVarDecl;
	private Block body;
	
	public TimesLoop(Block body, Declaration indexVar, Declaration indexCoverVar, Declaration endVar) {
		this.body = body;
		this.indexCoverVarDecl = indexCoverVar;
		this.indexVarDecl = indexVar;
		this.endVarDecl = endVar;
	}
	
	@Override
	public void apply(VariableHandler vars) {
		// Variable scope will hide any new variables here so do nothing.
	}

	@Override
	public void evaluate(VariableHandler vars) {
		endVarDecl.evaluate(vars);
		Variable endVar = endVarDecl.getVariable();
		vars.removeActiveVariable(endVar);

		indexVarDecl.evaluate(vars);
		indexCoverVarDecl.evaluate(vars);
		Variable indexVar = indexVarDecl.getVariable();
		Variable indexCoverVar = indexCoverVarDecl.getVariable();
		vars.removeActiveVariable(indexVar);
		
		int n = (Integer) endVar.getValue();
		for (int i=0; (i<n && i<MAX_ITERATIONS); i++) {
			// Record the number of variables to remove all new declarations.
			int noVars = vars.getNoActiveVariables();
			if (indexCoverVarDecl.getVariable().getValue() == null) {
				System.out.println("+++++++++++++++NULL");
			}
			
			body.evaluate(vars);
			
			// Remove all new declarations.
			vars.setNoActiveVariables(noVars);
			
			int indexValue = (Integer) indexVar.getValue();
			indexVar.setValue(indexValue+1);
			indexCoverVar.setValue(indexVar.getValue());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		// End var declaration.
		buffer.append(endVarDecl.toString());
		buffer.append('\n');
		
		buffer.append("for (");
		buffer.append(indexVarDecl.toString());
		buffer.append(" (");
		buffer.append(indexVarDecl.getVariable().getVariableName());
		buffer.append('<');
		buffer.append(endVarDecl.getVariable().getVariableName());
		buffer.append(" && ");
		buffer.append(indexVarDecl.getVariable().getVariableName());
		buffer.append('<');
		buffer.append(MAX_ITERATIONS);
		buffer.append("); ");
		buffer.append(indexVarDecl.getVariable().getVariableName());
		buffer.append("++,");
		buffer.append(indexCoverVarDecl.getVariable().getVariableName());
		buffer.append('=');
		buffer.append(indexVarDecl.getVariable().getVariableName());
		buffer.append(")");
		buffer.append(body.toString());

		return buffer.toString();
	}
	
	@Override
	public TimesLoop clone() {
		TimesLoop clone = null;
		try {
			clone = (TimesLoop) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.indexVarDecl = this.indexVarDecl.clone();
		clone.endVarDecl = this.endVarDecl.clone();
		clone.body = this.body.clone();
			
		return clone;
	}

	@Override
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars) {
		endVarDecl.modifyExpression(probability, rng, vars);
		body.modifyExpression(probability, rng, vars);
	}
}
