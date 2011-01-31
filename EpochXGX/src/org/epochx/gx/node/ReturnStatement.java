package org.epochx.gx.node;


public class ReturnStatement extends Statement {
	
	public ReturnStatement() {
		this(null);
	}

	public ReturnStatement(final Expression child) {
		super(child);
	}

	@Override
	public Object evaluate() {
		Expression exp = (Expression) getChild(0);

		return exp.evaluate();
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AndFunction which is AND.
	 */
	@Override
	public String getIdentifier() {
		return "RETURN";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 1) {
			return inputTypes[0];
		} else {
			return null;
		}
	}

	@Override
	public Class<? extends ASTNode>[] getChildTypes() {
		return (Class<ASTNode>[]) new Class<?>[]{Expression.class};
	}
	
	@Override
	public String toJava() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("return ");
		buffer.append(((ASTNode) getChild(0)).toJava());
		buffer.append(';');
		
		return buffer.toString();
	}
}
