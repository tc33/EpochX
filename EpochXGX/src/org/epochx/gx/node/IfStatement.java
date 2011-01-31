package org.epochx.gx.node;


public class IfStatement extends Statement {
	
	public IfStatement() {
		this(null, null);
	}

	public IfStatement(final Expression child1, final Block child2) {
		super(child1, child2);
	}

	@Override
	public Void evaluate() {
		boolean condition = (Boolean) getChild(0).evaluate();
		Block block = (Block) getChild(1);

		if (condition) {
			block.evaluate();
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
		return "IF";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 2 
				&& inputTypes[0] == Boolean.class 
				&& inputTypes[1] == Void.class) {
			return Void.class;
		} else {
			return null;
		}
	}

	@Override
	public Class<? extends ASTNode>[] getChildTypes() {
		return (Class<ASTNode>[]) new Class<?>[]{Expression.class, Block.class};
	}
	
	@Override
	public String toJava() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("if (");
		buffer.append(((ASTNode) getChild(0)).toJava());
		buffer.append(')');
		buffer.append(((ASTNode) getChild(1)).toJava());
		
		return buffer.toString();
	}
}
