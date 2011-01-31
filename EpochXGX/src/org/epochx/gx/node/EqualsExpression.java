package org.epochx.gx.node;


import org.epochx.tools.util.*;

public class EqualsExpression extends BinaryExpression {

	public EqualsExpression() {
		this(null, null);
	}
	
	public EqualsExpression(Expression child1, Expression child2) {
		super(child1, child2);
	}

	@Override
	public Boolean evaluate() {
		Comparable<Object> a = (Comparable<Object>) ((Expression) getChild(0)).evaluate();
		Comparable<Object> b = (Comparable<Object>) ((Expression) getChild(1)).evaluate();

		return (a.compareTo(b) == 0);
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AndFunction which is AND.
	 */
	@Override
	public String getIdentifier() {
		return "EQ";
	}
	
	/**
	 * Two inputs can only be compared if they are both instances of Comparable,
	 * and either: both numerical class or one is a subclass of the other.
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		//TODO Technically this could be considered differently to the other 
		// comparison operators, because it can be used to compare object references.
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
		buffer.append(" == ");
		buffer.append(((ASTNode) getChild(1)).toJava());
		buffer.append(')');
		
		return buffer.toString();
	}
}
