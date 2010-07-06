package org.epochx.gx.representation;

import org.epochx.tools.random.*;

public interface Statement extends Cloneable {

	public Statement clone();
	
	public void apply(VariableHandler vars);
	
	public void evaluate(VariableHandler vars);

	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars);	
}
