package org.epochx.gx.node;


import org.epochx.tools.util.*;

public class SubtractExpression extends BinaryExpression {

	public SubtractExpression() {
		this(null, null);
	}
	
	public SubtractExpression(Expression child1, Expression child2) {
		super(child1, child2);
	}

	@Override
	public Object evaluate() {
		Object c1 = getChild(0).evaluate();
		Object c2 = getChild(1).evaluate();
		
		Class<?> returnType = getReturnType();
		
		if (returnType == Double.class) {
			// Subtract as doubles.
			double d1 = NumericUtils.asDouble(c1);
			double d2 = NumericUtils.asDouble(c2);
			
			return d1 - d2;
		} else if (returnType == Long.class) {
			// Subtract as longs.
			long l1 = NumericUtils.asLong(c1);
			long l2 = NumericUtils.asLong(c2);
			
			return l1 - l2;
		} else if (returnType == Integer.class) {
			// Subtract as integers.
			int i1 = NumericUtils.asInteger(c1);
			int i2 = NumericUtils.asInteger(c2);
			
			return i1 - i2;
		}

		return null;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AndFunction which is AND.
	 */
	@Override
	public String getIdentifier() {
		return "SUB";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 2 && TypeUtils.isAllNumericType(inputTypes)) {
			return TypeUtils.getNumericType(inputTypes);
		}
		
		return null;
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
		buffer.append(" - ");
		buffer.append(((ASTNode) getChild(1)).toJava());
		buffer.append(')');
		
		return buffer.toString();
	}
}
