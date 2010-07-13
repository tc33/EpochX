package org.epochx.gx.representation;

import org.epochx.gx.op.init.*;
import org.epochx.tools.random.*;


public class Declaration implements Statement {

	private DataType type;
	
	private Variable variable;
	
	private Expression expression;
	
	public Declaration(DataType type, Variable variable, Expression expression) {
		this.type = type;
		this.variable = variable;
		this.expression = expression;
	}
	
	public static Declaration getDeclaration(RandomNumberGenerator rng, VariableHandler vars) {
		String varName = vars.getNewVariableName();
		
		DataType type = ProgramGenerator.getDataType(rng);
		Variable variable = new Variable(type, varName);
		Expression expression = ProgramGenerator.getExpression(rng, vars, type);
		
		vars.add(variable);
		
		Declaration decl = new Declaration(type, variable, expression);
		
		return decl;
	}
	
	@Override
	public void apply(VariableHandler vars) {
		vars.add(variable);
	}
	
	@Override
	public String toString() {
		return type.toString().toLowerCase() + ' ' + variable.getVariableName() + " = " + expression.toString() + ';';
	}
	
	@Override
	public Declaration clone() {
		Declaration clone = null;
		try {
			clone = (Declaration) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.type = this.type;
		clone.variable = this.variable.clone();
		clone.expression = this.expression.clone();
		
		return clone;
	}

	@Override
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars) {
		//TODO Should use model's RNG.
		double rand = Math.random();
		
		if (rand < probability) {
			expression = ProgramGenerator.getExpression(rng, vars, expression.getDataType());
		} else {
			expression.modifyExpression(probability, rng, vars);
		}
	}

	@Override
	public void evaluate(VariableHandler vars) {
		Object value = expression.evaluate(vars);
		
		// Update variable value.
		variable.setValue(value);
		vars.add(variable);
	}
	
}
