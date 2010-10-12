package org.epochx.gx.representation;

import java.util.*;

import org.epochx.gx.op.init.*;
import org.epochx.gx.tools.util.*;
import org.epochx.tools.random.*;


public class Declaration implements Statement {

	private DataType type;
	
	private Variable variable;
	
	private Expression expression;
	
	/**
	 * @return the type
	 */
	public DataType getType() {
		return type;
	}

	/**
	 * @return the variable
	 */
	public Variable getVariable() {
		return variable;
	}

	/**
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}

	public Declaration(DataType type, Variable variable, Expression expression) {
		this.type = type;
		this.variable = variable;
		this.expression = expression;
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
		double rand = rng.nextDouble();
		
		if (rand < probability) {
			expression = ProgramGenerator.getExpression(rng, vars, expression.getDataType(), 0);
		} else {
			expression.modifyExpression(probability, rng, vars, 0);
		}
	}

	@Override
	public void evaluate(VariableHandler vars) {
		Object value = expression.evaluate(vars);
		
		// Update variable value.
		variable.setValue(value);
		vars.add(variable);
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
	public Set<Variable> getDeclaredVariables() {
		Set<Variable> variables = new HashSet<Variable>();
		variables.add(variable);
		
		return variables;
	}

	@Override
	public Set<Variable> getUsedVariables() {
		return expression.getUsedVariables();
	}
	
	@Override
	public void copyVariables(VariableHandler vars, Map<Variable, Variable> variableCopies) {
		if (!variableCopies.containsKey(variable)) {
			variableCopies.put(variable, variable.copy());
		}
		variable = variableCopies.get(variable);
		
		expression = VariableUtils.copyVariable(expression, variableCopies, vars);
	}
	

	@Override
	public int getDepth() {
		return 0;
	}
}
