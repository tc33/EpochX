package org.epochx.gx.representation;

import java.util.*;

import org.epochx.tools.random.*;

public interface Expression extends Cloneable {

	public DataType getDataType();
	
	public Expression clone();
	
	public void modifyExpression(double probability, RandomNumberGenerator rng, VariableHandler vars, int level);
	
	public Object evaluate(VariableHandler vars);
	
	public Set<Variable> getUsedVariables();
	
}
