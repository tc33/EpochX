package org.epochx.gx.model;

import java.util.*;

import org.epochx.epox.Node;
import org.epochx.gp.model.*;
import org.epochx.gx.node.*;
import org.epochx.gx.op.init.*;
import org.epochx.gx.op.mutation.*;

public abstract class GXModel extends GPModel {

	private int minNoStatements;
	private int maxNoStatements;
	private int maxLoopDepth;
	private int maxExpressionDepth;
	
	private List<Variable> parameters;
	private Set<Literal> literals;
	
	public GXModel() {
		maxLoopDepth = 1;
		maxNoStatements = 10;
		minNoStatements = 1;
		maxExpressionDepth = 5;
		
		setSyntax(new ArrayList<Node>());
		
		literals = new HashSet<Literal>();
		literals.add(new BooleanERC(getRNG()));
		literals.add(new DoubleERC(getRNG(), 0.0, 1.0, 3));
		literals.add(new IntegerERC(getRNG(), 0, 10));
		
		setParameters(new ArrayList<Variable>());
		
		// Default operators.
		setInitialiser(new ImperativeInitialiser(this));
		setCrossover(null);
		setMutation(new ImperativeMutation(this, 0.0, 1.0, 0.0, 0.2));
	}
	
	@Override
	protected boolean isModelRunnable() {
		boolean valid = true;
		
		if (minNoStatements < 0) {
			valid = false;
		} else if (maxNoStatements < minNoStatements) {
			valid = false;
		} else if (maxLoopDepth < 0) {
			valid = false;
		} else if (maxExpressionDepth < 0) {
			valid = false;
		} else if (literals.isEmpty() && parameters.isEmpty()) {
			// Means there are no terminals available.
			valid = false;
		}
		
		return valid;
	}
	
	/**
	 * @return the maxExpressionDepth
	 */
	public int getMaxExpressionDepth() {
		return maxExpressionDepth;
	}

	/**
	 * @param maxExpressionDepth the maxExpressionDepth to set
	 */
	public void setMaxExpressionDepth(int maxExpressionDepth) {
		this.maxExpressionDepth = maxExpressionDepth;
	}

	/**
	 * @return the minNoStatements
	 */
	public int getMinNoStatements() {
		return minNoStatements;
	}
	/**
	 * @param minNoStatements the minNoStatements to set
	 */
	public void setMinNoStatements(int minNoStatements) {
		this.minNoStatements = minNoStatements;
	}
	/**
	 * @return the maxNoStatements
	 */
	public int getMaxNoStatements() {
		return maxNoStatements;
	}
	/**
	 * @param maxNoStatements the maxNoStatements to set
	 */
	public void setMaxNoStatements(int maxNoStatements) {
		this.maxNoStatements = maxNoStatements;
	}
	/**
	 * @param maxLoopDepth the maxLoopDepth to set
	 */
	public void setMaxLoopDepth(int maxLoopDepth) {
		this.maxLoopDepth = maxLoopDepth;
	}
	/**
	 * @return the maxLoopDepth
	 */
	public int getMaxLoopDepth() {
		return maxLoopDepth;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(List<Variable> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the parameters
	 */
	public List<Variable> getParameters() {
		return parameters;
	}
	
	public void addParameter(Variable parameter) {
		parameters.add(parameter);
	}
	
	public void removeParameter(Variable parameter) {
		parameters.remove(parameter);
	}
	
	public Set<Literal> getLiterals() {
		return literals;
	}
	
	public void setLiterals(Set<Literal> literals) {
		this.literals = literals;
	}
}
