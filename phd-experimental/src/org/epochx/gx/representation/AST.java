package org.epochx.gx.representation;

import java.util.*;

import org.epochx.tools.random.*;

public class AST implements Cloneable {

	private List<Statement> statements;
	
	public AST() {
		statements = new ArrayList<Statement>();
	}
	
	public void insertStatement(int index, Statement statement) {
		if (index == statements.size()) {
			addStatement(statement);
		} else {
			statements.add(index, statement);
		}
	}
	
	public void addStatement(Statement statement) {
		statements.add(statement);
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		
		for (Statement s: statements) {
			buffer.append(s.toString());
			buffer.append('\n');
		}
		
		return buffer.toString();
	}
	
	public List<Statement> getStatements() {
		return statements;
	}
	
	public void modifyExpression(double probability, VariableHandler vars, RandomNumberGenerator rng) {
		for (Statement s: statements) {
			s.modifyExpression(probability, rng, vars);
			s.apply(vars);
		}
	}
	
	public static AST getProgram(int noStatements, RandomNumberGenerator rng, VariableHandler vars) {
		AST p = new AST();
		
		for (int i=0; i<noStatements; i++) {
			p.statements.add(getStatement(rng, vars));
		}
		
		return p;
	}
	
	public static Statement getStatement(RandomNumberGenerator rng, VariableHandler vars) {
		Statement result = null;
		
		int noOptions = 3;
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i=0; i<noOptions; i++) {
			indexes.add(i);
		}
		
		while (result == null && !indexes.isEmpty()) {
			int ran = indexes.remove(rng.nextInt(indexes.size()));
			
			if (ran == 0) {
				// Declaration.
				result = Declaration.getDeclaration(rng, vars);
			} else if (ran == 1) {
				// Assignment.
				result = Assignment.getAssignment(rng, vars);
			} else if (ran == 2) {
				// If statement.
				result = IfStatement.getIf(rng, vars);
			} else {
				// Loop.
				result = Loop.getLoop(rng, vars);
			}
		}
		
		return result;
	}
	
	public static Expression getExpression(DataType type, RandomNumberGenerator rng, VariableHandler vars) {
		Expression result = null;
		
		int noOptions = 4;
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i=0; i<noOptions; i++) {
			indexes.add(i);
		}
		
		while (result == null && !indexes.isEmpty()) {
			int ran = indexes.remove(rng.nextInt(indexes.size()));
			
			if (ran == 0) {
				// Literal value.
				result = AST.getLiteral(type, rng);
			} else if (ran == 1) {
				// Variable.
				result = vars.getVariable(type);
			} else if (ran == 2) {
				// Binary expression.
				result = BinaryExpression.getBinaryExpression(type, rng, vars);
			} else {
				// Unary expression.
				result = UnaryExpression.getUnaryExpression(type, rng, vars);
			}
		}
		
		return result;
	}
	
	public static Expression getLiteral(DataType datatype, RandomNumberGenerator rng) {
		Literal result = null;
		
		if (datatype == DataType.BOOLEAN) {
			result = BooleanLiteral.getBooleanLiteral(rng);
		} else if (datatype == DataType.INT) {
			result = IntLiteral.getIntLiteral(rng);
		} else if (datatype == DataType.DOUBLE) {
			result = DoubleLiteral.getDoubleLiteral(rng);
		} else {
			// broken.
		}
		
		return result;
	}

	public static DataType getDataType(RandomNumberGenerator rng) {
		//DataType[] types = DataType.values();
		
		//return types[rng.nextInt(types.length)];
		
		return DataType.BOOLEAN;
	}
	
	/*public static Program getProgram() {
		return Program.getProgram(rng.nextInt(10) + 1);
	}*/
	
	public Object evaluate(VariableHandler vars) {
		// Evaluate each statement.
		for (Statement s: statements) {
			s.evaluate(vars);
		}
		
		return null;
	}
	
	@Override
	public Object clone() {
		AST clone = null;
		try {
			clone = (AST) super.clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}
		
		clone.statements = new ArrayList<Statement>();
		for (Statement s: this.statements) {
			clone.statements.add((Statement) s.clone());
		}
		
		return clone;
	}
	
}
