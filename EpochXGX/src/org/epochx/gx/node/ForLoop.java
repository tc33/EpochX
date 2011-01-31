package org.epochx.gx.node;


public class ForLoop extends Statement {

	private static int maxInterations = 100;
	
	private Variable indexVar;
	
	public ForLoop() {
		this(null, null);
	}

	public ForLoop(final Expression child1, final Block child2) {
		super(child1, child2);
		
		maxInterations = 100;
		indexVar = new Variable("i", Integer.class);
	}

	@Override
	public Void evaluate() {
		//TODO Needs to add an index variable to list of in-scope variables.
		int upperBound = (Integer) getChild(0).evaluate();
		Block block = (Block) getChild(1);

		for (int i=0; (i<upperBound && i<maxInterations); i++) {
			//TODO indexVar needs to be made immutable.
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
		return (Class<ASTNode>[]) new Class<?>[]{Expression.class, Block.class};
	}
	
	@Override
	public String toJava() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("for (int i=0; i<");
		buffer.append(((ASTNode) getChild(0)).toJava());
		buffer.append("; i++)");
		buffer.append(((ASTNode) getChild(1)).toJava());
		
		return buffer.toString();
	}
}
