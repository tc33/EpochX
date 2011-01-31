package org.epochx.gx.node;


public abstract class Statement extends ASTNode {

	public Statement(final ASTNode ... children) {
		super(children);
	}

	/**
	 * Should return the total number of statements that are held within this 
	 * statement. This should exclude the statement itself, so for a simple 
	 * statement such as assignment, the value should always be zero.
	 * @return
	 */
	public int getNoNestedStatements() {
		return getTotalNoStatements() - 1;
	}

}
