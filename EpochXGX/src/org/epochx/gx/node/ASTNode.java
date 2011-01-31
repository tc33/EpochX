package org.epochx.gx.node;


import org.epochx.epox.*;

public abstract class ASTNode extends Node {
	
	public ASTNode(final ASTNode ... children) {
		super(children);
	}
	
	public abstract Class<? extends ASTNode>[] getChildTypes();

	@Override
	public ASTNode newInstance() {
		return (ASTNode) super.newInstance();
	}
	
	/**
	 * Finds and returns the parent node of the nth node.
	 * 
	 * @param n
	 * @return
	 */
	public ASTNode getNthNodeParent(int n) {
		return getNthNodeParent(n, 0, this);
	}
	
	private ASTNode getNthNodeParent(int n, int index, ASTNode node) {
		if (node.isTerminal()) {
			return null;
		}
		
		for (int i=0; i<node.getArity(); i++) {
			if (index+1 == n) {
				return node;
			} else {
				ASTNode child = (ASTNode) node.getChild(i);
				int childLength = child.getLength();
				if (index+childLength < n) {
					return getNthNodeParent(n, index+1, child);
				} else {
					index += childLength;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Should return the total number of statements that is found beneath this node, 
	 * and including this node if it is a statement. Assumes all children are also
	 * ASTNodes.
	 * @return
	 */
	public int getTotalNoStatements() {
		int total = 0;
		int arity = getArity();
		
		if (this instanceof Statement) {
			total++;
		}
		
		for (int i=0; i<arity; i++) {
			ASTNode child = (ASTNode) getChild(i);
			
			total += child.getTotalNoStatements();
		}
		
		return total;
	}
	
	public Statement getNthStatement(int n) {
		return getNthStatement(n, 0, this);
	}
	
	private Statement getNthStatement(final int n, int statementCount, final ASTNode current) {
		// Found the nth statement.
		if (current instanceof Statement) {
			if (n == statementCount) {
				return (Statement) current;
			}
			statementCount++;
		}

		int arity = current.getArity();
		for (int i=0; i<arity; i++) {
			ASTNode child = (ASTNode) current.getChild(i);
			
			final int noStatements = child.getTotalNoStatements();

			// Only look at the subtree if it contains the right range of nodes.
			if (n < (noStatements + statementCount)) {
				return getNthStatement(n, statementCount, child);
			}

			// Skip the correct number of nodes from the subtree.
			statementCount += noStatements;
		}

		return null;
	}
	
	/**
	 * Should return a valid Java representation of this node tree.
	 * @return
	 */
	public abstract String toJava();
}
