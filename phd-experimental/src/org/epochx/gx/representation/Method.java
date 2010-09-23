package org.epochx.gx.representation;

import org.epochx.tools.random.*;

public class Method implements Cloneable {

	private String name;
	
	private Block body;
	
	private ReturnStatement returnStatement;
	
	public Method(String name, Block body, ReturnStatement returnStatement) {
		this.name = name;
		this.body = body;
		this.returnStatement = returnStatement;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the body
	 */
	public Block getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(Block body) {
		this.body = body;
	}

	/**
	 * @return the returnStatement
	 */
	public ReturnStatement getReturnStatement() {
		return returnStatement;
	}

	/**
	 * @param returnStatement the returnStatement to set
	 */
	public void setReturnStatement(ReturnStatement returnStatement) {
		this.returnStatement = returnStatement;
	}

	public void modifyExpression(double probability, VariableHandler vars, RandomNumberGenerator rng) {
		body.modifyExpression(probability, rng, vars);
	}
		
	public Object evaluate(VariableHandler vars) {
		// Evaluate the method body.
		body.evaluate(vars);
	
		// Evaluate the return statement with return value.
		return returnStatement.evaluateReturn(vars);
	}

	public Statement deleteStatement(int deletePosition) {
		return body.deleteStatement(deletePosition);
	}

	public void insertStatement(double probability, VariableHandler vars, RandomNumberGenerator rng, int maxNoStatements) {
		body.insertStatement(probability, rng, vars, maxNoStatements);
	}
	
	/**
	 * Returns the declaration statement for the given variable if one exists
	 * else returns null.
	 * @param v
	 * @return
	 */
	public Declaration getDeclaration(Variable v) {
		return body.getDeclaration(v);
	}
	
	@Override
	public Object clone() {
		Method clone = null;
		try {
			clone = (Method) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.name = this.name;
		clone.body = this.body.clone();
		clone.returnStatement = this.returnStatement.clone();
		
		return clone;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		DataType returnType = returnStatement.getReturnType();
		
		buffer.append("public static ");
		buffer.append(returnType.toString().toLowerCase());
		buffer.append(' ');
		buffer.append(name);
		buffer.append("()");
		
		body.addStatement(returnStatement);
		buffer.append(body.toString());
		body.removeStatement(returnStatement);
		
		return buffer.toString();
	}
}
