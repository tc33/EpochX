package org.epochx.gx.representation;

import java.util.*;

import org.epochx.tools.random.*;

public interface Statement extends Cloneable {

	public Statement clone();
	
	/**
	 * The upToStatement number refers to this statement and its nested 
	 * statements only, so the maximum value for upToStatement is the result of 
	 * getNoStatements - 1.
	 */
	public void apply(VariableHandler vars);
	
	public void evaluate(VariableHandler vars);

	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars);
	
	/**
	 * Should return the number of statements that make up this statement. For 
	 * most statement types this will just be 1, but for statements such as if 
	 * statements and loops, they may contain multiple nested statements, in 
	 * which case the result will be 1 plus the number of statements nested 
	 * internally.
	 * @return
	 */
	public int getNoStatements();

	public void insertStatement(double probability, RandomNumberGenerator rng, VariableHandler vars, int maxNoStatements);

	public Statement deleteStatement(int deletePosition);
	
	/**
	 * @return true if the statement can have nested statements, false otherwise.
	 */
	public boolean hasBlock();

	public Statement getStatement(int index);
	
	/**
	 * Returns a list of those variables that get declared in this statement or
	 * any nested statements.
	 * 
	 * @return
	 */
	public Set<Variable> getDeclaredVariables();
	
	/**
	 * Returns a list of those variables that are used in expressions within 
	 * this statement or any nested statements. It does not include those 
	 * variables that are declared but unused in expressions.
	 * 
	 * @return
	 */
	public Set<Variable> getUsedVariables();
	
	public Declaration getDeclaration(Variable v);
	
	public int getNoInsertPoints();

	public void insertStatements(int i, List<Statement> swapStatements);
}
