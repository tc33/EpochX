package org.epochx.gx.op.init;

import java.util.*;

import org.epochx.gx.representation.*;
import org.epochx.tools.random.*;

public class ProgramGenerator {

	private RandomNumberGenerator rng;
	
	private int noVars;
	
	private Stack<Variable> variables;
	
	public ProgramGenerator() {
		this(new MersenneTwisterFast());
	}
	
	public ProgramGenerator(final RandomNumberGenerator rng) {
		this.rng = rng;
		variables = new Stack<Variable>();

		noVars = 0;
	}
	
	public Program getProgram(int noStatements) {
		Program p = new Program();
		
		for (int i=0; i<noStatements; i++) {
			p.addStatement(getStatement());
		}
		
		return p;
	}
	
	/**
	 * Randomly chooses a statement type to construct. If there are no possible
	 * valid statements of that type then a different statement type will be 
	 * selected until all have been tried. If none are valid then null is 
	 * returned, otherwise the constructed statement is returned.
	 */
	public Statement getStatement() {
		Statement result = null;
		
		int noOptions = 4;
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i=0; i<noOptions; i++) {
			indexes.add(i);
		}
		
		while (result == null && !indexes.isEmpty()) {
			int ran = indexes.remove(rng.nextInt(indexes.size()));
	
			if (ran == 0) {
				// Declaration.
				result = getDeclaration();
			} else if (ran == 1) {
				// Assignment.
				result = getAssignment();
			} else if (ran == 2) {
				// If statement.
				result = getIf();
			} else {
				// Loop.
				//result = getForLoop();
			}
		}
		
		return result;
	}
	
	public Declaration getDeclaration() {
		String varName = getNewVariableName();
		
		DataType type = getDataType();
		Variable variable = new Variable(type, varName);
		Expression expression = getExpression(type);
		
		variables.push(variable);
		
		Declaration decl = new Declaration(type, variable, expression);
		
		return decl;
	}
	
	public Assignment getAssignment() {
		Assignment result = null;
		Variable variable = getVariable();

		if (variable != null) {
			Expression expression = getExpression(variable.getDataType());
			result = new Assignment(variable, expression);
		}
		
		return result;
	}
	
	public IfStatement getIf() {
		Expression condition = getExpression(DataType.BOOLEAN);
		Block ifCode = getBlock();
		
		IfStatement ifStatement = new IfStatement(condition, ifCode);
		
		return ifStatement;
	}
	
	/*public String getExpression(DataType datatype) {
		String result = null;
		
		if (datatype == DataType.BOOLEAN) {
			result = getBooleanExpression();
		} else if (datatype == DataType.INT) {
			result = getIntExpression();
		} else if (datatype == DataType.DOUBLE) {
			result = getDoubleExpression();
		} else {
			// Broken.
		}
	}*/
	
	public Expression getExpression(DataType type) {
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
				result = getLiteral(type);
			} else if (ran == 1) {
				// Variable.
				result = getVariable(type);
			} else if (ran == 2) {
				// Binary expression.
				result = getBinaryExpression(type);
			} else {
				// Unary expression.
				result = getUnaryExpression(type);
			}
		}
		
		return result;
	}
	
	public BinaryExpression getBinaryExpression(DataType dataType) {
		String[] operators = null;
		
		if (dataType == DataType.BOOLEAN) {
			operators = new String[]{"&&", "||", "^"};
		} else if (dataType == DataType.INT) {
			operators = new String[]{"+", "/", "%", "*", "-"};
		} else if (dataType == DataType.DOUBLE) {
			operators = new String[]{"+", "/", "%", "*", "-"};
		} else {
			// Broken.
		}
		
		int ran = rng.nextInt(operators.length);
		
		Expression leftExpression = getExpression(dataType);
		Expression rightExpression = getExpression(dataType);
		
		return new BinaryExpression(operators[ran], leftExpression, rightExpression);
	}
	
	public UnaryExpression getUnaryExpression(DataType dataType) {
		String[] operators = null;
		
		if (dataType == DataType.BOOLEAN) {
			operators = new String[]{"!"};
		} else if (dataType == DataType.INT) {
			operators = new String[]{"++", "--"};
		} else if (dataType == DataType.DOUBLE) {
			operators = new String[]{"++", "--"};
		} else {
			// Broken.
		}
		
		int ran = rng.nextInt(operators.length);
		
		Expression expression = getExpression(dataType);
		
		return new UnaryExpression(operators[ran], expression);
	}
	
	public Block getBlock() {
		// Record number of variables to return to.
		int noVariables = variables.size();
		
		List<Statement> statements = new ArrayList<Statement>();

		//int noStatements = rng.nextInt(10);
		int noStatements = 1;
		
		for (int i=0; i<noStatements; i++) {
			statements.add(getStatement());
		}
		
		Block result = new Block(statements);
		
		// Pop off any newly declared variables to return to size before block.
		variables.setSize(noVariables);
		
		return result;
	}
	
	public DataType getDataType() {
		DataType result = null;
		
		int ran = rng.nextInt(3);
		
		if (ran == 0) {
			result = DataType.BOOLEAN;
		} else if (ran == 1) {
			result = DataType.INT;
		} else {
			result = DataType.DOUBLE;
		}
		
		return result;
	}
	
	public String getNewVariableName() {
		return "var" + noVars++;
	}
	
	public Literal getLiteral(DataType datatype) {
		Literal result = null;
		
		if (datatype == DataType.BOOLEAN) {
			result = getBooleanLiteral();
		} else if (datatype == DataType.INT) {
			result = getIntLiteral();
		} else if (datatype == DataType.DOUBLE) {
			result = getDoubleLiteral();
		} else {
			// broken.
		}
		
		return result;
	}
	
	public BooleanLiteral getBooleanLiteral() {
		BooleanLiteral literal = new BooleanLiteral(rng.nextBoolean());
		
		return literal;
	}
	
	public IntLiteral getIntLiteral() {
		IntLiteral literal = new IntLiteral(rng.nextInt());
		
		return literal;
	}
	
	public DoubleLiteral getDoubleLiteral() {
		DoubleLiteral literal = new DoubleLiteral(rng.nextDouble());
		
		return literal;
	}
	
	public Variable getVariable() {
		Variable var = null;
		
		if (!variables.isEmpty()) {
			int choice = rng.nextInt(variables.size());
			
			var = variables.get(choice);
		}
		
		return var;
	}
	
	public Variable getVariable(DataType dataType) {
		Variable var = null;
		List<Variable> variables = getVariables(dataType);
		
		if (!variables.isEmpty()) {
			int choice = rng.nextInt(variables.size());
			
			var = variables.get(choice);
		}
		
		return var;
	}
	
	public List<Variable> getVariables(DataType dataType) {
		List<Variable> typeVariables = new ArrayList<Variable>();
		for (Variable v: variables) {
			if (v.getDataType() == dataType) {
				typeVariables.add(v);
			}
		}
		return typeVariables;
	}
	
	public static String format(CharSequence s) {
		int indentLevel = 0;
		StringBuilder formatted = new StringBuilder();
		
		for (int i=0; i<s.length(); i++) {
			char c = s.charAt(i);
			formatted.append(c);
			if (c == '{') {
				indentLevel++;
			} else if (c == '\n') {
				// If there's space then look ahead to test next char.
				if (i+1 < s.length()) {
					c = s.charAt(i+1);
					if (c == '}') {
						indentLevel--;
					}
				}
				formatted.append(indent(indentLevel));
			}
		}
		
		return formatted.toString();
	}
	
	private static String indent(int indentLevel) {
		StringBuilder buffer = new StringBuilder();
		for (int i=0; i<indentLevel; i++) {
			buffer.append("    ");
		}
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		ProgramGenerator generator = new ProgramGenerator();
		
		Program program = generator.getProgram(10);
		
		System.out.println(format(program.toString()));
	}
}
