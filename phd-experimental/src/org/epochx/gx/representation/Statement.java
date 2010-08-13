package org.epochx.gx.representation;

import org.epochx.tools.random.*;

public interface Statement extends Cloneable {

	public Statement clone();
	
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
}
