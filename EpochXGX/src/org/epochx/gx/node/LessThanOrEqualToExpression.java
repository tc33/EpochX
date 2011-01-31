package org.epochx.gx.node;


import org.epochx.tools.util.*;

public class LessThanOrEqualToExpression extends BinaryExpression {

	public LessThanOrEqualToExpression() {
		this(null, null);
	}
	
	public LessThanOrEqualToExpression(Expression child1, Expression child2) {
		super(child1, child2);
	}

	@Override
	public Boolean evaluate() {
		Object c1 = getChild(0).evaluate();
		Object c2 = getChild(1).evaluate();
		
		if (TypeUtils.isNumericType(c1.getClass()) 
				&& TypeUtils.isNumericType(c2.getClass())) {
			// Compare as doubles.
			final double d1 = NumericUtils.asDouble(c1);
			final double d2 = NumericUtils.asDouble(c2);

			return d1 <= d2;
		} else if (c1 instanceof Comparable<?> && c2 instanceof Comparable<?>) {
			Comparable<Object> a = (Comparable<Object>) c1;
			Comparable<Object> b = (Comparable<Object>) c2;
			
			return (a.compareTo(b) <= 0);
		} else {
			return null;
		}
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AndFunction which is AND.
	 */
	@Override
	public String getIdentifier() {
		return "LTET";
	}
	
	/**
	 * Two inputs can only be compared if they are both instances of Comparable,
	 * and either: both numerical class or one is a subclass of the other.
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 2 
				&& (TypeUtils.isAllNumericType(inputTypes)
				|| (TypeUtils.allSub(inputTypes, Comparable.class) 
						&& (TypeUtils.getSub(inputTypes) != null)))) {
			// Both inputs are numeric OR both are Comparable and one is assignable to the other.
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
		buffer.append(" <= ");
		buffer.append(((ASTNode) getChild(1)).toJava());
		buffer.append(')');
		
		return buffer.toString();
	}
}
