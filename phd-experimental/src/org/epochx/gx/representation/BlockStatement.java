package org.epochx.gx.representation;

import java.util.*;

import org.epochx.tools.random.*;

public abstract class BlockStatement implements Statement {

	public BlockStatement clone() {
		try {
			return (BlockStatement) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
			return null;
		}
	}

	public abstract Statement deleteStatement(int deletePosition);

	public abstract Declaration getDeclaration(Variable v);

	public abstract int getNoInsertPoints();

	public abstract void insertStatement(double probability, RandomNumberGenerator rng,
			VariableHandler vars, int maxNoStatements);

	public abstract void insertStatements(int i, List<Statement> swapStatements);

	public abstract int getDepthOfInsertPoint(int insertPoint);

}
