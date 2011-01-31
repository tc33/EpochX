package org.epochx.gx.node;


import org.epochx.tools.util.*;

public class OrExpression extends BinaryExpression {

	public OrExpression() {
		this(null, null);
	}
	
	public OrExpression(Expression child1, Expression child2) {
		super(child1, child2);
	}

	@Override
	public Boolean evaluate() {
		boolean a = (Boolean) ((Expression) getChild(0)).evaluate();
		boolean b = (Boolean) ((Expression) getChild(1)).evaluate();

		return (a || b);
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AndFunction which is AND.
	 */
	@Override
	public String getIdentifier() {
		return "AND";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 2 && TypeUtils.allEqual(inputTypes, Boolean.class)) {
			return Boolean.class;
		} else {
			return null;
		}
	}

	@Override
	public Class<? extends ASTNode>[] getChildTypes() {
		return (Class<ASTNode>[]) new Class<?>[]{Expression.class, Expression.class};
	}

	@Override
	public String toJava() {
		StringBuilder buffer = new StringBuilder();
		buffer.append('(');
		buffer.append(((ASTNode) getChild(0)).toJava());
		buffer.append(" || ");
		buffer.append(((ASTNode) getChild(1)).toJava());
		buffer.append(')');
		
		return buffer.toString();
	}
}
