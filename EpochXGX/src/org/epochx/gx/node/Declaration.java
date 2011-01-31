package org.epochx.gx.node;


public class Declaration extends Statement {

	public Declaration() {
		this(null, null);
	}

	public Declaration(final Variable child1, final Expression child2) {
		super(child1, child2);
	}

	@Override
	public Void evaluate() {
		//TODO Needs to add the variable to list of in-scope variables.
		Variable v = (Variable) getChild(0);
		Expression e = (Expression) getChild(1);

		v.setValue(e.evaluate());
		
		return null;
	}
	
	public Variable getVariable() {
		return (Variable) getChild(0);
	}

	/**
	 * Get the unique name that identifies this function.
	 * 
	 * @return the unique name for the AndFunction which is AND.
	 */
	@Override
	public String getIdentifier() {
		return "DECL";
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
		buffer.append(getChild(0).getReturnType().getSimpleName());
		buffer.append(' ');
		buffer.append(((ASTNode) getChild(0)).toJava());
		buffer.append(" = ");
		buffer.append(((ASTNode) getChild(1)).toJava());
		buffer.append(';');
		
		return buffer.toString();
	}
}
