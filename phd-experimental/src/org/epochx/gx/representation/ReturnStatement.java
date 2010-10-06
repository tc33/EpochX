package org.epochx.gx.representation;

import java.util.*;

import org.epochx.tools.random.*;

public class ReturnStatement implements Statement {

	private Variable expression;
	
	private DataType returnType;
	
	/**
	 * Currently expression should just be a variable.
	 * 
	 * @param expression
	 */
	public ReturnStatement(DataType returnType, Variable expression) {
		this.setReturnType(returnType);
		this.expression = expression;
	}
	
	public Object evaluateReturn(VariableHandler vars) {
		return expression.evaluate(vars);
	}
	
	public void evaluate(VariableHandler vars) {
		expression.evaluate(vars);
	}
	
	public void modifyExpression(double probability, RandomNumberGenerator rng,
			VariableHandler vars) {

	}
	
	/**
	 * @param returnType the returnType to set
	 */
	public void setReturnType(DataType returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the returnType
	 */
	public DataType getReturnType() {
		return returnType;
	}
	
	@Override
	public String toString() {
		return "return " + expression.toString() + ';';
	}
	
	@Override
	public ReturnStatement clone() {
		ReturnStatement clone = null;
		try {
			clone = (ReturnStatement) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.expression = this.expression.clone();
		clone.returnType = this.returnType;
			
		return clone;
	}

	@Override
	public void apply(VariableHandler vars) {
		// Not relevant here.
	}

	@Override
	public Statement deleteStatement(int deletePosition) {
		// No internal statements so not used here.
		return null;
	}

	@Override
	public int getNoStatements() {
		return 1;
	}

	@Override
	public Statement getStatement(int index) {
		if (index == 0) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public boolean hasBlock() {
		return false;
	}

	@Override
	public void insertStatement(double probability, RandomNumberGenerator rng,
			VariableHandler vars, int maxNoStatements) {
		// No internal statements so do nothing.
	}

	@Override
	public Set<Variable> getDeclaredVariables() {
		return new HashSet<Variable>();
	}

	@Override
	public Set<Variable> getUsedVariables() {
		return expression.getUsedVariables();
	}

	@Override
	public Declaration getDeclaration(Variable v) {
		// No internal statements so not used here.
		return null;
	}
	
	public int getNoInsertPoints() {
		return 0;
	}
	
	public void insertStatements(int insertPoint, List<Statement> swapStatements) {
		// No internal statements so not used here.
	}
}
