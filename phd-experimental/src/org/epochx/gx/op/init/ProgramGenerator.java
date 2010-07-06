package org.epochx.gx.op.init;

import java.util.*;

import org.epochx.gx.representation.*;
import org.epochx.tools.random.*;

public class ProgramGenerator {

	private RandomNumberGenerator rng;
	
	// The variables that are currently in scope.
	private Stack<Variable> variableStack;
	
	// The parameters which are available as variables.
	private Set<Variable> parameters;
	
	// All variables that are used throughout the program, in scope or otherwise.
	private Set<Variable> variables;
	
	public ProgramGenerator() {
		this(new MersenneTwisterFast());
	}
	
	public ProgramGenerator(final RandomNumberGenerator rng) {
		this.rng = rng;
		variableStack = new Stack<Variable>();
		parameters = new HashSet<Variable>();
		variables = new HashSet<Variable>();
	}
	
	/**
	 * Arguments are essentially variables but they are available at the 
	 * start of each program that is generated with the assumption that they
	 * have already been declared.
	 * 
	 * @param var
	 */
	public void setParameters(Variable ... parameters) {
		this.parameters.clear();
		for (Variable v: parameters) {
			this.parameters.add(v);
			variableStack.push(v);
		}
	}
	
	public void setVariableStack(Stack<Variable> variables) {
		this.variableStack = variables;
	}
	
	public Stack<Variable> getVariableStack() {
		return variableStack;
	}
	
//	public void reset() {
//		variableStack.clear();
//		variableStack.addAll(parameters);
//		variables.clear();
//	}
	
//	public void addVariable(Variable variable) {
//		variables.add(variable);
//	}
//	
//	public void setVariables(Set<Variable> variables) {
//		this.variables = variables;
//	}
//	
//	public Set<Variable> getVariables() {
//		return variables;
//	}
	
	public void setRNG(RandomNumberGenerator rng) {
		this.rng = rng;
	}
	
	/*public AST getProgram(int noStatements) {
		reset();
		AST p = new AST();
		
		for (int i=0; i<noStatements; i++) {
			p.addStatement(getStatement());
		}
		
		return p;
	}*/
	
	/**
	 * Randomly chooses a statement type to construct. If there are no possible
	 * valid statements of that type then a different statement type will be 
	 * selected until all have been tried. If none are valid then null is 
	 * returned, otherwise the constructed statement is returned.
	 */
	/*public Statement getStatement() {
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
				result = getDeclaration();
			} else if (ran == 1) {
				// Assignment.
				result = getAssignment();
			} else if (ran == 2) {
				// If statement.
				result = getIf();
			} else {
				// Loop.
				result = getLoop();
			}
		}
		
		return result;
	}*/
	
	/*public Declaration getDeclaration() {
		String varName = getNewVariableName();
		
		DataType type = getDataType();
		Variable variable = new Variable(type, varName);
		Expression expression = getExpression(type);
		
		variableStack.push(variable);
		variables.add(variable);
		
		Declaration decl = new Declaration(this, type, variable, expression);
		
		return decl;
	}*/
	
	/*public Assignment getAssignment() {
		Assignment result = null;
		Variable variable = getVariable();

		if (variable != null) {
			Expression expression = getExpression(variable.getDataType());
			result = new Assignment(this, variable, expression);
		}
		
		return result;
	}*/
	
	/*public IfStatement getIf() {
		Expression condition = getExpression(DataType.BOOLEAN);
		Block ifCode = getBlock();
		
		IfStatement ifStatement = new IfStatement(this, condition, ifCode);
		
		return ifStatement;
	}*/
	
	/*public Loop getLoop() {
		Expression condition = getExpression(DataType.BOOLEAN);
		Block body = getBlock();
		
		Loop loop = new Loop(this, condition, body);
		
		return loop;
	}*/
	
	/*public Expression getExpression(DataType type) {
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
	}*/
	
//	public BinaryExpression getBinaryExpression(DataType dataType) {
//		String[] operators = null;
//		
//		if (dataType == DataType.BOOLEAN) {
//			operators = new String[]{"&&", "||"}; // ^ not supported by bsh.
//		} else if (dataType == DataType.INT) {
//			operators = new String[]{"+", "/", "%", "*", "-"};
//		} else if (dataType == DataType.DOUBLE) {
//			operators = new String[]{"+", "/", "%", "*", "-"};
//		} else {
//			// Broken.
//		}
//		
//		int ran = rng.nextInt(operators.length);
//		
//		Expression leftExpression = AST.getExpression(dataType);
//		Expression rightExpression = AST.getExpression(dataType);
//		
//		return new BinaryExpression(this, operators[ran], leftExpression, rightExpression, dataType);
//	}
	
//	public UnaryExpression getUnaryExpression(DataType dataType) {
//		UnaryExpression result = null;
//		
//		String[] operators = null;
//		if (dataType == DataType.BOOLEAN) {
//			operators = new String[]{"!"};
//		} else if (dataType == DataType.INT) {
//			operators = new String[]{"++", "--"};
//		} else if (dataType == DataType.DOUBLE) {
//			//operators = new String[]{"++", "--"};
//		} else {
//			// Broken.
//		}
//		
//		if (operators != null) {
//			int ran = rng.nextInt(operators.length);
//		
//			Variable var = getVariable(dataType);
//			
//			if (var != null) {
//				result = new UnaryExpression(this, operators[ran], var, dataType);
//			}
//		}
//
//		return result;
//	}
	
	/*public Block getBlock() {
		// Record number of variables to return to.
		int noVariables = variableStack.size();
		
		List<Statement> statements = new ArrayList<Statement>();

		//int noStatements = rng.nextInt(10);
		int noStatements = 1;
		
		for (int i=0; i<noStatements; i++) {
			statements.add(getStatement());
		}
		
		Block result = new Block(statements);
		
		// Pop off any newly declared variables to return to size before block.
		variableStack.setSize(noVariables);
		
		return result;
	}*/
	
	/*public DataType getDataType() {
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
	}*/
	
//	public String getNewVariableName() {
//		String name;
//		
//		do {
//			name = "var" + noVars++;
//		} while (variablesContains(name));
//		
//		return name;
//	}
//	
//	public boolean variablesContains(String varName) {
//		for (Variable v: variables) {
//			if (v.getVariableName().equals(varName)) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	/*public Literal getLiteral(DataType datatype) {
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
	}*/
	
	/*public BooleanLiteral getBooleanLiteral() {
		BooleanLiteral literal = new BooleanLiteral(rng.nextBoolean());
		
		return literal;
	}*/
	
	/*public IntLiteral getIntLiteral() {
		IntLiteral literal = new IntLiteral(rng.nextInt());
		
		return literal;
	}*/
	
	/*public DoubleLiteral getDoubleLiteral() {
		DoubleLiteral literal = new DoubleLiteral(rng.nextDouble());
		
		return literal;
	}*/
	
//	public Variable getVariable() {
//		Variable var = null;
//		
//		if (!variableStack.isEmpty()) {
//			int choice = rng.nextInt(variableStack.size());
//			
//			var = variableStack.get(choice);
//		}
//		
//		return var;
//	}
	
//	public Variable getVariable(DataType dataType) {
//		Variable var = null;
//		List<Variable> variables = getVariablesFromStack(dataType);
//		
//		if (!variables.isEmpty()) {
//			int choice = rng.nextInt(variables.size());
//			
//			var = variables.get(choice);
//		}
//		
//		return var;
//	}
//	
//	public List<Variable> getVariablesFromStack(DataType dataType) {
//		List<Variable> typeVariables = new ArrayList<Variable>();
//		for (Variable v: variableStack) {
//			if (v.getDataType() == dataType) {
//				typeVariables.add(v);
//			}
//		}
//		return typeVariables;
//	}
	
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
		RandomNumberGenerator rng = new MersenneTwisterFast();
		AST program = AST.getProgram(10, rng, new VariableHandler());
		
		System.out.println(format(program.toString()));
	}
}
