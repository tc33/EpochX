package org.epochx.gx.representation;

import java.util.*;

import org.epochx.tools.random.*;

public interface Statement extends Cloneable {

	/**
	 * Provides a shortcut for determining variable scope without performing 
	 * evaluation. Variable values are not handled, only variable declarations.
	 */
	public void apply(VariableHandler vars);
	
	/**
	 * Evaluate this statement, updating variables in the VariableHandler as 
	 * necessary.
	 */
	public void evaluate(VariableHandler vars);

	/**
	 * Consider each expression within this statement for modification and 
	 * modify it with probability chance.
	 */
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars);
	
	/**
	 * Should return the number of statements that make up this statement. For 
	 * most statement types this will just be 1, but for statements such as if 
	 * statements and loops, they may contain multiple nested statements, in 
	 * which case the result will be 1 plus the number of statements nested 
	 * internally.
	 */
	public int getNoStatements();

	/**
	 * Returns the statement at the given index. A non-block statement counts 
	 * for 1 and block statements count for 1 plus another 1 for each internal
	 * statement.
	 */
	public Statement getStatement(int index);
	
	/**
	 * Returns a list of those variables that get declared in this statement or
	 * any nested statements.
	 */
	public Set<Variable> getDeclaredVariables();
	
	/**
	 * Returns a list of those variables that are used in expressions within 
	 * this statement or any nested statements. It does not include those 
	 * variables that are declared but unused in expressions.
	 */
	public Set<Variable> getUsedVariables();
	
	public Statement clone();
	
	/**
	 * If this statement contains any variable uses or declarations then they 
	 * should be replaced by the copy that is found in the given map. If no 
	 * entry exists for that variable then a new copy should be made.
	 */
	public void copyVariables(Map<Variable, Variable> variableCopies);
	
	/**
	 * Returns the nesting depth of this statement. This will be 0 for a 
	 * statement at the top level, and 1 for any statements within a block of an
	 * if statement or loop.
	 */
	public int getDepth();
}
