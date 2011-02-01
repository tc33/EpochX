package org.epochx.gx.node;


public class ForLoop extends Statement {

	private static int maxInterations = 100;
	
	public ForLoop() {
		this(null, null, null);
	}

	public ForLoop(final Variable indexVar, final Expression child1, final Block child2) {
		super(indexVar, child1, child2);
		
		maxInterations = 100;
	}

	@Override
	public Void evaluate() {
		Variable indexVar = (Variable) getChild(0);
		int upperBound = (Integer) getChild(1).evaluate();
		Block block = (Block) getChild(2);

		for (int i=0; (i<upperBound && i<maxInterations); i++) {
			// indexVar is immutable, but thats to protect from the body.
			indexVar.setValue(i);
			
			// Evaluate the block.
			block.evaluate();
			
			// Re-evaluate the expression before it is checked again.
			upperBound = (Integer) getChild(0).evaluate();
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
		return "FOR";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 2 
				&& inputTypes[0] == Integer.class
				&& inputTypes[1] == Void.class) {
			return Void.class;
		} else {
			return null;
		}
	}

	@Override
	public Class<? extends ASTNode>[] getChildTypes() {
		return (Class<ASTNode>[]) new Class<?>[]{Variable.class, Expression.class, Block.class};
	}
	
	@Override
	public String toJava() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("for (int ");
		buffer.append(((ASTNode) getChild(0)).toJava());
		buffer.append("=0; i<");
		buffer.append(((ASTNode) getChild(1)).toJava());
		buffer.append("; i++)");
		buffer.append(((ASTNode) getChild(2)).toJava());
		
		return buffer.toString();
	}
}
