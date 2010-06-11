package org.epochx.gx.representation;

import java.util.*;

public interface Statement extends Cloneable {

	public Statement clone();
	
	public void apply(Stack<Variable> variables);

	public void modifyExpression(double probability);	
}
