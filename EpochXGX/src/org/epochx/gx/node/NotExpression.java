package org.epochx.gx.node;



public class NotExpression extends UnaryExpression {
	
	public NotExpression() {
		this(null);
	}
	
	public NotExpression(Expression child) {
		super(child);
	}

	@Override
	public Boolean evaluate() {
		boolean b = (Boolean) getChild(0).evaluate();

		return !b;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AndFunction which is AND.
	 */
	@Override
	public String getIdentifier() {
		return "NOT";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 1 && inputTypes[0] == Boolean.class) {
			return Boolean.class;
		}
		
		return null;
	}

	@Override
	public Class<? extends ASTNode>[] getChildTypes() {
		return (Class<ASTNode>[]) new Class<?>[]{Expression.class};
	}
	
	@Override
	public String toJava() {
		StringBuilder buffer = new StringBuilder();
		buffer.append('!');
		buffer.append(((ASTNode) getChild(0)).toJava());
		
		return buffer.toString();
	}
}
