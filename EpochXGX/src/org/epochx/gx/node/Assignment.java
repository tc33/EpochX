package org.epochx.gx.node;



public class Assignment extends Statement {

	public Assignment() {
		this(null, null);
	}

	public Assignment(final Variable child1, final Expression child2) {
		super(child1, child2);
	}

	@Override
	public Void evaluate() {
		Variable v = (Variable) getChild(0);
		Expression e = (Expression) getChild(1);

		v.setValue(e.evaluate());
		
		return null;
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AndFunction which is AND.
	 */
	@Override
	public String getIdentifier() {
		return "ASSIGN";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 2 
				&& inputTypes[0].isAssignableFrom(inputTypes[1])
				&& (inputTypes[0] != Void.class)) {
			return Void.class;
		} else {
			return null;
		}
	}

	@Override
	public Class<? extends ASTNode>[] getChildTypes() {
		return (Class<ASTNode>[]) new Class<?>[]{Variable.class, Expression.class};
	}
	
	@Override
	public String toJava() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(((ASTNode) getChild(0)).toJava());
		buffer.append(" = ");
		buffer.append(((ASTNode) getChild(1)).toJava());
		buffer.append(';');
		
		return buffer.toString();
	}

}
