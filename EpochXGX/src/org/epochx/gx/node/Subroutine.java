package org.epochx.gx.node;


public class Subroutine extends ASTNode {

	public Subroutine() {
		this(null, null);
	}

	public Subroutine(final Block child1, final ReturnStatement child2) {
		super(child1, child2);
	}

	@Override
	public Object evaluate() {
		getChild(0).evaluate();
		
		return getChild(1).evaluate();
	}

	@Override
	public String getIdentifier() {
		return "SUBROUTINE";
	}
	
//	public int getNoStatements() {
//		Block body = (Block) getChild(0);
//		
//		int noStatements = 0;
//		if (body != null) {
//			noStatements = body.getNoNestedStatements();
//		}
//		
//		return noStatements;
//	}
	
	public Block getBody() {
		return (Block) getChild(0);
	}
	
	public ReturnStatement getReturnStatement() {
		return (ReturnStatement) getChild(1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getReturnType(Class<?> ... inputTypes) {
		if (inputTypes.length == 2 && inputTypes[0] == Void.class) {
			return inputTypes[1];
		} else {
			return null;
		}
	}

	@Override
	public Class<? extends ASTNode>[] getChildTypes() {
		return (Class<ASTNode>[]) new Class<?>[]{Block.class, ReturnStatement.class};
	}
	
	@Override
	public String toJava() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("public void method()");
		buffer.append(((ASTNode) getChild(0)).toJava());
		buffer.append(((ASTNode) getChild(1)).toJava());
		
		return buffer.toString();
	}
}
