package org.epochx.gx.representation;

public class UnaryOperator {

	private String operator;
	
	// Whether the operator should come before the expression (otherwise after).
	private boolean before;
	
	public UnaryOperator(String operator, boolean before) {
		this.operator = operator;
		this.before = before;
	}
	
	public boolean showBeforeExpression() {
		return before;
	}
	
	@Override
	public String toString() {
		return operator;
	}
}
